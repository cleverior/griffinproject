package com.zyt.close_gesture_sttings;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceScreen;
import android.provider.ContactsContract;
import android.text.TextUtils;
import com.android.systemui.R;

public class ContactObserver extends ContentObserver{
    
    private Context mContext  ;  
    private Handler mHandler ;
    Resources mResources;
	Editor shEditor;
    SharedPreferences sh ;
	public ContactObserver(Context c,Handler handler) {
		super(handler);
		// TODO Auto-generated constructor stub
		this.mHandler=handler;
		this.mContext=c;
		sh=c.getSharedPreferences(MainActivity.SHARED_FILE,Context.MODE_PRIVATE);
		android.util.Log.d("zhangpeifu", "ContactObserver     ");
		mResources=c.getResources();
		shEditor=mContext.getSharedPreferences(MainActivity.SHARED_FILE,
	            Context.MODE_PRIVATE).edit();
	}

	
@SuppressLint("NewApi")
@Override
public void onChange(boolean selfChange, Uri uri) {
	// TODO Auto-generated method stub
	super.onChange(selfChange, uri);
android.util.Log.d("zhangpeifu", "ContactObserver     "+uri+"        "+selfChange);
dispose();
}

public void dispose(){
	int preActionId=R.string.gesture_pre_action_not_define;//没有定义

	for(int i=0;i<MyAllKeyPrefsFragment.KYE_SIZE;i++){
		preActionId=mResources.getIdentifier(MyAllKeyPrefsFragment.KYE[i]+MyAllKeyPrefsFragment.DEF_ACTION_SUFFIX, "string","com.android.systemui");//预置的动作的字符串id
		
		disposeContactChanged(MyAllKeyPrefsFragment.KYE[i]+MyAllKeyPrefsFragment.KEY_SUFFIX,sh.getString(MyAllKeyPrefsFragment.KYE[i]+MyAllKeyPrefsFragment.KEY_SUFFIX, mResources.getString(preActionId)));
				
		
		
	
	}	
}

public static  void doSaveContact(String data,String key,Editor editor){
	
	editor.putString(key,data);
	editor.commit();
	
}


//解析联系人的key  就算是预置的也会保存为sh
public void disposeContactChanged(String key,String value){
	android.util.Log.d("zhangpeifu","ContactObserver       "+key+"   value    "+value);
	String para[]=value.split(";");
	int type=Integer.valueOf(para[0]);
	if(type==MygestureDetailSetPrefsFragment.TYPE_CALL_SOMEONE||type==MygestureDetailSetPrefsFragment.TYPE_SEND_MSG_TO_SOMEONE){
		Uri uri=Uri.parse(para[3]);
		 ContentResolver cr =mContext.getContentResolver();
	        Cursor cursor = cr.query(uri, null, null, null, null);
	        	android.util.Log.d("zhangpeifu","disposeContactChanged     don't exist   "+cursor.getCount()) ; 
			if(cursor.getCount()==0){
				//delete saved  Contact;
				doSaveContact(MygestureDetailSetPrefsFragment.TYPE_NO_DEFINE+"",key,shEditor);
				return;
			}
	       // if(cursor==null)  {android.util.Log.d("zhangpeifu","disposeContactChanged     don't exist") ; return;}
	      String contactName="";
	        while (cursor.moveToNext()) {
	            // 取得联系人名字
	            int nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
	             contactName = cursor.getString(nameColumnIndex);
	            // contactId=cursor.getString(idColumnIndex);
	           
	        }
	        
	        cursor.moveToFirst();
		
		String num=MygestureDetailSetPrefsFragment.getContactPhone(cursor,cr);
		
		// 使用contain的原因是就算是客户更新了联系人的号码的顺序（原来是手机，改成了home，但是这个号码始终属于这个联系人，拨号时已经已联系人的这个号码为主）
		if(!num.contains(para[2])){
			//delete the save number
			doSaveContact(MygestureDetailSetPrefsFragment.TYPE_NO_DEFINE+"",key,shEditor);
			return ;

		}
		if(num.contains(para[2])&&!para[1].equals(contactName)&&!TextUtils.isEmpty(contactName)){
			// update contact name
			doSaveContact(type+";"+contactName+";"+para[2]+";"+para[3],key,shEditor);
			return ;
			
		}
		
		
	}
	
}


	
}
