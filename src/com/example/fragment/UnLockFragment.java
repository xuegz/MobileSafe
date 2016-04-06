package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.bean.AppInfo;
import com.example.bean.AppInfos;
import com.example.db.AppLockDao;
import com.example.fragment.LockFragment.MyClockAdapter;
import com.example.fragment.LockFragment.ViewHolder;
import com.example.mobilesafe.R;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UnLockFragment extends Fragment {
	
	private TextView tv_unlock;
	private ListView lv_unlock;
	private List<AppInfo> unLocks;//初始化一个没有加锁的集合
	private MyUnClockAdapter adapter;
	private AppLockDao dao;//数据库操作

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v=inflater.inflate(R.layout.fragment_unlock, null);
		tv_unlock = (TextView) v.findViewById(R.id.tv_unlock);
		lv_unlock = (ListView) v.findViewById(R.id.lv_unlock);
		return v;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		List<AppInfo> appInfos= AppInfos.getAppInfos(getActivity());
		dao = new AppLockDao(getActivity());
		//初始化一个没有加锁的集合
		unLocks = new ArrayList<AppInfo>();
		for (AppInfo appInfo:appInfos){
			String packageName=appInfo.getPackageName();
			if (!dao.query(packageName)){
				unLocks.add(appInfo);
			}
		}
		adapter = new MyUnClockAdapter();
		lv_unlock.setAdapter(adapter);
	}
	
	class MyUnClockAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			tv_unlock.setText("未加锁软件"+unLocks.size()+"个");
			return unLocks.size();
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
			ViewHolder holder;
			final View view;
			if (convertView==null){
				holder=new ViewHolder();
				view=lv_unlock.inflate(getActivity(), R.layout.view_fragment_unlock, null);
				holder.iv_icon=(ImageView) view.findViewById(R.id.iv_icon);
				holder.tv_appName=(TextView) view.findViewById(R.id.tv_appName);
				holder.iv_lock=(ImageView) view.findViewById(R.id.iv_lock);
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			final AppInfo info=unLocks.get(position);
			holder.iv_icon.setImageDrawable(info.getIcon());
			holder.tv_appName.setText(info.getAppName());
			//设置点击事件
			holder.iv_lock.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					//设置移动动画
					TranslateAnimation animation=new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, 
								Animation.RELATIVE_TO_SELF,1.0f, Animation.RELATIVE_TO_SELF, 0, 
							Animation.RELATIVE_TO_SELF,0);
					animation.setDuration(500);
					view.startAnimation(animation);
					
					//此处需注意
					new Thread(){
						public void run() {
							SystemClock.sleep(500);
							//主线程刷新ui
							getActivity().runOnUiThread(new Runnable() {
								public void run() {
									String packageName=info.getPackageName();
									dao.insert(packageName);
									unLocks.remove(position);
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
		ImageView iv_lock;
	}
	
}
