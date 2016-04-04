package com.example.view;
import com.example.mobilesafe.R;
import com.example.mobilesafe.R.id;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
 * 设置中心自定义的控件
 */
public class SettingView extends RelativeLayout{
	private TextView tv_desc;
	private TextView tv_title;
	private CheckBox cb_status;
	private String NAME_SPACE="http://schemas.android.com/apk/res/com.example.mobilesafe";
	private String mtitle;
	private String desc_on;
	private String desc_off;
	public SettingView(Context context){
		super(context);
		initSet();
	}
	public SettingView(Context context, AttributeSet attrs){
		super(context, attrs);
		mtitle = attrs.getAttributeValue(NAME_SPACE,"title");
		desc_on = attrs.getAttributeValue(NAME_SPACE,"desc_on");
		desc_off = attrs.getAttributeValue(NAME_SPACE,"desc_off");
		initSet();
		setTitle(mtitle);
	}
	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initSet();
	}
	
	public void initSet(){
		View.inflate(getContext(), R.layout.view_setting_item, this);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_desc=(TextView) findViewById(R.id.tv_desc);
		cb_status = (CheckBox) findViewById(R.id.cb_status);
	}
	
	public void setTitle(String title){
		tv_title.setText(title);
	}
	
	public void setDesc(String desc){
		tv_desc.setText(desc);
	}
	
	public void setStatus(boolean check){
		cb_status.setChecked(check);
		if (check){
			setDesc(desc_on);
		}else{
			setDesc(desc_off);
		}
	}
	
	public boolean getStatus(){
		return cb_status.isChecked();
	}
}
