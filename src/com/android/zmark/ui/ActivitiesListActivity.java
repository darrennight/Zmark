package com.android.zmark.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.util.TimeUtils;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.ApiServerPath;
import com.android.zmark.R;
import com.android.zmark.bean.IntentInterface;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.support.asyncdrawable.FileLocationMethod;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpLoader;
import com.android.zmark.bean.support.http.HttpLoader.HttpLoaderParserCallback;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.util.TimeUtility;

public class ActivitiesListActivity<T> extends AbstractListActivity implements
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

	private LinkedList<Activitys> mList = new LinkedList<ActivitiesListActivity<T>.Activitys>();
	private BaseAdapter mAdapter;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		Bundle mBundle = new Bundle();
		mBundle.putString("ApplyWay", mList.get(position).ApplyWay);
		mBundle.putString("BeginTime", mList.get(position).BeginTime);
		mBundle.putString("Content", mList.get(position).Content);
		mBundle.putString("EndTime", mList.get(position).EndTime);
		mBundle.putString("ID", mList.get(position).ID);
		mBundle.putString("Name", mList.get(position).Name);
		mBundle.putString("Type", mList.get(position).Type);
		mBundle.putString("URL", mList.get(position).URL);
		mBundle.putInt("Number", mList.get(position).Number);
		mBundle.putInt("OverNumber", mList.get(position).OverNumber);
		IntentInterface.IntentAll(this, mBundle,
				ActivitiesDetailActivity.class, -1);

	}

	@Override
	public void initAdapterOrNotifyDataSet(boolean hasmore) {
		hideLoadingView();
		if (mAdapter == null) {
			mAdapter = new ActivityAdapter();
			actualListView.setAdapter(mAdapter);

		} else {
			if (actualListView.getAdapter() == null)
				actualListView.setAdapter(mAdapter);
		}
		mAdapter.notifyDataSetChanged();
		if (mList != null && mList.size() == 0) // 当服务器返回没有数据的时候，listiew需要设置一个空信息
		{
			showEmptyMessage("暂无活动内容");
			hideLoadingView();
		} else
			hideEmptyMessage();
		super.initAdapterOrNotifyDataSet(hasmore);

	}

	class ActivityAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public ActivityAdapter() {
			inflater = LayoutInflater.from(mContext);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HotViewHolder holder = null;
			if (convertView == null || convertView.getTag() == null) {
				convertView = inflater.inflate(R.layout.activity_item_layout,
						parent, false);

				holder = new HotViewHolder();
				holder.ivHeader = (ImageView) convertView
						.findViewById(R.id.iv_header);
				holder.tvTitle = (TextView) convertView
						.findViewById(R.id.tv_title);
				holder.tvAllCount = (TextView) convertView
						.findViewById(R.id.tv_allcount);
				holder.tvEnrollcount = (TextView) convertView
						.findViewById(R.id.tv_enrollcount);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.tv_time);
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.tvMethods = (TextView) convertView
						.findViewById(R.id.tv_methods);
				convertView.setTag(holder);
			} else {
				holder = (HotViewHolder) convertView.getTag();
			}
			if (!TextUtils.isEmpty(getItem(position).URL))
				commander.display(holder.ivHeader, 80, 80, DateInfo.IP
						+ getItem(position).URL,
						FileLocationMethod.picture_thumbnail);
			holder.tvTitle.setText(getItem(position).Name);
			holder.tvAllCount
					.setText(Html
							.fromHtml("<font color=#000000>活动人数:</font><font color=#FF0000>"
									+ getItem(position).Number + "</font>"));
			holder.tvEnrollcount
					.setText(Html
							.fromHtml("<font color=#000000>剩余名额:</font><font color=#FF0000>"
									+ getItem(position).OverNumber + "</font>"));
			holder.tvTime.setText(Html.fromHtml("<font color=#000000>时间:"
					+ TimeUtility.dateToString(getItem(position).BeginTime)
					+ "</font><font color=#FF0000>至</font><font color=#000000>"
					+ TimeUtility.dateToString(getItem(position).EndTime)
					+ "</font>"));

			holder.tvMethods.setText(String.format("报名方式:%s",
					getItem(position).ApplyWay));
			holder.tvContent.setText(getItem(position).Content);
			return convertView;
		}

		@Override
		public int getCount() {
			if (mList != null) {
				return mList.size();
			} else {
				return 0;
			}
		}

		@Override
		public Activitys getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		private class HotViewHolder {
			ImageView ivHeader;
			TextView tvTitle, tvAllCount, tvMethods, tvEnrollcount, tvTime,
					tvContent;
		}

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
		// 1.index=第几页
		// 2.count=每页条数（默认10条）
		paramMap.put("index", String.valueOf(page));
		paramMap.put("count", String.valueOf(size));
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
		paramMap.put("index", String.valueOf(page));
		paramMap.put("count", String.valueOf(size));
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
				return new HttpLoader(mContext,
						ApiServerPath.DM_ACTIVITYS_LIST, paramMap, isFresh,
						ActivitiesListActivity.this);
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
				if (((HttpLoader) loader).isFresh) // 是刷新
				{
					mList.clear();
				}
				if (responseBean.getStates() != HttpResponseBean.HTTP_OK) {
					if (!(responseBean.getStates() == HttpResponseBean.HTTP_NOMORE && page == 1))
						Toast.makeText(
								mContext,
								TextUtils.isEmpty(responseBean.getMessage()) ? (responseBean
										.getStates() == HttpResponseBean.HTTP_NOMORE ? "暂无更多"
										: "获取数据失败")
										: responseBean.getMessage(),
								Toast.LENGTH_SHORT).show();
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
	public void onParser(JSONObject data, HttpResponseBean httpBean) {
		if (null == data || httpBean == null)
			return;
		JSONArray array = data.optJSONArray("activityList");
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject itemJson = array.optJSONObject(i);
				Activitys activity = new Activitys();
				activity.ID = itemJson.optString("ID");
				activity.Type = itemJson.optString("Type");
				activity.Name = itemJson.optString("Name");
				activity.Content = itemJson.optString("Content");
				activity.BeginTime = itemJson.optString("BeginTime");
				activity.EndTime = itemJson.optString("EndTime");
				activity.URL = itemJson.optString("URL");
				activity.ApplyWay = itemJson.optString("ApplyWay");
				activity.Number = itemJson.optInt("Number");
				activity.OverNumber = itemJson.optInt("OverNumber");
				httpBean.mLinkedList.add(activity);
				activity = null;
			}
		}
		array = null;
		// {
		// "status": 255,
		// "message": "成功",
		// "data": {
		// "activityList": [
		// {
		// "ID": 1,
		// "Type": "小赛",
		// "Name": "活动名称1",
		// "Content": "活动内容1",
		// "BeginTime": "2014-04-21T00:00:00",
		// "EndTime": "2014-04-21T00:00:00",
		// "URL": null,
		// "Number": 200,
		// "OverNumber": 50,
		// "ApplyWay": "微信"
		// }
		// ]
		// }
		// }
	}

}
