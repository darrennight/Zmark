package com.android.zmark.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.R.id;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.wigets.ImageGallery;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * 活动详情
 * 
 * 家庭详情
 * 
 * @author yangwenfang
 * 
 * @param <T>
 */
public class ActivitiesFamilyDetailActivity<T> extends AbstractActivity
		implements OnClickListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activities_family_detail_activity);
		init();
	}

	Bundle mBundle = null;

	private void init() {
		mBundle = getIntent().getBundleExtra("data");
		onBackPressed(R.id.iv_button_menu_back);
		if (!TextUtils.isEmpty(mBundle.getString("URL")))
			// commander.display((ImageView) findViewById(R.id.iv_userheader),
			// 80,
			// 80, mBundle.getString("URL"),
			// FileLocationMethod.picture_thumbnail);
			imageLoader.displayImage(DateInfo.IP + mBundle.getString("URL"),
					(ImageView) findViewById(R.id.iv_userheader), options,
					new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {
							((ImageView) findViewById(R.id.iv_userheader))
									.setImageBitmap(ImageUtil
											.toRoundBitmap(loadedImage));
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
						}
					});

		((TextView) findViewById(R.id.tv_family_name)).setText(mBundle
				.getString("Name"));
		((TextView) findViewById(R.id.tv_family_slogan)).setText(String.format(
				"家庭活动口号:%s",
				TextUtils.isEmpty(mBundle.getString("Slogan")) ? "" : mBundle
						.getString("Slogan")));
		((TextView) findViewById(R.id.tv_family_like)).setText(String.format(
				"%s个赞", mBundle.getString("TicketCount")));
		((TextView) findViewById(R.id.tv_family_description)).setText(mBundle
				.getString("Description"));
		getSupportLoaderManager().restartLoader(LOADER_ALBUM, mBundle,
				httpCallback);
		findViewById(R.id.iv_hotfmaily_next).setOnClickListener(this);
		findViewById(R.id.iv_hotfmaily_pre).setOnClickListener(this);
		findViewById(R.id.bt_family_comment).setOnClickListener(this);
		findViewById(R.id.tv_family_comment_content).setOnClickListener(this);
		findViewById(R.id.bt_family_praise).setOnClickListener(this);
	}

	private Map<String, String> paramMap = new HashMap<String, String>();
	protected final int LOADER_ALBUM = 0x1;
	protected final int LOADER_GETCOMMENT = 0x2;
	protected final int LOADER_PARISE = 0x3;
	protected final int LOADER_COMMENT = 0x4;
	protected LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>> httpCallback = new LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>>() {
		@Override
		public Loader<AsyncTaskLoaderResult<T>> onCreateLoader(int id,
				Bundle args) {
			// count=10&fid=8&index=1
			switch (id) {
			case LOADER_ALBUM:
				if (!paramMap.isEmpty())
					paramMap.clear();
				paramMap.put("count", "10");
				paramMap.put("index", "1");
				paramMap.put("fid", mBundle.getString("ID"));
				return new HttpLoader(mContext,
						ApiServerPath.DM_ACTIVITYS_GETALBUM, paramMap);
			case LOADER_GETCOMMENT:
				if (!paramMap.isEmpty())
					paramMap.clear();
				// 1.fid=家庭ID
				// 2.index=第几页
				// 3.count=每页条数
				paramMap.put("count", "1");
				paramMap.put("index", "1");
				paramMap.put("fid", mBundle.getString("ID"));
				return new HttpLoader(mContext,
						ApiServerPath.DM_ACTIVITYS_GETCOMMENT, paramMap);
			case LOADER_PARISE:
				paramMap.clear();
				if (args != null) {
					boolean isFresh = args.getBoolean("fresh");
					paramMap.put("uid", args.getString("uid"));
					paramMap.put("fid", args.getString("fid"));
					paramMap.put("aid", args.getString("aid"));
				}
				return new HttpLoader(mContext,
						ApiServerPath.DM_ACTIVITYS_PARISE, paramMap);
			case LOADER_COMMENT:
				paramMap.clear();
				if (args != null) {
					boolean isFresh = args.getBoolean("fresh");
					paramMap.put("uid", args.getString("uid"));
					paramMap.put("fid", args.getString("fid"));
					paramMap.put("content", args.getString("content"));
				}
				return new HttpLoader(mContext,
						ApiServerPath.DM_ACTIVITYS_COMMENT, paramMap);
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
			case LOADER_ALBUM:
				findViewById(R.id.view_loading).setVisibility(View.GONE);
				if (responseBean != null
						&& responseBean.getStates() == HttpResponseBean.HTTP_OK) {
					initHotFamily(responseBean.getData());
				}
				getSupportLoaderManager().restartLoader(LOADER_GETCOMMENT,
						null, httpCallback);
				break;
			case LOADER_GETCOMMENT:
				findViewById(R.id.view_loading).setVisibility(View.GONE);
				if (responseBean != null
						&& responseBean.getStates() == HttpResponseBean.HTTP_OK) {
					JSONArray array = responseBean.getData().optJSONArray(
							"reviewsList");

					if (array != null && array.length() != 0) {
						((TextView) findViewById(R.id.tv_family_comment_content))
								.setText(array.optJSONObject(0).optString(
										"Content"));
						findViewById(R.id.tv_family_comment_content)
								.setVisibility(View.VISIBLE);
						if (TextUtils.isEmpty(array.optJSONObject(0).optString(
								"Content"))
								|| TextUtils.isEmpty(array.optJSONObject(0)
										.optString("Img")))
							findViewById(R.id.iv_comment_user).setVisibility(
									View.GONE);
						else {
							findViewById(R.id.iv_comment_user).setVisibility(
									View.VISIBLE);
							imageLoader
									.displayImage(
											DateInfo.IP
													+ array.optJSONObject(0)
															.optString("Img"),
											((ImageView) findViewById(R.id.iv_comment_user)),
											options,
											new ImageLoadingListener() {

												@Override
												public void onLoadingStarted(
														String imageUri,
														View view) {
												}

												@Override
												public void onLoadingFailed(
														String imageUri,
														View view,
														FailReason failReason) {
													// TODO Auto-generated
													// method stub

												}

												@Override
												public void onLoadingComplete(
														String imageUri,
														View view,
														Bitmap loadedImage) {
													((ImageView) findViewById(R.id.iv_comment_user))
															.setImageBitmap(ImageUtil
																	.toRoundBitmap(loadedImage));
												}

												@Override
												public void onLoadingCancelled(
														String imageUri,
														View view) {

												}
											});

						}
					} else {
						findViewById(R.id.tv_family_comment_content)
								.setVisibility(View.GONE);
					}

				}
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
				break;
			case LOADER_COMMENT:
				if (responseBean == null) { // 网络加载失败
					Toast.makeText(mContext, "获取数据失败，请检查网络", Toast.LENGTH_SHORT)
							.show();
					return;
				}
				if (responseBean.getStates() != HttpResponseBean.HTTP_OK) {
					Toast.makeText(
							mContext,
							TextUtils.isEmpty(responseBean.getMessage()) ? "评论失败"
									: responseBean.getMessage(),
							Toast.LENGTH_SHORT).show();
					return;
				}
				Toast.makeText(mContext, "评论成功", Toast.LENGTH_SHORT).show();
				JSONArray array = responseBean.getData().optJSONArray(
						"reviewsInfo");
				if (array != null && array.length() != 0) {
					findViewById(R.id.tv_family_comment_content).setVisibility(
							View.VISIBLE);
					((TextView) findViewById(R.id.tv_family_comment_content))
							.setText(array.optJSONObject(0)
									.optString("Content"));
					if (TextUtils.isEmpty(array.optJSONObject(0).optString(
							"Content"))
							|| TextUtils.isEmpty(array.optJSONObject(0)
									.optString("Img")))

						findViewById(R.id.iv_comment_user).setVisibility(
								View.GONE);
					else {
						findViewById(R.id.iv_comment_user).setVisibility(
								View.VISIBLE);
						imageLoader
								.displayImage(
										DateInfo.IP
												+ array.optJSONObject(0)
														.optString("Img"),
										((ImageView) findViewById(R.id.iv_comment_user)),
										options, new ImageLoadingListener() {

											@Override
											public void onLoadingStarted(
													String imageUri, View view) {
											}

											@Override
											public void onLoadingFailed(
													String imageUri, View view,
													FailReason failReason) {
												// TODO Auto-generated
												// method stub

											}

											@Override
											public void onLoadingComplete(
													String imageUri, View view,
													Bitmap loadedImage) {
												((ImageView) findViewById(R.id.iv_comment_user))
														.setImageBitmap(ImageUtil
																.toRoundBitmap(loadedImage));
											}

											@Override
											public void onLoadingCancelled(
													String imageUri, View view) {

											}
										});
					}
				} else {
					findViewById(R.id.tv_family_comment_content).setVisibility(
							View.GONE);
				}

				break;

			}
			getSupportLoaderManager().destroyLoader(loader.getId());
		}

		public String URL;

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> loader) {
		}
	};

	class Ablum {
		public int ID;
		public int FID;
		public String Description;
		public String CreateTime;
		public String SmallURL;
		public String URL;
	}

	List<Ablum> mAblumSet;
	GalleryAdapter mGalleryAdapter, mThumbnailAdapter;

	private void initHotFamily(JSONObject data) {
		findViewById(R.id.view_loading).setVisibility(View.GONE);
		if (data == null)
			return;
		JSONArray AblumJson = data.optJSONArray("albumList");
		if (AblumJson != null && AblumJson.length() != 0) {
			if (mAblumSet == null)
				mAblumSet = new ArrayList<ActivitiesFamilyDetailActivity<T>.Ablum>();
			for (int i = 0; i < AblumJson.length(); i++) {
				JSONObject item = AblumJson.optJSONObject(i);
				Ablum mAblum = new Ablum();
				mAblum.ID = item.optInt("ID");
				mAblum.FID = item.optInt("FID");
				mAblum.Description = item.optString("Description");
				mAblum.URL = item.optString("URL");
				mAblum.CreateTime = item.optString("CreateTime");
				mAblum.SmallURL = item.optString("SmallURL");
				mAblumSet.add(mAblum);
				mAblum = null;
			}
			if (mGalleryAdapter == null)
				mGalleryAdapter = new GalleryAdapter(400);
			if (mThumbnailAdapter == null)
				mThumbnailAdapter = new GalleryAdapter(100);
			((ImageGallery) findViewById(R.id.ig_hotfamily_thumbnail))
					.setAdapter(mThumbnailAdapter);
			((ImageGallery) findViewById(R.id.ig_hotfamily_thumbnail))
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							((ImageGallery) findViewById(R.id.ig_hotfamily))
									.setSelection(arg2);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
			((ImageGallery) findViewById(R.id.ig_hotfamily))
					.setAdapter(mGalleryAdapter);
			((ImageGallery) findViewById(R.id.ig_hotfamily))
					.setOnItemSelectedListener(new OnItemSelectedListener() {

						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							((ImageGallery) findViewById(R.id.ig_hotfamily_thumbnail))
									.setSelection(arg2);
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
			findViewById(R.id.ly_content).setVisibility(View.VISIBLE);

		} else
			findViewById(R.id.rl_hotfmaily_ablumset).setVisibility(View.GONE);

	}

	class GalleryAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		int width = 40;

		@Override
		public int getCount() {
			return mAblumSet.size();
		}

		public GalleryAdapter(int width) {
			inflater = LayoutInflater.from(mContext);
			this.width = width;
		}

		@Override
		public Ablum getItem(int arg0) {
			return mAblumSet.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHour holder = null;
			if (convertView == null || convertView.getTag() == null) {
				convertView = inflater.inflate(
						R.layout.ablumset_gallery_item_layout, parent, false);
				holder = new ViewHour();
				holder.tvImage = (ImageView) convertView
						.findViewById(R.id.iv_userimage);
				LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) holder.tvImage
						.getLayoutParams();
				if (width > 100)
					mLayoutParams.weight = LinearLayout.LayoutParams.FILL_PARENT;
				else
					mLayoutParams.width = width;
				mLayoutParams.height = width;
				convertView.setTag(holder);
			} else {
				holder = (ViewHour) convertView.getTag();
			}
			if (!TextUtils.isEmpty(getItem(position).URL))
				commander.display(holder.tvImage, width, width, DateInfo.IP
						+ getItem(position).URL,
						FileLocationMethod.picture_bmiddle);
			return convertView;
		}

		public class ViewHour {

			public ImageView tvImage;
		}
	}

	int index = 0;

	@Override
	public void onClick(View v) {
		int vId = v.getId();
		if (vId == R.id.iv_hotfmaily_next) {
			index++;
			if (mAblumSet != null && index >= mAblumSet.size()) {
				index = mAblumSet.size() - 1;
				return;

			}
			((ImageGallery) findViewById(R.id.ig_hotfamily_thumbnail))
					.setSelection(index);
			((ImageGallery) findViewById(R.id.ig_hotfamily))
					.setSelection(index);

		} else if (vId == R.id.iv_hotfmaily_pre) {
			index--;
			if (index < 0) {
				index = 0;
				return;

			}
			((ImageGallery) findViewById(R.id.ig_hotfamily_thumbnail))
					.setSelection(index);
			((ImageGallery) findViewById(R.id.ig_hotfamily))
					.setSelection(index);
		} else if (vId == R.id.tv_family_comment_content) {
			IntentInterface.IntentAll(this, mBundle,
					ActivitiesCommentListActivity.class, -1);
		}

		else if (vId == R.id.bt_family_comment) {
			AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
			builder.setTitle("评论");
			final EditText content = new EditText(mContext);
			content.setMinLines(6);
			content.setGravity(Gravity.TOP);
			builder.setView(content);
			builder.setPositiveButton("提交",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							if (TextUtils.isEmpty(content.getText().toString())) {
								Toast.makeText(mContext, "评论内容不能为空",
										Toast.LENGTH_SHORT).show();
								return;
							}
							Bundle bundle = new Bundle();
							bundle.putString("uid",
									SettingUtility.getDefaultUserId());
							bundle.putString("fid", mBundle.getString("ID"));
							bundle.putString("content", content.getText()
									.toString());
							getSupportLoaderManager().restartLoader(
									LOADER_COMMENT, bundle, httpCallback);
						}
					});
			builder.create().show();
			builder = null;

		} else if (vId == R.id.bt_family_praise) {
			Bundle bundle = new Bundle();
			bundle.putString("uid", SettingUtility.getDefaultUserId());
			bundle.putString("fid", mBundle.getString("ID"));
			bundle.putString("aid", mBundle.getString("AID"));
			getSupportLoaderManager().restartLoader(LOADER_PARISE, bundle,
					httpCallback);

		}
	}
}
