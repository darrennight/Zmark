package com.android.zmark.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.ApiServerPath;
import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;
import com.android.zmark.bean.CommonLog;
import com.android.zmark.bean.FragmentControlCenter;
import com.android.zmark.bean.FragmentModel;
import com.android.zmark.bean.LogFactory;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.db.ImageUtil;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpLoader;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.util.CalendarUtil;
import com.android.zmark.bean.util.ISFirstUntil;
import com.android.zmark.bean.util.Utility;
import com.android.zmark.ui.LoginActivity;
import com.android.zmark.ui.MainActivity;
import com.android.zmark.ui.UserCenterActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class MenuLeftFragment<T> extends Fragment implements OnClickListener {

	private static final CommonLog log = LogFactory.createLog();

	private View mView;
	public int flag = 1;
	TextView name, my_love_text, my_history_text;

	Context context;
	RelativeLayout item_1, item_2, item_3, item_4;
	ImageView item_img_1, item_img_2, item_img_3;
	TextView xingqi, time;
	RelativeLayout left_phone;

	ImageView touxiang;

	private FragmentControlCenter mControlCenter;

	public MenuLeftFragment() {
		super();
	}

	public MenuLeftFragment(Context context) {
		this.context = context;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mControlCenter = FragmentControlCenter.getInstance(getActivity());
		if (ZmarkApplication.getInstance().userIsOline()
				&& !TextUtils.isEmpty(SettingUtility.getUserInfo()))
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					getLoaderManager().restartLoader(LOADER_INITDATA, null,
							httpCallback);
				}
			}, 3000);
	}
	protected final static int LOADER_INITDATA = 0x100;
	protected LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>> httpCallback = new LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>>() {
		@Override
		public Loader<AsyncTaskLoaderResult<T>> onCreateLoader(int id,
				Bundle args) {
			switch (id) {
			case LOADER_INITDATA:
				Map<String, String> map = new HashMap<String, String>();
				map.clear();
				try {
					JSONObject info = new JSONObject(
							SettingUtility.getUserInfo());
					map.put("mobileId", Utility.getDeviceId(getActivity()));
					map.put("phone", info.getString("PhoneNo"));
					map.put("password", info.getString("Password"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return new HttpLoader(getActivity(),
						ApiServerPath.DM_USER_LOGIN, map);

			}
			return null;
		}

		@Override
		public void onLoadFinished(Loader<AsyncTaskLoaderResult<T>> loader,
				AsyncTaskLoaderResult<T> result) {
			T data = result != null ? result.data : null;
			Bundle args = result != null ? result.args : null;
			HttpResponseBean responseBean = (HttpResponseBean) data;
			switch (loader.getId()) {
			case LOADER_INITDATA:
				if (responseBean.getStates() != HttpResponseBean.HTTP_OK) {
				}
				JSONArray jsonArray = responseBean.getData().optJSONArray(
						"userInfo");
				SettingUtility.setUserInfo(jsonArray.optJSONObject(0)
						.toString());
				SettingUtility.setDefaultUserId(jsonArray.optJSONObject(0)
						.optString("Id"));
				SettingUtility.setDefaultToken(jsonArray.optJSONObject(0)
						.optString("Token"));
				SettingUtility.setDefaultPhone(jsonArray.optJSONObject(0)
						.optString("PhoneNo"));
				break;

			}
			getLoaderManager().destroyLoader(loader.getId());
		}

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> loader) {

		}
	};

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		log.e("MenuLeftFragment onDestroy");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log.e("MenuLeftFragment onCreateView");

		mView = inflater.inflate(R.layout.menu_left_fragment_layout, null);
		setupViews();
		return mView;
	}

	ImageLoader imageLoader = null;
	DisplayImageOptions options = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.defult1)
				.showImageOnFail(R.drawable.defult1).cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	public void UpdateIMage() {
		if (ZmarkApplication.getInstance().userIsOline()) {
			try {
				JSONObject infoObject = new JSONObject(
						SettingUtility.getUserInfo());
				if (infoObject != null) {
					if (!TextUtils.isEmpty(SettingUtility.getUserInfo())) {
						if (!TextUtils.isEmpty(infoObject.optString("Img"))) {

							imageLoader.displayImage(
									DateInfo.IP + infoObject.optString("Img"),
									touxiang, options,
									new ImageLoadingListener() {

										@Override
										public void onLoadingStarted(
												String imageUri, View view) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onLoadingFailed(
												String imageUri, View view,
												FailReason failReason) {
											// TODO Auto-generated method stub

										}

										@Override
										public void onLoadingComplete(
												String imageUri, View view,
												Bitmap loadedImage) {
											touxiang.setImageBitmap(ImageUtil
													.toRoundBitmap(loadedImage));
										}

										@Override
										public void onLoadingCancelled(
												String imageUri, View view) {
											// TODO Auto-generated method stub

										}
									});
						}
					} else {
					}
					// levl.setText("" + infoObject.optString("MemberLevel"));
					name.setText(TextUtils.isEmpty(infoObject
							.optString("BabyName")) ? "" : infoObject
							.optString("BabyName"));
				}
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} else {
			name.setText("未登录");
			// levl.setText("V0");
		}

	}

	private void setupViews() {
		item_1 = (RelativeLayout) mView
				.findViewById(R.id.leftmenu_item00Layout);
		item_2 = (RelativeLayout) mView
				.findViewById(R.id.leftmenu_item01Layout);
		item_3 = (RelativeLayout) mView
				.findViewById(R.id.leftmenu_item02Layout);
		item_4 = (RelativeLayout) mView
				.findViewById(R.id.leftmenu_item03Layout);

		touxiang = (ImageView) mView.findViewById(R.id.touxiang);
		name = (TextView) mView.findViewById(R.id.name);
		my_love_text = (TextView) mView.findViewById(R.id.my_love_text);
		// levl = (TextView) mView.findViewById(R.id.levl);
		my_history_text = (TextView) mView.findViewById(R.id.my_history_text);
		xingqi = (TextView) mView.findViewById(R.id.xingqi);
		time = (TextView) mView.findViewById(R.id.time);
		xingqi.setText(getDate());
		setTime();

		left_phone = (RelativeLayout) mView.findViewById(R.id.left_phone);
		item_1.setOnClickListener(this);
		item_2.setOnClickListener(this);
		item_3.setOnClickListener(this);
		item_4.setOnClickListener(this);
		mView.findViewById(R.id.leftmenu_item_activity)
				.setOnClickListener(this);

		left_phone.setOnClickListener(this);
	}

	public void setCollectNum(int num) {
		if (my_love_text != null)
			my_love_text.setText(String.valueOf(num));

	}

	public void getHistoryNum() {
		String strp = ISFirstUntil.isGetProductInfo(context);
		String strps[] = null;
		int num = 0;
		if (strp != null) {
			strps = strp.split(",");
			List<String> mList = new ArrayList<String>();
			for (int i = 0; i < strps.length; i++) {
				if (mList.contains(strps[i]))
					continue;
				mList.add(strps[i]);
			}
			num = mList.size();
			mList = null;
		} else {
			num = 0;
		}

		my_history_text.setText("" + num);
	}

	public String getDate() {
		Calendar calendar = Calendar.getInstance();
		int day = calendar.get(Calendar.DAY_OF_WEEK);
		Log.e("===", "" + day);
		String str = "";
		switch (day) {
		case 2:
			str = "星期一";
			break;
		case 3:
			str = "星期二";
			break;
		case 4:
			str = "星期三";
			break;
		case 5:
			str = "星期四";
			break;
		case 6:
			str = "星期五";
			break;
		case 7:
			str = "星期六";
			break;
		case 1:
			str = "星期日";
			break;

		default:
			break;
		}

		return str;
	}

	private void setTime() {

		StringBuilder buf = new StringBuilder();
		CalendarUtil cu = new CalendarUtil();

		String chineseMonth = cu.getChineseMonth(
				Integer.parseInt(getCurrentDate("yyyy")),
				Integer.parseInt(getCurrentDate("MM")),
				Integer.parseInt(getCurrentDate("dd")));
		String chineseDay = cu.getChineseDay(
				Integer.parseInt(getCurrentDate("yyyy")),
				Integer.parseInt(getCurrentDate("MM")),
				Integer.parseInt(getCurrentDate("dd")));
		buf.append("农历").append(chineseMonth).append(chineseDay);
		time.setText(buf);
	}

	public String getCurrentDate(String str) {
		String times = "";
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String s = sdf.format(date);
		String sn[] = s.split("-");
		if ("yyyy".equals(str)) {
			times = sn[0];
		} else if ("MM".equals(str)) {
			times = sn[1];
		} else if ("dd".equals(str)) {
			times = sn[2];
		}
		return times;
	}

	public void goBoKeFragment() {
		if (getActivity() == null)
			return;

		FragmentModel fragmentModel = mControlCenter.getBlogFragmentModel();
		if (getActivity() instanceof MainActivity) {
			MainActivity ra = (MainActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}

	private void goHuaTiFragment() {
		if (getActivity() == null)
			return;

		FragmentModel fragmentModel = mControlCenter.getYuLeFragmentModel();
		if (getActivity() instanceof MainActivity) {
			MainActivity ra = (MainActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}

	public void goSetFragment() {
		if (getActivity() == null)
			return;
		FragmentModel fragmentModel = mControlCenter.getTechFragmentModel();
		if (getActivity() instanceof MainActivity) {
			MainActivity ra = (MainActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}

	private void goHisotryFragment() {
		if (getActivity() == null)
			return;
		FragmentModel fragmentModel = mControlCenter.getTouTiaoFragmentModel();
		if (getActivity() instanceof MainActivity) {
			MainActivity ra = (MainActivity) getActivity();
			ra.switchContent(fragmentModel);
		}
	}

	@Override
	public void onResume() {
		UpdateIMage();
		getHistoryNum();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.leftmenu_item00Layout:
			changgeitem(0);
			if (flag == 1) {

			} else {
				goBoKeFragment();
			}

			break;
		case R.id.leftmenu_item01Layout:
			changgeitem(1);
			goHuaTiFragment();
			break;
		case R.id.leftmenu_item_activity:
			if (getActivity() instanceof MainActivity) {
				MainActivity ra = (MainActivity) getActivity();
				ra.intentActivity();
			}
			break;

		case R.id.leftmenu_item02Layout:
			changgeitem(2);
			goHisotryFragment();

			break;

		case R.id.leftmenu_item03Layout:
			changgeitem(3);
			goSetFragment();

			break;
		case R.id.left_phone:
			Intent intent;
			if (!ZmarkApplication.getInstance().userIsOline()) {
				intent = new Intent(context, LoginActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(context, UserCenterActivity.class);
				context.startActivity(intent);
			}
			intent = null;
			break;

		default:
			break;
		}

	}

	public void changgeitem(int i) {
		if (i == 0) {
			item_1.setBackgroundColor(0xff666666);
			item_2.setBackgroundColor(0x00000000);
			item_3.setBackgroundColor(0x00000000);
			item_4.setBackgroundColor(0x00000000);
		} else if (i == 1) {
			flag = 0;
			item_2.setBackgroundColor(0xff666666);
			item_1.setBackgroundColor(0x00000000);
			item_3.setBackgroundColor(0x00000000);
			item_4.setBackgroundColor(0x00000000);
		} else if (i == 2) {
			flag = 0;
			item_3.setBackgroundColor(0xff666666);
			item_2.setBackgroundColor(0x00000000);
			item_1.setBackgroundColor(0x00000000);
			item_4.setBackgroundColor(0x00000000);
		} else {
			flag = 0;
			item_4.setBackgroundColor(0xff666666);
			item_2.setBackgroundColor(0x00000000);
			item_3.setBackgroundColor(0x00000000);
			item_1.setBackgroundColor(0x00000000);
		}
	}

}
