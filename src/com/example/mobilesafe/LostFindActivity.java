package com.example.mobilesafe;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * 手机防盗页面
 */
public class LostFindActivity extends Activity {
	private SharedPreferences sp;
	private TextView tv_set;
	private TextView tv_phone;
	private ImageView iv_theft;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sp=getSharedPreferences("info", MODE_PRIVATE);
		boolean setUp=sp.getBoolean("setup", false);
		if (setUp){
			setContentView(R.layout.activity_lost_find);
		}else{
			Intent intent=new Intent(this, SetUp1Activity.class);
			startActivity(intent);
			finish();
			return;
		}
		tv_set=(TextView) findViewById(R.id.tv_set);
		tv_phone=(TextView) findViewById(R.id.tv_phone);
		iv_theft=(ImageView) findViewById(R.id.iv_theft);
		
		String phone=sp.getString("phone", null);//安全电话
		tv_phone.setText(phone);
		boolean checked=sp.getBoolean("theft", false);//防盗保护
		if (checked){
			iv_theft.setImageResource(R.drawable.lock);
		}else{
			iv_theft.setImageResource(R.drawable.unlock);
		}
		tv_set.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(LostFindActivity.this, SetUp1Activity.class));
				finish();
				//页面动态跳转
				overridePendingTransition(R.anim.activity_next_in, R.anim.activity_next_out);
			}
		});
	}

}
