package com.android.zmark.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zmark.bean.util.ViewHour;
import com.android.zmark.entities.Productlist;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.android.zmark.R;

public class ProductDetailAdapter extends BaseAdapter {

	Context context;
	List<Productlist> list;
	DisplayImageOptions options;
	protected ImageLoader imageLoader;
	
	List<HashMap<String, Boolean>> list_check;
	List<HashMap<String, Boolean>> list_check1;
	int indexs;
	public ProductDetailAdapter(Context context,List<Productlist> list) {
		
		this.context=context;
		this.list=list;
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.defult1)
				.showImageOnFail(R.drawable.defult1).cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
		
		list_check= new ArrayList<HashMap<String,Boolean>>();
		for(int i=0;i<this.list.size();i++){
			HashMap<String,Boolean> hashmap= new HashMap<String, Boolean>();
			hashmap.put("chick", false);
			list_check.add(hashmap);
		}
		
		list_check1= new ArrayList<HashMap<String,Boolean>>();
		for(int i=0;i<this.list.size();i++){
			HashMap<String,Boolean> hashmap= new HashMap<String, Boolean>();
			hashmap.put("chick", false);
			list_check1.add(hashmap);
		}
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
		ViewHour viewhour=null;
		
		 indexs=position;
		if(convertView!=null && convertView.getTag()!=null){
			viewhour=(ViewHour) convertView.getTag();
		}else {
			viewhour= new ViewHour();
			convertView=LayoutInflater.from(context).inflate(R.layout.product_list_item, null);
			viewhour.tvTitle=(TextView) convertView.findViewById(R.id.title);
			viewhour.tvTime=(TextView) convertView.findViewById(R.id.price);
			viewhour.tvInfo=(TextView) convertView.findViewById(R.id.info);
			
			viewhour.RadioButton=(CheckBox) convertView.findViewById(R.id.checktab);
			viewhour.tvImage=(ImageView) convertView.findViewById(R.id.piuter);
			final RelativeLayout tvRelative=(RelativeLayout) convertView.findViewById(R.id.rel_bg);
			viewhour.tvcheck=(CheckBox) convertView.findViewById(R.id.product_check);
			convertView.setTag(viewhour);
			 
			
			
			viewhour.tvRelativelayout=(RelativeLayout) convertView.findViewById(R.id.rel_context);
			//final RelativeLayout rel_context=(RelativeLayout) convertView.findViewById(R.id.rel_context);
			Log.e("=====pos", position+"    "+list_check1.get(position));
			
			
			
			if(list_check1.get(position).get("chick")){
				viewhour.tvRelativelayout.setVisibility(View.VISIBLE);
			}else{
				viewhour.tvRelativelayout.setVisibility(View.GONE);
			}
			
			
				
			viewhour.tvcheck.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Log.e("===点击check", " box");
					if(list_check.get(indexs).get("chick")){
						list_check.get(indexs).put("chick", false);
						tvRelative.setBackgroundResource(R.drawable.list_bg);
					}else{
						list_check.get(indexs).put("chick", true);
						tvRelative.setBackgroundColor(0xffD93D00);
						
					}
				}
			});
			
			

			  class lvButtonListener implements OnClickListener { 
			        private int positions ; 
			        RelativeLayout rels;
			        lvButtonListener( int pos,RelativeLayout rle) { 
			            positions = pos; 
			            rels=rle;
			        } 
			        
			        @Override 
			        public void onClick( View v) { 
			            int vid= (Integer) v. getTag() ; 
			            Log.i("=====", "vid:"+vid);
			            if(list_check1.get(vid).get("chick")){
							
							Log.e("===点击check",vid+ " flase");
							list_check1.get(vid).put("chick", false);
							rels.setVisibility(View.GONE);
							//notifyDataSetChanged();
						}else{
							Log.e("===点击check",vid+ " true");
							list_check1.get(vid).put("chick", true);
							rels.setVisibility(View.VISIBLE);
							//notifyDataSetChanged();
						}
			        } 
			    } 
			  
			 // ((ProductDetail_e)context).updateProduct();
			  viewhour.RadioButton.setOnClickListener(new lvButtonListener( indexs, viewhour.tvRelativelayout));
		}
		
		
		
		viewhour.tvTitle.setText(""+list.get(position).Name);
		
		//imageLoader.displayImage(DateInfo.IP +list.get(position).Img,viewhour.tvImage, options);
		
		
		viewhour.tvcheck.setTag(position);
		viewhour.RadioButton.setTag(position);
		viewhour.tvInfo.setText(""+list.get(position).Content);
		viewhour.tvTime.setText(""+list.get(position).DanGrading);
		return convertView;
	}

	
	
	
	
}
