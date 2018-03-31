package com.zyt.close_gesture_sttings;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import com.android.systemui.R;
public class WebsiteSelectActivity extends ListActivity implements View.OnClickListener{
	  private ArrayList<HashMap<String, Object>>   listItems;     
	  private SimpleAdapter listItemAdapter;           
	public static final int DEF_WEB_SITE_SIZE=5;
	public static final String ADDR_KEY="addr";
	public static final String NAME_KEY="name";
	public static final String ICON_KEY="icon";
	Button confirmBtn;
	EditText et;
	    @Override
		protected void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.web_input_list);
			initListView();
			confirmBtn=(Button) findViewById(R.id.confirm_btn);
			confirmBtn.setOnClickListener(this);
			et=(EditText) findViewById(R.id.web_site_input);
		}
			
	    @Override
		protected void onListItemClick(ListView l, View v, int position, long id) {
	    	
	    	if(listItems!=null){
	    		Intent ret=new Intent();
	    		ret.putExtra(MygestureDetailSetPrefsFragment.PARA_SITE_NAME, listItems.get(position).get(NAME_KEY).toString());
	    		ret.putExtra(MygestureDetailSetPrefsFragment.PARA_SITE_ADDR, listItems.get(position).get(ADDR_KEY).toString());
android.util.Log.d("zhangpeifu","      "+listItems.get(position).get(NAME_KEY).toString()+"          "+listItems.get(position).get(ADDR_KEY).toString());
				setResult(Activity.RESULT_OK, ret);
this.finish();
	    	}
	    	
	    }
		
		   private void initListView()   {   
			   if(!this.getResources().getBoolean(R.bool.config_has_pre_website)){
				   return ;
				   
			   }
			   
		        listItems = new ArrayList<HashMap<String, Object>>();
		        String[] def_website_addr = getResources().getStringArray(R.array.def_web_site_addr);
		        String[] def_website_name = getResources().getStringArray(R.array.def_web_site_name);
		        String[] def_website_icon = getResources().getStringArray(R.array.def_web_site_icon);
		        
				Class drawable  =  R.drawable.class;
				  Field field = null;
				 
				  
		        for(int i=0;i<DEF_WEB_SITE_SIZE;i++)    {   
		            HashMap<String, Object> map = new HashMap<String, Object>();   
		            
		            map.put(ADDR_KEY, def_website_addr[i]);    
		            map.put(NAME_KEY, def_website_name[i]);
		            
		            //;

		            try {
		                //field = drawable.getField(def_website_icon[i]);
		                //int r_id = field.getInt(field.getName());
			            map.put(ICON_KEY, getResources().getIdentifier(def_website_icon[i], "drawable", "com.android.systemui"));
		              //  android.util.Log.d("zhangpeifu", "r_id     "+r_id);

		                //iv.setBackgroundResource(r_id);
		            } catch (Exception e) {
			            map.put(ICON_KEY, 0);
		               // android.util.Log.d("zhangpeifu", "PICTURE NOT　FOUND！");
		            }
		            
		            listItems.add(map);   
		        }   
		        listItemAdapter = new SimpleAdapter(this,listItems,   // listItems数据源    
		                R.layout.website_item,  //ListItem的XML布局实现   
		                new String[] {ICON_KEY,NAME_KEY,ADDR_KEY},     //动态数组与ImageItem对应的子项          
		                new int[ ] {R.id.icon_iv, R.id.web_site_name,R.id.web_site_addr}      //list_item.xml布局文件里面的一个ImageView的ID,一个TextView 的ID   
		        );   
				 this.setListAdapter(listItemAdapter);  

		    }

		@Override
		public void onClick(View v) {
			String res=et.getText().toString();
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.confirm_btn:
				if(!TextUtils.isEmpty(res)){
					Intent ret=new Intent();
		    		ret.putExtra(MygestureDetailSetPrefsFragment.PARA_SITE_NAME, res);
		    		ret.putExtra(MygestureDetailSetPrefsFragment.PARA_SITE_ADDR, res);
		    		android.util.Log.d("zhangpeifu","web site intput   res   "+res);
					setResult(Activity.RESULT_OK, ret);
					this.finish();
				}
				break;

			default:
				break;
			}
		}
	
	
}
