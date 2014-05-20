//package com.android.zmark.adapter;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.text.TextUtils;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.BaseAdapter;
//import android.widget.CheckBox;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.android.zmark.ZmarkApplication;
//import com.android.zmark.bean.util.ViewHour;
//import com.android.zmark.entities.Productlist;
//import com.android.zmark.ui.MerchantInfoActivity;
//import com.nostra13.universalimageloader.core.DisplayImageOptions;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.assist.ImageScaleType;
//import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
//import com.android.zmark.R;
//
//public class ProductDetailAdapterTest extends BaseAdapter {
//
//	Context context;
//	List<Productlist> list;
//	DisplayImageOptions options;
//	protected ImageLoader imageLoader;
//
//	List<HashMap<String, Boolean>> list_check;
//	List<HashMap<String, Boolean>> list_check1;
//	int indexs;
//	ZmarkApplication app;
//
//	public ProductDetailAdapterTest(Context context, List<Productlist> list) {
//
//		this.context = context;
//		this.list = list;
//		imageLoader = ImageLoader.getInstance();
//		options = new DisplayImageOptions.Builder()
//				.showImageForEmptyUri(R.drawable.icon)
//				.showImageOnFail(R.drawable.icon).cacheInMemory(true)
//				.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
//				.bitmapConfig(Bitmap.Config.RGB_565)
//				.displayer(new FadeInBitmapDisplayer(300)).build();
//
//		list_check = new ArrayList<HashMap<String, Boolean>>();
//		for (int i = 0; i < this.list.size(); i++) {
//			HashMap<String, Boolean> hashmap = new HashMap<String, Boolean>();
//			hashmap.put("chick", false);
//			list_check.add(hashmap);
//		}
//
//		list_check1 = new ArrayList<HashMap<String, Boolean>>();
//		for (int i = 0; i < this.list.size(); i++) {
//			HashMap<String, Boolean> hashmap = new HashMap<String, Boolean>();
//			hashmap.put("chick", false);
//			list_check1.add(hashmap);
//		}
//
//		if (app == null) {
//			app = ZmarkApplication.getInstance();
//		}
//		app.list_check = list_check;
//	}
//
//	public void changeItem(int index) {
//		try {
//			int in = index - 1;
//			if (list_check1 != null && in < list_check1.size()) {
//				boolean flag = list_check1.get(in).get("chick");
//				HashMap<String, Boolean> hashmap;
//				if (flag == true) {
//					hashmap = new HashMap<String, Boolean>();
//					hashmap.put("chick", false);
//					list_check1.set(in, hashmap);
//				} else {
//					hashmap = new HashMap<String, Boolean>();
//					hashmap.put("chick", true);
//					list_check1.set(in, hashmap);
//				}
//			}
//			((MerchantInfoActivity) context).updateProduct();
//		} catch (Exception e) {
//		}
//	}
//
//	@Override
//	public int getCount() {
//		return list.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		return list.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		ViewHour viewhour = null;
//
//		indexs = position;
//		if (convertView != null && convertView.getTag() != null) {
//			viewhour = (ViewHour) convertView.getTag();
//		} else {
//			viewhour = new ViewHour();
//			convertView = LayoutInflater.from(context).inflate(
//					R.layout.product_list_item_test, null);
//			viewhour.tvTitle = (TextView) convertView.findViewById(R.id.title);
//			viewhour.tvTime = (TextView) convertView.findViewById(R.id.price);
//			viewhour.tvInfo = (TextView) convertView.findViewById(R.id.info);
//			viewhour.tvDiscount = (TextView) convertView
//					.findViewById(R.id.distcount);
//			viewhour.RadioButton = (CheckBox) convertView
//					.findViewById(R.id.checktab);
//			viewhour.tvImage = (ImageView) convertView
//					.findViewById(R.id.piuter);
//			viewhour.tvRelative = (RelativeLayout) convertView
//					.findViewById(R.id.rel_bg);
//			viewhour.tvcheck = (CheckBox) convertView
//					.findViewById(R.id.product_check);
//			convertView.setTag(viewhour);
//
//			viewhour.tvRelativelayout = (RelativeLayout) convertView
//					.findViewById(R.id.rel_context);
//
//		}
//
//		if (list_check.get(position).get("chick")) {
//			// viewhour.tvRelative .setBackgroundColor(0xffD93D00);
//			viewhour.tvTitle.setTextColor(0xffD93D00);
//			viewhour.tvTime.setTextColor(0xffD93D00);
//			viewhour.tvcheck.setChecked(true);
//
//		} else {
//			viewhour.tvTitle.setTextColor(0xffffffff);
//			viewhour.tvTime.setTextColor(0xffffffff);
//			// viewhour.tvRelative .setBackgroundResource(R.drawable.list_bg);
//			viewhour.tvcheck.setChecked(false);
//		}
//
//		/*
//		 * if(list_check1.get(position).get("chick")){
//		 * viewhour.tvRelativelayout.setVisibility(View.VISIBLE);
//		 * viewhour.RadioButton.setChecked(true); }else{
//		 * viewhour.tvRelativelayout.setVisibility(View.GONE);
//		 * viewhour.RadioButton.setChecked(false); }
//		 */
//
//		viewhour.RadioButton.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				int vid = (Integer) v.getTag();
//				if (list_check1.get(vid).get("chick")) {
//
//					Log.e("===点击check", vid + " flase");
//					list_check1.get(vid).put("chick", false);
//
//				} else {
//					Log.e("===点击check", vid + " true");
//					list_check1.get(vid).put("chick", true);
//
//				}
//				((MerchantInfoActivity) context).updateProduct();
//			}
//
//		});
//
//		viewhour.tvcheck.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				indexs = (Integer) v.getTag();
//				if (list_check.get(indexs).get("chick")) {
//					list_check.get(indexs).put("chick", false);
//
//				} else {
//					list_check.get(indexs).put("chick", true);
//					// ((MerchantInfoActivity)context).AddProductNum();
//				}
//				app.list_check = list_check;
//				((MerchantInfoActivity) context).updateProduct();
//			}
//		});
//
//		viewhour.tvTitle.setText("" + list.get(position).Name);
//		viewhour.tvDiscount
//				.setText(TextUtils.isEmpty(list.get(position).Discount) ? ""
//						: list.get(position).Discount);
//		// imageLoader.displayImage(DateInfo.IP
//		// +list.get(position).Img,viewhour.tvImage, options);
//
//		viewhour.tvcheck.setTag(position);
//		viewhour.RadioButton.setTag(position);
//		viewhour.tvInfo.setText("" + list.get(position).Content);
//		viewhour.tvTime.setText("" + list.get(position).DanGrading + " "
//				+ list.get(position).Type);
//		return convertView;
//	}
//
//	public void changes(int id) {
//		if (list_check.get(id).get("chick")) {
//			list_check.get(id).put("chick", false);
//
//		} else {
//			list_check.get(id).put("chick", true);
//			// ((MerchantInfoActivity)context).AddProductNum();
//		}
//		app.list_check = list_check;
//		notifyDataSetChanged();
//	}
//
//}
