package com.example.mobilesafe;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;

public class SetUp1Activity extends SetUpActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up1);
	}
	
	public void next(View v){
		toNext();
	}

	@Override
	public void toNext() {
		startActivity(new Intent(this, SetUp2Activity.class));
		finish();
		overridePendingTransition(R.anim.activity_next_in, R.anim.activity_next_out);
	}

	@Override
	public void toBack() {
		// TODO Auto-generated method stub	
	}

}
