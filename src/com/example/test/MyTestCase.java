package com.example.test;

import java.util.List;
import com.example.bean.BlackNumber;
import com.example.db.BlacknumberDao;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.test.AndroidTestCase;


public class MyTestCase extends AndroidTestCase {
	private Context context;
	private BlacknumberDao bbd;
	
	/*@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		this.context=getContext();
		bbd=new BlacknumberDao(context);
	}
	public void insert(){	
		for (int i=0;i<100;i++){
			BlackNumber blackNumber=new BlackNumber(i+"", ""+i);
			bbd.insert(blackNumber);
		}
		System.out.println("insert");
	}
	
	public void delete(){
		bbd.delete("0");
	}
	
	public void update(){
		bbd.changeByNumber("1", "----------");
		System.out.println("update");
	}
	
	public void queryAll(){
		List<BlackNumber> list=bbd.queryAll();
		for (BlackNumber b:list){
			System.out.println(b.getNumber());
		}
	}
	
	public void queryByNumber(){
		String b=bbd.queryByNumber("1");
		System.out.println(b);
	}*/
	
	public void test1() throws Exception {
		PackageManager manager=context.getPackageManager();
		PackageInfo info=manager.getPackageInfo("", 0);
		System.out.println(context.getPackageName());
	}

}
