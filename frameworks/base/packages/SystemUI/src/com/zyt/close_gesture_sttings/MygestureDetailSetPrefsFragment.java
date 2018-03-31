package com.zyt.close_gesture_sttings;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.provider.ContactsContract;
import android.text.TextUtils;
import java.io.File;
import java.io.IOException;
import com.android.systemui.R;

@SuppressLint("NewApi")
public class MygestureDetailSetPrefsFragment extends PreferenceFragment implements OnSharedPreferenceChangeListener{
	public static final int TYPE_NO_DEFINE				=1;
	public static final int TYPE_JUST_TURN_SCREEN		=2;
	public static final int TYPE_TURN_SCREEN_UNLOCK		=3;
	public static final int TYPE_CALL_SOMEONE			=4;
	public static final int TYPE_SEND_MSG_TO_SOMEONE	=5;
	public static final int TYPE_GET_INTO_WEBSITE		=6;
	public static final int TYPE_OPEN_CAMERA			=7;
	public static final int TYPE_PLAY_PLAUSE			=8;
	public static final int TYPE_PRE_SONG				=9;
	public static final int TYPE_NEXT_SONG				=10;
	public static final int TYPE_START_RECORD			=11;
	public static final int TYPE_OPEN_APP				=12;
	
	
	public static final String DETAIL_NO_DEFINE_KEY 			= "detail_no_define_key";
	public static final String DETAIL_JUST_TURN_SCREEN_KEY 		= "detail_just_turn_screen_key";
	public static final String DETAIL_TURN_SCREE_UNLOCK_KEY 	= "detail_turn_scree_unlock_key";
	public static final String DETAIL_CALL_SOMEBODY_KEY 		= "detail_call_somebody_key";
	public static final String DETAIL_SEND_MSG_TO_SOMEBODY_KEY 	= "detail_send_msg_to_somebody_key";
	public static final String DETAIL_GET_INTO_WEBSITE_KEY 		= "detail_get_into_website_key";
	public static final String DETAIL_OPEN_CAMERA_KEY 			= "detail_open_camera_key";
	public static final String DETAIL_PLAY_PLAUSE_KEY 			= "detail_play_plause_key";
	public static final String DETAIL_PRE_SONG_KEY 				= "detail_pre_song_key";
	public static final String DETAIL_NEXT_SONG_KEY 			= "detail_next_song_key";
	public static final String DETAIL_START_RECORDG_KEY 		= "detail_start_recordg_key";
	public static final String DETAIL_OPEN_APPLICATION 			= "detail_open_application";
	
	
	public static final String MUSIC_OP_PRE="_pre";
	public static final String MUSIC_OP_NEXT="_next";
	public static final String MUSIC_PLAY_OR_PAUSE="_play_pause";



	//public static final String PARA_NAME="type";
	public static final int CONTACT_PICK_REQ_CODE=1;
	public static final int APP_PICK_REQ_CODE=2;
	public static final int WEBSITE_PICK_REQ_CODE=3;
	public static final int CONTACT_SMS_PICK_REQ_CODE=4;

	public static final String CHOOSE_PACKAGE_PARA_NAME	="package_name";
	public static final String CHOOSE_CLASS_NAME		="class_name";
	public static final String CHOOSE_APP_NAME			="app_name";

	public static final String PARA_SITE_NAME="site_name";
	public static final String PARA_SITE_ADDR="site_addr";

	String mainKey;
	
	Editor shEditor;
	
	
	
	Dialog SelectContactsDialog;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	
		
		
		
		// Load the preferences from an XML resource
		addPreferencesFromResource(R.xml.gesture_detail_set);
		mainKey=this.getActivity().getIntent().getStringExtra(MyAllKeyPrefsFragment.KEY_SUFFIX);
		shEditor = getActivity().getSharedPreferences(MainActivity.SHARED_FILE,
	            Context.MODE_PRIVATE).edit();
	}
	
	@Override
	public void onSharedPreferenceChanged(SharedPreferences arg0, String arg1) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		android.util.Log.d("zhangpeifu","data   "+data);
		
		if(data==null){	return;	}
		
		   switch (requestCode) {
		   case CONTACT_SMS_PICK_REQ_CODE:
		   case  CONTACT_PICK_REQ_CODE : 
		   {dealChooseContactRet(data,requestCode);
			   break;
		   }
		   
		  
		   case  APP_PICK_REQ_CODE : 
		   {
			   dealChooseAppRet(data);

			   break;
		   }
		   case  WEBSITE_PICK_REQ_CODE : 
		   {
			   dealChooseWebSiteRet(data);
			   break;
		   }		   
		   
		   }
		
	}
	
	public void dealChooseAppRet(Intent data){
		if(data==null){
			
			return;
		}
		String pkg=data.getStringExtra(CHOOSE_PACKAGE_PARA_NAME);
		String cls=data.getStringExtra(CHOOSE_CLASS_NAME);
		String app=data.getStringExtra(CHOOSE_APP_NAME);
		String res=TYPE_OPEN_APP+";"+app+";"+pkg+";"+cls;
		shEditor.putString(mainKey,res);
		shEditor.commit();
		this.getActivity().finish();
	}
	
	public void dealChooseContactRet(Intent data , int code){
		String contactName = "";
		String contactNumber="";
		String contactId="";
        Uri uri = data.getData();
        if(uri==null){
        	return;
        	}
        ContentResolver cr = getActivity().getContentResolver();
        Cursor cursor = cr.query(uri, null, null, null, null);
        
        while (cursor.moveToNext()) {
            // 取得联系人名字
            int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
            int idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID);

             contactName = cursor.getString(nameColumnIndex);
             contactId=cursor.getString(idColumnIndex);
           
        }
        
        cursor.moveToFirst();
        
      contactNumber= getContactPhone(cursor,cr);
      android.util.Log.d("zhangpeifu", "    contactName   "+contactName+"  contactNumber  "+contactNumber+"	 contactId      "+contactId+"       "+cursor.getCount())  ;
  if(contactNumber.split(";").length>1){
	  showContactNumDialog(contactNumber,contactName,shEditor,mainKey,code,uri);
  }else{
	  if(!TextUtils.isEmpty(contactNumber)){
		  doSaveForContact(code,contactName+";"+contactNumber+uri,shEditor,mainKey);
		  
	  }
	  else{
		  
		  new GestureToast(this.getActivity(),R.string.gesture_choose_contact_no_number);
		  
	  }
	  this.getActivity().finish();

  }
      
      
      
      if(cursor!=null&&!cursor.isClosed()){
    	  cursor.close();  
      }
        if(TextUtils.isEmpty(contactName)||TextUtils.isEmpty(contactNumber)){
        	return ;
        }
      //res = (code==CONTACT_PICK_REQ_CODE?TYPE_CALL_SOMEONE:TYPE_SEND_MSG_TO_SOMEONE)+";"+contactName+";"+contactNumber+";"+contactId;
    //    shEditor.putString(mainKey,res);
		//shEditor.commit();
	}
	
	public static void doSaveForContact(int code,String realData,Editor mShEditor,String key){
		
		 int type = code==CONTACT_PICK_REQ_CODE?TYPE_CALL_SOMEONE:TYPE_SEND_MSG_TO_SOMEONE;
		 android.util.Log.d("zhangpeifu","type;realData        "+type+";"+realData);
		 mShEditor.putString(key,type+";"+realData);
		 mShEditor.commit();
	}
	
	public void dealChooseWebSiteRet(Intent data){
		String siteName=data.getStringExtra(PARA_SITE_NAME);
		String siteAddr=data.getStringExtra(PARA_SITE_ADDR);
		if(!TextUtils.isEmpty(siteName)&&!TextUtils.isEmpty(siteAddr)){
			
			String res=TYPE_GET_INTO_WEBSITE+";"+siteName+";"+siteAddr;
			shEditor.putString(mainKey,res);
			shEditor.commit();
			this.getActivity().finish();
			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	public static String getContactPhone(Cursor cursor,ContentResolver cr) 
	{ 
	 
	    int phoneColumn = cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);   
	    int phoneNum = cursor.getInt(phoneColumn);  
	    String phoneResult=""; 
	    if (phoneNum > 0) 
	    { 
	        int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID); 
	        String contactId = cursor.getString(idColumn); 
	            Cursor phones = cr.query( 
	            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
	            null, 
	            ContactsContract.CommonDataKinds.Phone.CONTACT_ID+ " = " + contactId,  
	            null, null); 
	        
	            if (phones.moveToFirst()) 
	            { 
	                    // 遍历所有的电话号码 
	                    for (;!phones.isAfterLast();phones.moveToNext()) 
	                    {                                             
	                        int index = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER); 
	                        int typeindex = phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE); 
	                        int phone_type = phones.getInt(typeindex); 
	                        android.util.Log.d("zhangpeifu","         "+ phone_type);
	                        String phoneNumber = phones.getString(index); 
	                        
	                        	phoneResult+=phoneNumber+";";
	                      
	                    } 
	                    if (!phones.isClosed()) 
	                    { 
	                           phones.close(); 
	                    } 
	            } 
	    } 
	    android.util.Log.d("zhangpeifu","phoneResult    "+phoneResult);
	    return phoneResult; 
	} 
	
	
	public void showContactNumDialog(String data,final String name,final Editor mShEditor,final String key,final int code,final Uri uri){
		  final String[] arrayContactsNum = data.split(";");
		
if(SelectContactsDialog!=null&&SelectContactsDialog.isShowing()){
			SelectContactsDialog.dismiss();
}
		 SelectContactsDialog = new AlertDialog.Builder(this.getActivity())
		 						.setTitle(R.string.gesture_choose_number)
		 						.setItems(arrayContactsNum, new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										//arrayContactsNum[which];
										doSaveForContact(code,name+";"+arrayContactsNum[which]+";"+uri,mShEditor,key);
										SelectContactsDialog.dismiss();
										MygestureDetailSetPrefsFragment.this.getActivity().finish();
									}
								})
								
		 						.setOnDismissListener(new DialogInterface.OnDismissListener() {
									
									@Override
									public void onDismiss(DialogInterface dialog) {
										// TODO Auto-generated method stub
										
									}
								}).create();
		 android.util.Log.d("zhangpeifu", "     SelectContactsDialog      "+SelectContactsDialog);
		 SelectContactsDialog.show();
	}
	
	
	
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        
        android.util.Log.d("zhangpeifu",MygestureDetailSetPrefsFragment.class.getSimpleName()+ "   "+preference.getKey());
    
        android.util.Log.d("zhangpeifu",MygestureDetailSetPrefsFragment.class.getSimpleName()+"  the mainKey  "+mainKey);
 
		
        if(shEditor==null){
        	return false;
        }
        String res="";
        if(DETAIL_NO_DEFINE_KEY.equals(preference.getKey())){
        	
        	res=""+TYPE_NO_DEFINE;
        	
        }else if(DETAIL_JUST_TURN_SCREEN_KEY.equals(preference.getKey())){
        	
  
    			res=""+TYPE_JUST_TURN_SCREEN;
        	
        }else if(DETAIL_TURN_SCREE_UNLOCK_KEY.equals(preference.getKey())){
        	
        		res=""+TYPE_TURN_SCREEN_UNLOCK;
        	
        }else if(DETAIL_CALL_SOMEBODY_KEY.equals(preference.getKey())){
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 
            
            this.startActivityForResult(intent, CONTACT_PICK_REQ_CODE); 
        	
        	
        }else if(DETAIL_SEND_MSG_TO_SOMEBODY_KEY.equals(preference.getKey())){
        	
            Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI); 
            
            this.startActivityForResult(intent, CONTACT_SMS_PICK_REQ_CODE); 
        	
        }else if(DETAIL_GET_INTO_WEBSITE_KEY.equals(preference.getKey())){
        	
        	 Intent intent = new Intent(Intent.ACTION_MAIN);
        	intent.setClass(this.getActivity(), WebsiteSelectActivity.class);
            this.startActivityForResult(intent, WEBSITE_PICK_REQ_CODE); 

        	
        	
        }else if(DETAIL_OPEN_CAMERA_KEY.equals(preference.getKey())){
        	
        	res=""+TYPE_OPEN_CAMERA;
        	
        }else if(DETAIL_PLAY_PLAUSE_KEY.equals(preference.getKey())){
        	
        	res=""+TYPE_PLAY_PLAUSE;
        	
        }else if(DETAIL_PRE_SONG_KEY.equals(preference.getKey())){
        	
        	res=""+TYPE_PRE_SONG;
        	
        }else if(DETAIL_NEXT_SONG_KEY.equals(preference.getKey())){
        	
        	res=""+TYPE_NEXT_SONG;
        	
        }else if(DETAIL_START_RECORDG_KEY.equals(preference.getKey())){
        	
        	res=""+TYPE_START_RECORD;

        	
        }else if(DETAIL_OPEN_APPLICATION.equals(preference.getKey())){
         Intent chooser = new Intent(Intent.ACTION_MAIN);   
         chooser.setClass(this.getActivity(), AllAppGridActivity.class);
            startActivityForResult(chooser,APP_PICK_REQ_CODE);  
        	
        	
        }
        if(!res.equals("")){
        	shEditor.putString(mainKey,res);
        	shEditor.commit();
        }
        dealForSpecialMusicKey(res,mainKey);
        if((DETAIL_NO_DEFINE_KEY+";"+ 		
        		DETAIL_JUST_TURN_SCREEN_KEY  +";"+ 		
        		DETAIL_TURN_SCREE_UNLOCK_KEY +";"+ 	
        		DETAIL_OPEN_CAMERA_KEY		 +";"+ 		
        		DETAIL_PLAY_PLAUSE_KEY 		 +";"+ 		
        		DETAIL_PRE_SONG_KEY 		 +";"+ 		
        		DETAIL_NEXT_SONG_KEY 		 +";"+ 		
        		DETAIL_START_RECORDG_KEY	).contains(preference.getKey())
){
        		this.getActivity().finish();
        	
        }
        
    	return true;
	
    }
private void dealForSpecialMusicKey(String op,String key){
	String pre_suf="";
	if((pre_suf=converseKey(key)).equals("")){
		return ;
	}
	File preFile=new File("data/data/com.android.systemui/"+pre_suf+MUSIC_OP_PRE);
	File nextFile=new File("data/data/com.android.systemui/"+pre_suf+MUSIC_OP_NEXT);
	File playPauseFile=new File("data/com.android.systemui/"+pre_suf+MUSIC_PLAY_OR_PAUSE);
	
if(preFile.exists()){
	preFile.delete();
}

if(nextFile.exists()){
	nextFile.delete();
}

if(playPauseFile.exists()){
	playPauseFile.delete();
	}
File resKeyFile=null;
	if((TYPE_PRE_SONG+"").equals(op)){
		resKeyFile=preFile;
		}
	if((TYPE_NEXT_SONG+"").equals(op)){
		resKeyFile=nextFile;
		}
if((TYPE_PLAY_PLAUSE+"").equals(op)){
		resKeyFile=playPauseFile;
		}
	if(resKeyFile==null){
		return ;
	}
		try{
			Runtime runtime = Runtime.getRuntime();

			resKeyFile.createNewFile();
			Process proc = runtime.exec("chmod 777 "+resKeyFile.getAbsolutePath());   
		}catch(Exception e){
			e.printStackTrace();
			
		}
	
	//File resKeyFile=new File("data/data/com.zyt.close_gesture_sttings/"+key);
	
	}
public static String converseKey(String mainKey){
	if((MyAllKeyPrefsFragment.DOUBLE_CLICK_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "u";
	}
	if((MyAllKeyPrefsFragment.UP_SLIP_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "up";
	}
	if((MyAllKeyPrefsFragment.DOWN_SLIP_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "down";
	}
	if((MyAllKeyPrefsFragment.LEFT_SLIP_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "left";
	}
	if((MyAllKeyPrefsFragment.RIGHT_SLIP_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "right";
	}
	if((MyAllKeyPrefsFragment.LETTER_O_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "o";
	}
	if((MyAllKeyPrefsFragment.LETTER_W_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "w";
	}
	if((MyAllKeyPrefsFragment.LETTER_M_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "m";
	}
	if((MyAllKeyPrefsFragment.LETTER_E_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "e";
	}
	if((MyAllKeyPrefsFragment.LETTER_C_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "c";
	}
	if((MyAllKeyPrefsFragment.LETTER_S_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "s";
	}
	if((MyAllKeyPrefsFragment.LETTER_V_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "v";
	}
	if((MyAllKeyPrefsFragment.LETTER_Z_KEY+MyAllKeyPrefsFragment.KEY_SUFFIX).equals(mainKey)){
		return "z";
	}
	return "";
	
	}

}
