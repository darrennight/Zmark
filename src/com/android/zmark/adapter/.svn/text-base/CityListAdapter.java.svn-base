package com.android.zmark.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.zmark.bean.util.ViewHour;
import com.android.zmark.entities.CityList;
import com.android.zmark.R;

public class CityListAdapter extends BaseAdapter{
	Context context;
	List<CityList> list;
	public CityListAdapter(List<CityList> list,Context context) {
		this.context=context;
		this.list=list;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ViewHour viewhour=null;
		if(convertView!=null && convertView.getTag()!=null){
			viewhour=(ViewHour) convertView.getTag();
		}else {
			viewhour= new ViewHour();
			convertView=LayoutInflater.from(context).inflate(R.layout.city_list_item, null);
			viewhour.tvTitle=(TextView) convertView.findViewById(R.id.citylist_item_name);
			viewhour.tvLinearlayout=(LinearLayout) convertView.findViewById(R.id.city_lin);
			convertView.setTag(viewhour);
		}
		if(arg0==0||arg0==2){
			viewhour.tvTitle.setTextSize(18);
		}
		
		if(arg0%2==0){
			viewhour.tvLinearlayout.setBackgroundColor(0xffffffff);
		}else{
			viewhour.tvLinearlayout.setBackgroundColor(0xffdfdfdf);
		}
		viewhour.tvTitle.setText(list.get(arg0).Name);
		return convertView;
	}

}
