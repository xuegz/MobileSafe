package com.example.mobilesafe;

import com.example.utils.QueryPhone;
import com.example.utils.VibratorUtil;
import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/*
 * 归属地查询页面
 */
public class AddressActivity extends Activity {
	private EditText et_phone;
	private TextView tv_address;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		et_phone=(EditText) findViewById(R.id.et_phone);
		tv_address=(TextView) findViewById(R.id.tv_address);
		et_phone.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				String number=et_phone.getText().toString();
				String address=QueryPhone.getAddress(number);
				tv_address.setText(address);		
			}
			public void beforeTextChanged(CharSequence s, int start, int count,int after) {}
			public void afterTextChanged(Editable s) {}
		});
		
	}
	/*
	 * 查询归属地
	 */
	public void query(View v){
		String number=et_phone.getText().toString();
		String address=QueryPhone.getAddress(number);
		tv_address.setText(address);
		VibratorUtil.vibrate(AddressActivity.this, 500);//手机震动,500毫秒
	}
	
}
