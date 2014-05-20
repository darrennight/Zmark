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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.ApiServerPath;
import com.android.zmark.R;
import com.android.zmark.bean.IntentInterface;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.db.ImageUtil;
import com.android.zmark.bean.support.asyncdrawable.FileLocationMethod;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpLoader;
import com.android.zmark.bean.support.http.HttpLoader.HttpLoaderParserCallback;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.util.ISFirstUntil;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * 活动
 * 
 * 赞 家庭列表
 * 
 * @author yangwenfang
 * 
 * @param <T>
 */
public class ActivitiesPraiseListActivity<T> extends AbstractListActivity
		implements HttpLoaderParserCallback {
	String ID = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activities_praise_activity);
		init();
	}

	private void init() {
		initListView(R.id.empty, R.id.tv_message, R.id.include_progress,
				R.id.pb_progress, R.id.listView);
		Bundle bundle = getIntent().getBundleExtra("data");
		ID = bundle.getString("ID");
		((TextView) findViewById(R.id.tv_title)).setText(bundle
				.getString("Title"));
		onBackPressed(R.id.iv_button_menu_back);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Bundle bundle = new Bundle();
		bundle.putString("ID", String.valueOf(mList.get(arg2).ID));
		bundle.putString("UID", String.valueOf(mList.get(arg2).UID));
		bundle.putString("Name", mList.get(arg2).Name);
		bundle.putString("URL", mList.get(arg2).URL);
		bundle.putString("Slogan", mList.get(arg2).Slogan);
		bundle.putString("CreateTime", mList.get(arg2).CreateTime);
		bundle.putString("FatherName", mList.get(arg2).FatherName);
		bundle.putString("MotherName", mList.get(arg2).MotherName);
		bundle.putString("BabyName", mList.get(arg2).BabyName);
		bundle.putString("BabySex", mList.get(arg2).BabySex);
		bundle.putString("Description", mList.get(arg2).Description);
		bundle.putString("BabyAge", String.valueOf(mList.get(arg2).BabyAge));
		bundle.putString("TicketCount",
				String.valueOf(mList.get(arg2).TicketCount));
		bundle.putString("AID", String.valueOf(ID));
		IntentInterface.IntentAll(this, bundle,
				ActivitiesFamilyDetailActivity.class, -1);
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
		paramMap.put("aid", ID);
		paramMap.put("index", String.valueOf(page));
		paramMap.put("c", String.valueOf(size));
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
		paramMap.put("aid", ID);
		paramMap.put("index", String.valueOf(page));
		paramMap.put("c", String.valueOf(size));
		getSupportLoaderManager().restartLoader(LOADER_LIST_DATA, null,
				httpCallback);

	}

	private LinkedList<Family> mList = new LinkedList<Family>();

	class Family {
		public int ID;
		public int UID;
		public String Name;
		public String URL;
		public String Slogan;
		public String CreateTime;
		public String FatherName;
		public String MotherName;
		public String BabyName;
		public String BabySex;
		public String Description;
		public int BabyAge;
		public int TicketCount;
	}

	protected final int LOADER_PARISE = 0x1;
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
						ApiServerPath.DM_ACTIVITYS_FAMILY, paramMap, isFresh,
						ActivitiesPraiseListActivity.this);
			case LOADER_PARISE:
				paramMap.clear();
				if (args != null) {
					isFresh = args.getBoolean("fresh");
					paramMap.put("uid", args.getString("uid"));
					paramMap.put("fid", args.getString("fid"));
					paramMap.put("aid", args.getString("aid"));
				}
				return new HttpLoader(mContext,
						ApiServerPath.DM_ACTIVITYS_PARISE, paramMap);
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
				if (responseBean.mLinkedList != null) {
					mList.addAll(responseBean.mLinkedList);
					responseBean.mLinkedList = null;
				}
				more = responseBean.HasMore();
				initAdapterOrNotifyDataSet(more);
				break;
			case LOADER_PARISE:
				if (responseBean == null) { // 网络加载失败
					Toast.makeText(mContext, "获取数据失败，请检查网络", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (responseBean.getStates() != HttpResponseBean.HTTP_OK) {
					Toast.makeText(
							mContext,
							TextUtils.isEmpty(responseBean.getMessage()) ? "获取数据失败"
									: responseBean.getMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(mContext, "赞成功", Toast.LENGTH_SHORT).show();
				loadNewData(null); // 刷新数据
				break;
			}
			getSupportLoaderManager().destroyLoader(loader.getId());
		}

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> loader) {

		}
	};

	BaseAdapter mAdapter = null;

	@Override
	public void initAdapterOrNotifyDataSet(boolean hasmore) {
		hideLoadingView();
		if (mAdapter == null) {
			mAdapter = new ActivityPraiseAdapter();
			actualListView.setAdapter(mAdapter);

		} else {
			if (actualListView.getAdapter() == null)
				actualListView.setAdapter(mAdapter);
		}
		mAdapter.notifyDataSetChanged();
		if (mList != null && mList.size() == 0) // 当服务器返回没有数据的时候，listiew需要设置一个空信息
		{
			showEmptyMessage("暂无内容");
			hideLoadingView();
		} else
			hideEmptyMessage();
		super.initAdapterOrNotifyDataSet(hasmore);

	}

	class ActivityPraiseAdapter extends BaseAdapter {
		private LayoutInflater inflater;

		public ActivityPraiseAdapter() {
			inflater = LayoutInflater.from(mContext);
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			HotViewHolder holder = null;
			if (convertView == null || convertView.getTag() == null) {
				convertView = inflater.inflate(
						R.layout.activity_praise_item_layout, parent, false);
				holder = new HotViewHolder();
				holder.ivHeader = (ImageView) convertView
						.findViewById(R.id.iv_userheader);
				holder.tvFamilyName = (TextView) convertView
						.findViewById(R.id.tv_family_name);
				holder.tvSlogan = (TextView) convertView
						.findViewById(R.id.tv_family_slogan);
				holder.tvLike = (TextView) convertView
						.findViewById(R.id.tv_family_like);
				holder.tvDescription = (TextView) convertView
						.findViewById(R.id.tv_family_description);
				holder.btPraise = (Button) convertView
						.findViewById(R.id.bt_family_praise);
				convertView.setTag(holder);
			} else {
				holder = (HotViewHolder) convertView.getTag();
			}
			final ImageView mImageView = holder.ivHeader;
			if (!TextUtils.isEmpty(getItem(position).URL))
				imageLoader.displayImage(DateInfo.IP + getItem(position).URL,
						holder.ivHeader, options, new ImageLoadingListener() {

							@Override
							public void onLoadingStarted(String imageUri,
									View view) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// TODO Auto-generated method stub

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
								// TODO Auto-generated method stub

							}
						});

			else
				mImageView.setImageResource(R.drawable.main_baby);
			holder.tvFamilyName.setText(getItem(position).Name);
			holder.tvSlogan.setText(String.format("家庭活动口号:%s", TextUtils
					.isEmpty(getItem(position).Slogan) ? ""
					: getItem(position).Slogan));
			holder.tvLike.setText(String.format("%d赞",
					getItem(position).TicketCount));
			holder.tvDescription.setText(getItem(position).Description);
			holder.btPraise.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					Bundle bundle = new Bundle();
					bundle.putString("uid", SettingUtility.getDefaultUserId());
					bundle.putString("fid",
							String.valueOf(getItem(position).ID));
					bundle.putString("aid", ID);
					getSupportLoaderManager().restartLoader(LOADER_PARISE,
							bundle, httpCallback);

				}
			});
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
		public Family getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		private class HotViewHolder {
			ImageView ivHeader;
			TextView tvFamilyName, tvSlogan, tvLike, tvDescription;
			Button btPraise;
		}

	}

	@Override
	public void onParser(JSONObject data, HttpResponseBean httpBean) {
		if (null == data || httpBean == null)
			return;
		JSONArray array = data.optJSONArray("familyList");
		if (array != null) {
			for (int i = 0; i < array.length(); i++) {
				JSONObject item = array.optJSONObject(i);
				Family family = new Family();
				family.ID = item.optInt("ID");
				family.UID = item.optInt("UID");
				family.Name = item.optString("Name");
				family.URL = item.optString("URL");
				family.Description = item.optString("Description");
				family.CreateTime = item.optString("CreateTime");
				family.FatherName = item.optString("FatherName");
				family.MotherName = item.optString("MotherName");
				family.BabyName = item.optString("BabyName");
				family.BabySex = item.optString("BabySex");
				family.BabyAge = item.optInt("BabyAge");
				family.TicketCount = item.optInt("TicketCount");
				httpBean.mLinkedList.add(family);
				family = null;
			}
			array = null;
		}

	}
}
