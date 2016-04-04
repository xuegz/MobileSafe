package com.example.bean;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

/*
 * 获取手机上所有安装的app的信息
 * packageManager
 */
public class AppInfos {
	public static List<AppInfo> getAppInfos(Context context){
		List<AppInfo> packageAppInfos = new ArrayList<AppInfo>();
		//获取到包的管理者
		PackageManager manager=context.getPackageManager();
		//获取到安装包
		List<PackageInfo> packageInfos=manager.getInstalledPackages(0);
		for (PackageInfo packageInfo:packageInfos){
			AppInfo app=new AppInfo();
			//获取到应用程序的图标
			Drawable icon=packageInfo.applicationInfo.loadIcon(manager);
			app.setIcon(icon);
			//获取到应用程序的名字
			String appName=(String) packageInfo.applicationInfo.loadLabel(manager);
			app.setAppName(appName);
			//获取到应用程序的包名
			String packageName =packageInfo.packageName;
			app.setPackageName(packageName);
			//获取到apk资源的路径
			String sourceDir = packageInfo.applicationInfo.sourceDir;
			File file=new File(sourceDir);
			long appSize=file.length();
			app.setAppSize(appSize);
			//获取到安装应用程序的标记
			int flags=packageInfo.applicationInfo.flags;
			if ((flags&ApplicationInfo.FLAG_SYSTEM)!=0){
				//表示系统app
				app.setUserApp(false);
			}else{
				app.setUserApp(true);
			}
			if((flags& ApplicationInfo.FLAG_EXTERNAL_STORAGE) !=0 ){
                //表示在sd卡
                app.setRom(false);
            }else{
                //表示内存
                app.setRom(true);
            }
			packageAppInfos.add(app);
		}
		return packageAppInfos ;
	}
}
