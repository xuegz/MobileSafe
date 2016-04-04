package com.example.mobilesafe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.utils.StreamUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Paint.Join;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.AlteredCharSequence;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class SplashActivity extends Activity {
	protected static final int CODE_UPDATE_DIALOG = 0;
	protected static final int CODE_URL_ERROR = 1;
	protected static final int CODE_NET_ERROR = 2;
	protected static final int CODE_JSON_ERROR = 3;
	protected static final int CODE_ENTER_HOME = 4;
	private String mVersionName;//版本名
	private int mVersionCode;//版本号
	private String mDesc;// 描述
	private String mDownloadUrl;//下载路径
	private TextView tv_version;
	private TextView tv_progress;
	private Intent shortCutIntent;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
				case CODE_UPDATE_DIALOG:
					showUpdateDailog();
					break;
				case CODE_URL_ERROR:
					enterHome();
					break;
				case CODE_NET_ERROR:
					enterHome();
					break;
				case CODE_JSON_ERROR:
					enterHome();
					break;
				case CODE_ENTER_HOME:
					enterHome();
					break;						
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		createShortcut();
		RelativeLayout rl_splash=(RelativeLayout) findViewById(R.id.rl_splash);
		tv_version=(TextView) findViewById(R.id.tv_version);
		tv_progress=(TextView) findViewById(R.id.tv_progress);
		String versionName=getVersionName();
		tv_version.setText(versionName);
		copyDB("address.db");//拷贝归属地查询数据库
		SharedPreferences sp=getSharedPreferences("info",MODE_PRIVATE);
		boolean checked=sp.getBoolean("status", true);
		if (checked){
			checkVersion();
		}else{
			handler.sendEmptyMessageDelayed(CODE_ENTER_HOME, 500);
		}	
		
		AlphaAnimation animation=new AlphaAnimation(0.3f, 1f);
		animation.setDuration(1000);
		rl_splash.startAnimation(animation);
	}
	/*
	 * 获取版本名
	*/
	private String getVersionName() {
		PackageManager manager=getPackageManager();
		try {
			PackageInfo info=manager.getPackageInfo(getPackageName(), 0);
			String versionName=info.versionName;
			return versionName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "-1";
	}
	/*
	 * 获取版本号
	*/
	private int getVersionCode() {
		PackageManager manager=getPackageManager();
		try {
			PackageInfo info=manager.getPackageInfo(getPackageName(), 0);
			int versionCode=info.versionCode;
			return versionCode;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}
	/*
	 *检查是否更新
	 */
	public void checkVersion(){
		final long startTime=System.currentTimeMillis();
		Thread t=new Thread(){
			public void run() {
				Message msg=handler.obtainMessage();
				String path="http://10.0.2.2:8080/update.json";
				HttpURLConnection conn = null;
				try {
					URL url=new URL(path);
					conn=(HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(5000);
					conn.setConnectTimeout(5000);
					if (conn.getResponseCode()==200){
						InputStream is=conn.getInputStream();
						String result=StreamUtil.readFromStream(is);
						//json解析
						JSONObject json=new JSONObject(result);
						mVersionName = json.getString("versionName");
						mVersionCode = json.getInt("versionCode");
						mDesc = json.getString("description");
						mDownloadUrl = json.getString("downloadUrl");
						if (mVersionCode>getVersionCode()){
							msg.what=CODE_UPDATE_DIALOG;
						}else{
							msg.what=CODE_ENTER_HOME;		
						}
					}
				} catch (MalformedURLException e) {
					// url������쳣
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// ��������쳣
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// json����ʧ��
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				}finally{
					long endTime=System.currentTimeMillis();
					long time=endTime-startTime;
					if (time<500){
						try {
							Thread.sleep(500-time);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					handler.sendMessage(msg);
					if (conn!=null){
						conn.disconnect();
					}
				}
			};
		};
		t.start();
	}
	/*
	 * 更新对话框
	 */
	public void showUpdateDailog(){
		AlertDialog.Builder builder=new Builder(SplashActivity.this);
		builder.setTitle("版本名:" + mVersionName);
		builder.setMessage(mDesc);
		builder.setPositiveButton("确认更新", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				downLoad();
			}
		});
		builder.setNegativeButton("取消更新", new OnClickListener() {			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				enterHome();
			}
		});
		builder.setOnCancelListener(new OnCancelListener() {		
			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		builder.create().show();
	}
	/*
	 * 下载
	 */
	public void downLoad(){
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			String apkFile=Environment.getExternalStorageDirectory()+"/MobileSafe.apk";
			File file=new File(apkFile);
			if (file.exists()){
				file.delete();
			}
			tv_progress.setVisibility(View.VISIBLE);
			HttpUtils utils=new HttpUtils();
			utils.download(mDownloadUrl,apkFile, 
					true,false,new RequestCallBack<File>() {
				public void onSuccess(ResponseInfo<File> arg0) {
					Intent intent = new Intent("android.intent.action.VIEW");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setDataAndType(Uri.fromFile(arg0.result), "application/vnd.android.package-archive");
					startActivityForResult(intent, 0);
				}
				public void onLoading(long total, long current, boolean isUploading) {
					tv_progress.setText(current*100/total+"%");
				};
				public void onFailure(HttpException arg0, String arg1) {
					Toast.makeText(SplashActivity.this, "连接失败",Toast.LENGTH_SHORT).show();
					enterHome();
				}
			});
		}else {
			Toast.makeText(SplashActivity.this, "sdcard不能正常使用!",Toast.LENGTH_SHORT).show();
			enterHome();
		}
	}
	/*
	 * 进入home页面
	 */
	public void enterHome() {
		Intent intent=new Intent();
		intent.setClass(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
	
	/*
	 *初始化数据库
	 */
	public void copyDB(String dbName){
		File file=new File(getFilesDir(),dbName);
		if (file.exists()){
			return;
		}
		InputStream is=null;
		FileOutputStream fos=null;
		try {
			is=getAssets().open(dbName);
			fos=new FileOutputStream(file);
			int len=0;
			byte[] bys=new byte[1024];
			while((len=is.read(bys))!=-1){
				fos.write(bys, 0, len);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{		
			try {
				is.close();
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	/*
	 * 创建快捷方式
	 */
	public void createShortcut(){
		boolean checked=hasShortcut(this);
		if (checked){
			return;
		}
		shortCutIntent = new Intent();
    	Intent dowhtIntent = new Intent(this,SplashActivity.class);
    	shortCutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
    	shortCutIntent.putExtra("duplicate", false); //不允许重复创建快捷方式
    	shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "MobileSafe");
    	shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
    	shortCutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,dowhtIntent);
    	sendBroadcast(shortCutIntent);
    	Toast.makeText(this, "快捷方式已创建", 0).show();
	}
	 /** 
     * 判断当前应用在桌面是否有桌面快捷方式 
     *  
     * @param cx 
     */  
    public boolean hasShortcut(Context cx) {  
        boolean result = false;  
        String title = null;  
        try {  
            final PackageManager pm = cx.getPackageManager();  
            title = pm.getApplicationLabel(  
                    pm.getApplicationInfo(cx.getPackageName(),  
                            PackageManager.GET_META_DATA)).toString();  
        } catch (Exception e) {  
        	
        }  
  
        final String uriStr;  
        if (android.os.Build.VERSION.SDK_INT < 8) {  
            uriStr = "content://com.android.launcher.settings/favorites?notify=true";  
        } else {  
            uriStr = "content://com.android.launcher2.settings/favorites?notify=true";  
        }  
        final Uri CONTENT_URI = Uri.parse(uriStr);  
        final Cursor c = cx.getContentResolver().query(CONTENT_URI, null,  
                "title=?", new String[] { title }, null);  
        if (c != null && c.getCount() > 0) {  
            result = true;  
        }  
        return result;  
    } 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (data==null){
			enterHome();
		}
	}
}
