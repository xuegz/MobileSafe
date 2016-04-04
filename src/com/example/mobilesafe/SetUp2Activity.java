package com.example.mobilesafe;

import com.example.view.SettingView;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

/*
 * 手机防盗设置界面2
 */
public class SetUp2Activity extends SetUpActivity {
	private SharedPreferences sp;
	private SettingView sv;
	private TelephonyManager tManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up2);
		sp=getSharedPreferences("info", MODE_PRIVATE);
		sv=(SettingView) findViewById(R.id.setSIM);
		tManager=(TelephonyManager)getSystemService(this.TELEPHONY_SERVICE);
		final boolean setSIM=sp.getBoolean("setSIM", false);
		if (setSIM){
			sv.setStatus(true);
		}else{
			sv.setStatus(false);
		}
		
		sv.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				boolean checked=sv.getStatus();
				if (checked){
					sv.setStatus(false);
					sp.edit().putString("simNum",null).commit();
					sp.edit().putBoolean("setSIM", false).commit();
				}else{
					sv.setStatus(true);
					sp.edit().putString("simNum", tManager.getSimSerialNumber()).commit();
					sp.edit().putBoolean("setSIM", true).commit();
				}
			}
		});
	}

	public void next(View v){
		toNext();
	}
	
	public void back(View v){
		toBack();
	}

	@Override
	public void toNext() {
		startActivity(new Intent(this, SetUp3Activity.class));
		finish();
		overridePendingTransition(R.anim.activity_next_in, R.anim.activity_next_out);
	}

	@Override
	public void toBack() {
		startActivity(new Intent(this, SetUp1Activity.class));
		finish();
		overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
	}
}
