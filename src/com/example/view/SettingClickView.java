package com.example.view;

import com.example.mobilesafe.R;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SettingClickView extends RelativeLayout {
	private TextView tv_title;
	private TextView tv_desc;
	
	public SettingClickView(Context context) {
		super(context);
		initView();
	}
	public SettingClickView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}
	public SettingClickView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}
	
	public void initView(){
		View.inflate(getContext(), R.layout.view_setting_click, this);
		tv_title=(TextView) findViewById(R.id.tv_title);
		tv_desc=(TextView) findViewById(R.id.tv_desc);
	}
	
	public void setTitle(String title){
		tv_title.setText(title);
	}
	
	public void setDesc(String desc){
		tv_desc.setText(desc);
	}
}
