package com.example.mobilesafe;

import java.util.List;
import com.example.bean.BlackNumber;
import com.example.db.BlacknumberDao;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class CallSafeActivity extends Activity {
	private ListView lv_safe;
	private ProgressBar pb_refresh;
	private BlacknumberDao blacknumberDao;
	private MyAdapter adapter;
	private List<BlackNumber> blist;
	private int startIndex=0;
	private int maxIndex=20;
	private int totalNumber;
	private EditText et_black_phone;
	Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			pb_refresh.setVisibility(View.INVISIBLE);
			if(adapter == null){
                adapter = new MyAdapter();
                lv_safe.setAdapter(adapter);
            }else{
            	//notifyDataSetChanged方法通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容。
                adapter.notifyDataSetChanged();
            }
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_safe);
		initUI();
		initData();
	}
	/*
	 * 初始化ui
	 */
	public void initUI(){
		lv_safe=(ListView) findViewById(R.id.lv_safe);
		pb_refresh=(ProgressBar) findViewById(R.id.pb_refresh);
		pb_refresh.setVisibility(View.VISIBLE);
		blacknumberDao=new BlacknumberDao(CallSafeActivity.this);
		 //设置listview的滚动监听
		lv_safe.setOnScrollListener(new OnScrollListener() {
			/**
            * @param view
            * @param scrollState  表示滚动的状态
            *                     AbsListView.OnScrollListener.SCROLL_STATE_IDLE 闲置状态
            *                     AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL 手指触摸的时候的状态
            *                     AbsListView.OnScrollListener.SCROLL_STATE_FLING 惯性
            */
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
	                case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
	                    //获取到最后一条显示的数据
	                    int lastVisiblePosition = lv_safe.getLastVisiblePosition();
                        if(lastVisiblePosition == blist.size() - 1){
                            // 加载更多的数据。 更改加载数据的开始位置
                            startIndex += maxIndex;
                            if (startIndex >= totalNumber) {
                                return;
                            } 
                        }
	                    break;
				}
			}
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {}
		});
		lv_safe.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				AlertDialog.Builder builder=new Builder(CallSafeActivity.this);
				builder.setTitle("从黑名单删除");
				builder.setMessage("你确定要删除吗？");
				builder.setPositiveButton("确定",  new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						BlackNumber blackNumber=blist.get(position);
						blacknumberDao.delete(blackNumber.getNumber());
						blist.remove(position);
						adapter.notifyDataSetChanged();
					}
				});
				builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				builder.create().show();
				return true;
			}
		});
	}
	/*
	 * 初始化数据
	 */
	public void initData(){
		pb_refresh.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				totalNumber=blacknumberDao.getCount();
				//分批加载数据
				if (blist==null){
					blist=blacknumberDao.query(startIndex, maxIndex);
				}else{
					//把后面的数据。追加到blist集合里面。防止黑名单被覆盖
					blist.addAll(blacknumberDao.query(startIndex, maxIndex));
				}
				handler.sendEmptyMessage(0);
			}
		}.start();
	}
	/*
	 * button  添加
	 */
	public void addPhone(View v){
		AlertDialog.Builder builder=new Builder(this);
		builder.setTitle("添加黑名单");
		final View view=View.inflate(this, R.layout.view_add_black, null);
		et_black_phone=(EditText) view.findViewById(R.id.et_black_phone);
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				CheckBox cb_message_method=(CheckBox) view.findViewById(R.id.cb_message_method);
				CheckBox cb_call_method=(CheckBox) view.findViewById(R.id.cb_call_method);
				String black_phone=et_black_phone.getText().toString();
				String mode="";
				if (cb_call_method.isChecked()&&cb_message_method.isChecked()){
					mode="1";
				}else if (cb_call_method.isChecked()){
					mode="2";
				}else if(cb_message_method.isChecked()){
					mode="3";
				}else{
					Toast.makeText(CallSafeActivity.this, "请勾选拦截模式", 0).show();
					return;
				}
				BlackNumber blackNumber=new BlackNumber(black_phone, mode);
				blist.add(0, blackNumber);
				blacknumberDao.insert(blackNumber);
				if(adapter == null){
	                adapter = new MyAdapter();
	                lv_safe.setAdapter(adapter);
	            }else{
	            	//notifyDataSetChanged方法通过一个外部的方法控制如果适配器的内容改变时需要强制调用getView来刷新每个Item的内容。
	                adapter.notifyDataSetChanged();
	            }
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.create().show();
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
               et_black_phone.setText(num);  
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
//                 switch (phone_type) {//此处请看下方注释  
//                 case 2:  
//                     result = phoneNumber;  
//                     break;  
// 
//                 default:  
//                     break;  
//                 }  
               }  
               if (!phone.isClosed()) {  
                   phone.close();  
               }  
           }  
       }  
       return result;  
    }  
	
	class MyAdapter extends BaseAdapter{
		public int getCount() {
			return blist.size();
		}
		public Object getItem(int position) {
			return null;
		}
		public long getItemId(int position) {
			return 0;
		}
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder vHolder;
			if(convertView==null){
				vHolder=new ViewHolder();
				convertView=View.inflate(CallSafeActivity.this, R.layout.view_call_safe, null);
				vHolder.tv_black_phone=(TextView) convertView.findViewById(R.id.tv_black_phone);
				vHolder.tv_black_mode=(TextView) convertView.findViewById(R.id.tv_black_mode);
				convertView.setTag(vHolder);
			}else{
				vHolder=(ViewHolder) convertView.getTag();
			}
			vHolder.tv_black_phone.setText(blist.get(position).getNumber());
			if (blist.get(position).getMode().equals("1")) {
				vHolder.tv_black_mode.setText("全部拦截");
            } else if (blist.get(position).getMode().equals("2")) {
            	vHolder.tv_black_mode.setText("电话拦截");
            } else if (blist.get(position).getMode().equals("3")) {
            	vHolder.tv_black_mode.setText("短信拦截");
            }
			return convertView;
		}
	}
	static class ViewHolder{
		TextView tv_black_phone;
		TextView tv_black_mode;
	}
}
