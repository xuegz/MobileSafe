package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.List;

import com.example.bean.AppInfo;
import com.example.bean.AppInfos;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * 软件管理页面
 */
@SuppressLint("NewApi")
public class AppManagerActivity extends Activity implements OnClickListener {
	private ListView lv_app_manager;
	private TextView tv_rom;
	private TextView tv_sd;
	private TextView tv_app;
	private MyApapter adapter;
	private List<AppInfo> appInfos;
	private List<AppInfo> userAppInfos;
	private List<AppInfo> systemAppInfos;
	private PopupWindow popupWindow;
	private AppInfo click_appInfo;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (adapter==null){
				adapter=new MyApapter();
			}
			lv_app_manager.setAdapter(adapter);
		};
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}
	
	private void initUI(){
		setContentView(R.layout.activity_app_manager);
		lv_app_manager=(ListView) findViewById(R.id.lv_app_manager);
		tv_rom=(TextView) findViewById(R.id.tv_rom);
		tv_sd=(TextView) findViewById(R.id.tv_sd);
		tv_app=(TextView) findViewById(R.id.tv_app);
		final View view=View.inflate(this, R.layout.dialog_popup_window, null);
		long rom_free=Environment.getDataDirectory().getFreeSpace();
		long sd_free=Environment.getExternalStorageDirectory().getFreeSpace();
		tv_rom.setText(Formatter.formatFileSize(this, rom_free));
		tv_sd.setText(Formatter.formatFileSize(this, sd_free));
		lv_app_manager.setOnScrollListener(new OnScrollListener() {
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				popupWindowDismiss();
				if (userAppInfos!=null&&systemAppInfos!=null){
					if (firstVisibleItem<(userAppInfos.size()+1)){
						tv_app.setText("用户程序(" + userAppInfos.size() + ")个");
					}else{
						tv_app.setText("系统程序(" + systemAppInfos.size() + ")个");
					}
				}
			}
		});
		lv_app_manager.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long arg3) {
				popupWindowDismiss();
				if (position!=0&&position!=userAppInfos.size()){
					LinearLayout ll_run=(LinearLayout) view.findViewById(R.id.ll_run);
					LinearLayout ll_share=(LinearLayout) view.findViewById(R.id.ll_share);
					LinearLayout ll_uninstall=(LinearLayout) view.findViewById(R.id.ll_uninstall);
					if (position<userAppInfos.size()){
						click_appInfo=userAppInfos.get(position-1);
					}else{
						click_appInfo=systemAppInfos.get(position-userAppInfos.size()-2);
					}
					
					//生成popupWindow对话框
					popupWindow=new PopupWindow(view,-2,-2);
					//需要注意：使用popupwindow必须设置背景，否则没有动画，此处为透明背景
					popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
					int[] location=new int[2];
					v.getLocationInWindow(location);
					popupWindow.showAtLocation(parent, Gravity.TOP+Gravity.LEFT, 70,location[1]);
					//设置缩放动画
					ScaleAnimation animation=new ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF,0.5f);
					animation.setDuration(300);
					view.startAnimation(animation);
					
					ll_run.setOnClickListener(AppManagerActivity.this);
					ll_share.setOnClickListener(AppManagerActivity.this);
					ll_uninstall.setOnClickListener(AppManagerActivity.this);
				}
			}
		});
	}
	//popupwindow对话框消失
	private void popupWindowDismiss(){
		if (popupWindow!=null&&popupWindow.isShowing()){
			popupWindow.dismiss();
			popupWindow=null;
		}
	}
	
	private void initData(){
		new Thread(){
			public void run() {
				appInfos=AppInfos.getAppInfos(AppManagerActivity.this);
				userAppInfos = new ArrayList<AppInfo>();
				systemAppInfos = new ArrayList<AppInfo>();
				for (AppInfo appInfo:appInfos){
					if (appInfo.isUserApp()){
						userAppInfos.add(appInfo);
					}else{
						systemAppInfos.add(appInfo);
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
		
	}
	
	class MyApapter extends BaseAdapter{
		public int getCount() {
			// TODO Auto-generated method stub
			return userAppInfos.size()+1+systemAppInfos.size()+1;
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position==0){
				TextView tv=new TextView(AppManagerActivity.this);
				tv.setText("用户程序("+userAppInfos.size()+")");
				tv.setTextColor(Color.WHITE);
				tv.setTextSize(20);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}else if(position==userAppInfos.size()+1){
				TextView tv=new TextView(AppManagerActivity.this);
				tv.setText("系统程序("+systemAppInfos.size()+")");
				tv.setTextColor(Color.WHITE);
				tv.setTextSize(20);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}
			AppInfo appInfo;
			if (position<userAppInfos.size()+1){
				appInfo=userAppInfos.get(position-1);
			}else{
				appInfo=systemAppInfos.get(position-userAppInfos.size()-2);
			}
			
			ViewHolder holder;
			if (convertView!=null&&convertView instanceof RelativeLayout){
				holder=(ViewHolder) convertView.getTag();
			}else{
				holder=new ViewHolder();
				convertView=View.inflate(AppManagerActivity.this, R.layout.view_app_manager, null);
				holder.iv_app=(ImageView) convertView.findViewById(R.id.iv_app);
				holder.tv_appName=(TextView) convertView.findViewById(R.id.tv_appName);
				holder.tv_isRom=(TextView) convertView.findViewById(R.id.tv_isRom);
				holder.tv_appSize=(TextView) convertView.findViewById(R.id.tv_appSize);
				convertView.setTag(holder);
			}
			holder.iv_app.setImageDrawable(appInfo.getIcon());
			holder.tv_appName.setText(appInfo.getAppName());
			holder.tv_appSize.setText(Formatter.formatFileSize(AppManagerActivity.this, appInfo.getAppSize()));
			if (appInfo.isRom()){
				holder.tv_isRom.setText("手机内存");
			}else{
				holder.tv_isRom.setText("外部存储");
			}
			return convertView;
		}
	}
	class ViewHolder{
		ImageView iv_app;
		TextView tv_appName;
		TextView tv_isRom;
		TextView tv_appSize;
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		popupWindowDismiss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.ll_run://运行
				Intent LaunchIntent = getPackageManager().getLaunchIntentForPackage(click_appInfo.getPackageName());
	            startActivity(LaunchIntent);
	            popupWindowDismiss();
				break;
			case R.id.ll_uninstall://卸载
		        Intent intent=new Intent(Intent.ACTION_DELETE,Uri.parse("package:"+click_appInfo.getPackageName()));  
		        startActivityForResult(intent, 1);
		        popupWindowDismiss();
				break;
			case R.id.ll_share://分享
				Intent shareIntent = new Intent();
		        shareIntent.setAction(Intent.ACTION_SEND);
		        shareIntent.putExtra(Intent.EXTRA_TEXT, "推荐您的软件为"+click_appInfo.getAppName());
		        shareIntent.setType("text/plain");
		        //设置分享列表的标题，并且每次都显示分享列表
		        startActivity(Intent.createChooser(shareIntent, "分享到"));
				popupWindowDismiss();
				break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		initUI();
		initData();
	}

}
