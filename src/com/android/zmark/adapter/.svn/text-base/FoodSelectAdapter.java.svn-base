package com.android.zmark.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.zmark.bean.util.ViewHour;
import com.android.zmark.R;

public class FoodSelectAdapter extends BaseAdapter {

	Context context;
	List<HashMap<String, Object>> list;
	public FoodSelectAdapter(Context context,List<HashMap<String, Object>> list) {
		
		this.context=context;
		this.list=list;
	}
	
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		try {
			ViewHour viewhour=null;
			HashMap<String, Object> hm=list.get(position);
			if(convertView!=null && convertView.getTag()!=null){
				viewhour=(ViewHour) convertView.getTag();
			}else {
				viewhour= new ViewHour();
				convertView=LayoutInflater.from(context).inflate(R.layout.shopselect_list_item, null);
				viewhour.tvTitle=(TextView) convertView.findViewById(R.id.title);
				convertView.setTag(viewhour);
			}
			
			viewhour.tvTitle.setText(hm.get("title").toString());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return convertView;
	}

}
