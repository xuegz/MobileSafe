package com.example.mobilesafe;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class SetUp4Activity extends SetUpActivity {
	private SharedPreferences sp;
	private CheckBox cb_set;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up4);
		sp=getSharedPreferences("info", MODE_PRIVATE);
		cb_set=(CheckBox) findViewById(R.id.cb_set);
		boolean check=sp.getBoolean("theft", false);
		if (check){
			cb_set.setText("防盗保护开启");
			cb_set.setChecked(true);
		}else{
			cb_set.setText("防盗保护没有开启");
			cb_set.setChecked(false);
		}
		cb_set.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked){
					sp.edit().putBoolean("theft", true).commit();
					cb_set.setText("防盗保护开启");
				}else{
					sp.edit().putBoolean("theft", false).commit();
					cb_set.setText("防盗保护没有开启");
				}
			}
		});
		
		
	}
	
	public void toNext(){
		sp.edit().putBoolean("setup", true).commit();
		startActivity(new Intent(this, LostFindActivity.class));
		finish();
		overridePendingTransition(R.anim.activity_next_in, R.anim.activity_next_out);
	}
	
	public void toBack(){
		startActivity(new Intent(this, SetUp3Activity.class));
		finish();
		overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
	}
	
	public void next(View v){
		toNext();
	}
	
	public void back(View v){	
		toBack();
	}
	
}
