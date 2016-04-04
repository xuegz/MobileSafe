package com.example.bean;

import android.graphics.drawable.Drawable;

/*
 * 任务管理
 */
public class TaskInfo {
	
	private Drawable icon;//图标
	private String packageName;//包名
	private String appName;//app名字
	private long memorySize;//内存大小
	private boolean userApp;//是否为用户app
	private boolean checked;//
	
	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public long getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(long memorySize) {
		this.memorySize = memorySize;
	}

	public boolean isUserApp() {
		return userApp;
	}

	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}

	@Override
	public String toString() {
		return "TaskInfo [packageName=" + packageName + ", appName=" + appName
				+ ", memorySize=" + memorySize + ", userApp=" + userApp + "]";
	}
	
	
	
	
}
