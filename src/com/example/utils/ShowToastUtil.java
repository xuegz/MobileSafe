package com.example.utils;

import android.content.Context;
import android.widget.Toast;

public class ShowToastUtil {
	public static void showToast(Context context,String text){
		Toast.makeText(context, text, 0).show();
	}
}
