package com.android.zmark.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.ZmarkApplication;
import com.android.zmark.adapter.FoodAdapter;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.util.NetWorkHelper;
import com.android.zmark.entities.FoodEntity;
import com.android.zmark.R;
public class NearbyEnterainmentActivity extends Activity implements OnClickListener{

	LinearLayout lin_1, lin_2, lin_3, lin_4;
	TextView sp1, sp2, sp3;
	List<FoodEntity> listfoodall;
	List<FoodEntity> listfood;
	private ProgressDialog progressDialog;
	FoodAdapter adapter;
	ImageView daohan,back;
	boolean flag=false;
	
	ImageView xiala_2,xiala_1,xiala_0;
	ListView shop_list;
	int index,index1,index2;
	
	Context context;
	List<HashMap<String, Object>> hashmap_list;
	
	private View moreView; //加载更多页面
	private TextView tvLoadMore;
	
	String[] name = new String[] { "距离","500米", "1000米", "5000米", "10000米"};
	String[] name1 = new String[] { "类型","KTV", "清吧", "迪厅","桑拿","养生"};
	String[] name2 = new String[] { "价格","100元以下", "100-200元", "200-500元", 
			"500-1000元","1000元以上" };
	RelativeLayout rel_0,rel_1,rel_2;
	FrameLayout select_all_rel;

	ZmarkApplication app;
	String cord;
    int page=1;
    String city;
    String distence="all",price="",foodname="";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.nearby_enterainment_activity_layout);
		context=this;
		this.init();
		if(app==null){
			app= ZmarkApplication.getInstance();
			
		}
		if(app.cord==null){
			cord="0,0";
		}else{
			cord=app.cord;
		}
		
		
		if(app.city!=null){
			city=app.city;
		}else{
			city="";
		}
		GetProductTypeTask task= new GetProductTypeTask();
		task.execute(cord,""+page,foodname,price,distence,city);
		
	}

	public void init() {
		
		try {
			daohan=(ImageView) findViewById(R.id.daohan);
			daohan.setOnClickListener(this);
			hashmap_list= new ArrayList<HashMap<String,Object>>();
			rel_0=(RelativeLayout) findViewById(R.id.rel_0);
			rel_1=(RelativeLayout) findViewById(R.id.rel_1);
			rel_2=(RelativeLayout) findViewById(R.id.rel_2);
			
			rel_0.setOnClickListener(this);
			rel_1.setOnClickListener(this);
			rel_2.setOnClickListener(this);
			back=(ImageView) findViewById(R.id.back);
			back.setOnClickListener(this);
			
			sp1=(TextView) findViewById(R.id.sp1);
			sp2=(TextView) findViewById(R.id.sp2);
			sp3=(TextView) findViewById(R.id.sp3);
			
			sp1.setText(name[0]);
			sp2.setText(name1[0]);
			sp3.setText(name2[0]);
			
			xiala_2=(ImageView) findViewById(R.id.xiala_2);
			xiala_1=(ImageView) findViewById(R.id.xiala_1);
			xiala_0=(ImageView) findViewById(R.id.xiala_0);
			
			shop_list=(ListView) findViewById(R.id.shop_list);
			
			shop_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					// TODO Auto-generated method stub
					Intent intent= new Intent(context, MerchantInfoActivity.class);
					Bundle bundle= new Bundle();
					bundle.putSerializable("info", listfoodall.get(arg2));
					intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			
			//select_all_rel=(FrameLayout) findViewById(R.id.select_all_rel);
			

			
			moreView = getLayoutInflater().inflate(R.layout.footer_loadmoer_layout, null);
			tvLoadMore = (TextView) moreView.findViewById(R.id.tv_loadmore_text);
			moreView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					page++;
					loadMoreData();
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			Log.i("**", "初始化错误");
		}

		
	}

	
	
	/**
	 * 异步加载首页数据
	 * 
	 * @author lenovo
	 * 
	 */

	class GetProductTypeTask extends AsyncTask<String, Void, List<?>> {

		@Override
		protected List<?> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return DateInfo.getDataFromShop(context, DateInfo.GetEntertainmentShop, arg0[0],  arg0[1],  arg0[2],  arg0[3],  arg0[4],  arg0[5]);
		}
		
		@Override
		protected void onPostExecute(List<?> result) {
			// TODO Auto-generated method stub

			if (result != null) {
				String str = result.toString().substring(1,
						result.toString().length() - 1);
				if (!"".equals(str)) {
					listfoodall=(List<FoodEntity>) result;
					listfood=listfoodall;
					addApapter();
				}
			}
			progressDialog.dismiss();
			super.onPostExecute(result);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if (progressDialog == null) {
				progressDialog = new ProgressDialog(context);
			}
			if (progressDialog.isShowing()) {
				progressDialog.dismiss();
			}
			progressDialog.setCanceledOnTouchOutside(false);
			progressDialog.show();
			View view = LayoutInflater.from(context).inflate(
					R.layout.progressdialog, null);
			progressDialog.setContentView(view);
			super.onPreExecute();
		}
		
	}
	
	public void addApapter() {
		try {
			if (listfoodall != null) {
				if (listfood != null && listfood.size() == 10) {
					int count = shop_list.getFooterViewsCount();
					if (count < 1) {
						shop_list.addFooterView(moreView);
					}

				} else {
					if(moreView!=null && shop_list.getFooterViewsCount()<1)
					shop_list.removeFooterView(moreView);
				}
				
				

				hashmap_list.clear();
				for(int i=0;i<listfoodall.size();i++){
					HashMap<String, Object> hash = new HashMap<String, Object>();
					String coordinate = listfoodall.get(i).Coord;
					if (coordinate != null
							&& coordinate.indexOf(",") > 0) {
						String strs[] = coordinate.split(",");
						hash.put("lon2", strs[0]);
						hash.put("lat2", strs[1]);
					}

					hash.put("address", listfoodall.get(i).Address);
					hash.put("Mobilephone", listfoodall.get(i).Telephone);
					hash.put("title", listfoodall.get(i).Title);
					hashmap_list.add(hash);
				}
				
				
				adapter = new FoodAdapter(context, listfoodall);
				if(listfoodall.size()==0){
					shop_list.setVisibility(View.GONE);
					Toast.makeText(context, "该条件下暂无门店数据", Toast.LENGTH_SHORT).show();
				}else{
					shop_list.setVisibility(View.VISIBLE);
				}
				shop_list.setAdapter(adapter);
			}else{
				Toast.makeText(context, "该条件下暂无门店数据", Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	
	
	private void loadMoreData() { // 加载更多数据
		// 当所有数据加载完成后，currentPage设为0
		
		if(flag==true){
			return ;
		}
		flag=true;
		try {
			
			if(progressDialog!=null){
				progressDialog.show();
			}else{
				
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(context);
				}
				if (progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
				progressDialog.setCanceledOnTouchOutside(false);
				progressDialog.show();
				View view = LayoutInflater.from(context).inflate(
						R.layout.progressdialog, null);
				progressDialog.setContentView(view);
			}
			
			
			new Thread(new Runnable() {

				@Override
				public void run() {
					Log.e("=====", "开始线程");
					if (page == 1 && listfoodall!=null) {
						listfoodall.clear();
					}
					
					listfood = (List<FoodEntity>) getNewsListData(page);
					Log.e("=====", "技术线程");
					mHandler.sendEmptyMessage(4);
					Log.e("=====", "技术线程4");
				}
			}).start();
			
		} catch (Exception e) {
			
		}
	}
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			Log.e("****", "handler");
			switch (msg.what) {
			
			case 4:
				
				try {
					if (listfood != null && listfoodall!=null) {
						String str = listfood.toString().substring(1,
								listfood.toString().length() - 1);
						
                          
						if (!"".equals(str.trim())) {
							for (int i = 0; i < listfood.size(); i++) {
								listfoodall.add(listfood.get(i));
								
							}

							if (page == 1) {
								addApapter();
							} else {
								adapter.notifyDataSetChanged();
							}

							
						} else {
							Toast.makeText(context, "该条件下暂无数据", Toast.LENGTH_SHORT).show();
							//addApapter();
						}

					} else {
						Toast.makeText(context, "该条件下暂无数据", Toast.LENGTH_SHORT).show();
						//addApapter();
					}

					
				} catch (Exception e) {
				}
				progressDialog.dismiss();
				flag=false;
				break;
			default:
				break;
			}
		}

	};

	public List<?> getNewsListData(int index) {
	  try {
		  return DateInfo.getDataFromShop(context, DateInfo.GetEntertainmentShop, cord,  ""+index,  foodname,  price, distence, city);
	  } catch (Exception e) {
		return null;
	  }
		 
	}
	
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		try {
			Intent intent;
			switch (v.getId()) {
			case R.id.back:
				this.finish();
				break;
			case R.id.daohan:
				if(NetWorkHelper.isInternetConnected(NearbyEnterainmentActivity.this)){
					if(hashmap_list!=null){
						if(hashmap_list.size()>0){
							intent = new Intent(NearbyEnterainmentActivity.this, BaiduMapActivity.class);
							app.list = hashmap_list;
							startActivity(intent);
						}else{
							Toast.makeText(NearbyEnterainmentActivity.this, "无数据，不能进行此操作", Toast.LENGTH_SHORT).show();
						}
					}else{
						Toast.makeText(NearbyEnterainmentActivity.this, "无数据，不能进行此操作", Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(NearbyEnterainmentActivity.this, "无数据，不能进行此操作", Toast.LENGTH_SHORT).show();
				}
				break;
				
			case R.id.rel_0:
				index=0;
				for(int i=0;i<name.length;i++){
					
					if((distence+"米").equals(name[i])){
						index=i;
					}
				}
				new AlertDialog.Builder(context).setTitle("距离选择")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(name, index, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						index=which;
						sp1.setText(name[which]);
						page=1;
						if(which==0){
							distence="all";
							
						}else{
							distence=name[which];
							distence=distence.substring(0, distence.indexOf("米"));
						}
						page=1;
						if(moreView!=null && shop_list.getFooterViewsCount()>0){
							shop_list.removeFooterView(moreView);
						}
						loadMoreData();
						if(dialog!=null){
							dialog.dismiss();
						}
					}
				}).setNegativeButton("取消", null).show();
				
				break;
				
				
			case R.id.rel_1:
				index1=0;
				for(int i=0;i<name1.length;i++){
					
					if((foodname).equals(name1[i])){
						index1=i;
					}
				}
				new AlertDialog.Builder(context).setTitle("类型选择")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(name1, index1, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						index1=which;
						sp2.setText(name1[which]);
						page=1;
						if(which==0){
							foodname="all";
							
						}else{
							foodname=name1[which];
							
						}
						page=1;
						if(moreView!=null && shop_list.getFooterViewsCount()>0){
							shop_list.removeFooterView(moreView);
						}
						loadMoreData();
						if(dialog!=null){
							dialog.dismiss();
						}
					}
				}).setNegativeButton("取消", null).show();
				break;
				
			case R.id.rel_2:
				index2=0;
				for(int i=0;i<name2.length;i++){
					
					if((price).equals(name2[i])){
						index2=i;
					}
				}
				new AlertDialog.Builder(context).setTitle("价格选择")
				.setIcon(android.R.drawable.ic_dialog_info)
				.setSingleChoiceItems(name2, index2, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						index2=which;
						sp3.setText(name2[which]);
						page=1;
						if(which==0){
							price="all";
							
						}else{
							price=name2[which];
							
						}
						page=1;
						if(moreView!=null && shop_list.getFooterViewsCount()>0){
							shop_list.removeFooterView(moreView);
						}
						loadMoreData();
						if(dialog!=null){
							dialog.dismiss();
						}
					}
				}).setNegativeButton("取消", null).show();
				break;
			
			default:
				break;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	


}
