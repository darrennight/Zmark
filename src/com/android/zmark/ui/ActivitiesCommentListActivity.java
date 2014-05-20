package com.android.zmark.ui;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.graphics.Bitmap;
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
import com.android.zmark.bean.db.ImageUtil;
import com.android.zmark.bean.support.asyncdrawable.FileLocationMethod;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpLoader;
import com.android.zmark.bean.support.http.HttpLoader.HttpLoaderParserCallback;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.util.TimeUtility;
import com.android.zmark.bean.util.Utility;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class ActivitiesCommentListActivity<T> extends AbstractListActivity
		implements HttpLoaderParserCallback {

	// 评论列表
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.specialedition_list_activity);
		init();
	}

	private String familyID = null;

	private void init() {
		((TextView) findViewById(R.id.tv_title)).setText("评论");
		initListView(R.id.empty, R.id.tv_message, R.id.include_progress,
				R.id.pb_progress, R.id.listView);
		Bundle bundle = getIntent().getBundleExtra("data");
		familyID = bundle.getString("ID");
		onBackPressed(R.id.iv_button_menu_back);
		// actualListView.setSelector(R.drawable.activity_background);
		// actualListView.setDividerHeight(0);
	}

	private LinkedList<Comment> mList = new LinkedList<ActivitiesCommentListActivity<T>.Comment>();
	private BaseAdapter mAdapter;

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//
		// Bundle mBundle = new Bundle();
		// mBundle.putString("ApplyWay", mList.get(position).ApplyWay);
		// mBundle.putString("BeginTime", mList.get(position).BeginTime);
		// mBundle.putString("Content", mList.get(position).Content);
		// mBundle.putString("EndTime", mList.get(position).EndTime);
		// mBundle.putString("ID", mList.get(position).ID);
		// mBundle.putString("Name", mList.get(position).Name);
		// mBundle.putString("Type", mList.get(position).Type);
		// mBundle.putString("URL", mList.get(position).URL);
		// mBundle.putInt("Number", mList.get(position).Number);
		// mBundle.putInt("OverNumber", mList.get(position).OverNumber);
		// IntentInterface.IntentAll(this, mBundle,
		// ActivitiesDetailActivity.class, -1);

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
			showEmptyMessage("暂无评论内容");
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
				convertView = inflater.inflate(R.layout.comment_item_layout,
						parent, false);
				holder = new HotViewHolder();
				holder.ivHeader = (ImageView) convertView
						.findViewById(R.id.iv_comment_user);
				holder.tvNickName = (TextView) convertView
						.findViewById(R.id.tv_nickname);
				holder.tvContent = (TextView) convertView
						.findViewById(R.id.tv_content);
				holder.tvTime = (TextView) convertView
						.findViewById(R.id.tv_time);
				convertView.setTag(holder);
			} else {
				holder = (HotViewHolder) convertView.getTag();
			}
			final ImageView mImageView = holder.ivHeader;
			if (!TextUtils.isEmpty(getItem(position).Img))
				imageLoader.displayImage(DateInfo.IP + getItem(position).Img,
						mImageView, options, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
							}

							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								mImageView.setImageBitmap(ImageUtil
										.toRoundBitmap(loadedImage));
							}

							@Override
							public void onLoadingCancelled(String imageUri,
									View view) {
							}
						});
			else
				mImageView.setImageResource(R.drawable.main_baby);
			holder.tvNickName.setText(TextUtils
					.isEmpty(getItem(position).BabyName)
					|| "null".equals(getItem(position).BabyName) ? ""
					: getItem(position).BabyName);
			holder.tvContent.setText(getItem(position).Content);
			holder.tvTime.setText(TimeUtility
					.dateToString(getItem(position).CreateTime));
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
		public Comment getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		private class HotViewHolder {
			ImageView ivHeader;
			TextView tvNickName, tvContent, tvTime;
		}

	}

	class Comment {
		public String ID;
		public String UID;
		public String FID;
		public String Content;
		public String CreateTime;
		public String PhoneNo;
		public String BabyName;
		public String Img;
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
		paramMap.put("fid", familyID);
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
		paramMap.put("fid", familyID);
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
						ApiServerPath.DM_ACTIVITYS_COMMENT_LIST, paramMap,
						isFresh, ActivitiesCommentListActivity.this);
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
										: "暂无评论数据")
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
		JSONArray array = data.optJSONArray("reviewsList");
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject itemJson = array.optJSONObject(i);
				Comment activity = new Comment();
				activity.ID = itemJson.optString("ID");
				activity.UID = itemJson.optString("UID");
				activity.FID = itemJson.optString("FID");
				activity.Content = itemJson.optString("Content");
				activity.CreateTime = itemJson.optString("CreateTime");
				activity.PhoneNo = itemJson.optString("PhoneNo");
				activity.BabyName = itemJson.optString("BabyName");
				activity.Img = itemJson.optString("Img");
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
