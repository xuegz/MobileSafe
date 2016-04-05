package com.example.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

import com.example.service.CallSafeService;
import com.example.service.WatchDogService;
import com.example.view.SettingClickView;
import com.example.view.SettingView;

/*
 * 设置中心页面
 */
public class SettingActivity extends Activity {
	private SettingView sv_update;
	private SettingView sv_watch_dog;
	private SettingView sv_phone;
	private SettingView sv_black_number;
	private SettingClickView scv_style;
	private SettingClickView scv_location;
	private SharedPreferences sp;
	private Intent watchDogIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sv_update = (SettingView) findViewById(R.id.sv_update);
		sv_watch_dog = (SettingView) findViewById(R.id.sv_watch_dog);
		sv_phone=(SettingView) findViewById(R.id.sv_phone);
		sv_black_number=(SettingView) findViewById(R.id.sv_black_number);
		scv_style=(SettingClickView) findViewById(R.id.scv_style);
		scv_location=(SettingClickView) findViewById(R.id.scv_location);
		sp = getSharedPreferences("info", MODE_PRIVATE);
		initUpdate();
		initWatchDog();
		initPhone();
		initAddress();
		initDialogLocation();
		initBlackNumber();
	}
	/*
	 * 自动更新
	 */
	public void initUpdate(){
		boolean check_update = sp.getBoolean("status", true);
		if (check_update){
			//sv_update.setDesc("自动更新已开启");
			sv_update.setStatus(true);
		}else{
			//sv_update.setDesc("自动更新已关闭");
			sv_update.setStatus(false);
		}
		//sv_update.setTitle("自动更新设置");
		sv_update.setOnClickListener(new OnClickListener() {			
			private boolean checked;
			public void onClick(View v) {
				checked = sv_update.getStatus();
				System.out.println(checked+"check_update");
				if (checked){
					//sv_update.setDesc("自动更新已关闭");
					sv_update.setStatus(false);
					sp.edit().putBoolean("status", false).commit();
				}else{
					//sv_update.setDesc("自动更新已开启");
					sv_update.setStatus(true);
					sp.edit().putBoolean("status", true).commit();
				}
			}
		});
	}
	
	/*
	 * 看萌狗
	 */
	public void initWatchDog(){
		boolean check_watchDog=sp.getBoolean("status_watchDog", false);
		watchDogIntent = new Intent(this, WatchDogService.class);
		if (check_watchDog){
			sv_watch_dog.setStatus(true);
			// 开启拦截服务
			startService(watchDogIntent);
		}else{
			sv_watch_dog.setStatus(false);
		}	
		sv_watch_dog.setOnClickListener(new OnClickListener() {
			private boolean checked;
			public void onClick(View v) {
				checked = sv_watch_dog.getStatus();
				if (checked) {
					sv_watch_dog.setStatus(false);
					// 停止拦截服务
					stopService(watchDogIntent);
					sp.edit().putBoolean("status_watchDog", false).commit();
				} else {
					sv_watch_dog.setStatus(true);
					// 开启拦截服务
					startService(watchDogIntent);
					sp.edit().putBoolean("status_watchDog", true).commit();
				}
			}
		});
	}
	/*
	 * 归属地
	 */
	public void initPhone(){	
		boolean check_phone=sp.getBoolean("status_phone", false);
		if (check_phone){
			sv_phone.setStatus(true);
		}else{
			sv_phone.setStatus(false);
		}
		sv_phone.setOnClickListener(new OnClickListener() {
			private boolean checked;
			public void onClick(View v) {
				checked = sv_phone.getStatus();
				if (checked){
					sv_phone.setStatus(false);
					sp.edit().putBoolean("status_phone", false).commit();
				}else{
					sv_phone.setStatus(true);
					sp.edit().putBoolean("status_phone", true).commit();
				}
			}
		});
	}
	/*
	 * 设置归属地风格
	 */
	public void initAddress(){
		scv_style.setTitle("归属地提示框风格");
		sp=getSharedPreferences("info", MODE_PRIVATE);
		String addressStyle=sp.getString("addressStyle","半透明");
		scv_style.setDesc(addressStyle);
		scv_style.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				showSingleChoiceDialog();
			}
		});
	}
	//单选对话框
	public void showSingleChoiceDialog(){
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("归属地提示框风格");
		builder.setIcon(R.drawable.ic_launcher);
		final String[] items=new String[]{"半透明","活力橙","卫士蓝","金属灰","土豪金"};
		builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				sp.edit().putString("addressStyle", items[which]).commit();
				scv_style.setDesc(items[which]);
				dialog.dismiss();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		builder.create().show();
	}
	
	/*
	 * 设置归属地提示框的显示位置
	 */
	public void initDialogLocation(){
		scv_location.setTitle("归属地提示框显示位置");
		scv_location.setDesc("归属地提示框显示位置");
		scv_location.setOnClickListener(new OnClickListener() {	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(SettingActivity.this, DialogLocationActivity.class));
			}
		});
	}
	/*
	 * 黑名单
	 */
	public void initBlackNumber(){
		boolean checked=sp.getBoolean("setBlackNumber", true);
		if (checked){
			sv_black_number.setStatus(true);
			startService(new Intent(this, CallSafeService.class));
		}else{
			sv_black_number.setStatus(false);
		}
		sv_black_number.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean b=sv_black_number.getStatus();
				if (b){
					sp.edit().putBoolean("setBlackNumber", false).commit();
					sv_black_number.setStatus(false);
					stopService(new Intent(SettingActivity.this, CallSafeService.class));
				}else{
					sp.edit().putBoolean("setBlackNumber", true).commit();
					sv_black_number.setStatus(true);
					startService(new Intent(SettingActivity.this, CallSafeService.class));
					Log.v("System.out", "开启服务");
				}
			}
		});
	}
	
	
	
}
