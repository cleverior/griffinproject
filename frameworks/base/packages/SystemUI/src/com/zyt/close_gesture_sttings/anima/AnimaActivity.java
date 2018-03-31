package com.zyt.close_gesture_sttings.anima;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.zyt.close_gesture_sttings.GestureService;
//import com.zyt.close_gesture_sttings.GestureService.MusicBinder;
import com.zyt.close_gesture_sttings.CallBackLock;
import com.zyt.close_gesture_sttings.IGestureMusicService;
import com.zyt.close_gesture_sttings.MainActivity;
import com.zyt.close_gesture_sttings.MyAllKeyPrefsFragment;
import com.zyt.close_gesture_sttings.MygestureDetailSetPrefsFragment;
import com.android.systemui.R;
import com.zyt.close_gesture_sttings.anima.CustomAnimDrawable.AnimationDrawableListener;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.view.IWindowManager;
import android.os.ServiceManager;

public class AnimaActivity extends Activity{
    private Context mContext;
    private ImageView mImageWidget;
    public  int GesturekeyCode ;
    private boolean flag;
    SharedPreferences sh ;
public static final String SEND_TO_POLICY_KEY_CODE_PARA_NAME="key_code";
    
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

CallBackLock mCbl;
//private IGestureMusicService gestureMusicService;  
//public boolean gestureMusicServiceBind=false;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
    requestWindowFeature(Window.FEATURE_NO_TITLE);  
	 getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); 
     getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
     getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
     setContentView(R.layout.frame_effect);
     mContext = this;
     GesturekeyCode = getIntent().getIntExtra("key_kode", -1);
     sh=mContext.getSharedPreferences(MainActivity.SHARED_FILE,Context.MODE_PRIVATE);
     mCbl=new CallBackLock(this);
     handleFrameEffect(GesturekeyCode);
   //  Intent intent = new Intent(this, GestureService.class);  
	//     if(!gestureMusicServiceBind){
	  //   gestureMusicServiceBind=bindService(intent, gestureMusicServiceConnection, BIND_AUTO_CREATE);  
	  //   }
     }
/*
ServiceConnection gestureMusicServiceConnection = new ServiceConnection()
{

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		
		MusicBinder mb  =((MusicBinder)service);
		gestureMusicService=mb.getService();
	}
	@Override
	public void onServiceDisconnected(ComponentName name) {
		gestureMusicServiceConnection = null;
		gestureMusicServiceBind=false;

	}
};
*/
@Override
protected void onResume() {
     super.onResume();				
 }
	
 @Override
 protected void onStop() {
     super.onStop();
 }
@Override
protected void onDestroy() {
	super.onDestroy();
	mCbl.unbindService();
}


 private void exit(){
     finish();
 }
 
 class FrameAnimationListener implements AnimationDrawableListener{
     @Override
     public void onAnimationEnd(AnimationDrawable animation) {
         
      
     }
     @Override
     public void onAnimationStart(AnimationDrawable animation) {
     
     }
 }
 
 
 
 
 private void handleFrameEffect(int code) {
	 forwardTo(code);
	 exit();
//		AnimationDrawable anim;
//		int animRes=KEY_ANIMA_MAP.get(code).intValue();
//		anim = (AnimationDrawable)getResources().getDrawable(animRes);
//
//		CustomAnimDrawable cusAnim = new CustomAnimDrawable(anim);
//		cusAnim.setAnimationListener(new FrameAnimationListener());
//	     mImageWidget = (ImageView) findViewById(R.id.imageSwitcher);
//		mImageWidget.setImageDrawable(anim);
//		cusAnim.start();
	 
//	  mImageWidget = (ImageView) findViewById(R.id.imageSwitcher);
//	  mImageWidget.setImageDrawable(getResources().getDrawable(R.drawable.smart_key_o_11));
//	  final AlphaAnimation animation = new AlphaAnimation(0, 1);
//	  animation.setDuration(2000);//设置动画持续时间 
//	  animation.setFillAfter(true);
//	  animation.setAnimationListener(new AnimationListener() {
//		
//		@Override
//		public void onAnimationStart(Animation animation) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void onAnimationRepeat(Animation animation) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void onAnimationEnd(Animation animation) {
//			// TODO Auto-generated method stub
//			 forwardTo(GesturekeyCode);
//			 	
//
//		     exit();
//		}
//	});
//	  mImageWidget.setAnimation(animation); 

	  
//  final AlphaAnimation animation = new AlphaAnimation(0, 1);
//	  animation.setDuration(2000);//设置动画持续时间 
//	  animation.setFillAfter(true);
//	  LinearLayout ll=(LinearLayout) findViewById(R.id.frame_effect_layout);
//	  ll.setBackground(getResources().getDrawable(R.drawable.smart_key_o_11));
//	  ll.setAnimation(animation);
//	  animation.startNow(); 
	
	}
 
 public  String getSharedByCode(int code){
	 String key=KEY_KEYSTRING_MAP.get(code);
	 int preActionId=R.string.gesture_pre_action_not_define;//没有定义
	 preActionId=mContext.getResources().getIdentifier(key.replace(MyAllKeyPrefsFragment.KEY_SUFFIX, "")+MyAllKeyPrefsFragment.DEF_ACTION_SUFFIX, "string","com.android.systemui");
	 return  sh.getString(key, mContext.getResources().getString(preActionId));
 }
 
 
 public void forwardTo(int code){
 	flag = true;
 	android.util.Log.d("zhangpeifu","getSharedByCode(code)    "+getSharedByCode(code));
 	String value[]= getSharedByCode(code).split(";");
 	
 	int type=Integer.valueOf(value[0]);
 	
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
			 if(flag){ 
			this.startActivity(dialIntent);
			 }
			break;
	case MygestureDetailSetPrefsFragment.TYPE_SEND_MSG_TO_SOMEONE:
		
		Uri uri = Uri.parse("smsto:"+value[2]); 
		Intent mmsIntent = new Intent(Intent.ACTION_SENDTO, uri); 
		mmsIntent.putExtra("sms", ""); 
		 if(flag){ 
			 startActivity(mmsIntent); 
		 }		
		break;
	case MygestureDetailSetPrefsFragment.TYPE_GET_INTO_WEBSITE:
		
		Uri websiteUri = Uri.parse(value[2]);
		Intent websiteIntent = new Intent(Intent.ACTION_VIEW);
		websiteIntent.setData(websiteUri);     
		websiteIntent.setClassName("com.android.browser","com.android.browser.BrowserActivity");   
		 if(flag){ 
		startActivity(websiteIntent);
		 }
		break;
	case MygestureDetailSetPrefsFragment.TYPE_OPEN_CAMERA:
		Intent cameraIntent =new Intent(Intent.ACTION_MAIN);
		cameraIntent.setClassName("com.android.gallery3d", "com.android.camera.VideoCamera");   

		
		cameraIntent.addFlags(
	                Intent.FLAG_ACTIVITY_NEW_TASK
	                | Intent.FLAG_ACTIVITY_SINGLE_TOP
	                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		 if(flag){ 
				startActivity(cameraIntent);
				 }
		break;
	case MygestureDetailSetPrefsFragment.TYPE_PLAY_PLAUSE:
	//	if(gestureMusicServiceBind)
	//	gestureMusicService.musicPlayPlause();
	//	this.getApplicationContext().sendBroadcast(new Intent("com.zyt.gestureControlRec").putExtra(SEND_TO_POLICY_KEY_CODE_PARA_NAME, KeyEvent.KEYCODE_MEDIA_PAUSE));
		Intent openMusicIntent =new Intent(Intent.ACTION_MAIN);
		openMusicIntent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_SINGLE_TOP
                | Intent.FLAG_ACTIVITY_CLEAR_TOP);
  
		openMusicIntent.setComponent(new ComponentName("com.android.music","com.android.music.MusicBrowserActivity"));
		 if(flag){ 
				startActivity(openMusicIntent);
				 }
		break;
	case MygestureDetailSetPrefsFragment.TYPE_PRE_SONG:
		//if(gestureMusicServiceBind)
		//gestureMusicService.musicpre();
	//	this.getApplicationContext().sendBroadcast(new Intent("com.zyt.gestureControlRec").putExtra(SEND_TO_POLICY_KEY_CODE_PARA_NAME, KeyEvent.KEYCODE_MEDIA_PREVIOUS));
		sendVKeyDelay(KeyEvent.KEYCODE_MEDIA_PREVIOUS);		
		break;
	case MygestureDetailSetPrefsFragment.TYPE_NEXT_SONG:
		//if(gestureMusicServiceBind)
		//	gestureMusicService.musicNext();
		//this.getApplicationContext().sendBroadcast(new Intent("com.zyt.gestureControlRec").putExtra(SEND_TO_POLICY_KEY_CODE_PARA_NAME, KeyEvent.KEYCODE_MEDIA_NEXT));
		sendVKeyDelay(KeyEvent.KEYCODE_MEDIA_NEXT);		

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
		 if(flag){ 
				startActivity(openAppIntent);
				 }
		break;

	default:
		flag = false;
		break;
	}
 	

 }
 
 private void sendVKeyDelay(int key) {
	
		
 }
 
 
 
 
 
 
}
