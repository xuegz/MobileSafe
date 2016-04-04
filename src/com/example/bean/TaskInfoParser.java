package com.example.bean;

import java.util.ArrayList;
import java.util.List;

import com.example.mobilesafe.R;

import android.R.integer;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;
import android.text.format.Formatter;


public class TaskInfoParser {

	public static List<TaskInfo> getTaskInfos(Context context) {

		PackageManager packageManager = context.getPackageManager();

		List<TaskInfo> TaskInfos = new ArrayList<TaskInfo>();

		
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(context.ACTIVITY_SERVICE);
		
		List<RunningAppProcessInfo> appProcesses = activityManager
				.getRunningAppProcesses();

		for (RunningAppProcessInfo runningAppProcessInfo : appProcesses) {
			TaskInfo taskInfo = new TaskInfo();
			String processName = runningAppProcessInfo.processName;
			taskInfo.setPackageName(processName);
			try {
				MemoryInfo[] memoryInfo = activityManager
						.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
				int totalPrivateDirty = memoryInfo[0].getTotalPrivateDirty() * 1024;
				taskInfo.setMemorySize(totalPrivateDirty);
				PackageInfo packageInfo = packageManager.getPackageInfo(
						processName, 0);
				Drawable icon = packageInfo.applicationInfo
						.loadIcon(packageManager);
				taskInfo.setIcon(icon);
				String appName = packageInfo.applicationInfo.loadLabel(
						packageManager).toString();
				taskInfo.setAppName(appName);
				int flags = packageInfo.applicationInfo.flags;
				if((flags & ApplicationInfo.FLAG_SYSTEM) != 0 ){
					taskInfo.setUserApp(false);
				}else{
					taskInfo.setUserApp(true);
				}

			} catch (Exception e) {
				e.printStackTrace();
				taskInfo.setAppName(processName);
				taskInfo.setIcon(context.getResources().getDrawable(
						R.drawable.ic_launcher));
			}
			
			TaskInfos.add(taskInfo);
		}

		return TaskInfos;
	}

}
