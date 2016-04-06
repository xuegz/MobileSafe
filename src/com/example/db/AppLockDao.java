package com.example.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class AppLockDao {
	private Context context;
	private AppLockOpenHelper oh;
	private static SQLiteDatabase db;

	public AppLockDao(Context context) {
		this.context=context;
		oh = new AppLockOpenHelper(context);
		db = oh.getWritableDatabase();
	}
	//查询
	public boolean query(String packageName){
		Cursor cursor=db.query("applock", null,"packagename=?",new String[]{packageName}, null, null, null);
		if(cursor.moveToNext()){
			return true;
		}
		cursor.close();
		return false;	
	}
	
	public List<String> queryAll() {
		List<String> list=new ArrayList<String>();
		Cursor cursor=db.query("applock", null,null,null, null, null, null);
		while(cursor.moveToNext()){
			String packagename=cursor.getString(cursor.getColumnIndex("packagename"));
			list.add(packagename);
		}
		cursor.close();
		return list;
	}
	
	//增加
	public void insert(String packageName){
		ContentValues values=new ContentValues();
		values.put("packagename", packageName);
		db.insert("applock", null, values);
		//内容观察者，发送内容改变的通知
		context.getContentResolver().notifyChange(Uri.parse("content://com.watchDog"), null);
	}
	
	//删除
	public void delete(String packageName){
		db.delete("applock", "packagename=?", new String[]{packageName});
		//内容观察者，发送内容改变的通知
		context.getContentResolver().notifyChange(Uri.parse("content://com.watchDog"), null);
	}
	
}
