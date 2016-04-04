package com.example.db;

import java.util.ArrayList;
import java.util.List;

import com.example.bean.BlackNumber;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BlacknumberDao {
	private BlacknumberOpenHelper oh;
	private SQLiteDatabase db;
	
	public BlacknumberDao(Context context) {
		// TODO Auto-generated constructor stub
		oh=new BlacknumberOpenHelper(context);
		db=oh.getWritableDatabase();
	}
	/*
	 * 添加   
	 * 联系人   电话号码   拦截模式
	 */
	public void insert(BlackNumber blackNumber){
		ContentValues values=new ContentValues();
		values.put("number", blackNumber.getNumber());
		values.put("mode", blackNumber.getMode());
		db.insert("blacknumber", null, values);
	}	
	/*
	 * 删除
	 */
	public void delete(String number){
		db.delete("blacknumber", "number=?", new String[]{number});
	}
	/*
	 * 修改
	 */
	public void changeByNumber(String number,String mode){
		ContentValues values=new ContentValues();
		values.put("mode", mode);
		db.update("blacknumber", values, "number=?", new String[]{number});
	}
	/*
	 * 查询所有
	 */
	public List<BlackNumber> queryAll(){
		List<BlackNumber> list=new ArrayList<BlackNumber>();
		Cursor cursor=db.query("blacknumber", new String[]{"number","mode"}, null, null, null, null, null);
		while(cursor.moveToNext()){
			BlackNumber blackNumber=new BlackNumber();
			blackNumber.setNumber(cursor.getString(0));
			blackNumber.setMode(cursor.getString(1));
			list.add(blackNumber);
		}
		cursor.close();
		return list;
	}
	/*
	 * 通过number查询一个
	 */
	public String queryByNumber(String number){
		String mode="";
		Cursor cursor=db.query("blacknumber", new String[]{"mode"}, "number=?", new String[]{number}, null,null, null);
		if (cursor.moveToNext()){
			mode=cursor.getString(0);
		}
		cursor.close();
		return mode;
	}
	/*
	 * 分批加载
	 * startIndex开始位置
	 * maxIndex每页加载数
	 */
	public List<BlackNumber> query(int startIndex,int maxIndex){
		List<BlackNumber> list=new ArrayList<BlackNumber>();
		Cursor cursor=db.rawQuery("select number,mode from blacknumber limit ? offset ?", new String[]{String.valueOf(maxIndex),String.valueOf(startIndex)});
		while(cursor.moveToNext()){
			BlackNumber blackNumber=new BlackNumber();
			blackNumber.setNumber(cursor.getString(0));
			blackNumber.setMode(cursor.getString(1));
			list.add(blackNumber);
		}
		cursor.close();
		return list;
	}
	
	/*
	 * 得到总记录数
	 */
	public int getCount(){
		int count=0;
		Cursor cursor=db.rawQuery("select count(*) from blacknumber", null);
		if (cursor.moveToNext()){
			count=cursor.getInt(0);
		}
		return count;
	}
}
