package com.example.mobilesafe;

import com.example.fragment.LockFragment;
import com.example.fragment.UnLockFragment;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.TextView;

public class AppLockActivity extends FragmentActivity implements OnClickListener {

	private TextView tv_unlock;
	private TextView tv_lock;
	private FrameLayout fl_content;
	private LockFragment lockFragment;
	private UnLockFragment unLockFragment;
	private FragmentManager fm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		
	}
	/*
	 * 初始化ui
	 */
	public void initUI(){
		setContentView(R.layout.activity_app_lock);
		tv_unlock = (TextView) findViewById(R.id.tv_unlock);
		tv_lock = (TextView) findViewById(R.id.tv_lock);
		fl_content = (FrameLayout) findViewById(R.id.fl_content);
		unLockFragment = new UnLockFragment();
		lockFragment = new LockFragment();
		
		tv_lock.setOnClickListener(this);
		tv_unlock.setOnClickListener(this);
		fm = getSupportFragmentManager();
		FragmentTransaction fTransaction=fm.beginTransaction();
		fTransaction.replace(R.id.fl_content, unLockFragment);
		fTransaction.commit();
	}
	@Override
	public void onClick(View v) {
		FragmentTransaction ft=fm.beginTransaction();
		switch (v.getId()) {
			case R.id.tv_unlock:
				tv_unlock.setBackgroundResource(R.drawable.tab_left_pressed);
				tv_lock.setBackgroundResource(R.drawable.tab_right_default);
				ft.replace(R.id.fl_content, unLockFragment);
				ft.commit();
				break;
			case R.id.tv_lock:
				tv_unlock.setBackgroundResource(R.drawable.tab_left_default);
				tv_lock.setBackgroundResource(R.drawable.tab_right_pressed);
				ft.replace(R.id.fl_content, lockFragment);
				ft.commit();
				break;
		}		
	}
	

	
}
