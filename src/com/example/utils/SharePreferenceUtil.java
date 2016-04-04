package com.example.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePreferenceUtil {

	public static final String SP_NAME="info";
	
	public static void saveBoolean(Context context,String key,boolean value){
		SharedPreferences sp=context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
		sp.edit().putBoolean(key, value).commit();
	}
	
	public static boolean getBoolean(Context context,String key,boolean defValue){
		SharedPreferences sp=context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
		return sp.getBoolean(key, defValue);
	}
}
