package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.widget.Toast;

/*
 * 监听手机启动广播
 */
public class SIMReceiver extends BroadcastReceiver {
	private SharedPreferences sp;
	private TelephonyManager manager;
	@Override
	public void onReceive(Context context, Intent intent) {	
		sp=context.getSharedPreferences("info", context.MODE_PRIVATE);
		String simNum=sp.getString("simNum", null);//绑定的sim卡
		if(!TextUtils.isEmpty(simNum)){
			manager=(TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
			if (!simNum.equals(manager.getSimSerialNumber())){
				//发送报警短信
				String phone=sp.getString("phone", null);
				if (phone!=null){
					SmsManager sms=SmsManager.getDefault();
					sms.sendTextMessage(phone, null, "SIM changed!", null, null);
				}
			}
		}	
	}

}
