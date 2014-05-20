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
import android.widget.Toast;

import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.R;
/**
 * 意见反馈
 *
 */
public class FeedBackActivity extends Activity implements OnClickListener{

	ImageView back,mian_title_img;
	Button ysubmit;
	EditText content;
	Context context;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.feedback_activity_layout);
		context=this;
		this.init();
	}
	public void init(){
		InputFilter[] filters = {new InputFilter.LengthFilter(240)}; // 设置最大长度为240个字符  

		back=(ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		
		ysubmit=(Button) findViewById(R.id.ysubmit);
		ysubmit.setOnClickListener(this);
		
		content=(EditText) findViewById(R.id.content);
		content.setFilters(filters);  
		
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.back:
			this.finish();
			break;
		case R.id.ysubmit:
			if(content.getText().toString()!=null){
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						String str=DateInfo.getDataFromYiJianFankui(FeedBackActivity.this, DateInfo.YiJianFanKui, "反馈标题", content.getText().toString(), null, null, null, null);
						if(str!=null){
							Message msg= new Message();
							msg.what=1;
							msg.obj=str;
							handler.sendMessage(msg);
						}
					}
				}).start();
			}else{
				Toast.makeText(FeedBackActivity.this, "反馈内容不能为空", Toast.LENGTH_SHORT).show();
			}
			
			break;
		
		default:
			break;
		}
	}
	
	
	Handler handler= new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				Toast.makeText(FeedBackActivity.this, "用户反馈信息"+msg.obj, Toast.LENGTH_SHORT).show();
				FeedBackActivity.this.finish();
				break;

			default:
				break;
			}
		}
		
	};
}
