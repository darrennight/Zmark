package com.android.zmark.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.android.zmark.adapter.MessageAdapter;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.util.ISFirstUntil;
import com.android.zmark.entities.MyMessage;
import com.android.zmark.R;

/**
 * 消息，私信列表
 * 
 * @author yangwenfang
 * 
 */
public class MessageListActivity extends Activity implements OnClickListener {

	ListView listview;
	List<HashMap<String, Object>> list;
	List<MyMessage> list_message;
	List<MyMessage> list_message_all;
	// List<PushInfo> list_message;
	MessageAdapter adapter;
	private ProgressDialog progressDialog;
	Button message_back;
	LinearLayout message_layout;
	int pageindex = 1;
	TelephonyManager tm;
	String mid;
	private View moreView; // 加载更多页面
	private TextView tvLoadMore;
	String token = null, telnumber = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list_activity);
		this.init();

		final Messagetask task = new Messagetask();

		moreView = getLayoutInflater().inflate(R.layout.footer_loadmoer_layout,
				null);
		tvLoadMore = (TextView) moreView.findViewById(R.id.tv_loadmore_text);
		moreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadMoreData();
			}
		});

		task.execute("" + pageindex);
	}

	private void setAlias() {

		String alias = null;
		if (telnumber != null) {
			alias = telnumber.toString().trim();
		}
		if (TextUtils.isEmpty(alias)) {
			// Toast.makeText(MessageListActivity.this,"alias不能为空",
			// Toast.LENGTH_SHORT).show();
			return;
		}

		JPushInterface.setAliasAndTags(getApplicationContext(), alias, null);
	}

	public void init() {
		tm = (TelephonyManager) MessageListActivity.this
				.getSystemService(Context.TELEPHONY_SERVICE);
		mid = tm.getDeviceId();
		telnumber = SettingUtility.getDefaultPhone();
		token = SettingUtility.getDefaultToken();
		list_message = new ArrayList<MyMessage>();
		message_back = (Button) findViewById(R.id.message_back);
		message_back.setOnClickListener(this);
		listview = (ListView) findViewById(R.id.message_list);
		list = new ArrayList<HashMap<String, Object>>();
		message_layout = (LinearLayout) findViewById(R.id.message_layout);
		// setAlias();//设置tag
	}

	public void addAdapter() {

		adapter = new MessageAdapter(MessageListActivity.this, list);
		listview.setAdapter(adapter);

		listview.setOnItemClickListener(new OnItemClickListener() {
			Intent intent;

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				intent = new Intent(MessageListActivity.this,
						MessageInfoActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("message", list.get(arg2));
				intent.putExtras(bundle);
				startActivity(intent);

			}
		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		case R.id.message_back:
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
			return DateInfo.getDataFromSer(MessageListActivity.this,
					DateInfo.GetMessage, arg0[0], mid, telnumber, token);
		}

		@Override
		protected void onPostExecute(List<?> result) {
			// TODO Auto-generated method stub
			try {
				list_message = (List<MyMessage>) result;
				String st = list_message.toString().substring(0,
						list_message.toString().length() - 1);
				if (list_message != null && !"".equals(st)) {
					for (int i = 0; i < list_message.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("title", list_message.get(i).Title);
						String str = list_message.get(i).Time;
						int length = str.indexOf("T");
						if (length > 0) {
							str = str.substring(0, length) + " "
									+ str.substring(length + 1, str.length());
						}
						map.put("time", str);
						map.put("info", list_message.get(i).Content);
						map.put("id", list_message.get(i).Id);
						map.put("PushType", list_message.get(i).Sourse);
						map.put("Img", list_message.get(i).Img);
						list.add(map);
						if (list.size() == 10) {
							listview.addFooterView(moreView);
						}
					}
					addAdapter();
				}

				progressDialog.dismiss();
				super.onPostExecute(result);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(MessageListActivity.this, "消息信息加载失败",
						Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(MessageListActivity.this);
			}
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			View view = LayoutInflater.from(MessageListActivity.this).inflate(
					R.layout.progressdialog, null);
			progressDialog.setContentView(view);
			super.onPreExecute();
		}
	}

	private void loadMoreData() { // 加载更多数据
		// 当所有数据加载完成后，currentPage设为0
		if (pageindex == 0) {
			tvLoadMore.setText("已没有数据了");
			return;
		}
		pageindex++;
		new Thread(new Runnable() {

			@Override
			public void run() {
				list_message = (List<MyMessage>) getNewsListData(pageindex);
				mHandler.sendEmptyMessage(4);
			}
		}).start();

	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:

				break;
			case 2:

				break;
			case 3:

				break;
			case 4:
				if (list_message != null) {
					for (int i = 0; i < list_message.size(); i++) {
						HashMap<String, Object> map = new HashMap<String, Object>();
						map.put("title", list_message.get(i).Title);
						String str = list_message.get(i).Time;
						int length = str.indexOf("T");
						if (length > 0) {
							str = str.substring(0, length) + " "
									+ str.substring(length + 1, str.length());
						}
						map.put("time", str);
						map.put("info", list_message.get(i).Content);
						map.put("id", list_message.get(i).Id);
						map.put("PushType", list_message.get(i).Sourse);
						map.put("Img", list_message.get(i).Img);
						list.add(map);
						if (list_message.size() < 10) {
							tvLoadMore.setText("数据加载完毕");
						}
					}
				}
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}

	};

	public List<?> getNewsListData(int index) {
		return DateInfo.getDataFromSer(MessageListActivity.this,
				DateInfo.GetMessage, "" + index, mid, telnumber, token);

	}
}
