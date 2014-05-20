package com.android.zmark.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.android.zmark.ApiServerPath;
import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;
import com.android.zmark.adapter.FoodAdapter;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.support.http.IHttpLoader;
import com.android.zmark.bean.support.http.IHttpLoader.HttpLoaderParserCallback;
import com.android.zmark.entities.FoodEntity;

public class SearchListActivity<T> extends AbstractListActivity implements
		HttpLoaderParserCallback {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specialedition_list_activity);
		init();
	}

	private void init() {
		initListView(R.id.empty, R.id.tv_message, R.id.include_progress,
				R.id.pb_progress, R.id.listView);
		Bundle bundle = getIntent().getBundleExtra("data");
		onBackPressed(R.id.iv_button_menu_back);
		// actualListView.setSelector(R.drawable.activity_background);
		// actualListView.setDividerHeight(0);
	}

	List<FoodEntity> mList = new ArrayList<FoodEntity>();
	private BaseAdapter mAdapter;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Intent intent = new Intent(mContext, MerchantInfoActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("info", mList.get(position));
		intent.putExtras(bundle);
		startActivity(intent);

	}

	@Override
	public void initAdapterOrNotifyDataSet(boolean hasmore) {
		hideLoadingView();
		if (mAdapter == null) {
			mAdapter = new FoodAdapter(mContext, mList);
			actualListView.setAdapter(mAdapter);

		} else {
			if (actualListView.getAdapter() == null)
				actualListView.setAdapter(mAdapter);
		}
		mAdapter.notifyDataSetChanged();
		if (mList != null && mList.size() == 0) // 当服务器返回没有数据的时候，listiew需要设置一个空信息
		{
			showEmptyMessage("暂无搜索内容");
			hideLoadingView();
		} else
			hideEmptyMessage();
		super.initAdapterOrNotifyDataSet(hasmore);

	}

	class Activitys {
		public String ID;
		public String Type;
		public String Name;
		public String Content;
		public String BeginTime;
		public String EndTime;
		public String URL;
		public int OverNumber;
		public int Number;
		public String ApplyWay;
	}

	private Map<String, String> paramMap = new HashMap<String, String>();
	private int page = 1;
	private int size = 10;
	private boolean more = true;

	@Override
	public void loadNewData(Object object) {
		more = true;
		paramMap.clear();
		page = 1;
		paramMap.put("c",
				TextUtils.isEmpty(ZmarkApplication.getInstance().cord) ? "0,0"
						: ZmarkApplication.getInstance().cord);
		paramMap.put("i", String.valueOf(page));
		paramMap.put("content", getIntent().getStringExtra("info"));
		Bundle bundle = new Bundle();
		bundle.putBoolean("fresh", true);
		getSupportLoaderManager().restartLoader(LOADER_LIST_DATA, bundle,
				httpCallback);

	}

	@Override
	protected void loadMoreData(Object object) {
		if (!more) {
			return;
		}
		page++;
		paramMap.clear();
		paramMap.put("c",
				TextUtils.isEmpty(ZmarkApplication.getInstance().cord) ? "0,0"
						: ZmarkApplication.getInstance().cord);
		paramMap.put("i", String.valueOf(page));
		paramMap.put("content", getIntent().getStringExtra("info"));
		getSupportLoaderManager().restartLoader(LOADER_LIST_DATA, null,
				httpCallback);

	}

	protected LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>> httpCallback = new LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>>() {
		@Override
		public Loader<AsyncTaskLoaderResult<T>> onCreateLoader(int id,
				Bundle args) {
			switch (id) {
			case LOADER_LIST_DATA:
				boolean isFresh = false;
				if (args != null)
					isFresh = args.getBoolean("fresh");
				return new IHttpLoader(mContext,
						ApiServerPath.DM_SEARCH_MERCHANT, paramMap, isFresh,
						SearchListActivity.this);

				// boolean fresh, HttpLoaderParserCallback
			default:
				break;
			}
			return null;

		}

		@Override
		public void onLoadFinished(Loader<AsyncTaskLoaderResult<T>> loader,
				AsyncTaskLoaderResult<T> result) {
			T data = result != null ? result.data : null;
			HttpResponseBean responseBean = (HttpResponseBean) data;
			switch (loader.getId()) {
			case LOADER_LIST_DATA:
				if (data == null) { // 网络加载失败
					Toast.makeText(mContext, "获取数据失败，请检查网络", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (((IHttpLoader) loader).isFresh) // 是刷新
				{
					mList.clear();
				}
				if (TextUtils.isEmpty(responseBean.getStr())) {

					if (mList != null && mList.size() == 0)
						netWorkLoadError();
					more = responseBean.HasMore();
					initAdapterOrNotifyDataSet(more);
					page--;
					return;
				}
				more = responseBean.HasMore();
				initAdapterOrNotifyDataSet(more);
				break;
			}
			if (responseBean.mLinkedList != null) {
				mList.addAll(responseBean.mLinkedList);
				responseBean.mLinkedList = null;
			}
			more = responseBean.HasMore();
			initAdapterOrNotifyDataSet(more);
			getSupportLoaderManager().destroyLoader(loader.getId());
		}

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> loader) {

		}
	};

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
					food.Discount = item.optDouble("Discount");
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
