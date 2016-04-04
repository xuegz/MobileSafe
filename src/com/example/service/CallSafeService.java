package com.example.service;

import com.example.db.BlacknumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.SmsMessage;

/*
 * 
 */
public class CallSafeService extends Service {
	private BlacknumberDao blacknumberDao;
	private InnerReceiver receiver;

	public IBinder onBind(Intent intent) {
		return null;
	}
	
	public void onCreate() {
		super.onCreate();
		blacknumberDao=new BlacknumberDao(this);
		/*
		 * 绑定短信接受广播
		 */
		receiver = new InnerReceiver();
		IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, filter);
	}
	
	class InnerReceiver extends BroadcastReceiver{
		public void onReceive(Context context, Intent intent) {
			Object[] objs= (Object[])intent.getExtras().get("pdus");  
	        for(Object obj: objs){   
	            SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);  
	            String number = sms.getOriginatingAddress();//发送短信的手机号码  
	            String content = sms.getMessageBody(); //短信内容  
	            String mode=blacknumberDao.queryByNumber(number);
	            if (mode.equals("1")){
	            	abortBroadcast();
	            }else if(mode.equals("3")){
	            	abortBroadcast();
	            }
	        }  
		}
	}
	
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}

}
