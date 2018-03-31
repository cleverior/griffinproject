package com.zyt.close_gesture_sttings.anima;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.Animation;
 
public class CustomAnimDrawable extends AnimationDrawable {
	private final String TAG = "nuonuo";
	private AnimationDrawable mOriAnim;
	private AnimationDrawable mSelf;
	private Handler mHandler;
	private boolean mStarted;
	private AnimEndListenerRunnable mEndRunnable;
	private AnimationDrawableListener mListener;

	public CustomAnimDrawable(AnimationDrawable anim) {
		mOriAnim = anim;
		initialize();
	}

	private void initialize() {
		mSelf = this;
		mStarted = false;
		mHandler = new Handler();
		mEndRunnable = new AnimEndListenerRunnable();
		for (int i = 0; i < mOriAnim.getNumberOfFrames(); i++) {
			mSelf.addFrame(mOriAnim.getFrame(i), mOriAnim.getDuration(i));
		}
	}

	@Override
	public void start() {
		mOriAnim.start();
		mStarted = true;
		mHandler.post(mEndRunnable);
		if (mListener != null) {
			mListener.onAnimationStart(mSelf);
		}
		Log.v(TAG, "------CustomAnimDrawable------>start");
	}
	
	class AnimEndListenerRunnable implements Runnable {
		@Override
		public void run() {
			if (!mStarted) {
				return;
			}
			if (!isEnd()) {
				mHandler.postDelayed(mEndRunnable,50);
				return;
			}
			Log.v(TAG, "----------->over");
			if (mListener != null) {
				mStarted = false;
				mListener.onAnimationEnd(mSelf);
			}
		}
	}
	private boolean isEnd(){
		Class<AnimationDrawable> animClass = AnimationDrawable.class;
		try{  
			Field field = animClass.getDeclaredField("mCurFrame");
	        field.setAccessible(true);
	        
	        int currFrameNum = field.getInt(mOriAnim);
	        int totalFrameNum = mOriAnim.getNumberOfFrames();
	        if((currFrameNum == totalFrameNum - 1)||
	           (currFrameNum == -1)){
	        	return true;
	        }
		}
		catch (Exception e) {
			Log.v(TAG,"-------->Exception");
		}
		
		return false;
	}

	public void setAnimationListener(AnimationDrawableListener listener) {
		mListener = listener;
	}
	
	public interface AnimationDrawableListener {
		public void onAnimationStart(AnimationDrawable animation);
		public void onAnimationEnd(AnimationDrawable animation);
	}
}