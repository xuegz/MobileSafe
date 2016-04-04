package com.example.mobilesafe;

import com.example.utils.ShowToastUtil;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

public class SetUp3Activity extends SetUpActivity {
	private EditText et_phone;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up3);
		et_phone=(EditText) findViewById(R.id.et_phone);
		sp=getSharedPreferences("info", MODE_PRIVATE);
		String telphone=sp.getString("phone", null);
		if (telphone!=null){
			et_phone.setText(telphone);
		}
	}
	/*
	 * 选择联系人
	 */
	public void choosePhone(View v){
		 Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);  
         startActivityForResult(intent, 1);
	}
	@SuppressWarnings("deprecation")
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {  
        case 1:  
            if (resultCode == RESULT_OK) {  
                Uri contactData = data.getData();  
                Cursor cursor = managedQuery(contactData, null, null, null,null);  
                cursor.moveToFirst();  
                String num = this.getContactPhone(cursor);  
                et_phone.setText(num);  
            }  
            break;  
        default:  
            break;  
        }  
	}
    private String getContactPhone(Cursor cursor) {  
        int phoneColumn = cursor  
                .getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER);  
        int phoneNum = cursor.getInt(phoneColumn);  
        String result = "";  
        if (phoneNum > 0) {  
            // 获得联系人的ID号  
            int idColumn = cursor.getColumnIndex(ContactsContract.Contacts._ID);  
            String contactId = cursor.getString(idColumn);  
            // 获得联系人电话的cursor  
            Cursor phone = getContentResolver().query(  
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,  
                    null,  
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "="  
                            + contactId, null, null);  
            if (phone.moveToFirst()) {  
                for (; !phone.isAfterLast(); phone.moveToNext()) {  
                    int index = phone  
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);  
                    int typeindex = phone  
                            .getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE);  
                    int phone_type = phone.getInt(typeindex);  
                    String phoneNumber = phone.getString(index);  
                    result = phoneNumber;  
//                  switch (phone_type) {//此处请看下方注释  
//                  case 2:  
//                      result = phoneNumber;  
//                      break;  
//  
//                  default:  
//                      break;  
//                  }  
                }  
                if (!phone.isClosed()) {  
                    phone.close();  
                }  
            }  
        }  
        return result;  
    }  
	
	public void next(View v){
		String num=et_phone.getText().toString();
		sp.edit().putString("phone", num).commit();
		toNext();
	}
	public void back(View v){
		toBack();
	}
	@Override
	public void toNext() {
		if (TextUtils.isEmpty(et_phone.getText().toString().trim())){
			ShowToastUtil.showToast(this, "安全号码不能为空");
			return;
		}
		startActivity(new Intent(this, SetUp4Activity.class));
		finish();
		overridePendingTransition(R.anim.activity_next_in, R.anim.activity_next_out);
	}
	@Override
	public void toBack() {
		startActivity(new Intent(this, SetUp2Activity.class));
		finish();
		overridePendingTransition(R.anim.activity_back_in, R.anim.activity_back_out);
	}
}
