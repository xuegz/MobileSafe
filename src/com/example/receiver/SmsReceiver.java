package com.example.receiver;

import com.example.mobilesafe.R;
import com.example.service.LocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.sax.StartElementListener;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/*
 * 监听短信
 */
public class SmsReceiver extends BroadcastReceiver {
	SharedPreferences sp;
	@Override
	public void onReceive(Context context, Intent intent) {
		sp=context.getSharedPreferences("info", context.MODE_PRIVATE);
		boolean theft=sp.getBoolean("theft", false);//防盗保护是否开启
		if (theft){	
			Bundle bundle=intent.getExtras();
			if (bundle!=null){
				Object[] objs = (Object[]) bundle.get("pdus"); //以puds为键，取出一个object数组
				for (Object obj:objs){
					SmsMessage sms=SmsMessage.createFromPdu((byte[]) obj);
					String savedPhone=sp.getString("phone", null);//保存的安全号码
					if (sms.getOriginatingAddress().equals(savedPhone)){
						if (sms.getMessageBody().equals("#*alarm*#")){			
							//播放报警音乐
							MediaPlayer player=MediaPlayer.create(context, R.raw.ylzs);
							player.setLooping(true);
							player.setVolume(1, 1);
							player.start();
							abortBroadcast();
						}else if(sms.getMessageBody().equals("#*location*#")){
							Intent intent2=new Intent(context, LocationService.class); 
							context.startService(intent2);
							String location=sp.getString("location","getting location.....");
							//发送短信
							SmsManager manager=SmsManager.getDefault();
							manager.sendTextMessage(savedPhone, null, location, null, null);
							abortBroadcast();
						}
					}
				}
			}
		}
	}
}
