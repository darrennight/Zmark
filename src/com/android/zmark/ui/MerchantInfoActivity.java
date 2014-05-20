package com.android.zmark.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.android.zmark.ApiServerPath;
import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.db.JSONUtil;
import com.android.zmark.bean.socialshare.OnekeyShare;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.HttpLoader;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.util.BitmapUtils;
import com.android.zmark.bean.util.ISFirstUntil;
import com.android.zmark.bean.util.Utility;
import com.android.zmark.bean.util.ViewHour;
import com.android.zmark.entities.FoodEntity;
import com.android.zmark.entities.Productlist;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

/**
 * 商家详情列表
 * 
 * @author yangwenfang
 * 
 */
public class MerchantInfoActivity<T> extends AbstractActivity implements
		OnClickListener {

	TextView title, introuuct, address, yinye_content, tvDiscount;
	TextView yinye_songhuo, yinye_price, yinye_time;
	ImageView imgs, back, love, share;
	RelativeLayout dizhi_rel;
	private ProgressDialog progressDialog;
	Context context;
	FoodEntity foodentity;
	HashMap<String, Object> hashmap;

	List<String> list_love;
	Button dianhua, duanxin;

	int pid;
	DisplayImageOptions options;
	protected ImageLoader imageLoader;
	ZmarkApplication app;
	Button but_detail_all;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchant_detail_activity);
		context = this;
		foodentity = (FoodEntity) getIntent().getSerializableExtra("info");
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.defult1)
				.showImageOnFail(R.drawable.defult1).cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		ShareSDK.initSDK(this);
		// AbstractWeibo.initSDK(this);
		this.init();
		String pinfo = ISFirstUntil.isGetProductInfo(context);
		if (foodentity != null) {
			if (pinfo != null) {
				pinfo = pinfo + foodentity.Id + ",";
			} else {
				pinfo = foodentity.Id + ",";
			}

			ISFirstUntil.saveProductInfo(context, pinfo);
		}

		if (app == null) {
			app = ZmarkApplication.getInstance();
		}
	}

	/*
	 * public void initshare() { if(foodentity!=null){ AbstractWeibo[] weiboList
	 * = AbstractWeibo.getWeiboList(this); Intent i = new Intent(this,
	 * ShareAllGird.class); // 分享时Notification的图标 i.putExtra("notif_icon",
	 * R.drawable.ic_launcher); // 分享时Notification的标题 i.putExtra("notif_title",
	 * getString(R.string.app_name)); // 分享内容的标题（仅部分平台需要此字段） i.putExtra("title",
	 * ""+foodentity.Title); // qzone i.putExtra("titleUrl",
	 * "www.zimakaimen.com"); i.putExtra("site", "www.zimakaimen.com");
	 * i.putExtra("siteUrl", "www.zimakaimen.com"); // 分享内容的文本 i.putExtra(
	 * "text", ""+foodentity.Intro);
	 * 
	 * Boolean silent = false; i.putExtra("silent", silent); startActivity(i); }
	 * 
	 * }
	 */

	public void share() {
		Platform platform = ShareSDK.getPlatform(context, SinaWeibo.NAME);

		OnekeyShare oks = new OnekeyShare();

		oks.setNotification(R.drawable.ic_launcher, "芝麻开门");
		oks.setAddress(getString(R.string.app_name));
		oks.setTitle(getString(R.string.app_name));// 标题
		oks.setTitleUrl("www.zmkaimen.com");
		// www.kekestudio.com
		oks.setText("" + foodentity.Title);// 分享内容
		// oks.setImagePath(getString(R.string.setImagePath));//没测试过应该是图片本地地址吧
		// oks.setImageUrl(getString(R.string.setImageUrl));//图片网络地址
		oks.setUrl("www.zmkaimen.com");// 链接
		// oks.setAppPath(getString(R.string.setAppPath));//分享apk（微信好像不能）
		oks.setComment("芝麻开门");// qq说说主题
		oks.setSite("www.zmkaimen.com");
		oks.setSiteUrl("www.zmkaimen.com");
		oks.setVenueName("法信-法律图书馆");
		oks.setVenueDescription("法信-法律图书馆");
		oks.setSilent(false);
		if (platform.getName() == QQ.NAME) {
			oks.setText("" + foodentity.Intro);// 分享内容
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

	public void AddProductNum() {
		new AlertDialog.Builder(context).setTitle("选择数量").create().show();
	}

	List<HashMap<String, Boolean>> list_check;
	List<HashMap<String, Boolean>> list_check1;
	int indexs = 0;
	List<Productlist> list = new ArrayList<Productlist>();

	public void init() {
		title = (TextView) findViewById(R.id.title);
		imgs = (ImageView) findViewById(R.id.imgs);
		introuuct = (TextView) findViewById(R.id.introuuct);
		tvDiscount = (TextView) findViewById(R.id.tv_discount);
		dizhi_rel = (RelativeLayout) findViewById(R.id.dizhi_rel);
		dizhi_rel.setOnClickListener(this);
		address = (TextView) findViewById(R.id.address);

		but_detail_all = (Button) findViewById(R.id.but_detail_all);
		but_detail_all.setOnClickListener(this);

		yinye_songhuo = (TextView) findViewById(R.id.yinye_songhuo);
		yinye_price = (TextView) findViewById(R.id.yinye_price);
		yinye_time = (TextView) findViewById(R.id.yinye_time);
		yinye_content = (TextView) findViewById(R.id.yinye_content);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(this);
		love = (ImageView) findViewById(R.id.love);
		share = (ImageView) findViewById(R.id.share);

		dianhua = (Button) findViewById(R.id.dianhua);
		duanxin = (Button) findViewById(R.id.duanxin);
		dianhua.setOnClickListener(this);
		duanxin.setOnClickListener(this);

		love.setOnClickListener(this);
		share.setOnClickListener(this);

		if (foodentity != null) {
			hashmap = new HashMap<String, Object>();
			hashmap.put("title", foodentity.Title);
			hashmap.put("address", foodentity.Title);
			String coode = foodentity.Coord;
			String st[] = coode.split(",");
			hashmap.put("lon2", st[0]);
			hashmap.put("lat2", st[1]);
			pid = foodentity.Id;
			title.setText(foodentity.Title);
			if (foodentity.Discount > 0) {
				tvDiscount.setText(String.format("%.1f折",
						foodentity.Discount * 10));
				tvDiscount.setVisibility(View.VISIBLE);
			} else
				tvDiscount.setVisibility(View.GONE);
			// imageLoader.displayImage(DateInfo.IP +foodentity.Img,imgs,
			// options);
			ImageSize imagesize = new ImageSize(100, 100);
			imageLoader.loadImage(DateInfo.IP + foodentity.Img, imagesize,
					options, new ImageLoadingListener() {

						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onLoadingComplete(String imageUri,
								View view, Bitmap loadedImage) {

							imgs.setImageBitmap(BitmapUtils
									.getRoundCornerBitmap(loadedImage, 52));
						}

						@Override
						public void onLoadingCancelled(String imageUri,
								View view) {

						}
					});

			introuuct.setText(foodentity.Intro);
			address.setText("地址：" + foodentity.Address);

			if ("美食".equals(foodentity.ClassName)) {
				yinye_price.setText("人均消费："
						+ (TextUtils.isEmpty(foodentity.Price) ? "-"
								: foodentity.Price) + "元");
				yinye_time.setText("营业时间：" + foodentity.Time);
				yinye_content.setText("行业：" + foodentity.ClassName + " 菜系："
						+ foodentity.ContentName);
				yinye_songhuo.setText("是否送餐：" + foodentity.Status);
				yinye_price.setVisibility(View.VISIBLE);
				yinye_time.setVisibility(View.VISIBLE);
				yinye_songhuo.setVisibility(View.VISIBLE);
			} else if ("酒店".equals(foodentity.ClassName)) {
				yinye_content.setText("行业：" + foodentity.ClassName + " 星级：");
				yinye_price.setText("人均消费："
						+ (TextUtils.isEmpty(foodentity.Price) ? "-"
								: foodentity.Price) + "元");
				yinye_price.setVisibility(View.VISIBLE);
				yinye_time.setVisibility(View.GONE);
				yinye_songhuo.setVisibility(View.GONE);
			} else {
				yinye_content.setText("行业：" + foodentity.ClassName + " 类别："
						+ foodentity.ContentName);
				yinye_price.setText("人均消费："
						+ (TextUtils.isEmpty(foodentity.Price) ? "-"
								: foodentity.Price) + "元");
				yinye_time.setText("营业时间：" + foodentity.Time);
				yinye_price.setVisibility(View.VISIBLE);
				yinye_time.setVisibility(View.VISIBLE);
				yinye_songhuo.setVisibility(View.GONE);
			}

			// 添加店主推荐信息 ly_recommend
			if (foodentity.productlist != null
					&& foodentity.productlist.size() != 0)
				list.addAll(foodentity.productlist);
			if (!TextUtils.isEmpty(foodentity.Productlist) && list.size() == 0) {
				try {
					JSONArray mListJson = new JSONArray(foodentity.Productlist);
					for (int i = 0; i < mListJson.length(); i++) {
						JSONObject item = mListJson.optJSONObject(i);
						Productlist product = new Productlist();
						product.Discount = item.optString("Discount");
						product.Id = item.optInt("Id");
						product.Name = item.optString("Name");
						product.Content = item.optString("Content");
						product.DanGrading = item.optString("DanGrading");
						product.ProductStandard = item
								.optString("ProductStandard");
						product.Img = item.optString("Img");
						product.Type = item.optString("Type");
						product.Stock = item.optString("Stock");
						list.add(product);
						product = null;
						item = null;
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			if (list != null && list.size() != 0) {
				list_check = new ArrayList<HashMap<String, Boolean>>();
				for (int i = 0; i < this.list.size(); i++) {
					HashMap<String, Boolean> hashmap = new HashMap<String, Boolean>();
					hashmap.put("chick", false);
					list_check.add(hashmap);
				}

				list_check1 = new ArrayList<HashMap<String, Boolean>>();
				for (int i = 0; i < this.list.size(); i++) {
					HashMap<String, Boolean> hashmap = new HashMap<String, Boolean>();
					hashmap.put("chick", false);
					list_check1.add(hashmap);
				}

				if (app == null) {
					app = ZmarkApplication.getInstance();
				}
				app.list_check = list_check;
				for (int position = 0; position < list.size(); position++) {
					View convertView = LayoutInflater.from(context).inflate(
							R.layout.product_list_item_test, null);
					ViewHour viewhour = new ViewHour();
					viewhour.tvTitle = (TextView) convertView
							.findViewById(R.id.title);
					viewhour.tvTime = (TextView) convertView
							.findViewById(R.id.price);
					viewhour.tvInfo = (TextView) convertView
							.findViewById(R.id.info);
					viewhour.tvDiscount = (TextView) convertView
							.findViewById(R.id.distcount);
					viewhour.RadioButton = (CheckBox) convertView
							.findViewById(R.id.checktab);
					viewhour.tvImage = (ImageView) convertView
							.findViewById(R.id.piuter);
					viewhour.tvRelative = (RelativeLayout) convertView
							.findViewById(R.id.rel_bg);
					viewhour.tvcheck = (CheckBox) convertView
							.findViewById(R.id.product_check);
					convertView.setTag(viewhour);
					viewhour.tvRelativelayout = (RelativeLayout) convertView
							.findViewById(R.id.rel_context);
					if (position < list_check.size()) {
						if (list_check.get(position).get("chick")) {
							viewhour.tvcheck.setChecked(true);
						} else {
							viewhour.tvcheck.setChecked(false);
						}
					}

					viewhour.RadioButton
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									int vid = (Integer) v.getTag();
									if (list_check1.get(vid).get("chick")) {
										list_check1.get(vid)
												.put("chick", false);
									} else {
										Log.e("===点击check", vid + " true");
										list_check1.get(vid).put("chick", true);

									}
								}

							});

					viewhour.tvcheck
							.setOnClickListener(new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									indexs = (Integer) v.getTag();
									if (list_check.get(indexs).get("chick")) {
										list_check.get(indexs).put("chick",
												false);

									} else {
										list_check.get(indexs).put("chick",
												true);
									}
									app.list_check = list_check;
								}
							});

					viewhour.tvTitle.setText("" + list.get(position).Name);
					viewhour.tvDiscount
							.setVisibility(TextUtils.isEmpty(list.get(position).Discount) ? View.GONE
									: View.VISIBLE);

					if (!TextUtils.isEmpty(list.get(position).Discount))
						viewhour.tvDiscount.setText(String
								.format("%.1f折", Double.valueOf(list
										.get(position).Discount) * 10));
					// imageLoader.displayImage(DateInfo.IP
					// +list.get(position).Img,viewhour.tvImage,
					// options);
					viewhour.tvcheck.setTag(position);
					viewhour.RadioButton.setTag(position);
					viewhour.tvInfo.setText("" + list.get(position).Content);
					viewhour.tvTime.setText("" + list.get(position).DanGrading
							+ " " + list.get(position).Type);
					((LinearLayout) findViewById(R.id.ly_recommend))
							.addView(convertView);
				}
			} else {
				findViewById(R.id.tv_tuijian).setVisibility(View.INVISIBLE);
			}

		}

		// product_list.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
		// long arg3) {
		// Intent intent= new Intent(context, ShopInfoDetailActivity.class);
		// Bundle bundle= new Bundle();
		// bundle.putSerializable("info", list.get(arg2-1));
		// intent.putExtras(bundle);
		// startActivity(intent);
		// /*if(arg2>0){
		// adpater.changes(arg2-1);
		// }*/
		//
		//
		// }
		// });

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent = null;
		switch (v.getId()) {
		case R.id.but_detail_all:
			if (foodentity != null) {
				intent = new Intent(context, MerchantDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putSerializable("info", foodentity);
				intent.putExtras(bundle);
				startActivity(intent);

			} else {
				Toast.makeText(context, "数据为空，不能查看详情", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.dizhi_rel:
			intent = new Intent(context, MapActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("mothinfo", hashmap);
			intent.putExtras(bundle);
			startActivity(intent);
			break;
		case R.id.back:
			this.finish();

			break;

		case R.id.love:
			if (ZmarkApplication.getInstance().userIsOline()) {
				GetSavetask task = new GetSavetask();
				task.execute(SettingUtility.getDefaultUserId(), "" + pid);
			} else {
				Toast.makeText(context, "请先登录，在进行收藏", Toast.LENGTH_SHORT)
						.show();
			}
			break;

		case R.id.duanxin:
			List<HashMap<String, Boolean>> ls = app.list_check;
			HashMap<String, Boolean> hm;
			StringBuffer sb = new StringBuffer();
			String tel = null;
			if (ls != null && list != null) {
				for (int i = 0; i < ls.size(); i++) {
					hm = ls.get(i);
					if (hm.get("chick")) {
						sb.append(list.get(i).Name + "   ");
					}
				}

				if (foodentity.Mobilephone != null) {
					tel = foodentity.Mobilephone;
				} else {
					tel = foodentity.Telephone;
				}
				if (tel != null) {
					intent = new Intent(context, MessageOrderActivity.class);
					intent.putExtra("tel", tel);
					if (sb.toString().equals("")) {
						intent = new Intent(context, MessageOrderActivity.class);
						intent.putExtra("tel", tel);
						intent.putExtra("info", "芝麻开门提醒您,收到新的预定:");
						startActivity(intent);
						// Toast.makeText(context, "门店产品暂无数据，不能短信订购",
						// Toast.LENGTH_SHORT).show();
					} else {
						intent.putExtra("info",
								"芝麻开门提醒您,收到新的预定:" + sb.toString());
						startActivity(intent);
					}

				} else {
					Toast.makeText(context, "门店未留电话，不能短信订购", Toast.LENGTH_SHORT)
							.show();
				}
			} else {
				if (foodentity.Mobilephone != null) {
					tel = foodentity.Mobilephone;
				} else {
					tel = foodentity.Telephone;
				}
				intent = new Intent(context, MessageOrderActivity.class);
				intent.putExtra("tel", tel);
				intent.putExtra("info", "芝麻开门提醒您,收到新的预定:");
				startActivity(intent);
				// Toast.makeText(context, "门店产品暂无数据，不能短信订购",
				// Toast.LENGTH_SHORT).show();
				// 调用一次接口
				getSupportLoaderManager().restartLoader(LOADER_INITDATA, null,
						httpCallback);
			}

			break;
		case R.id.dianhua:
			if (foodentity != null) {
				String tels = null;
				if (foodentity.Mobilephone != null) {
					tels = foodentity.Mobilephone;
				} else {
					tels = foodentity.Telephone;
				}
				Uri uri = Uri.parse("tel:" + tels);
				intent = new Intent(Intent.ACTION_DIAL, uri);
				context.startActivity(intent);
				// 调用一次接口
				getSupportLoaderManager().restartLoader(LOADER_INITDATA, null,
						httpCallback);
			}
			break;
		case R.id.share:
			share();
			break;
		default:
			break;
		}
	}

	protected LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>> httpCallback = new LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>>() {
		// praisBbundle.putBoolean("isPrais", type != 0 && selectType == 1);
		@Override
		public Loader<AsyncTaskLoaderResult<T>> onCreateLoader(int id,
				Bundle args) {
			switch (id) {
			case LOADER_INITDATA:
				Map<String, String> map = new HashMap<String, String>();
				map.clear();
				// 1.uid=用户ID
				// 2.sid=门店
				// 3.mobilephone=手机号码
				// 4.time=订餐时间
				// http://app10014.yunbosoft.com:9090/Interface/GetSlide.ashx?w=720&h=1280
				map.put("uid", SettingUtility.getDefaultUserId());
				map.put("mobilephone", SettingUtility.getDefaultPhone());
				map.put("sid", String.valueOf(pid));
				map.put("time",
						Utility.getFormatTime(System.currentTimeMillis()));
				return new HttpLoader(mContext, ApiServerPath.DM_ORDER_ADD, map);

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
				DismissDialog();
				if (responseBean == null) {
					return;
				}
				break;

			}
			getLoaderManager().destroyLoader(loader.getId());
		}

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> loader) {

		}
	};

	class GetSavetask extends AsyncTask<String, Void, List<?>> {

		@Override
		protected List<?> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return DateInfo.getDataFromSer(context, DateInfo.UserCollecting,
					arg0[0], arg0[1], null, null);
		}

		@Override
		protected void onPostExecute(List<?> result) {
			// TODO Auto-generated method stub
			if (result != null) {
				String res = result.toString();
				if (res.substring(1, res.length() - 1) != null
						&& !"".equals(res.substring(1, res.length() - 1))) {
					list_love = (List<String>) result;
					try {
						HashMap<String, Object> map = (HashMap<String, Object>) JSONUtil
								.getInstance().toMap(list_love.get(0));
						Toast.makeText(context, "" + map.get("text"),
								Toast.LENGTH_SHORT).show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
			progressDialog.dismiss();
			super.onPostExecute(result);

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(context);
			}
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			View view = LayoutInflater.from(context).inflate(
					R.layout.progressdialog, null);
			progressDialog.setContentView(view);
			super.onPreExecute();
		}
	}
}
