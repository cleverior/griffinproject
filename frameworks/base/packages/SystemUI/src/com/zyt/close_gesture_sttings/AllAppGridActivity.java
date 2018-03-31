package com.zyt.close_gesture_sttings;

import java.util.List;
 
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import com.android.systemui.R;

public class AllAppGridActivity extends Activity {
	private GridView myGrid;
	private List<ResolveInfo> mApps;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		loadApps();
		setContentView(R.layout.allapp_grid);
		myGrid = (GridView) findViewById(R.id.all_app_view);
		final IconsAdapter iconsAdapter = new IconsAdapter(this);
		myGrid.setAdapter(iconsAdapter);

		myGrid.setOnItemClickListener(new OnItemClickListener() {
	
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				try {
					Intent it = new Intent();
					Log.d("zhangpeifu",AllAppGridActivity.class+"             "+mApps.get(position).activityInfo.packageName+"         "+mApps.get(position).activityInfo.name+"          "+getPackageManager().getApplicationLabel(mApps.get(position).activityInfo.applicationInfo));
					
					it.putExtra(MygestureDetailSetPrefsFragment.CHOOSE_PACKAGE_PARA_NAME, mApps.get(position).activityInfo.packageName);
					it.putExtra(MygestureDetailSetPrefsFragment.CHOOSE_CLASS_NAME, mApps.get(position).activityInfo.name);
					//markkkkkkkkkkkkkkkkkkkkk
					RelativeLayout tempRelativeLayout=(RelativeLayout)view;
					TextView tempTv=(TextView)tempRelativeLayout.findViewById(R.id.ItemText);

					//it.putExtra(MygestureDetailSetPrefsFragment.CHOOSE_APP_NAME, getPackageManager().getApplicationLabel(mApps.get(position).activityInfo.applicationInfo));
					it.putExtra(MygestureDetailSetPrefsFragment.CHOOSE_APP_NAME,tempTv.getText());
					
					
					setResult(Activity.RESULT_OK, it);
					AllAppGridActivity.this.finish();
				} catch (Exception e) {
					// TODO: handle exception
				}
				/*
				 * mApps.remove(index); 26. iconsAdapter.notifyDataSetChanged();
				 */

			}

		});

	}

	private void loadApps() {
		//得到系统中所有安装的应用程序
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		mApps = getPackageManager().queryIntentActivities(intent, 0);
	}

	public class IconsAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		
		

		public IconsAdapter(Context context) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mApps.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mApps.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			/* ImageView i;
			if (null == convertView) {
				i = new ImageView(GridviewTextActivity.this);
				  i.setScaleType(ScaleType.FIT_CENTER);
				  i.setLayoutParams(new GridView.LayoutParams(50, 50));
			}else {
				 i=(ImageView)convertView;
			}
			  ResolveInfo info=mApps.get(position);
			  i.setImageDrawable(info.activityInfo.loadIcon(getPackageManager()));*/
			   return composeLayout(position,convertView);
		}

		  public View composeLayout(int i,View convertView){
			  convertView = this.inflater.inflate(R.layout.allapp_item, null);
			  
			  //layout.setOrientation(LinearLayout.VERTICAL);
			  ImageView iv = (ImageView) convertView.findViewById(R.id.ItemImage);
			  iv.setImageDrawable(mApps.get(i).activityInfo.loadIcon(getPackageManager()));
			  //layout.addView(iv,new LinearLayout.LayoutParams(50,50));
			  
			  TextView tv = (TextView) convertView.findViewById(R.id.ItemText);
			  //得到安装应用程序的名字
			  tv.setText(mApps.get(i).activityInfo.loadLabel(getPackageManager()));
//			  tv.setText(mApps.get(i).activityInfo.packageName);  //得到应用程序的包名
			 // layout.addView(tv,new LinearLayout.LayoutParams(50,LayoutParams.WRAP_CONTENT));
			  
			  return convertView;
			  
		  }
	}

}