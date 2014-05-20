package com.android.zmark.ui;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.android.zmark.ApiServerPath;
import com.android.zmark.R;
import com.android.zmark.bean.IntentInterface;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.db.ImageUtil;
import com.android.zmark.bean.db.SP;
import com.android.zmark.bean.socialshare.OnekeyShare;
import com.android.zmark.bean.socialshare.OnekeyShare.ShareListener;
import com.android.zmark.bean.support.asyncdrawable.FileLocationMethod;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpLoader;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.util.TimeUtility;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

/**
 * 活动详情
 * 
 * @author yangwenfang
 * 
 * @param <T>
 */
public class ActivitiesDetailActivity<T> extends AbstractActivity implements
		OnClickListener ,ShareListener{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activities_detail_activity);
		init();
	}

	String ID = null; // 活动ID
	Bundle bundle = null;

	private void init() {
		bundle = getIntent().getBundleExtra("data");
		onBackPressed(R.id.iv_button_menu_back);
		findViewById(R.id.bt_activity_registration).setOnClickListener(this);
		findViewById(R.id.bt_activity_like).setOnClickListener(this);
		findViewById(R.id.bt_activity_recommend).setOnClickListener(this);
		findViewById(R.id.iv_hotfmaily_next).setOnClickListener(this);
		findViewById(R.id.iv_hotfmaily_pre).setOnClickListener(this);
		((TextView) findViewById(R.id.tv_activity_title)).setText(bundle
				.getString("Name"));
		((TextView) findViewById(R.id.tv_title)).setText(bundle
				.getString("Name"));

		((TextView) findViewById(R.id.tv_allcount))
				.setText(Html
						.fromHtml("<font color=#000000>活动人数:</font><font color=#FF0000>"
								+ bundle.getInt("Number") + "</font>"));
		((TextView) findViewById(R.id.tv_enrollcount))
				.setText(Html
						.fromHtml("<font color=#000000>剩余名额:</font><font color=#FF0000>"
								+ bundle.getInt("OverNumber") + "</font>"));

		((TextView) findViewById(R.id.tv_time))
				.setText(Html.fromHtml("<font color=#000000>时间:"
						+ TimeUtility.dateToString(bundle
								.getString("BeginTime"))
						+ "</font><font color=#FF0000>至</font><font color=#000000>"
						+ TimeUtility.dateToString(bundle.getString("EndTime"))
						+ "</font>"));
		((TextView) findViewById(R.id.tv_methods)).setText(String.format(String
				.format("报名方式:%s", bundle.getString("ApplyWay"))));
		((TextView) findViewById(R.id.tv_content)).setText(bundle
				.getString("Content"));
		// 1.i=第几页
		// 2.c=每页条数（默认10条）
		// 3.aid=活动ID
		ID = bundle.getString("ID");
		getSupportLoaderManager().restartLoader(LOADER_INITDATA, bundle,
				httpCallback);
		ShareSDK.initSDK(this);
	}
	final int LOADER_ADD=0x102;
	private Map<String, String> paramMap = new HashMap<String, String>();

	protected LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>> httpCallback = new LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>>() {
		@Override
		public Loader<AsyncTaskLoaderResult<T>> onCreateLoader(int id,
				Bundle args) {
			switch (id) {
			case LOADER_INITDATA:
				paramMap.put("aid", ID);
				paramMap.put("i", "1");
				paramMap.put("c", "18");
				return new HttpLoader(mContext,
						ApiServerPath.DM_ACTIVITYS_FAMILY, paramMap);
			case LOADER_ADD:
				paramMap.clear();
				paramMap.put("uid", SettingUtility.getDefaultUserId());
				paramMap.put("token", SettingUtility.getDefaultToken());
				return new HttpLoader(mContext, ApiServerPath.DM_ADDSHARE,
						paramMap);
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
			case LOADER_INITDATA:
				findViewById(R.id.view_loading).setVisibility(View.GONE);
				if (responseBean != null
						&& responseBean.getStates() == HttpResponseBean.HTTP_OK) {
					initHotFamily(responseBean.getData());
				}
				
			break;
			case LOADER_ADD:
				
				break;
				
			}
			getSupportLoaderManager().destroyLoader(loader.getId());
		}

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> loader) {

		}
	};
	List<View> viewList;
	List<Family> mList;
	ViewPager mViewPager;

	class Family {
		public int ID;
		public int UID;
		public String Name;
		public String URL;
		public String Description;
		public String CreateTime;
		public String FatherName;
		public String MotherName;
		public String BabyName;
		public String BabySex;
		public String Slogan;
		public int BabyAge;
		public int TicketCount;
	}

	private void initHotFamily(JSONObject data) {
		JSONArray familyArray = data.optJSONArray("familyList");
		if (familyArray != null && familyArray.length() != 0) {
			findViewById(R.id.ly_hotFamily).setVisibility(View.VISIBLE);
			mViewPager = (ViewPager) findViewById(R.id.ig_hotfamily);
			viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
			mList = new ArrayList<ActivitiesDetailActivity<T>.Family>();
			for (int i = 0; i < familyArray.length(); i++) {
				JSONObject item = familyArray.optJSONObject(i);
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
				mList.add(family);
				family = null;
			}
			if (mList.size() > 0) {
				int size = (mList.size() % 6 == 0 ? mList.size() / 6 : (mList
						.size() / 6 + 1));
				for (int i = 0; i < size; i++) {
					LayoutInflater lf = getLayoutInflater().from(mContext);
					View view = lf.inflate(R.layout.activity_hotfamily_layout,
							null);
					viewList.add(view);
				}
				PagerAdapter pagerAdapter = new PagerAdapter() {
					@Override
					public boolean isViewFromObject(View arg0, Object arg1) {
						return arg0 == arg1;
					}

					@Override
					public int getCount() {
						return viewList.size();
					}

					@Override
					public void destroyItem(ViewGroup container, int position,
							Object object) {
						container.removeView(viewList.get(position));
					}

					@Override
					public int getItemPosition(Object object) {

						return super.getItemPosition(object);
					}

					@Override
					public CharSequence getPageTitle(int position) {
						return "";
					}

					@Override
					public Object instantiateItem(ViewGroup container,
							int position) {
						container.addView(viewList.get(position));
						excueItemView(viewList.get(position), position);
						// weibo_button=(Button) findViewById(R.id.button1);
						// weibo_button.setOnClickListener(new OnClickListener()
						// {
						//
						// public void onClick(View v) {
						// intent=new
						// Intent(ViewPagerDemo.this,WeiBoActivity.class);
						// startActivity(intent);
						// }
						// });
						return viewList.get(position);
					}

				};
				mViewPager.setAdapter(pagerAdapter);

			}

		} else
			findViewById(R.id.ly_hotFamily).setVisibility(View.GONE);

	}

	private void excueItemView(View mView, int postion) {
		int size = mList.size();
		int index = postion * 6;
		for (int i = 0; i < 6; i++, index++) {
			switch (i) {
			case 0:
				excueItemView(index < size ? mList.get(index) : null,
						(ImageView) mView.findViewById(R.id.iv_user1),
						(TextView) mView.findViewById(R.id.bt_like1),
						(TextView) mView.findViewById(R.id.bt_family_name1));
				break;
			case 1:
				excueItemView(index < size ? mList.get(index) : null,
						(ImageView) mView.findViewById(R.id.iv_user2),
						(TextView) mView.findViewById(R.id.bt_like2),
						(TextView) mView.findViewById(R.id.bt_family_name2));
				break;
			case 2:
				excueItemView(index < size ? mList.get(index) : null,
						(ImageView) mView.findViewById(R.id.iv_user3),
						(TextView) mView.findViewById(R.id.bt_like3),
						(TextView) mView.findViewById(R.id.bt_family_name3));
				break;
			case 3:
				excueItemView(index < size ? mList.get(index) : null,
						(ImageView) mView.findViewById(R.id.iv_user4),
						(TextView) mView.findViewById(R.id.bt_like4),
						(TextView) mView.findViewById(R.id.bt_family_name4));
				break;
			case 4:
				excueItemView(index < size ? mList.get(index) : null,
						(ImageView) mView.findViewById(R.id.iv_user5),
						(TextView) mView.findViewById(R.id.bt_like5),
						(TextView) mView.findViewById(R.id.bt_family_name5));
				break;
			case 5:
				excueItemView(index < size ? mList.get(index) : null,
						(ImageView) mView.findViewById(R.id.iv_user6),
						(TextView) mView.findViewById(R.id.bt_like6),
						(TextView) mView.findViewById(R.id.bt_family_name6));
				break;
			default:
				break;
			}

		}

	}

	private void excueItemOnclick(Family mFamily) {
		Bundle bundle = new Bundle();
		bundle.putString("ID", String.valueOf(mFamily.ID));
		bundle.putString("UID", String.valueOf(mFamily.UID));
		bundle.putString("Name", mFamily.Name);
		bundle.putString("URL", mFamily.URL);
		bundle.putString("Slogan", mFamily.Slogan);
		bundle.putString("CreateTime", mFamily.CreateTime);
		bundle.putString("FatherName", mFamily.FatherName);
		bundle.putString("MotherName", mFamily.MotherName);
		bundle.putString("BabyName", mFamily.BabyName);
		bundle.putString("BabySex", mFamily.BabySex);
		bundle.putString("Description", mFamily.Description);
		bundle.putString("BabyAge", String.valueOf(mFamily.BabyAge));
		bundle.putString("TicketCount", String.valueOf(mFamily.TicketCount));
		bundle.putString("AID", String.valueOf(ID));
		IntentInterface.IntentAll(this, bundle,
				ActivitiesFamilyDetailActivity.class, -1);
	}

	private void excueItemView(final Family mFamily,
			final ImageView headerView, TextView like, TextView familyName) {
		if (mFamily == null) {
			headerView.setVisibility(View.INVISIBLE);
			like.setVisibility(View.INVISIBLE);
			familyName.setVisibility(View.INVISIBLE);
			return;
		}

		if (!TextUtils.isEmpty(mFamily.URL)) {
			imageLoader.displayImage(DateInfo.IP + mFamily.URL, headerView,
					options, new ImageLoadingListener() {

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
							headerView.setImageBitmap(ImageUtil
									.toRoundBitmap(loadedImage));
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {
						}
					});
		}
		like.setText(String.format("%d票", mFamily.TicketCount));
		familyName.setText(mFamily.Name);
		headerView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				excueItemOnclick(mFamily);
			}
		});

	}

	@Override
	public void onClick(View v) {
		int vId = v.getId();
		if (vId == R.id.bt_activity_registration) {
			showDialog(-1);
		} else if (vId == R.id.bt_activity_like) {
			Bundle bundle = getIntent().getBundleExtra("data");
			bundle.putString("ID", ID);
			bundle.putString("Title", bundle.getString("Name"));
			IntentInterface.IntentAll(this, bundle,
					ActivitiesPraiseListActivity.class, -1);
		} else if (vId == R.id.bt_activity_recommend) {
			share();
		} else if (vId == R.id.iv_hotfmaily_next) {
			index++;
			if (index >= viewList.size()) {
				index = viewList.size() - 1;
				return;

			}
			mViewPager.setCurrentItem(index);
		} else if (vId == R.id.iv_hotfmaily_pre) {
			index--;
			if (index < 0) {
				index = 0;
				return;

			}
			mViewPager.setCurrentItem(index);
		}
	}

	AlertDialog.Builder mBuilder = null;

	@Override
	protected Dialog onCreateDialog(int id) {
		if (mBuilder == null)
			mBuilder = new Builder(mContext);
		mBuilder.setTitle("用户协议");
		TextView tvView = new TextView(mContext);
		mBuilder.setMessage(getResources().getString(R.string.protocol));
		mBuilder.setPositiveButton("同意", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Bundle bundle = getIntent().getBundleExtra("data");
				bundle.putString("ID", ID);
				bundle.putString("Title", bundle.getString("Name"));
				IntentInterface.IntentAll(ActivitiesDetailActivity.this,
						bundle, ActivitiesEnrollActivity.class, -1);
			}
		});
		mBuilder.setNegativeButton("取消", null);
		return mBuilder.create();

	}

	int index = 0;

	public void share() {
		Platform platform = ShareSDK.getPlatform(mContext, SinaWeibo.NAME);
		OnekeyShare oks = new OnekeyShare(this);
		oks.setNotification(R.drawable.ic_launcher, "芝麻开门");
		oks.setAddress(getString(R.string.app_name));
		oks.setTitle(getString(R.string.app_name));// 标题
		oks.setTitleUrl("www.zmkaimen.com");
		oks.setText(bundle.getString("Content"));// 分享内容
		oks.setUrl("www.zmkaimen.com");// 链接
		oks.setComment("芝麻开门");// qq说说主题
		oks.setSite("www.zmkaimen.com");
		oks.setSiteUrl("www.zmkaimen.com");
		oks.setVenueName("法信-法律图书馆");
		oks.setVenueDescription("法信-法律图书馆");
		oks.setSilent(false);
		if (platform.getName() == QQ.NAME) {
			oks.setText(bundle.getString("Content"));// 分享内容
		}
		if (platform != null) {
			oks.setPlatform("sdd");
		}
		OnClickListener listener = new OnClickListener() {
			public void onClick(View v) {

			}
		};
		// oks.setCustomerLogo(null, null, listener);
		// 启动分享页面
		oks.show(getApplicationContext());
	}

	@Override
	public void onShareScuss() {
		getSupportLoaderManager().restartLoader(LOADER_ADD, null,
				httpCallback);
	}

	@Override
	public void onFail() {
		
	}

	@Override
	public void onCancel() {
		
	}

}
