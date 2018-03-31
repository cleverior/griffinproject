package com.zyt.close_gesture_sttings;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import com.android.systemui.R;
import android.os.SystemProperties;
public class GestureBroadcastReceiver extends BroadcastReceiver{
	
	
public  void updateForAppKey(Context c,String pkgName){
		
		android.util.Log.d("zhangpeifu","updateForAppKey   "+pkgName);
		Resources mResources=c.getResources();
		
	    SharedPreferences sh=c.getSharedPreferences(MainActivity.SHARED_FILE,Context.MODE_PRIVATE);
	   
		int preActionId=R.string.gesture_pre_action_not_define;//没有定义
		
		for(int i=0;i<MyAllKeyPrefsFragment.KYE_SIZE;i++){
			preActionId=mResources.getIdentifier(MyAllKeyPrefsFragment.KYE[i]+MyAllKeyPrefsFragment.DEF_ACTION_SUFFIX, "string","com.android.systemui");//预置的动作的字符串id
			
				disposeAppChanged(pkgName,sh,MyAllKeyPrefsFragment.KYE[i]+MyAllKeyPrefsFragment.KEY_SUFFIX,sh.getString(MyAllKeyPrefsFragment.KYE[i]+MyAllKeyPrefsFragment.KEY_SUFFIX, mResources.getString(preActionId)));					
			
			
		}	
		
		
	}
	
	public  void disposeAppChanged(String pkgName,SharedPreferences sh,String key,String value){
		Editor shEditor=sh.edit();
		String para[]=value.split(";");
		int type=Integer.valueOf(para[0]);
		if(type==MygestureDetailSetPrefsFragment.TYPE_OPEN_APP){
			if(para[2].equals(pkgName)){
					
				shEditor.putString(key,MygestureDetailSetPrefsFragment.TYPE_JUST_TURN_SCREEN+"");
				
			}
			
			
		}
				
	} 


	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action =intent.getAction();
		
		  Message message = new Message();   
		
		if(Intent.ACTION_BOOT_COMPLETED.equals(action)||"com.zyt.gesture_test".equals(action)){
			android.util.Log.d("zhangpeifu","action    "+action);
			
			if (SystemProperties.get("ro.product.tp_ges", "false").equals("true")) {
			context.startService(new Intent(context,GestureService.class));
			}
		}
		
		

        if (Intent.ACTION_PACKAGE_CHANGED.equals(action)
        		||Intent.ACTION_PACKAGE_REPLACED.equals(action)
                || Intent.ACTION_PACKAGE_REMOVED.equals(action)) {
        	
        	String packageName=intent.getData().getSchemeSpecificPart();
        	if (packageName == null || packageName.length() == 0) {
                //bad intent
                return;
            }
        	updateForAppKey(context,packageName);
        }
		
		
		
		if("com.zyt.gesture_test".equals(action)){
			
			// cb=new ContactObserver(context, mHandler);
				//context.getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, cb);
				
		}
		
		
		
	}

}
