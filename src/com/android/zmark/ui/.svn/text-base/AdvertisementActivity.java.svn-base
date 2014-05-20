package com.android.zmark.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.R;

/**
 * 广告展示
 * 
 */
public class AdvertisementActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.adds_activity_layout);
		this.init();
	}

	public void init() {
		findViewById(R.id.back).setOnClickListener(this);
		((TextView) findViewById(R.id.content)).setText(getIntent()
				.getBundleExtra("data").getString("content"));

	}

	@Override
	public void onClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		default:
			break;
		}
	}
}
