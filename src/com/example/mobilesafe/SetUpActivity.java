package com.example.mobilesafe;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;

/*
 * 手机防盗设置界面  抽象类
 */
public abstract class SetUpActivity extends Activity {
	private GestureDetector detector;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//手势识别器gestruedetector
		detector=new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				//下一页
				if (e1.getRawX()-e2.getRawX()>200){
					toNext();
				}
				//上一页
				if (e2.getRawX()-e1.getRawX()>200){
					toBack();
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
		});
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return detector.onTouchEvent(event);
	}
	public abstract void toNext();
	
	public abstract void toBack();
}
