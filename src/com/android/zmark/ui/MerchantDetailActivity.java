package com.android.zmark.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.entities.FoodEntity;
import com.android.zmark.entities.ImageList;
import com.android.zmark.entities.MyMessage;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.android.zmark.R;

public class MerchantDetailActivity extends Activity implements OnClickListener {

	/**
	 * 商家详情页面
	 */
	Button messagedetail_back;
	FoodEntity map;
	MyMessage list;
	protected ImageLoader imageLoader;
	DisplayImageOptions options;
	TextView messagedetail_info_title, messagedetail_from, 
			messagedetail_time, messagedetail_info;
	Context context;
	ViewPager main_viewpager;
	int pageindex = 0;
	private ViewGroup group;
	private ImageView[] imageViews;// 存放小圆点
	private ImageView imageView;
	ArrayList<View> arraylist_img = new ArrayList<View>();// 存放滑动图片

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_info_detail);
		map = (FoodEntity) getIntent().getSerializableExtra("info");
		context = this;
		this.init();
	}

	// 从网络上下载图片
	public void getImage(List<ImageList> list) {
		pageindex = 0;
		ImageView iv;

		if (list != null) {
			arraylist_img.clear();
			for (int i = 0; i < list.size(); i++) {
				LinearLayout imageLinearLayout = new LinearLayout(this);

				LinearLayout.LayoutParams imageLinerLayoutParames = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.FILL_PARENT,
						LinearLayout.LayoutParams.FILL_PARENT);
				iv = new ImageView(this);
				iv.setScaleType(ScaleType.FIT_XY);
				imageLoader.displayImage(DateInfo.IP + list.get(i).URL, iv,
						options);

				imageLinearLayout.addView(iv, imageLinerLayoutParames);
				// iv.setOnClickListener(new ImageOnClickListener());
				arraylist_img.add(imageLinearLayout);

			}

			// 小圆点
			group = (ViewGroup) findViewById(R.id.ll_dot);
			group.removeAllViews();
			imageViews = new ImageView[arraylist_img.size()];

			for (int i = 0; i < arraylist_img.size(); i++) {
				imageView = new ImageView(context);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
						10, 10);
				lp.setMargins(5, 8, 0, 0);
				imageView.setLayoutParams(lp);
				imageView.setPadding(25, 8, 25, 25);
				imageViews[i] = imageView;
				if (i == 0) {
					// 默认选中第一张图片
					imageViews[i]
							.setBackgroundResource(R.drawable.huadongdian_press);
				} else {
					imageViews[i]
							.setBackgroundResource(R.drawable.huadongdian_normal);
				}
				group.addView(imageViews[i]);

			}
			SlideImageAdapter adapter = new SlideImageAdapter(arraylist_img);
			main_viewpager.setAdapter(adapter);

			main_viewpager.setOnPageChangeListener(new ImagePageChangeListener(
					pageindex));

		}

	}

	public class ImagePageChangeListener implements OnPageChangeListener {
		ArrayList<HashMap<String, Object>> listHm = new ArrayList<HashMap<String, Object>>();

		public ImagePageChangeListener(int page) {
			pageindex = page;
		}

		public ImagePageChangeListener(int pageIndex2,
				ArrayList<HashMap<String, Object>> listHm1) {
			listHm = listHm1;
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageSelected(int index) {
			pageindex = index;
			dotChange(index);
		}
	}

	// 划动图片的小圆点
	private void dotChange(int index) {
		for (int i = 0; i < arraylist_img.size(); i++) {
			if (i == index) {
				imageViews[i]
						.setBackgroundResource(R.drawable.huadongdian_press);
			} else {
				imageViews[i]
						.setBackgroundResource(R.drawable.huadongdian_normal);
			}
		}
	}

	// pageadapter
	public class SlideImageAdapter extends PagerAdapter {
		private ArrayList<View> imagePageViews = null;

		public SlideImageAdapter(ArrayList<View> al) {
			imagePageViews = al;
		}

		@Override
		public int getCount() {
			return imagePageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getItemPosition(Object object) {
			// TODO Auto-generated method stub
			return super.getItemPosition(object);
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			// TODO Auto-generated method stub
			((ViewPager) arg0).removeView(imagePageViews.get(arg1));
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(imagePageViews.get(arg1));

			return imagePageViews.get(arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
			// TODO Auto-generated method stub

		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void finishUpdate(View arg0) {
			// TODO Auto-generated method stub

		}
	}

	public void init() {
		main_viewpager = (ViewPager) findViewById(R.id.main_viewpager);
		// messagedetail_img=(LinearLayout)
		// findViewById(R.id.messagedetail_img);
		messagedetail_back = (Button) findViewById(R.id.messagedetail_back);
		messagedetail_info_title = (TextView) findViewById(R.id.messagedetail_info_title);
		messagedetail_from = (TextView) findViewById(R.id.messagedetail_from);
		messagedetail_time = (TextView) findViewById(R.id.messagedetail_time);
		messagedetail_info = (TextView) findViewById(R.id.messagedetail_info);
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.defult1).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565).build();
		messagedetail_back.setOnClickListener(this);

		if (map != null) {
			messagedetail_info_title.setText(map.Title);
			messagedetail_from.setText("地址：" + map.Address);

			messagedetail_time.setText("");
			messagedetail_info.setText(map.Intro);
			if (map.ImageList == null && map.Img != null) {
				map.ImageList = new ArrayList<ImageList>();
				ImageList im = new ImageList();
				im.URL = map.Img;
				map.ImageList.add(im);

			}

			if (map.ImageList != null) {
				getImage(map.ImageList);
			}

			/*
			 * if(!DateInfo.IP.equals(DateInfo.IP +map.Img)){ final ImageView
			 * imageViews = new ImageView(context);
			 * //imageLoader.displayImage(DateInfo.IP +map.Img,
			 * messagedetail_img, options); imageLoader.displayImage(DateInfo.IP
			 * +map.Img, imageViews, new ImageLoadingListener() {
			 * 
			 * @Override public void onLoadingStarted(String imageUri, View
			 * view) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public void onLoadingFailed(String imageUri, View view,
			 * FailReason failReason) { // TODO Auto-generated method stub
			 * 
			 * }
			 * 
			 * @Override public void onLoadingComplete(String imageUri, View
			 * view, Bitmap loadedImage) { // TODO Auto-generated method stub
			 * int h = loadedImage.getHeight(); int w = loadedImage.getWidth();
			 * double width = getWindowManager()
			 * .getDefaultDisplay().getWidth(); double bili=width/w;
			 * LinearLayout.LayoutParams vParams; vParams = new LayoutParams(
			 * (int) (width), (int) (h*bili));
			 * imageViews.setLayoutParams(vParams);
			 * imageViews.setAdjustViewBounds(true);
			 * imageViews.setScaleType(ScaleType.FIT_XY);
			 * //messagedetail_img.addView(imageViews); }
			 * 
			 * @Override public void onLoadingCancelled(String imageUri, View
			 * view) { // TODO Auto-generated method stub
			 * 
			 * } }); }
			 */

			// ImageUtil.setDrawable(messagedetail_img,DateInfo.IP+list.Img,
			// MessageInfoActivity.this);
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.messagedetail_back:
			this.finish();
			break;

		default:
			break;
		}
	}

	class Messagetask extends AsyncTask<String, Void, List<?>> {

		@Override
		protected List<?> doInBackground(String... arg0) {
			return DateInfo.getDataFromSer(MerchantDetailActivity.this,
					DateInfo.GetMessage, null, "1", null, null);
		}

		@Override
		protected void onPostExecute(List<?> result) {
			// TODO Auto-generated method stub

			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
		}
	}
}
