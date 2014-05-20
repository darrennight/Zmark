package com.android.zmark.adapter;

import java.math.BigDecimal;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.db.ImageUtil;
import com.android.zmark.bean.util.BitmapUtils;
import com.android.zmark.bean.util.ViewHour;
import com.android.zmark.entities.FoodEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.android.zmark.R;

public class FoodAdapter extends BaseAdapter {

	Context context;
	List<FoodEntity> list;
	DisplayImageOptions options;
	protected ImageLoader imageLoader;

	public FoodAdapter(Context context, List<FoodEntity> list) {

		this.context = context;
		this.list = list;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.icon)
				.showImageOnFail(R.drawable.icon).cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHour viewhour = null;
		if (convertView != null && convertView.getTag() != null) {
			viewhour = (ViewHour) convertView.getTag();
		} else {
			viewhour = new ViewHour();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.shop_list_item, null);
			viewhour.tvTitle = (TextView) convertView.findViewById(R.id.title);
			viewhour.tvTime = (TextView) convertView
					.findViewById(R.id.discenten);
			viewhour.tvInfo = (TextView) convertView
					.findViewById(R.id.introduct);
			viewhour.tvFrom = (TextView) convertView.findViewById(R.id.address);
			viewhour.tvImage = (ImageView) convertView.findViewById(R.id.call);
			viewhour.tvPrice = (TextView) convertView.findViewById(R.id.price);
			viewhour.tvDiscount = (TextView) convertView
					.findViewById(R.id.discount);
			viewhour.tvImage1 = (ImageView) convertView
					.findViewById(R.id.small_img);
			convertView.setTag(viewhour);
		}
		ImageSize imagesize = new ImageSize(80, 80);
		final ImageView imageView = viewhour.tvImage1;
		imageLoader.loadImage(DateInfo.IP + list.get(position).Img, imagesize,
				options, new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						imageView.setImageBitmap(ImageUtil
								.toRoundBitmap(loadedImage));
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
					}
				});

		try {
			if (list.get(position).Discount > 0) {
				viewhour.tvDiscount.setText(String.format("%.1f折",
						list.get(position).Discount * 10));
				viewhour.tvDiscount.setVisibility(View.VISIBLE);
			} else
				viewhour.tvDiscount.setVisibility(View.GONE);
			viewhour.tvTitle
					.setText(!TextUtils.isEmpty(list.get(position).Title)
							&& list.get(position).Title.length() >= 8 ? list
							.get(position).Title.subSequence(0, 8) : list
							.get(position).Title);
			float df = Float.valueOf(list.get(position).Distance);
			double t = 1000;
			if (df >= 1000) {
				df = (float) (df / t);
				viewhour.tvTime.setText(String.format("%.1f千米", df));
			} else {
				viewhour.tvTime.setText(String.format("%d米", (int) df));
			}
			final int index = position;
			viewhour.tvImage.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ImageView iv = (ImageView) v;
					Integer curPosition = (Integer) iv.getTag();
					Uri uri = Uri.parse("tel:" + list.get(index).Telephone);
					Intent intent = new Intent(Intent.ACTION_DIAL, uri);
					context.startActivity(intent);
				}
			});

			viewhour.tvImage.setTag(position);
			viewhour.tvPrice.setText("人均消费："
					+ (TextUtils.isEmpty(list.get(position).Price) ? "-" : list
							.get(position).Price) + "元");
			if ("酒店".equals(list.get(position).ClassName)) {
				viewhour.tvInfo.setText("行业：" + list.get(position).ClassName
						+ "  星级：" + list.get(position).ContentName);
			} else if ("美食".equals(list.get(position).ClassName)) {
				viewhour.tvInfo.setText("行业：" + list.get(position).ClassName
						+ "  菜系：" + list.get(position).ContentName);
			} else {
				viewhour.tvInfo.setText("行业：" + list.get(position).ClassName
						+ "  类别：" + list.get(position).ContentName);
			}
			// if (list.get(position).productlist == null
			// || list.get(position).productlist.size() == 0)
			// viewhour.tvDiscount.setText("");
			// else {
			// viewhour.tvDiscount.setText(TextUtils.isEmpty(list
			// .get(position).productlist.get(0).Discount) ? "" : list
			// .get(position).productlist.get(0).Discount);
			// }
			viewhour.tvFrom.setText("地址：" + list.get(position).Address);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return convertView;
	}
}
