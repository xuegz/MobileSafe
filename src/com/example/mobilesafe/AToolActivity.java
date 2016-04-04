package com.example.mobilesafe;

import com.example.utils.ShowToastUtil;
import com.example.utils.SmsUtil;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.ProgressBar;

/*
 * 高级工具页面
 */
public class AToolActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_atool);
	}
	
	/*
	 * 归属地查询页面
	 */
	public void queryAddress(View v){
		startActivity(new Intent(this, AddressActivity.class));
	}
	
	/*
	 * 备份短信
	 */
	public void smsBackup(View v){
		SmsUtil.backup(this);
		SystemClock.sleep(2000);	
		ShowToastUtil.showToast(this, "备份成功！！！");
	}
	
}
