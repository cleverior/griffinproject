package com.zyt.close_gesture_sttings;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;

@SuppressLint("NewApi")
public class MainActivity extends  Activity {
public static final String SHARED_FILE="gesture_shared_file";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content,
                new MyAllKeyPrefsFragment()).commit();
	}



}
