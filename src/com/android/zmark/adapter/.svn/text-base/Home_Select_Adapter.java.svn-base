package com.android.zmark.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.zmark.bean.util.ViewHour;
import com.android.zmark.R;

public class Home_Select_Adapter extends BaseAdapter{

	List<String> list;
	Context context;
	private String selectedText = "册数0";
	public Home_Select_Adapter(List<String> list,Context context) {
		this.list=list;
		this.context=context;
	}
	
	public void selectchang(int index){
		selectedText=list.get(index);
		notifyDataSetChanged();
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
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHour viewhour=null;
		if(convertView!=null && convertView.getTag()!=null){
			viewhour=(ViewHour) convertView.getTag();
		}else{
			viewhour= new ViewHour();
			convertView=LayoutInflater.from(context).inflate(R.layout.home_select_list_item, null);
			viewhour.tvTitle=(TextView) convertView.findViewById(R.id.home_select_list_item_text);
		    convertView.setTag(viewhour);
		}
		viewhour.tvTitle.setText(list.get(position));
		if (selectedText != null && selectedText.equals(list.get(position))) {
			viewhour.tvTitle.setBackgroundResource(R.drawable.select_2_bg);//设置选中的背景图片
		} else {
			viewhour.tvTitle.setBackgroundResource(R.drawable.select1_bg);;//设置未选中状态背景图片
		}
		return convertView;
	}

}
