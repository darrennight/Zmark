package com.android.zmark.ui;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.android.zmark.R;

public class MessageOrderActivity extends Activity implements OnClickListener {

	ImageView back;
	EditText telnum,content;
	Button ssubmit;
	String conString,telstr;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_order_activity_layout);
		this.init();
		
		telstr=getIntent().getStringExtra("tel");
		conString=getIntent().getStringExtra("info");
		telnum.setText(""+telstr);
		content.setText(""+conString);
	}

	
	public void init(){
		back=(ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		telnum=(EditText) findViewById(R.id.telnum);
		content=(EditText) findViewById(R.id.content);
		ssubmit=(Button) findViewById(R.id.ssubmit);
		ssubmit.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.ssubmit:
			Uri uri = Uri.parse("smsto:"+telnum.getText().toString());
			Intent it = new Intent(Intent.ACTION_SENDTO, uri);
			it.putExtra("sms_body",content.getText().toString());
			startActivity(it);
			break;
		default:
			break;
		}
	}
}
