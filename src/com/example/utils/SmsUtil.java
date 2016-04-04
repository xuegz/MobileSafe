package com.example.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Xml;
import android.view.View;
import android.widget.ProgressBar;

public class SmsUtil {
	/*
	 * 短信备份
	 * xml
	 */
	public static void backup(Context context){
		XmlSerializer xs=Xml.newSerializer();
		File file=new File(Environment.getExternalStorageDirectory(), "backup.xml");
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(file);
			xs.setOutput(fos, "utf-8");
			xs.startDocument("utf-8", true);
			ContentResolver resolver=context.getContentResolver();
			Cursor cursor=resolver.query(Uri.parse("content://sms"), new String[]{"address","date","body","type"}, null, null, null);	
			xs.startTag(null, "smss");
			while(cursor.moveToNext()){
				xs.startTag(null, "sms");
					xs.startTag(null, "address");
						xs.text(cursor.getString(0));
					xs.endTag(null, "address");
					
					xs.startTag(null, "date");
						xs.text(cursor.getString(1));
					xs.endTag(null, "date");
					
					xs.startTag(null, "body");
						xs.text(cursor.getString(2));
						xs.endTag(null, "body");
						
					xs.startTag(null, "type");
						xs.text(cursor.getString(3));
					xs.endTag(null, "type");
				xs.endTag(null, "sms");
			}
			xs.endTag(null, "smss");
			xs.endDocument();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
