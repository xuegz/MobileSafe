package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;
import com.example.utils.QueryPhone;

public class PhoneAddressReceiver extends BroadcastReceiver {
	private SharedPreferences sp;
	
	@Override
	public void onReceive(final Context context, Intent intent) {
		sp=context.getSharedPreferences("info", context.MODE_PRIVATE);
		boolean checked=sp.getBoolean("status_phone", false);
	    if (checked){
			if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {//去电
				String phoneNumber =getResultData();
				String location = QueryPhone.getAddress(phoneNumber);
				Toast.makeText(context,location, Toast.LENGTH_LONG).show();
			} else {//来电
				// 获取电话通讯服务
				TelephonyManager tpm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
				// 创建一个监听对象，监听电话状态改变事件
				tpm.listen(new PhoneStateListener() {
					public void onCallStateChanged(int state,
							String incomingNumber) {
						switch (state) {
						case TelephonyManager.CALL_STATE_RINGING: // 来电
							String location = QueryPhone.getAddress(incomingNumber);
							System.out.println(location);
							//Toast.makeText(context, location, Toast.LENGTH_LONG).show();
							
							break;
						case TelephonyManager.CALL_STATE_OFFHOOK: // 摘机（正在通话中）
						case TelephonyManager.CALL_STATE_IDLE://挂断
							
							break;
						}
					}
				}, PhoneStateListener.LISTEN_CALL_STATE);
			}
		}
	}
	
	
	
}
