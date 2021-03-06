package com.zyt.close_gesture_sttings;

import java.util.HashMap;
import java.util.Map;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.view.KeyEvent;
import java.io.File;
import com.android.systemui.R;
import java.io.IOException;

import android.os.AsyncTask;

import android.hardware.input.InputManager;
import android.os.SystemClock;

import java.util.Timer;
import java.util.TimerTask;

public class GestureDelegate {
public static final int CALL_CODE_FROM_KEYGUARD=100;
public static final int CALL_JUST_TURN_ON_SCREEN_FROM_KEYGUARD=101;

public static final String GESTURE_KEY_CODE="key_code";
public static final int INVALID_KEY_VALUE=-1;
public SharedPreferences sh ;
public static final String PARA_FOR_GET_BINDER="for_binder";
public static final String PARA_TO_KEYGUARD_JUST_TURN="just_turn";
public static final String IS_FIRST_RUN_KEY="is_first_run";
public static final int RETURN_PHONE_WINDOW_HAND=103;//在keyguard中定义的值
public static final boolean IS_LOG=true;
public Context mContext;
public GestureSystemCallBack mGestureSystemCallBack;
public static final Map<Integer, Integer> KEY_ANIMA_MAP=new HashMap<Integer, Integer>();
public static final Map<Integer, String> KEY_KEYSTRING_MAP=new HashMap<Integer, String>();
static{
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_DPAD_UP,		 R.drawable.openlogoframe_keyl2h);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_DPAD_DOWN,	 R.drawable.openlogoframe_keyh2l);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_DPAD_LEFT, 	 R.drawable.openlogoframe_keyr2l);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_DPAD_RIGHT,	 R.drawable.openlogoframe_keyl2r);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_U, 			 R.drawable.openlogoframe_keyc);//双击 随便写一个
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_O, 			 R.drawable.openlogoframe_keyo);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_W, 			 R.drawable.openlogoframe_keyw);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_C, 			 R.drawable.openlogoframe_keyc);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_E, 			 R.drawable.openlogoframe_keye);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_M, 			 R.drawable.openlogoframe_keym);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_S,			 R.drawable.openlogoframe_keys);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_W, 			 R.drawable.openlogoframe_keyw);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_V, 			 R.drawable.openlogoframe_keyv);
	KEY_ANIMA_MAP.put(KeyEvent.KEYCODE_Z, 			 R.drawable.openlogoframe_keyz);
	
	
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_DPAD_UP,		MyAllKeyPrefsFragment.UP_SLIP_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX);
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_DPAD_DOWN,	MyAllKeyPrefsFragment.DOWN_SLIP_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX);	
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_DPAD_LEFT,	MyAllKeyPrefsFragment.LEFT_SLIP_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX); 	
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_DPAD_RIGHT,	MyAllKeyPrefsFragment.RIGHT_SLIP_KEY	+MyAllKeyPrefsFragment.KEY_SUFFIX);	
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_U, 			MyAllKeyPrefsFragment.DOUBLE_CLICK_KEY	+MyAllKeyPrefsFragment.KEY_SUFFIX);		
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_O,			MyAllKeyPrefsFragment.LETTER_O_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX); 		
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_W, 			MyAllKeyPrefsFragment.LETTER_W_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX);		
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_C, 			MyAllKeyPrefsFragment.LETTER_C_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX);		
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_E, 			MyAllKeyPrefsFragment.LETTER_E_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX);	
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_M,			MyAllKeyPrefsFragment.LETTER_M_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX);		
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_S,			MyAllKeyPrefsFragment.LETTER_S_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX);		
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_W,			MyAllKeyPrefsFragment.LETTER_W_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX); 		
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_V,			MyAllKeyPrefsFragment.LETTER_V_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX);		
	KEY_KEYSTRING_MAP.put(KeyEvent.KEYCODE_Z,			MyAllKeyPrefsFragment.LETTER_Z_KEY		+MyAllKeyPrefsFragment.KEY_SUFFIX); 		
}


	public GestureDelegate(Context c,GestureSystemCallBack back){
		mContext =c;
		mGestureSystemCallBack = back;

		forFirstRun();
	}
	public void doForGesture(Bundle data){
		
		//int keyCode=data.getInt(GESTURE_KEY_CODE, INVALID_KEY_VALUE);
		//android.util.Log.d("zyt.zhangpeifu.gesture5.1","GestureDelegate doForGesture   keyCode    "+keyCode);
			if(data==null) return;
			int keyCode=data.getInt(GESTURE_KEY_CODE, INVALID_KEY_VALUE);
			android.util.Log.d("zyt.zhangpeifu.gesture5.1","GestureDelegate doForGesture   keyCode    "+keyCode);

				//
				android.util.Log.d("gesture","CALL_JUST_TURN_ON_SCREEN_FROM_KEYGUARD   ");
				//Bundle toKeyguardData= new Bundle();
				data.putBoolean(PARA_TO_KEYGUARD_JUST_TURN, justTurnonScreen(keyCode));
				
				disposeKeyCode(keyCode);
				try {
				//	android.util.Log.d("gesture","will return to  keyguard   ");
printLog("will return to  keyguard   ");

					if(mGestureSystemCallBack!=null){
						mGestureSystemCallBack.systemuiCallBack(data);
						
						}
				} catch (Exception e) {
					//android.util.Log.d("gesture"," return to  RemoteException   "+e);
						printLog(" return to  RemoteException   "+e);
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		 
		 


	}
	

public  String getSharedByCode(int code){
	 String key=KEY_KEYSTRING_MAP.get(code);
	 int preActionId=R.string.gesture_pre_action_not_define;//没有定义
	 preActionId=mContext.getResources().getIdentifier(key.replace(MyAllKeyPrefsFragment.KEY_SUFFIX, "")+MyAllKeyPrefsFragment.DEF_ACTION_SUFFIX, "string","com.android.systemui");
     sh=mContext.getSharedPreferences(MainActivity.SHARED_FILE,Context.MODE_PRIVATE);
     if(sh==null){
    	 return  mContext.getResources().getString(preActionId);//如果文件还没有创建
     }
     return  sh.getString(key, mContext.getResources().getString(preActionId));//文件已经创建了，但是没有那个值，或者有那个值。
}


public void disposeKeyCode(int code){
	android.util.Log.d("zhangpeifu","getSharedByCode(code)    "+getSharedByCode(code));
	String value[]= getSharedByCode(code).split(";");
	int type;
	try{
	 type=Integer.valueOf(value[0]);
	}catch (Exception e) {
		// TODO: handle exception
		type=-1;
	}
	android.util.Log.d("tangyb","tangby ++++"+type);
	switch (type) {
	case MygestureDetailSetPrefsFragment.TYPE_NO_DEFINE:
		
		break;
	case MygestureDetailSetPrefsFragment.TYPE_JUST_TURN_SCREEN:
		//回调锁屏函数！！！
		//mCbl.callbackKeyguard();
		//this.sendBroadcast(new Intent("com.zyt.gesture_lockback"));
		break;
	case MygestureDetailSetPrefsFragment.TYPE_TURN_SCREEN_UNLOCK:
		//什么都不做
		break;
	case MygestureDetailSetPrefsFragment.TYPE_CALL_SOMEONE:
			String phoneNum=value[2];
			Intent dialIntent = new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+phoneNum));
			ComponentName  cn=new ComponentName("com.android.server.telecom", "com.android.server.telecom.components.UserCallActivity");
							dialIntent.setComponent(cn);
			dialIntent.addFlags(
	                Intent.FLAG_ACTIVITY_NEW_TASK
	                | Intent.FLAG_ACTIVITY_SINGLE_TOP
	                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mContext.startActivity(dialIntent);
			break;
	case MygestureDetailSetPrefsFragment.TYPE_SEND_MSG_TO_SOMEONE:
		
		Uri uri = Uri.parse("smsto:"+value[2]); 
		Intent mmsIntent = new Intent(Intent.ACTION_SENDTO, uri); 
		mmsIntent.putExtra("sms", ""); 
		mmsIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
			 mContext.startActivity(mmsIntent); 
		break;
	case MygestureDetailSetPrefsFragment.TYPE_GET_INTO_WEBSITE:
		
		Uri websiteUri = Uri.parse(value[2]);
        if(!value[2].toLowerCase().startsWith("http")) {
            websiteUri = Uri.parse("http://" + value[2]);
        }
		Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
		websiteIntent.setData(websiteUri);     
		websiteIntent.setClassName("com.android.browser","com.android.browser.BrowserActivity");   
		websiteIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mContext.startActivity(websiteIntent);
		 
		break;
	case MygestureDetailSetPrefsFragment.TYPE_OPEN_CAMERA:
		Intent cameraIntent =new Intent(Intent.ACTION_MAIN);
		
		cameraIntent.setClassName("com.android.camera2", "com.android.camera.CameraLauncher");   

		
		cameraIntent.addFlags(
	                Intent.FLAG_ACTIVITY_NEW_TASK
	                | Intent.FLAG_ACTIVITY_SINGLE_TOP
	                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(cameraIntent);
		break;
	case MygestureDetailSetPrefsFragment.TYPE_PLAY_PLAUSE:
	//	Intent openMusicIntent =new Intent(Intent.ACTION_MAIN);
	//	openMusicIntent.addFlags(
          //     Intent.FLAG_ACTIVITY_NEW_TASK
            //   | Intent.FLAG_ACTIVITY_SINGLE_TOP
              // | Intent.FLAG_ACTIVITY_CLEAR_TOP);
 
		//openMusicIntent.setComponent(new ComponentName("com.android.music","com.android.music.MusicBrowserActivity"));
		//		startActivity(openMusicIntent);
		new DoKeyPressTask().execute(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
		break;
	case MygestureDetailSetPrefsFragment.TYPE_PRE_SONG:
		//if(gestureMusicServiceBind)
		//gestureMusicService.musicpre();
	//	this.getApplicationContext().sendBroadcast(new Intent("com.zyt.gestureControlRec").putExtra(SEND_TO_POLICY_KEY_CODE_PARA_NAME, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
		new DoKeyPressTask().execute(KeyEvent.KEYCODE_MEDIA_PREVIOUS);
		break;
	case MygestureDetailSetPrefsFragment.TYPE_NEXT_SONG:
		//if(gestureMusicServiceBind)
		//	gestureMusicService.musicNext();
		//this.getApplicationContext().sendBroadcast(new Intent("com.zyt.gestureControlRec").putExtra(SEND_TO_POLICY_KEY_CODE_PARA_NAME, KeyEvent.KEYCODE_MEDIA_NEXT));
		new DoKeyPressTask().execute(KeyEvent.KEYCODE_MEDIA_NEXT);
		break;
	case MygestureDetailSetPrefsFragment.TYPE_START_RECORD:
		
		break;
	case MygestureDetailSetPrefsFragment.TYPE_OPEN_APP:
		Intent openAppIntent =new Intent(Intent.ACTION_MAIN);
		openAppIntent.addFlags(
               Intent.FLAG_ACTIVITY_NEW_TASK
               | Intent.FLAG_ACTIVITY_SINGLE_TOP
               | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		openAppIntent.setComponent(new ComponentName(value[2], value[3]));
				mContext.startActivity(openAppIntent);
		break;

	default:
		break;
	}
	

}



public boolean isMusicControlKey(int code){
		String value[]= getSharedByCode(code).split(";");
		//int type=Integer.valueOf(value[0]);
		try{
			return (Integer.valueOf(value[0])==MygestureDetailSetPrefsFragment.TYPE_PLAY_PLAUSE)||
			(Integer.valueOf(value[0])== MygestureDetailSetPrefsFragment.TYPE_PRE_SONG)|| 
			(Integer.valueOf(value[0])== MygestureDetailSetPrefsFragment.TYPE_NEXT_SONG);
		}catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}

	
public static void printLog(String content){
if(IS_LOG){
					android.util.Log.d("zyt.zhangpeifu.gesture5","  "+content);

}
}	 

/*	
	 private final IServiceForKeyguardCallBack.Stub mBinder = new IServiceForKeyguardCallBack.Stub() {

		@Override
		public boolean justTurnScreen(int code) throws RemoteException {
			// TODO Auto-generated method stub
			return justTurnonScreen(code);
		}       
	       
	    };  
	*/
	public boolean justTurnonScreen(int code){
		String value[]= getSharedByCode(code).split(";");
		//int type=Integer.valueOf(value[0]);
		try{
			return Integer.valueOf(value[0])==MygestureDetailSetPrefsFragment.TYPE_JUST_TURN_SCREEN;
		}catch (Exception e) {
			// TODO: handle exception
			return false;
		}
		
	}
	

	
	private void forFirstRun(){
		SharedPreferences sh_pre=mContext.getSharedPreferences(IS_FIRST_RUN_KEY,
	            Context.MODE_PRIVATE );
	            		printLog("forFirstRun    "+sh_pre.getString(IS_FIRST_RUN_KEY, "yes").equals("yes"));
   			SharedPreferences.Editor  shEditor = sh_pre.edit();	
	        if(sh_pre.getString(IS_FIRST_RUN_KEY, "yes").equals("yes")){
	        	//frist run

            boolean def_switch = "1".equals(mContext.getResources().getString(R.string.function_default));
            if(!def_switch){
                if(!MyAllKeyPrefsFragment.switchFile.exists()){
                    try {
                        Runtime runtime = Runtime.getRuntime();
                        MyAllKeyPrefsFragment.switchFile.createNewFile();
                        Process proc = runtime.exec("chmod 777 "+MyAllKeyPrefsFragment.switchFile.getAbsolutePath());   
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

	   /*for(int i=0;i<MyAllKeyPrefsFragment.KYE_SIZE;i++){
	   	String key=MyAllKeyPrefsFragment.KYE[i];     	
	   int preActionId=R.string.gesture_pre_action_not_define;
	 preActionId=mContext.getResources().getIdentifier(key+MyAllKeyPrefsFragment.DEF_ACTION_SUFFIX, "string","com.android.systemui");
	        	
	        	String value[]= mContext.getResources().getString(preActionId).split(";");
	int type;
	try{
	 type=Integer.valueOf(value[0]);
	}catch (Exception e) {
		// TODO: handle exception
		type=-1;
	}
		   printLog("forFirstRun   type    "+type);

	String resFileName=null;
	        	if(type==MygestureDetailSetPrefsFragment.TYPE_PRE_SONG){
	        	resFileName="data/data/com.android.systemui/"+MygestureDetailSetPrefsFragment.converseKey(key+MyAllKeyPrefsFragment.KEY_SUFFIX)+MygestureDetailSetPrefsFragment.MUSIC_OP_PRE;
	        		}
	        	if(type==MygestureDetailSetPrefsFragment.TYPE_NEXT_SONG){
   			resFileName="data/data/com.android.systemui/"+MygestureDetailSetPrefsFragment.converseKey(key+MyAllKeyPrefsFragment.KEY_SUFFIX)+MygestureDetailSetPrefsFragment.MUSIC_OP_NEXT;

	        		}
	        	if(type==MygestureDetailSetPrefsFragment.TYPE_PLAY_PLAUSE){
   			resFileName="data/data/com.android.systemui/"+MygestureDetailSetPrefsFragment.converseKey(key+MyAllKeyPrefsFragment.KEY_SUFFIX)+MygestureDetailSetPrefsFragment.MUSIC_PLAY_OR_PAUSE;

	        		}
	        			   printLog("forFirstRun   resFileName    "+resFileName);
	
	        if(resFileName==null){
	        	continue ;
	        	}	
	       	File resFile=new File(resFileName);
 		if(resFile.exists()){
			resFile.delete();
		}

		try{
			Runtime runtime = Runtime.getRuntime();

			resFile.createNewFile();
			Process proc = runtime.exec("chmod 777 "+resFile.getAbsolutePath());   
		}catch(Exception e){
			e.printStackTrace();
			
		}
	        			   printLog("forFirstRun   runtime    ");

	        }*/	
	   printLog("forFirstRun   shEditor    "+shEditor);
		 shEditor.putString(IS_FIRST_RUN_KEY, "no");
		 shEditor.commit();
	        	}
	            
		
		//	shEditor.putString(IS_FIRST_RUN_KEY,"yes");
			
		}
	
	
	class DoKeyPressTask extends AsyncTask<Integer, Void, Void>{

		@Override
		protected Void doInBackground(Integer... params) {
			// TODO Auto-generated method stub
			int keycode = params[0];
			sendKeyEvent(keycode);
			return null;
		}
		
		
		
	};


			
			public void sendKeyEvent(int keyCode) {
			
				try{
                   android.util.Log.d("tangyb","tangby sendKeyEvent"+keyCode);
InputManager.getInstance().injectInputEvent(new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keyCode, 0),
             InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
InputManager.getInstance().injectInputEvent(new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, keyCode, 0),
        InputManager.INJECT_INPUT_EVENT_MODE_WAIT_FOR_FINISH);
     }
     catch(Exception e){
     	 android.util.Log.d("ssssdddd","Exception    "+e);
     	}
			}


	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
