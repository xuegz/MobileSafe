package com.example.mobilesafe;

import android.os.Bundle;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;

/*
 * 进程管理设置页面
 */
public class TaskManagerSettingActivity extends Activity {
	private CheckBox cb_task_setting;
	private RelativeLayout rl_task;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}

	private void initUI(){
		setContentView(R.layout.activity_task_manager_setting);
		cb_task_setting=(CheckBox) findViewById(R.id.cb_task_setting);
		rl_task=(RelativeLayout) findViewById(R.id.rl_task);
	}
	
	private void initData(){
		sp=getSharedPreferences("info", MODE_PRIVATE);
		boolean checked=sp.getBoolean("task_set", true);
		if (checked){
			cb_task_setting.setChecked(true);
		}else{
			cb_task_setting.setChecked(false);
		}
		rl_task.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean checkbox=cb_task_setting.isChecked();
				if (checkbox){
					cb_task_setting.setChecked(false);
					sp.edit().putBoolean("task_set", false).commit();
				}else{
					cb_task_setting.setChecked(true);
					sp.edit().putBoolean("task_set", true).commit();
				}
			}
		});
	}

}
