package com.android.zmark.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;
import com.android.zmark.bean.IntentInterface;
import com.android.zmark.bean.LocationProvider;
import com.android.zmark.bean.LocationProvider.LocationListener;
import com.android.zmark.bean.LocationProvider.LocationType;
import com.android.zmark.bean.SettingUtility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class LogoActivity extends AbstractActivity {

	Context mContext;
	DisplayImageOptions options;
	protected ImageLoader imageLoader;
	ImageView imgs;
	// List<String> list;
	private Timer timer;
	private int longTime = 0;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (mContext == null)
				return;
			if (SettingUtility.isFirstLancher()) {
				Intent intent = new Intent(getApplicationContext(),
						LogoSwitchActivity.class);
				SettingUtility.setFirstLancher(false);
				startActivity(intent);
			} else {
				Intent intent = new Intent(getApplicationContext(),
						MainActivity.class);
				startActivity(intent);
			}
			timer.cancel();
			finish();
			super.handleMessage(msg);
		}

	};

	protected void onDestroy() {
		super.onDestroy();
		if (mProvider != null)
			mProvider.onStop();
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = LogoActivity.this;
		setContentView(R.layout.logo_activity);
		initLocation();
		init();
	}

	LocationProvider mProvider = null;

	// 定位
	private void initLocation() {
		ShowDialog("获取位置中...");
		if (mProvider == null)
			mProvider = ZmarkApplication.getInstance().getLocationProvider();
		// 开始定位
		mProvider.setLocationListener(new LocationListener() {

			@Override
			public void onReceiveLocation(double Latitude, double Longitude,
					String city, String address) {
				ZmarkApplication.getInstance().city = city;
				ZmarkApplication.getInstance().mycity = city;
				ZmarkApplication.getInstance().Latitude = Latitude;
				ZmarkApplication.getInstance().Longitude = Longitude;
				ZmarkApplication.getInstance().cord = String.format("%s,%s",
						String.valueOf(Longitude), String.valueOf(Latitude));
				SettingUtility.setLocationCity(city);
				SettingUtility.setLocationInfo(String.format("%s,%s",
						String.valueOf(Longitude), String.valueOf(Latitude)));
				mProvider.onStop();
				location = true;
				if (longTime >= 3 && location) {
					mHandler.sendEmptyMessage(0);
				}
				DismissDialog();
			}

			@Override
			public void onFailLocation() { // 定位失敗
				mProvider.onStop();
				Toast.makeText(mContext, "定位失败!", Toast.LENGTH_SHORT).show();
				location = true;
				if (longTime >= 3 && location) {
					mHandler.sendEmptyMessage(0);
				}
				DismissDialog();
			}
		});
		mProvider.onRequestLocation();
	}

	boolean location = false;

	private void init() {
		timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				longTime++;
				if ((longTime > 8 && !TextUtils.isEmpty(ZmarkApplication
						.getInstance().cord)) || (longTime > 2 && location)) {
					mHandler.sendEmptyMessage(0);
				}
			}
		}, 0, 1000);
	}
	/**
	 * 异步加载首页数据
	 * 
	 * @author lenovo
	 * 
	 */

	// class WelcomePagesTask extends AsyncTask<String, Void,List<?>> {
	//
	// @Override
	// protected List<?> doInBackground(String... arg0) {
	// return DateInfo.getDataFromSer(context, DateInfo.GetAdvertisingSlots,
	// arg0[0], arg0[1], null, null);
	//
	// }
	//
	// @Override
	// protected void onPostExecute(List<?> result) {
	// if (result != null) {
	// list=(List<String>) result;
	// }
	//
	// super.onPostExecute(result);
	// }
	//
	// @Override
	// protected void onPreExecute() {
	// super.onPreExecute();
	// }
	// }
}
