package com.android.zmark.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.ApiServerPath;
import com.android.zmark.HttpConnection;
import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;
import com.android.zmark.adapter.FoodAdapter;
import com.android.zmark.bean.IntentInterface;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.support.BitmapDownloader;
import com.android.zmark.bean.support.asyncdrawable.FileLocationMethod;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpLoader;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.support.http.IHttpLoader;
import com.android.zmark.bean.support.http.IHttpLoader.HttpLoaderParserCallback;
import com.android.zmark.bean.util.Utility;
import com.android.zmark.entities.FoodEntity;
import com.android.zmark.entities.ImageList;
import com.android.zmark.ui.AdvertisementActivity;
import com.android.zmark.ui.CityListActivity;
import com.android.zmark.ui.FoodListActivity;
import com.android.zmark.ui.LoginActivity;
import com.android.zmark.ui.MainActivity;
import com.android.zmark.ui.MerchantInfoActivity;
import com.android.zmark.ui.NearbyEnterainmentActivity;
import com.android.zmark.ui.NearbyHotelActivity;
import com.android.zmark.ui.UserCenterActivity;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public class HomePageFragment<T> extends AbstractListFragment implements
		OnClickListener, HttpLoaderParserCallback {
	// private LinkedList<EveryDayItemBean> mListBean = new
	// LinkedList<EveryDayItemBean>();
	TextView city = null;

	public HomePageFragment() {
		super();
	}

	@Override
	public void onResume() {
		if (city != null) {
			if (app.city != null) {
				city.setText(app.city);
			}
		}
		if (autoRunnable != null && autoScrollHander != null) {
			try {
				autoScrollHander.postDelayed(autoRunnable, 5000);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		super.onResume();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		contentView = inflater.inflate(R.layout.home_page_fragment, null);
		return contentView;
	}

	ImageLoader imageLoader = null;
	DisplayImageOptions options = null;
	ImageSize imagesize = new ImageSize(200, 120);

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		start = false;
		app = ZmarkApplication.getInstance();
		super.onViewCreated(view, savedInstanceState);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.defalut_image_large)
				.showImageOnFail(R.drawable.defalut_image_large)
				.cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		view.findViewById(R.id.bt_search).setOnClickListener(this);
		view.findViewById(R.id.bt_mycenter).setOnClickListener(this);
		view.findViewById(R.id.bt_more).setOnClickListener(this);
		view.findViewById(R.id.bt_activity).setOnClickListener(this);
		city = ((TextView) view.findViewById(R.id.city));
		if (TextUtils.isEmpty(app.city)
				&& !TextUtils.isEmpty(SettingUtility.getLocationCity()))
			if (TextUtils.isEmpty(app.cord)
					&& TextUtils.isEmpty(SettingUtility.getLocationInfo()))
				app.cord = SettingUtility.getLocationInfo();
		city.setText(app.city);
		view.findViewById(R.id.mainitem).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						app.sm.toggle();
					}
				});
		view.findViewById(R.id.mainmessage).setOnClickListener(
				new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								com.android.zmark.ui.MessageListActivity.class);
						getActivity().startActivity(intent);
					}
				});
		view.findViewById(R.id.city_rel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								CityListActivity.class);
						getActivity().startActivity(intent);
					}
				});
		initCity();
		getLoaderManager().restartLoader(LOADER_INITDATA, null, httpCallback);
		mHandler.postDelayed(mMessageRunnable, 3000);

	}

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.obj != null) {
				try {
					JSONObject object = new JSONObject(msg.obj.toString());
					if (object != null) {
						if (object.optInt("MessageNum") > 0) {
							((TextView) getView().findViewById(R.id.tvMessage))
									.setText(String.valueOf(object
											.optInt("MessageNum")));
						}
						if (object.optInt("CollectionNum") > 0
								&& getActivity() != null) {
							((MainActivity) getActivity()).setCollectNum(object
									.optInt("CollectionNum"));
						}
					}
					object = null;
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}

	};

	String mDeviceId = null;
	final int checkMessage = 60 * 3; // 每隔3分钟检查一次
	Runnable mMessageRunnable = new Runnable() {

		@Override
		public void run() {

			// 检查新消息
			if (TextUtils.isEmpty(mDeviceId)) {
				TelephonyManager tm = (TelephonyManager) getActivity()
						.getSystemService(Context.TELEPHONY_SERVICE);
				mDeviceId = tm.getDeviceId();
				tm = null;
			}
			String str = SettingUtility.getUserInfo();
			if (TextUtils.isEmpty(str))
				return;
			try {
				JSONObject infoObject = new JSONObject(str);
				RequestParams params = new RequestParams();
				params.put("id", infoObject.optString("Id"));
				params.put("token", infoObject.optString("Token"));
				params.put("mid", infoObject.optString("MobileId"));
				params.put("n", "");
				AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						Log.w("新消息检查", TextUtils.isEmpty(content) ? ""
								: content);
						Message msg = mHandler.obtainMessage();
						msg.obj = content;
						mHandler.sendMessage(msg);
						msg = null;
					}

				};
				HttpConnection.AsyncGet(DateInfo.ROOT
						+ "GetCollectionNumAndMessageNum.ashx", params,
						responseHandler);
				mHandler.postDelayed(mMessageRunnable, 1000 * checkMessage);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 初始化地图模块
	 */
	LocalActivityManager manager = null;
	String cord;
	double lon1;
	double lat1;
	ZmarkApplication app;
	String citys;
	String coord;

	public void initCity() {
		app = ZmarkApplication.getInstance();
		if (app.city != null && !"".equals(app.city)) {
			citys = app.city;
		} else {
			citys = "";
		}
		if (app.cord != null && !"".equals(app.cord)) {
			coord = app.cord;
		} else {
			coord = "0,0";
		}
		page = 1;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		position = position - 1;
		if (position >= 0 && position < mList.size()) {
			Intent intent = new Intent(getActivity(),
					MerchantInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("info", mList.get(position));
			intent.putExtras(bundle);
			getActivity().startActivity(intent);
		}
	}

	@Override
	protected void initAdapterOrNotifyDataSet(boolean hasmore) {
		hideLoadingView();
		if (mAdapter == null) {
			mAdapter = new FoodAdapter(getActivity(), mList);
			actualListView.setAdapter(mAdapter);
		} else {
			if (actualListView.getAdapter() == null)
				actualListView.setAdapter(mAdapter);
			mAdapter.notifyDataSetChanged();
		}
		super.initAdapterOrNotifyDataSet(hasmore);
	}

	private Map<String, String> map = new HashMap<String, String>();
	private int page = 1;
	private int size = 10;
	private boolean more = true;
	private BaseAdapter mAdapter;

	@Override
	public void loadNewData(Object object) {
		more = true;
		map.clear();
		page = 1;
		map.put("i", String.valueOf(page));
		// map.put("city", TextUtils.isEmpty(citys) ? "北京" : citys);
		map.put("c", coord);
		Bundle bundle = new Bundle();
		bundle.putBoolean("fresh", true);
		getLoaderManager()
				.restartLoader(LOADER_LIST_DATA, bundle, httpCallback);
	}

	@Override
	protected void loadMoreData(Object object) {
		if (!more) {
			return;
		}
		// 1.c=坐标
		// 2.city=城市
		// 3.i=第几页
		page++;
		map.clear();
		map.put("i", String.valueOf(page));
		map.put("city", String.valueOf(citys));
		map.put("c", coord);
		getLoaderManager().restartLoader(LOADER_LIST_DATA, null, httpCallback);
	}

	List<FoodEntity> mList = new ArrayList<FoodEntity>();
	protected LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>> httpCallback = new LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>>() {
		// praisBbundle.putBoolean("isPrais", type != 0 && selectType == 1);
		@Override
		public Loader<AsyncTaskLoaderResult<T>> onCreateLoader(int id,
				Bundle args) {
			switch (id) {
			case LOADER_LIST_DATA:
				boolean isFresh = false;
				if (args != null)
					isFresh = args.getBoolean("fresh");
				return new IHttpLoader(getActivity(),
						ApiServerPath.DM_HOMEPAGE_HOTMERCHANT, map, isFresh,
						HomePageFragment.this);

			case LOADER_INITDATA:
				map.clear();
				// http://app10014.yunbosoft.com:9090/Interface/GetSlide.ashx?w=720&h=1280
				map.put("w", String.valueOf(720));
				map.put("h", String.valueOf(1280));
				return new HttpLoader(getActivity(),
						ApiServerPath.DM_HOMEPAGE_ADDS, map);
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
			case LOADER_LIST_DATA:
				if (responseBean == null) { // 网络加载失败
					Toast.makeText(getActivity(), "获取数据失败，请检查网络",
							Toast.LENGTH_SHORT).show();
					if (actualListView.getHeaderViewsCount() < 1
							&& mList != null && mList.size() == 0)
						netWorkLoadError();
					onPullDownUpRefreshComplete();
					page--;
					return;
				}
				if (loader instanceof IHttpLoader
						&& ((IHttpLoader) loader).isFresh) // 是刷新
				{
					mList.clear();
				}
				if (TextUtils.isEmpty(responseBean.getStr())) {
					if (!(responseBean.getStates() == HttpResponseBean.HTTP_NOMORE && page == 1))
						Toast.makeText(
								getActivity(),
								TextUtils.isEmpty(responseBean.getMessage()) ? (responseBean
										.getStates() == HttpResponseBean.HTTP_NOMORE ? "暂无更多数据"
										: "获取数据失败")
										: responseBean.getMessage(),
								Toast.LENGTH_SHORT).show();
					if (actualListView.getHeaderViewsCount() < 1
							&& mList != null && mList.size() == 0)
						netWorkLoadError();
					more = responseBean.getStates() == HttpResponseBean.HTTP_NOMORE ? false
							: true;
					initAdapterOrNotifyDataSet(more);
					page--;
					return;
				}
				if (responseBean.mLinkedList != null) {
					mList.addAll(responseBean.mLinkedList);
					responseBean.mLinkedList = null;
				}
				if (actualListView.getHeaderViewsCount() < 1 && mList != null
						&& mList.size() == 0)
					netWorkLoadError();
				else
					initAdapterOrNotifyDataSet(false);
				break;
			case LOADER_INITDATA:
				// 动态头部信息
				if (responseBean != null
						&& responseBean.getStates() == HttpResponseBean.HTTP_OK) {
					initHeaderViewInfo(responseBean.getData());
				}
				loadNewData(null);
				break;

			}
			getLoaderManager().destroyLoader(loader.getId());
		}

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> loader) {

		}
	};
	List<ImageList> mAdds;
	final Handler autoScrollHander = new Handler();
	Runnable autoRunnable;

	private void initHeaderViewInfo(JSONObject flag) {
		hideLoadingView();
		if (flag == null)
			return;
		JSONArray array = flag.optJSONArray("imageList");
		if (array != null && array.length() > 0) {
			if (mAdds == null)
				mAdds = new ArrayList<ImageList>();
			if (!mAdds.isEmpty())
				mAdds.clear();
			for (int i = 0; i < array.length(); i++) {
				JSONObject image = array.optJSONObject(i);
				ImageList mImageList = new ImageList();
				mImageList.ContentCategoryName = image
						.optString("ContentCategoryName");
				mImageList.Content = image.optString("Content");
				mImageList.URL = image.optString("URL");
				mImageList.Id = image.optInt("Id");
				mImageList.ContentId = image.optInt("ContentId");
				mAdds.add(mImageList);
				mImageList = null;
				image = null;
			}
		}
		final View headerView = LayoutInflater.from(getActivity()).inflate(
				R.layout.home_page_header_view, null, false);
		headerView.findViewById(R.id.hotel).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								NearbyHotelActivity.class);
						getActivity().startActivity(intent);
					}
				});
		headerView.findViewById(R.id.enterainment).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								NearbyEnterainmentActivity.class);
						getActivity().startActivity(intent);
					}
				});
		headerView.findViewById(R.id.food).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(),
								FoodListActivity.class);
						getActivity().startActivity(intent);

					}
				});
		if (mAdds != null && mAdds.size() != 0) {
			((Gallery) headerView.findViewById(R.id.main_viewpager))
					.setAdapter(new AddsAdapter());
			((SlidingFragmentActivity) getActivity()).getSlidingMenu()
					.addIgnoredView(
							((Gallery) headerView
									.findViewById(R.id.main_viewpager)));
			headerView.findViewById(R.id.main_viewpager).setVisibility(
					View.VISIBLE);
			((RelativeLayout.LayoutParams) headerView.findViewById(
					R.id.main_viewpager).getLayoutParams()).height = Utility
					.DisplayPixels(getActivity())[0] / 2;

			((Gallery) headerView.findViewById(R.id.main_viewpager))
					.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							if (mAdds.get(arg2).ContentCategoryName
									.equals("门店")) {
								// Intent intent = new Intent(getActivity(),
								// MerchantInfoActivity.class);
								// Bundle bundle = new Bundle();
								// bundle.putSerializable("info",
								// mList.get(position));
								// intent.putExtras(bundle);
								// getActivity().startActivity(intent);
							} else {
								Bundle bundle = new Bundle();
								bundle.putString("content",
										mAdds.get(arg2).Content);
								IntentInterface
										.IntentAll(getActivity(), bundle,
												AdvertisementActivity.class, -1);

							}

						}
					});

			if (autoRunnable == null) {
				autoRunnable = new Runnable() {
					int pos = 0;

					@Override
					public void run() {
						if (pos >= mAdds.size())
							pos = 0;
						((Gallery) headerView.findViewById(R.id.main_viewpager))
								.setSelection(pos);
						pos++;
						autoScrollHander.postDelayed(this, 5000);
					}
				};
				autoScrollHander.postDelayed(autoRunnable, 5000);
			}
		} else
			headerView.findViewById(R.id.main_viewpager).setVisibility(
					View.GONE);
		mPullRefreshListView.addHeaderView(headerView);
		if (mAdapter == null) {
			mAdapter = new FoodAdapter(getActivity(), mList);
			actualListView.setAdapter(mAdapter);
		}

	}

	@Override
	public void onPause() {
		super.onPause();
		if (autoRunnable != null && autoScrollHander != null) {
			try {
				autoScrollHander.removeCallbacks(autoRunnable);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	class AddsAdapter extends BaseAdapter {
		BitmapDownloader commander;

		public AddsAdapter() {
			super();
			commander = BitmapDownloader.getInstance();
		}

		@Override
		public int getCount() {
			return mAdds.size();
		}

		@Override
		public ImageList getItem(int position) {
			return mAdds.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder = null;
			if (convertView == null || convertView.getTag() == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(
						R.layout.adds_item_layout, parent, false);
				holder = new Holder();
				holder.userImage = (ImageView) convertView
						.findViewById(R.id.iv_adds);
				holder.lyDot = (LinearLayout) convertView
						.findViewById(R.id.layout_dot);

				android.widget.RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) holder.userImage
						.getLayoutParams();
				params.width = Utility.DisplayPixels(getActivity())[0];
				params.height = Utility.DisplayPixels(getActivity())[0] / 2;
				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}
			if (holder.lyDot.getChildCount() > 0)
				holder.lyDot.removeAllViews();
			for (int i = 0; i < getCount(); i++) {
				ImageView image = new ImageView(getActivity());
				image.setBackgroundDrawable(getResources().getDrawable(
						i == position ? R.drawable.ic_point_selected
								: R.drawable.ic_point_unselected));
				LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.WRAP_CONTENT,
						LinearLayout.LayoutParams.WRAP_CONTENT, 1);
				layout.setMargins(3, 0, 3, 0);
				image.setLayoutParams(layout);
				holder.lyDot.addView(image);
			}
			final ImageView mImageView = holder.userImage;
			if (!TextUtils.isEmpty(getItem(position).URL)) {
				BitmapDownloader.getInstance()
						.display(mImageView, 500, 400, getItem(position).URL,
								FileLocationMethod.picture_large);
				// imageLoader.loadImage(getItem(position).URL, imagesize,
				// options, new ImageLoadingListener() {
				//
				// @Override
				// public void onLoadingStarted(String imageUri,
				// View view) {
				// }
				//
				// @Override
				// public void onLoadingFailed(String imageUri,
				// View view, FailReason failReason) {
				// }
				//
				// @Override
				// public void onLoadingComplete(String imageUri,
				// View view, Bitmap loadedImage) {
				//
				// mImageView.setImageBitmap(BitmapUtils
				// .getRoundCornerBitmap(loadedImage, 42));
				// }
				//
				// @Override
				// public void onLoadingCancelled(String imageUri,
				// View view) {
				// }
				// });

				// imageLoader.displayImage(, mImageView,
				// new ImageLoadingListener() {
				//
				// @Override
				// public void onLoadingStarted(String imageUri,
				// View view) {
				//
				// }
				//
				// @Override
				// public void onLoadingFailed(String imageUri,
				// View view, FailReason failReason) {
				//
				// }
				//
				// @Override
				// public void onLoadingComplete(String imageUri,
				// View view, Bitmap loadedImage) {
				// mImageView.setImageBitmap(loadedImage);
				// }
				//
				// @Override
				// public void onLoadingCancelled(String imageUri,
				// View view) {
				//
				// }
				// });
			}
			return convertView;
		}

		private class Holder {
			public ImageView userImage;
			public LinearLayout lyDot;

		}
	}

	@Override
	public void onClick(View v) {

		InputMethodManager imm;
		switch (v.getId()) {
		case R.id.bt_activity:
			if (getActivity() == null)
				return;
			((MainActivity) getActivity()).intentActivity();
			break;
		case R.id.bt_search:
			if (getActivity() == null)
				return;
			((MainActivity) getActivity()).intentSearch();
			break;
		case R.id.bt_mycenter:
			Intent intent;
			if (!ZmarkApplication.getInstance().userIsOline()) {
				intent = new Intent(getActivity(), LoginActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(getActivity(), UserCenterActivity.class);
				getActivity().startActivity(intent);
			}
			intent = null;
			break;
		case R.id.bt_more:
			if (getActivity() == null)
				return;
			((MainActivity) getActivity()).intentSetting();
			break;
		case R.id.serach_but:
			break;
		default:
			break;
		}

	}

	@Override
	public void onParser(Object data, HttpResponseBean httpBean) {
		if (data == null || httpBean == null)
			return;
		try {
			JSONArray array = new JSONArray(data.toString());
			if (array != null && array.length() != 0) {
				for (int i = 0; i < array.length(); i++) {
					JSONObject item = array.optJSONObject(i);
					FoodEntity food = new FoodEntity();
					food.Time = item.optString("Time");
					food.Price = item.optString("Price");
					food.Intro = item.optString("Intro");
					food.City = item.optString("City");
					food.Area = item.optString("Area");
					food.Id = item.optInt("Id");
					food.Title = item.optString("Title");
					food.Address = item.optString("Address");
					food.Coord = item.optString("Coord");
					food.Telephone = item.optString("Telephone");
					food.Img = item.optString("Img");
					food.Status = item.optString("Status");
					food.IsOrder = item.optString("IsOrder");
					food.Distance = item.optString("Distance");
					food.ClassName = item.optString("ClassName");
					food.ContentId = item.optInt("ContentId");
					food.ContentName = item.optString("ContentName");
					food.SlideId = item.optString("SlideId");
					food.Discount = item.optDouble("Discount");
					food.DetailIntro = item.optString("DetailIntro");
					food.Productlist= item.optString("productlist");
					httpBean.mLinkedList.add(food);
					food = null;
					item = null;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}
