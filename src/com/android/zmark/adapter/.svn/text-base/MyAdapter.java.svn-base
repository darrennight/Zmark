package com.android.zmark.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.zmark.bean.util.ViewHour;
import com.android.zmark.entities.Productlist;
import com.android.zmark.R;

public class MyAdapter extends BaseExpandableListAdapter{
	
	Context context;
	List<Productlist>list;
	List<Productlist> list_child;
	public MyAdapter(Context context,List<Productlist>list,List<Productlist> list_child) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.list=list;
		this.list_child=list_child;
		Log.e("======list_child", ""+list);
	}

	

	
	
	@Override
	public Object getChild(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return list_child.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
           ViewHour viewhour=null;
           Log.i("======", ""+list);
		if(convertView!=null && convertView.getTag()!=null){
			viewhour=(ViewHour) convertView.getTag();
		}else {
			viewhour= new ViewHour();
			convertView=LayoutInflater.from(context).inflate(R.layout.product_list_item, null);
			//viewhour.tvTitle=(TextView) convertView.findViewById(R.id.title);
			//viewhour.tvTime=(TextView) convertView.findViewById(R.id.counts);
			
			convertView.setTag(viewhour);
		}
		
		
		HashMap<String, Object> hashmap=(HashMap<String, Object>) getChild(groupPosition, childPosition);
		
		if(hashmap!=null){
			viewhour.tvTitle.setText(""+hashmap.get("name"));
			viewhour.tvTime.setText(""+hashmap.get("cases_count"));
		}
		
		
		return convertView;
		
		/*LinearLayout ll = new LinearLayout(
				context);
		ll.setOrientation(0);
	
		TextView textView = getTextView();
		textView.setText(""+getChild(groupPosition, childPosition));
		ll.addView(textView);
		return ll;*/
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		// TODO Auto-generated method stub
		return list_child.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		// TODO Auto-generated method stub
		return list.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		// TODO Auto-generated method stub
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Log.e("===加载", "加载信息");
		ViewHour viewhour=null;
		
		if(convertView!=null && convertView.getTag()!=null){
			viewhour=(ViewHour) convertView.getTag();
		}else {
			viewhour= new ViewHour();
			convertView=LayoutInflater.from(context).inflate(R.layout.product_list_item, null);
			viewhour.tvTitle=(TextView) convertView.findViewById(R.id.title);
			viewhour.tvTime=(TextView) convertView.findViewById(R.id.price);
			
			convertView.setTag(viewhour);
		}
		Productlist pl=(Productlist) getGroup(groupPosition);
		viewhour.tvTitle.setText(""+pl.Name);
		viewhour.tvTime.setText(""+pl.DanGrading);
		
		return convertView;
        
		
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	

}
