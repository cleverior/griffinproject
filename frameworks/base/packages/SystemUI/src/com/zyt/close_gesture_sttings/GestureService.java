package com.zyt.close_gesture_sttings;

import java.util.List;


import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.ContactsContract;

public class GestureService extends Service {
	ContactObserver cb;
    public static final int MSG_APP_CHANGED		=100;
    public static final String APP_CHANGED_PARA = "package_name";
    public static Context mContext;
    public static boolean musicBinderFlag=false;
  //  IMediaPlaybackService musicService = null;
    
   // private final IBinder musicBinder = new MusicBinder();  

  /*  
    ServiceConnection musicServiceConnection = new ServiceConnection()
    {

    	@Override
    	public void onServiceConnected(ComponentName name, IBinder service) {
    		
    		musicService = IMediaPlaybackService.Stub.asInterface(service);
    		//myService = (IMediaPlaybackService)service;//Wrong !!!跨进程不可行
    	}
    	@Override
    	public void onServiceDisconnected(ComponentName name) {
    		musicServiceConnection = null;
    	}
    };
    */
    
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		android.util.Log.d("zhangpeifu","onBind    ");
		return null;//musicBinder
	}


	
	

	
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		cb=new ContactObserver(this, new Handler());
		this.getContentResolver().registerContentObserver(ContactsContract.Contacts.CONTENT_URI, true, cb);
		mContext=this.getApplicationContext();
		android.util.Log.d("zhangpeifu","GestureService onCreate   registerContentObserver ");
		//if(!musicBinderFlag){
		//	musicBinderFlag = bindService(new Intent("com.android.music.IMediaPlaybackService"),musicServiceConnection, Context.BIND_AUTO_CREATE);
		//}   
		//android.util.Log.d("zhangpeifu","musicBinderFlag    "+musicBinderFlag);
		
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	
		this.getContentResolver().unregisterContentObserver(cb);
		android.util.Log.d("zhangpeifu","GestureService onDestroy   unregisterContentObserver ");
		//if(musicBinderFlag){	
			//unbindService(musicServiceConnection);
			//musicBinderFlag=false;
		//}
	}

	
	
 
/*
	  public class MusicBinder extends Binder {  
		public  GestureService getService() {  
	            // 返回Activity所关联的Service对象，这样在Activity里，就可调用Service里的一些公用方法和公用属性  
	            return GestureService.this;  
	        }  
	
	@Override
	public void musicPlayPlause() {
		// TODO Auto-generated method stub
			if(musicBinderFlag){
				try {
					if(musicService.isPlaying()){
						musicService.pause();
						
					}
					else{
						musicService.play();
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	@Override
	public void musicNext() {
		// TODO Auto-generated method stub
		if(musicBinderFlag){
			
			try {
				
					musicService.next();
					
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void musicpre() {
		// TODO Auto-generated method stub
	if(musicBinderFlag){
			
			try {
				
					musicService.prev();
					
				
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
	

	

}

