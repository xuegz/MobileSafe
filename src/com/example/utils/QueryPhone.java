package com.example.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class QueryPhone {
	private static final String PATH="data/data/com.example.mobilesafe/files/address.db";
	private static String location;
	public static String getAddress(String number){
		/*
		 * 打开数据流库
		 */
		SQLiteDatabase db=SQLiteDatabase.openDatabase(PATH, null, SQLiteDatabase.OPEN_READONLY);
		Cursor cursor;
		String sql="select location from data2 where id=(SELECT outkey FROM data1 where id=?)";
		if (number.matches("^1[3-8]\\d{5}$")){
			cursor=db.rawQuery(sql, new String[]{number});
		}else if (number.matches("^1[3-8]\\d{9}$")){
			cursor=db.rawQuery(sql, new String[]{number.substring(0, 7)});
		}else{
			return "请输入正确的手机号";
		}
		if (cursor.moveToNext()){
			location = cursor.getString(0);
		}
		return location;
	}
}
