package com.example.mobilesafe;

import android.os.Bundle;
import android.os.SystemClock;
import android.app.Activity;
import android.content.SharedPreferences;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DialogLocationActivity extends Activity {
	private ImageView iv_drag;
	private TextView tv_dialog_top;
	private TextView tv_dialog_button;
	private SharedPreferences sp;
	
	long[] mHits=new long[2];
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	private WindowManager wm;
	private Display display;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dialog_location);
		iv_drag=(ImageView) findViewById(R.id.iv_drag);
		tv_dialog_top=(TextView) findViewById(R.id.tv_dialog_top);
		tv_dialog_button=(TextView) findViewById(R.id.tv_dialog_button);
		sp=getSharedPreferences("info", MODE_PRIVATE);
		int savedX=sp.getInt("startX", 0);
		int savedY=sp.getInt("startY", 0);
		/*
		 * onMeasure(测量),onLayout(安放位置),onDraw(绘制)
		 * iv_drag.layout(savedX, savedY, savedX+iv_drag.getWidth(), savedY+iv_drag.getHeight());
		 * 这里不能用这个方法，因为还没有测量完
		 */
		RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) iv_drag.getLayoutParams();
		layoutParams.leftMargin=savedX;
		layoutParams.topMargin=savedY;
		iv_drag.setLayoutParams(layoutParams);
		wm = getWindowManager();
		display = wm.getDefaultDisplay();
		if (savedY<display.getHeight()/2){
			tv_dialog_top.setVisibility(View.INVISIBLE);
			tv_dialog_button.setVisibility(View.VISIBLE);
		}else{
			tv_dialog_top.setVisibility(View.VISIBLE);
			tv_dialog_button.setVisibility(View.INVISIBLE);
		}
		//双击居中
		iv_drag.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				System.arraycopy(mHits, 1, mHits, 0, mHits.length-1);
				mHits[mHits.length-1]=SystemClock.uptimeMillis();
				if (mHits[0]>(SystemClock.uptimeMillis()-500)){//最后一次点击时间-第一次点击时间，2000可以设置成任意数
					int x=iv_drag.getWidth()/2;
					int y=iv_drag.getHeight()/2;
					iv_drag.layout(display.getWidth()/2-x, iv_drag.getTop(), display.getWidth()/2+x, iv_drag.getBottom());
					sp.edit().putInt("startX", iv_drag.getLeft()).commit();
					sp.edit().putInt("startY", iv_drag.getTop()).commit();
				}
			}
		});
		iv_drag.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					//起点坐标
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					//重点坐标
					endX=(int) event.getRawX();
					endY=(int) event.getRawY();
					if (endY<display.getHeight()/2){
						tv_dialog_top.setVisibility(View.INVISIBLE);
						tv_dialog_button.setVisibility(View.VISIBLE);
					}else{
						tv_dialog_top.setVisibility(View.VISIBLE);
						tv_dialog_button.setVisibility(View.INVISIBLE);
					}
					//位移量
					int dX=endX-startX;
					int dY=endY-startY;
					//上下左右坐标
					int l=iv_drag.getLeft()+dX;
					int t=iv_drag.getTop()+dY;
					int r=iv_drag.getRight()+dX;
					int b=iv_drag.getBottom()+dY;
					//判断是否超出屏幕边界，20为状态栏高度
					if (l<0||t<0||r>display.getWidth()||b>display.getHeight()-20){
						break;
					}
					iv_drag.layout(l, t, r, b);
					//重新设置起点坐标
					startX=(int) event.getRawX();
					startY=(int) event.getRawY();	
					break;
				case MotionEvent.ACTION_UP:
					sp.edit().putInt("startX", iv_drag.getLeft()).commit();
					sp.edit().putInt("startY", iv_drag.getTop()).commit();
					break;
				}
				return false;//false表示事件可以往下传递
			}
		});
	}
}
