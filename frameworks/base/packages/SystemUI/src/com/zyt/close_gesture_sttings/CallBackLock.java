package com.zyt.close_gesture_sttings;

import com.android.internal.policy.IKeyguardService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;

public class CallBackLock {
    public static final String KEYGUARD_PACKAGE = "com.android.keyguard";
    public static final String KEYGUARD_CLASS = "com.android.keyguard.KeyguardService";
    public IKeyguardService  kyeguarService;
    public boolean hasbind=false;
    Context mContext;
    public CallBackLock(Context context){
    	  Intent intent = new Intent();
    	  mContext=context;
          intent.setClassName(KEYGUARD_PACKAGE, KEYGUARD_CLASS);
          if (!context.bindService(intent, mKeyguardConnection,
                  Context.BIND_AUTO_CREATE)) {
        	  android.util.Log.d("zhangpeifu","bindfial");
        	  hasbind=false;
          } else {
        	  hasbind=true;
        	  android.util.Log.d("zhangpeifu","success");
          }
    }
    
    public void unbindService(){
    	mContext.unbindService(mKeyguardConnection);
    }
    public void callbackKeyguard(){
    	if(hasbind){
    		try {
	        	  android.util.Log.d("zhangpeifu","callbackKeyguard    go");
				kyeguarService.doKeyguardTimeout(null);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
	        	  android.util.Log.d("zhangpeifu","callbackKeyguard    "+e);
			}
    		
    	}
    	
    }
    ServiceConnection mKeyguardConnection = new ServiceConnection()
    {

    	@Override
    	public void onServiceConnected(ComponentName name, IBinder service) {
    		
    		kyeguarService = IKeyguardService.Stub.asInterface(service);
    		//myService = (IMediaPlaybackService)service;//Wrong !!!跨进程不可行
    	}
    	@Override
    	public void onServiceDisconnected(ComponentName name) {
    		mKeyguardConnection = null;
    		hasbind=true;
    	}
    };
    
}
