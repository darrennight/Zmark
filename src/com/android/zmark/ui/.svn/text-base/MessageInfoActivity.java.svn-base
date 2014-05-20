package com.android.zmark.ui;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.entities.MyMessage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.android.zmark.R;

public class MessageInfoActivity extends Activity implements OnClickListener{

	/**
	 * 消息详情页面
	 */
	Button messagedetail_back;
	HashMap<String, Object> map;
	MyMessage list;
	protected ImageLoader imageLoader;
	DisplayImageOptions options;
	ImageView messagedetail_img;
	TextView messagedetail_info_title,messagedetail_from,messagedetail_time,messagedetail_info;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_detail_activity);
		 map=(HashMap<String, Object>) getIntent().getSerializableExtra("message");
		
		this.init();
	}
	public void init(){
		messagedetail_img=(ImageView) findViewById(R.id.messagedetail_img);
		messagedetail_back=(Button) findViewById(R.id.messagedetail_back);
		messagedetail_info_title=(TextView) findViewById(R.id.messagedetail_info_title);
		messagedetail_from=(TextView) findViewById(R.id.messagedetail_from);
		messagedetail_time=(TextView) findViewById(R.id.messagedetail_time);
		messagedetail_info=(TextView) findViewById(R.id.messagedetail_info);
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.defult1).cacheInMemory(true)
		.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		messagedetail_back.setOnClickListener(this);
		
		if(map!=null){
			messagedetail_info_title.setText(map.get("title").toString());
			messagedetail_from.setText("官方"+map.get("PushType"));
			String str=map.get("time").toString();
			int h=str.indexOf(" ");
			if(h>0){
				str=str.substring(0, str.indexOf(" "));
			}
			
			messagedetail_time.setText(str);
			messagedetail_info.setText(map.get("info").toString());
			if(!DateInfo.IP.equals(DateInfo.IP+ map.get("Img").toString())){
				imageLoader.displayImage(DateInfo.IP
						+ map.get("Img").toString(), messagedetail_img, options);
			}
			
			//ImageUtil.setDrawable(messagedetail_img,DateInfo.IP+list.Img, MessageInfoActivity.this);
		}
		
		/*if(list!=null){
			messagedetail_info_title.setText(list.Title.toString());
			messagedetail_from.setText("官方�?+list.Sourse);
			String str=list.Time.toString();
			int h=str.indexOf(" ");
			if(h>0){
				str=str.substring(0, str.indexOf(" "));
			}
			
			messagedetail_time.setText(str);
			messagedetail_info.setText(list.Content);
			if(!DateInfo.IP.equals(DateInfo.IP+ list.Img)){
				imageLoader.displayImage(DateInfo.IP
						+ list.Img, messagedetail_img, options);
			}
			
			//ImageUtil.setDrawable(messagedetail_img,DateInfo.IP+list.Img, MessageInfoActivity.this);
		}*/
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.messagedetail_back:
			this.finish();
			break;

		default:
			break;
		}
	}
	
	
	class Messagetask extends AsyncTask<String, Void, List<?>> {

		@Override
		protected List<?> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return DateInfo.getDataFromSer(MessageInfoActivity.this,DateInfo.GetMessage, null, "1",
					null, null);
		}

		@Override
		protected void onPostExecute(List<?> result) {
			// TODO Auto-generated method stub
			
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			
			super.onPreExecute();
		}
	}
}
