package com.example.mobilesafe;

import java.util.ArrayList;
import java.util.List;
import com.example.bean.TaskInfo;
import com.example.bean.TaskInfoParser;
import android.os.Bundle;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class TaskActivity extends Activity {
	private ListView lv_task;
	private TextView tv_task_run,tv_task_memory;
	private MyTaskAdapter adapter;
	private List<TaskInfo> taskInfos;
	private List<TaskInfo> userTaskInfos;
	private List<TaskInfo> systemTaskInfos;
	private int count=0;
	private int memory=0;
	private SharedPreferences sp;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (adapter==null){
				adapter=new MyTaskAdapter();
			}
			lv_task.setAdapter(adapter);
		};
	};
	private List<RunningAppProcessInfo> runInfos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initData();
	}

	private void initUI(){
		setContentView(R.layout.activity_task);
		lv_task=(ListView) findViewById(R.id.lv_task);
		tv_task_memory=(TextView) findViewById(R.id.tv_task_memory);
		tv_task_run=(TextView) findViewById(R.id.tv_task_run);
		sp=getSharedPreferences("info", MODE_PRIVATE);
	}
	
	@SuppressLint("NewApi")
	private void initData(){
		ActivityManager manager=(ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		runInfos = manager.getRunningAppProcesses();
		int size=runInfos.size();//获取运行的数量
		tv_task_run.setText("进程"+size+"个");
		MemoryInfo outInfo=new MemoryInfo();
		manager.getMemoryInfo(outInfo);
		long availMem=outInfo.availMem;//获取剩余内存
		long totalMem=outInfo.totalMem;//获取总内存
		tv_task_memory.setText("剩余/总内存"+Formatter.formatFileSize(this, availMem)+"/"+Formatter.formatFileSize(this, totalMem));
		new Thread(){
			public void run() {
				taskInfos=TaskInfoParser.getTaskInfos(TaskActivity.this);
				userTaskInfos=new ArrayList<TaskInfo>();
				systemTaskInfos=new ArrayList<TaskInfo>();
				for (TaskInfo taskInfo:taskInfos){
					if (taskInfo.isUserApp()){
						userTaskInfos.add(taskInfo);
					}else{				
						systemTaskInfos.add(taskInfo);			
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start();
		lv_task.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				ViewHolder holder=(ViewHolder) view.getTag();
				if (position!=0||position!=userTaskInfos.size()+1){
					TaskInfo info;
					if (position<userTaskInfos.size()+1){
						info=userTaskInfos.get(position-1);
					}else{
						info=systemTaskInfos.get(position-userTaskInfos.size()-2);
					}
					if (info.isChecked()){
						info.setChecked(false);
						holder.cb_app_status.setChecked(false);
					}else{
						info.setChecked(true);
						holder.cb_app_status.setChecked(true);
					}
				}
			}
		});
	}
	
	class MyTaskAdapter extends BaseAdapter{
		public int getCount() {
			boolean isChecked=sp.getBoolean("task_set", true);//判断是否显示系统进程
			if (isChecked){
				return userTaskInfos.size()+1+systemTaskInfos.size()+1;
			}else{
				return userTaskInfos.size()+1;
			}
			
		}
		public Object getItem(int position) {
			if (position==0||position==userTaskInfos.size()+1){
				return null;
			}
			if (position<userTaskInfos.size()+1){
				return userTaskInfos.get(position-1);
			}else{
				return systemTaskInfos.get(position-userTaskInfos.size()-2);
			}
		}
		public long getItemId(int position) {
			return 0;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			if (position==0){
				TextView tv=new TextView(TaskActivity.this);
				tv.setText("用户进程");
				tv.setTextColor(Color.WHITE);
				tv.setTextSize(20);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}else if(position==userTaskInfos.size()+1){
				TextView tv=new TextView(TaskActivity.this);
				tv.setText("系统程序");
				tv.setTextColor(Color.WHITE);
				tv.setTextSize(20);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			}
			ViewHolder holder;	
			if (convertView!=null&&convertView instanceof LinearLayout){
				holder=(ViewHolder) convertView.getTag();
			}else{
				holder=new ViewHolder();
				convertView=View.inflate(TaskActivity.this, R.layout.view_task_manager, null);
				holder.tv_app_name=(TextView) convertView.findViewById(R.id.tv_app_name);
				holder.tv_app_memory_size=(TextView)convertView.findViewById(R.id.tv_app_memory_size);
				holder.iv_app_icon=(ImageView) convertView.findViewById(R.id.iv_app_icon);
				holder.cb_app_status=(CheckBox) convertView.findViewById(R.id.cb_app_status);
				convertView.setTag(holder);	
			}
			TaskInfo taskInfo;
			if (position<userTaskInfos.size()+1){
				taskInfo=userTaskInfos.get(position-1);
			}else{
				taskInfo=systemTaskInfos.get(position-userTaskInfos.size()-2);
			}
			holder.iv_app_icon.setImageDrawable(taskInfo.getIcon());
			holder.tv_app_name.setText(taskInfo.getAppName());
			holder.tv_app_memory_size.setText("内存占用："+Formatter.formatFileSize(TaskActivity.this, taskInfo.getMemorySize()));
			if(taskInfo.isChecked()){
				holder.cb_app_status.setChecked(true);
			}else{
				holder.cb_app_status.setChecked(false);
			}
			
			return convertView;
		}
	}
	
	static class ViewHolder{
		TextView tv_app_name;
		TextView tv_app_memory_size;
		ImageView iv_app_icon;
		CheckBox cb_app_status;
	}
	
	/*
	 * 全选
	 */
	public void selectAll(View v){
		for (TaskInfo info:userTaskInfos){
			if (!info.getPackageName().equals(getPackageName())){
				info.setChecked(true);
			}
		}
		for (TaskInfo info:systemTaskInfos){
			info.setChecked(true);
		}
		adapter.notifyDataSetChanged();
	}
	/*
	 * 反选
	 */
	public void selectInverse(View v){
		for (TaskInfo info:userTaskInfos){
			if (!info.getPackageName().equals(getPackageName())){
				info.setChecked(!info.isChecked());
			}
		}
		for (TaskInfo info:systemTaskInfos){
			info.setChecked(!info.isChecked());
		}
		adapter.notifyDataSetChanged();
	}
	/*
	 * 任务管理的设置界面
	 */
	public void setting(View v){
		startActivity(new Intent(this, TaskManagerSettingActivity.class));
	}
	
	/*
	 * 一键清理
	 */
	public void clean(View v){
		ActivityManager manager=(ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<TaskInfo> list=new ArrayList<TaskInfo>();
		
		for (TaskInfo info:userTaskInfos){
			if (info.isChecked()){
				list.add(info);
			}
		}
		for (TaskInfo info:systemTaskInfos){
			if (info.isChecked()){
				list.add(info);
			}
		}
		for (TaskInfo info:list){
			if (info.isUserApp()){
				userTaskInfos.remove(info);
				memory+=info.getMemorySize();
				count++;
			}else{
				systemTaskInfos.remove(info);
				memory+=info.getMemorySize();
				count++;
			}
			manager.killBackgroundProcesses(info.getPackageName());
		}
		MemoryInfo outInfo=new MemoryInfo();
		manager.getMemoryInfo(outInfo);
		long availMem=outInfo.availMem;//获取剩余内存
		long totalMem=outInfo.totalMem;//获取总内存
		tv_task_memory.setText("剩余/总内存"+
				Formatter.formatFileSize(this, (availMem+memory))+
					"/"+Formatter.formatFileSize(this, totalMem));
		
		tv_task_run.setText("进程"+(runInfos.size()-count)+"个");
		adapter.notifyDataSetChanged();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
		super.onRestart();
	}
}
