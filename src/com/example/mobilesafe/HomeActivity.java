package com.example.mobilesafe;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends Activity {
	private GridView gv_home;
	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量统计", "手机杀毒", "缓存清理", "高级工具", "设置中心" };
	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		gv_home = (GridView) findViewById(R.id.gv_home);
		gv_home.setAdapter(new MyHomeAdapter());
		gv_home.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent=new Intent();
				switch (position) {
					case 0://手机防盗
						showPasswdDialog();
						break;
					case 1://通讯卫士
						intent.setClass(HomeActivity.this, CallSafeActivity.class);
						startActivity(intent);
						break;
					case 2://软件管理
						intent.setClass(HomeActivity.this, AppManagerActivity.class);
						startActivity(intent);
						break;
					case 3://进程管理
						intent.setClass(HomeActivity.this, TaskActivity.class);
						startActivity(intent);
						break;
					case 4://流量统计
						
						break;
					case 5://手机杀毒
						
						break;
					case 6://缓存清理
						
						break;
					case 7://高级工具
						intent.setClass(HomeActivity.this, AToolActivity.class);
						startActivity(intent);
						break;
					case 8://设置中心
						intent.setClass(HomeActivity.this, SettingActivity.class);
						startActivity(intent);
						break;
				}
			}
		});
	}
	/*
	 * 手机防盗功能模块
	 */
	protected void showPasswdDialog() {
		sp=getSharedPreferences("info", MODE_PRIVATE);
		String savedPasswd=sp.getString("password", null);
		if (!TextUtils.isEmpty(savedPasswd)){
			showPasswdInputDialog();
		}else{
			showPasswdSetDialog();
		}
	}
	//验证密码弹窗
	private void showPasswdInputDialog() {
		View v=View.inflate(this, R.layout.dialog_input_password, null);
		final EditText et_password=(EditText)v.findViewById(R.id.et_input_password);	
		Button btn_ok=(Button) v.findViewById(R.id.btn_ok);
		Button btn_cancel=(Button) v.findViewById(R.id.btn_cancel);
		AlertDialog.Builder builder=new Builder(this);
		builder.setView(v);
		final AlertDialog dialog=builder.create();
		/*//将对话框的大小按屏幕大小的百分比设置
		Window dialogWindow = dialog.getWindow();
		WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.65); // 宽度设置为屏幕的0.65
        dialogWindow.setAttributes(p);*/
		dialog.show();
		btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String passwd=et_password.getText().toString();
				String savedPasswd=sp.getString("password", null);
				if (!TextUtils.isEmpty(passwd)){
					if (passwd.equals(savedPasswd)){
						enterLostFind();
						dialog.dismiss();
					}else{
						Toast.makeText(HomeActivity.this, "密码不正确", 0).show();
					}
				}else{
					Toast.makeText(HomeActivity.this, "请输入密码", 0).show();
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		
	}
	//密码设置弹窗
	private void showPasswdSetDialog() {
		View v=View.inflate(this, R.layout.dialog_set_password, null);		
		final EditText et_password=(EditText) v.findViewById(R.id.et_password);
		final EditText et_passwordConfirm=(EditText) v.findViewById(R.id.et_password_confirm);
		Button btn_ok=(Button) v.findViewById(R.id.btn_ok);
		Button btn_cancel=(Button) v.findViewById(R.id.btn_cancel);
		
		AlertDialog.Builder builder=new Builder(this);
		builder.setView(v);
		final AlertDialog dialog=builder.create();
		dialog.show();
		btn_ok.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				String password=et_password.getText().toString();
				String passwordConfirm=et_passwordConfirm.getText().toString();
				if (!TextUtils.isEmpty(password)&&!TextUtils.isEmpty(passwordConfirm)){
					if (password.equals(passwordConfirm)){
						sp.edit().putString("password", password).commit();
						enterLostFind();
						dialog.dismiss();
					}else{
						Toast.makeText(HomeActivity.this, "请输入密码", 0).show();
					}
				}else{
					Toast.makeText(HomeActivity.this, "请输入密码", 0).show();
				}
			}
		});
		btn_cancel.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}
	// 进入手机防盗页面
	public void enterLostFind(){
		Intent intent=new Intent();
		intent.setClass(HomeActivity.this, LostFindActivity.class);
		startActivity(intent);
	}

	/*
	 * class MyHomeAdapter
	 */
	class MyHomeAdapter extends BaseAdapter {
		@Override
		public int getCount() {
			return mItems.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = View.inflate(HomeActivity.this, R.layout.home_list_item,
					null);
			ImageView iv_item = (ImageView) v.findViewById(R.id.iv_item);
			TextView tv_item = (TextView) v.findViewById(R.id.tv_item);
			iv_item.setImageResource(mPics[position]);
			tv_item.setText(mItems[position]);
			return v;
		}
	}

}
