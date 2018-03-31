package com.zyt.close_gesture_sttings;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import com.android.systemui.R;

@SuppressLint("NewApi")
public class MyAllKeyPrefsFragment extends PreferenceFragment implements  OnSharedPreferenceChangeListener{

public static final String DOUBLE_CLICK_KEY =	"double_click";
public static final String UP_SLIP_KEY		=	"up_slip";
public static final String DOWN_SLIP_KEY	=	"down_slip";
public static final String LEFT_SLIP_KEY	=	"left_slip";
public static final String RIGHT_SLIP_KEY	=	"right_slip";
public static final String LETTER_O_KEY		=	"letter_o";
public static final String LETTER_W_KEY		=	"letter_w";
public static final String LETTER_M_KEY		=	"letter_m";
public static final String LETTER_E_KEY		=	"letter_e";
public static final String LETTER_C_KEY		=	"letter_c";
public static final String LETTER_S_KEY		=	"letter_s";
public static final String LETTER_V_KEY		=	"letter_v";
public static final String LETTER_Z_KEY		=	"letter_z";
public static final String DEF_ACTION_SUFFIX =	"_def";
public static final String KEY_SUFFIX =	"_key";

public static final int KYE_SIZE		=12;//13

public static final String KYE[]={//DOUBLE_CLICK_KEY,
									UP_SLIP_KEY	,	
									DOWN_SLIP_KEY,	
									LEFT_SLIP_KEY,	
									RIGHT_SLIP_KEY,	
									LETTER_O_KEY,		
									LETTER_W_KEY,		
									LETTER_M_KEY,		
									LETTER_E_KEY,		
									LETTER_C_KEY,		
									LETTER_S_KEY,		
									LETTER_V_KEY,		
									LETTER_Z_KEY		
};



SharedPreferences sh ;
Resources mResources;
private Switch mEnabledSwitch;


private final ArrayList<Preference> mAllPrefs = new ArrayList<Preference>();

public static final String FLAG_FILE_PATH="data/data/com.android.systemui/gesture_switch";

public static final File switchFile=new File(FLAG_FILE_PATH);

public static boolean getTheSwitch(){
	return switchFile.exists();
}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.gesture_main);
		for(int i =0;i<KYE_SIZE;i++){
			
			mAllPrefs.add((PreferenceScreen)findPreference(KYE[i]+KEY_SUFFIX));
		}

android.util.Log.d("zhangpeifu","onCreate start");
		
		sh=getActivity().getSharedPreferences(MainActivity.SHARED_FILE,
		        Context.MODE_PRIVATE);
		mResources = getActivity().getResources();
		mEnabledSwitch = new Switch(getActivity());
		
	     final int padding = this.getActivity().getResources().getDimensionPixelSize(
	                R.dimen.action_bar_switch_padding);
	        mEnabledSwitch.setPaddingRelative(0, 0, 16, 0);
		
		mEnabledSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				setAllEnnableState(isChecked);
				
			}
		});

	
	  this.getActivity().getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM,
                ActionBar.DISPLAY_SHOW_CUSTOM);
	  this.getActivity().getActionBar().setCustomView(mEnabledSwitch, new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER_VERTICAL | Gravity.END));
		if(getTheSwitch()){
			mEnabledSwitch.setChecked(false);
			for(int i =0;i<KYE_SIZE;i++){
			
			mAllPrefs.get(i).setEnabled(false);
		}
			
			
		}
		else{
			mEnabledSwitch.setChecked(true);
			for(int i =0;i<KYE_SIZE;i++){
			
			mAllPrefs.get(i).setEnabled(true);
			}
		} 
	}
	
	
	
	public void setAllEnnableState(boolean state){

	android.util.Log.d("zhangpeifu","setAllEnnableState start state = "+state);
		
		for(int i =0;i<KYE_SIZE;i++){
			
			mAllPrefs.get(i).setEnabled(state);

			
		}
		
		if(state){
			
			if(getTheSwitch()){
				switchFile.delete();
			}
		}
		else{
			
			if(!getTheSwitch()){
				try {
					Runtime runtime = Runtime.getRuntime();

					switchFile.createNewFile();
					Process proc = runtime.exec("chmod 777 "+switchFile.getAbsolutePath());   

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
		}
	}
	
	@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			  
			loadPef();
	android.util.Log.d("zhangpeifu","onResume start");
	}
	
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
			android.util.Log.d("zhangpeifu","onSharedPreferenceChanged start");
	}
	
	public void loadPef(){
		int preActionId=R.string.gesture_pre_action_not_define;//没有定义
android.util.Log.d("zhangpeifu","loadPef start");
		for(int i=0;i<KYE_SIZE;i++){
			preActionId=mResources.getIdentifier(KYE[i]+DEF_ACTION_SUFFIX, "string","com.android.systemui");//预置的动作的字符串id
			
			((PreferenceScreen)findPreference(KYE[i]+KEY_SUFFIX)).
			setSummary(resolveTheShPefForSummary(
					sh.getString(KYE[i]+KEY_SUFFIX, mResources.getString(preActionId))
					
					)
				);


			
		
		}	
	}
	
	public String  resolveTheShPefForSummary(String value){
		String res="";
	
	
			String para[]=value.split(";");
			for(int i=0;i<para.length;i++){
				
				android.util.Log.d("zhangpeifu",MyAllKeyPrefsFragment.class.getSimpleName()+"    "+para[i]);

				
			}
				switch(Integer.valueOf(para[0])){
						case 	MygestureDetailSetPrefsFragment.TYPE_NO_DEFINE			:
							{

								res=mResources.getString(R.string.gesture_static_summary_no_define);
								break;
							}
						case	MygestureDetailSetPrefsFragment.TYPE_JUST_TURN_SCREEN	:
							{
								
								res=mResources.getString(R.string.gesture_static_summary_just_turn_screen);
								
								break;
							}
						case	MygestureDetailSetPrefsFragment.TYPE_TURN_SCREEN_UNLOCK	:
							{
								
								res=mResources.getString(R.string.gesture_static_summary_turn_screen_and_unlock);

								
								break;
							}
						case	MygestureDetailSetPrefsFragment.TYPE_OPEN_CAMERA		:
							{
								
								res=mResources.getString(R.string.gesture_static_summary_open_camera);

								
								break;
							}
						case	MygestureDetailSetPrefsFragment.TYPE_PLAY_PLAUSE		:
							{
								
								res=mResources.getString(R.string.detail_temp_music_summary);

								
								break;
							}
						case	MygestureDetailSetPrefsFragment.TYPE_PRE_SONG			:
							{
								
								res=mResources.getString(R.string.gesture_static_summary_pre_song);

								
								break;
							}	
						case	MygestureDetailSetPrefsFragment.TYPE_NEXT_SONG			:
							{
								
								res=mResources.getString(R.string.gesture_static_summary_next_song);

								
								break;
							}
						case	MygestureDetailSetPrefsFragment.TYPE_START_RECORD		:
						{
							
							res=mResources.getString(R.string.gesture_static_summary_start_record);

							
							break;
						}
						case	MygestureDetailSetPrefsFragment.TYPE_CALL_SOMEONE		:
						{
							res=mResources.getString(R.string.gesture_static_summary_call_somebody_base)+para[1];
							
							break;
			
						}
						case	MygestureDetailSetPrefsFragment.TYPE_SEND_MSG_TO_SOMEONE:
						{
							res=mResources.getString(R.string.gesture_static_summary_send_msg_to_somebody_base)+para[1];

							break;
						}
						case	MygestureDetailSetPrefsFragment.TYPE_GET_INTO_WEBSITE	:
						{
							res=mResources.getString(R.string.gesture_static_summary_get_into_website_base)+para[1];

							break;
						}
						case	MygestureDetailSetPrefsFragment.TYPE_OPEN_APP			:
						{
							res=mResources.getString(R.string.gesture_static_summary_open_app_base)+para[1];

							break;
						}
				}
			
		
		return res;
	}

 
	
	
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        
        android.util.Log.d("zhangpeifu", MyAllKeyPrefsFragment.class.getSimpleName()+"   "+preference.getKey());
        startActivity(new Intent(Intent.ACTION_MAIN).setClass(this.getActivity(),MyGestureSettingActivity.class).putExtra(KEY_SUFFIX, preference.getKey()));
    	
    	return true;
    }
    
  
    
}
