package com.android.zmark.adapter;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.db.JSONUtil;
import com.android.zmark.bean.util.ISFirstUntil;
import com.android.zmark.bean.util.ViewHour;
import com.android.zmark.entities.FoodEntity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.android.zmark.R;

public class MyLoveAdapter extends BaseAdapter {

	Context context;
	List<FoodEntity> list;
	DisplayImageOptions options;
	protected ImageLoader imageLoader;
	int curPosition;

	public MyLoveAdapter(Context context, List<FoodEntity> list) {

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

	public void deleteindex(int index) {
		list.remove(index);
		notifyDataSetChanged();
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
					R.layout.shop_list_item_detel, null);
			viewhour.tvTitle = (TextView) convertView.findViewById(R.id.title);
			viewhour.tvTime = (TextView) convertView
					.findViewById(R.id.discenten);
			viewhour.tvInfo = (TextView) convertView
					.findViewById(R.id.introduct);
			viewhour.tvFrom = (TextView) convertView.findViewById(R.id.address);
			viewhour.tvImage = (ImageView) convertView.findViewById(R.id.call);
			viewhour.tvImage1 = (ImageView) convertView
					.findViewById(R.id.small_img);
			viewhour.delete_img = (ImageView) convertView
					.findViewById(R.id.delete_img);
			viewhour.tvDistance = (TextView) convertView
					.findViewById(R.id.distance);
			viewhour.tvDiscount = (TextView) convertView
					.findViewById(R.id.discount);

			convertView.setTag(viewhour);
		}

		viewhour.tvTitle.setText("" + list.get(position).Title);
		double df = Double.valueOf(list.get(position).Distance);
		BigDecimal b;
		double t = 1000;
		if (df >= t) {
			df = df / t;
			b = new BigDecimal(df);
			df = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
			viewhour.tvDistance.setText("" + df + "千米");
		} else {
			viewhour.tvDistance.setText(String.format("%.1f米", df));
		}
		// viewhour.tvTime.setText(""+list.get(position).Distance);
		final int index = position;
		imageLoader.displayImage(DateInfo.IP + list.get(position).Img,
				viewhour.tvImage1, options);
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
		if (list.get(position).Discount > 0) {
			viewhour.tvDiscount.setText(String.format("%.1f折",
					list.get(position).Discount*10));
			viewhour.tvDiscount.setVisibility(View.VISIBLE);
		} else
			viewhour.tvDiscount.setVisibility(View.GONE);
		viewhour.delete_img.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageView iv = (ImageView) v;
				Integer curPosition = (Integer) iv.getTag();

				DeleteMyLovetask task = new DeleteMyLovetask();
				task.execute(SettingUtility.getDefaultUserId(),
						"" + list.get(curPosition).Id);
				deleteindex(curPosition);
				// ((CollectionFragment) context).updateMyLove(curPosition);
			}
		});

		viewhour.delete_img.setTag(position);
		viewhour.tvImage.setTag(position);
		viewhour.tvInfo.setText("" + list.get(position).Intro);
		viewhour.tvFrom.setText("" + list.get(position).Address);
		int distance = 0;
		if (!TextUtils.isEmpty(list.get(position).Distance))
			distance = Integer.valueOf(list.get(position).Distance);
		return convertView;
	}

	class DeleteMyLovetask extends AsyncTask<String, Void, List<?>> {

		@Override
		protected List<?> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return DateInfo.getDataFromSer(context, DateInfo.DeleteCollections,
					arg0[0], arg0[1], null, null);
		}

		@Override
		protected void onPostExecute(List<?> result) {
			// TODO Auto-generated method stub
			if (result != null) {
				String res = result.toString();
				if (res.substring(1, res.length() - 1) != null
						&& !"".equals(res.substring(1, res.length() - 1))) {
					List<String> ll = (List<String>) result;
					HashMap<String, Object> map;
					try {
						map = (HashMap<String, Object>) JSONUtil.getInstance()
								.toMap(ll.get(0));
						Toast.makeText(context, "" + map.get("text"),
								Toast.LENGTH_SHORT).show();

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
			super.onPostExecute(result);

		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub

			super.onPreExecute();
		}
	}

}
