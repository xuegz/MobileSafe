package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.bean.AppInfo;
import com.example.bean.AppInfos;
import com.example.db.AppLockDao;
import com.example.mobilesafe.R;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LockFragment extends Fragment {
	
	private TextView tv_lock;
	private ListView lv_lock;
	private MyClockAdapter adapter;
	private AppLockDao dao;
	private List<AppInfo> locks;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.fragment_lock, null);
		tv_lock = (TextView) v.findViewById(R.id.tv_lock);
		lv_lock = (ListView) v.findViewById(R.id.lv_lock);
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		List<AppInfo> appInfos= AppInfos.getAppInfos(getActivity());
		locks = new ArrayList<AppInfo>();//初始化一个加锁的集合
		dao = new AppLockDao(getActivity());
		for (AppInfo info:appInfos){
			String packageName=info.getPackageName();
			if (dao.query(packageName)){
				locks.add(info);
			}
		}
		adapter = new MyClockAdapter();
		lv_lock.setAdapter(adapter);
		
	}
	
	class MyClockAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return locks.size();
		}
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder= null;
			final View view;
			if (convertView==null){
				holder=new ViewHolder();
				view=lv_lock.inflate(getActivity(), R.layout.view_fragment_lock, null);
				holder.iv_icon=(ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_appName=(TextView) view.findViewById(R.id.tv_appName);
				holder.iv_unlock=(ImageView) view.findViewById(R.id.iv_unlock);
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			final AppInfo info=locks.get(position);
			holder.iv_icon.setImageDrawable(info.getIcon());
			holder.tv_appName.setText(info.getAppName());
			holder.iv_unlock.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//设置移动动画
					TranslateAnimation animation=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, 
								Animation.RELATIVE_TO_SELF,-1f, Animation.RELATIVE_TO_SELF, 0, 
							Animation.RELATIVE_TO_SELF,0);
					animation.setDuration(500);
					view.startAnimation(animation);
					
					//此处需注意   animation在子线程执行
					new Thread(){
						public void run() {
							SystemClock.sleep(500);
							//主线程刷新ui
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									String packageName=info.getPackageName();
									dao.delete(packageName);
									locks.remove(position);
									//刷新界面
									adapter.notifyDataSetChanged();
								}
							});
							
						};
					}.start();
					
				}
			});
			return view;
		}
	}
	
	static class ViewHolder{
		ImageView iv_icon;
		TextView tv_appName;
		ImageView iv_unlock;
	}
	
}
