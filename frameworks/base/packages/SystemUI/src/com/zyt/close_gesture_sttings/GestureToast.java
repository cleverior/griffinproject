package com.zyt.close_gesture_sttings;

import android.content.Context;
import android.widget.Toast;

public class GestureToast extends Toast {

	public GestureToast(Context context ,int resId) {
		super(context);
		// TODO Auto-generated constructor stub
		GestureToast.makeText(context, context.getResources().getString(resId), Toast.LENGTH_SHORT).show();

	}

	
	
}
