package com.android.zmark.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.zmark.bean.util.ViewHour;
import com.android.zmark.R;

public class MessageAdapter extends BaseAdapter {

	Context context;
	List<HashMap<String, Object>> list;
	public MessageAdapter(Context context,List<HashMap<String, Object>> list) {
		
		this.context=context;
		this.list=list;
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
		ViewHour viewhour=null;
		Log.i("====", ""+position);
		HashMap<String, Object> hm=list.get(position);
		if(convertView!=null && convertView.getTag()!=null){
			viewhour=(ViewHour) convertView.getTag();
		}else {
			viewhour= new ViewHour();
			convertView=LayoutInflater.from(context).inflate(R.layout.message_list_item, null);
			viewhour.tvTitle=(TextView) convertView.findViewById(R.id.message_item_title);
			viewhour.tvTime=(TextView) convertView.findViewById(R.id.message_item_time);
			viewhour.tvInfo=(TextView) convertView.findViewById(R.id.message_item_info);
			convertView.setTag(viewhour);
		}
		
		viewhour.tvTitle.setText(hm.get("title").toString());
		viewhour.tvTime.setText(hm.get("time").toString());
		viewhour.tvInfo.setText(hm.get("info").toString());
		return convertView;
	}

}
