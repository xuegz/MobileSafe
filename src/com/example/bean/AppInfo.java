package com.example.bean;

import android.graphics.drawable.Drawable;

public class AppInfo {
	private Drawable icon;//图片
	private String appName;//程序的名字
	private long appSize;	//程序的大小
	private boolean userApp;//表示到底是用户app还是系统app，如果表示为true 就是用户app，如果是false表示系统app
	private boolean isRom;//app放置的位置
	private String packageName;//包名
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public long getAppSize() {
		return appSize;
	}
	public void setAppSize(long appSize) {
		this.appSize = appSize;
	}
	public boolean isUserApp() {
		return userApp;
	}
	public void setUserApp(boolean userApp) {
		this.userApp = userApp;
	}
	public boolean isRom() {
		return isRom;
	}
	public void setRom(boolean isRom) {
		this.isRom = isRom;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return appName+"---------"+packageName;
	}
}
