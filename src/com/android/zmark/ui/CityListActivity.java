package com.android.zmark.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.zmark.ZmarkApplication;
import com.android.zmark.adapter.CityListAdapter;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.R;

public class CityListActivity extends Activity {

	Context context;
	private ProgressDialog progressDialog;
	List<com.android.zmark.entities.CityList> city_list;
	List<com.android.zmark.entities.CityList> list_all;
	CityListAdapter adapter;
	ListView citylistview;
	ImageView back;
	ZmarkApplication app;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.city_list_activity_layout);
		this.init();
		context=this;
		app= ZmarkApplication.getInstance();
		list_all= new ArrayList<com.android.zmark.entities.CityList>();
		com.android.zmark.entities.CityList citys= new com.android.zmark.entities.CityList();
		citys.Coord=app.cord;
		citys.Name="我的位置";
		com.android.zmark.entities.CityList citys1= new com.android.zmark.entities.CityList();
		citys1.Coord=app.cord;
		citys1.Name=app.mycity;
		com.android.zmark.entities.CityList citys2= new com.android.zmark.entities.CityList();
		citys2.Coord=app.cord;
		citys2.Name="热门城市";
		list_all.add(citys);
		list_all.add(citys1);
		list_all.add(citys2);
		GetCityListTask task= new GetCityListTask();
		task.execute("");
		
		citylistview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(arg2!=0 && arg2!=2 && arg2!=1){
					app.city=list_all.get(arg2).Name.toString();
//					if("曲靖市".equals(app.city)){
//						
//					}else{
//						Toast.makeText(context, "该城市暂未开通", Toast.LENGTH_SHORT).show();
//					}
					CityListActivity.this.finish();
				}
				
				if(arg2==1){
					try {
						app.city=list_all.get(arg2).Name.toString();
					} catch (Exception e) {
						CityListActivity.this.finish();
					}
					
					
				}
				
			}
		});
	}
	
	
	public void init(){
		
		citylistview=(ListView) findViewById(R.id.city_list);
		back=(ImageView) findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				CityListActivity.this.finish();
			}
		});
		
	}
	
	class GetCityListTask extends AsyncTask<String, Void, List<?>> {

		@Override
		protected List<?> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return DateInfo.getDataFromSer(context,DateInfo.GetCityInfo, null, null, null, null);
		}

		@Override
		protected void onPostExecute(List<?> result) {
			// TODO Auto-generated method stub
			
			if(result!=null){
				String str=result.toString();
				String s=str.substring(1, str.length()-1);
				if(s!=null&&!"".equals(s)){
					city_list=(List<com.android.zmark.entities.CityList>) result;
					
					for(int i=0;i<city_list.size();i++){
						list_all.add(city_list.get(i));
					}
					adapter= new CityListAdapter(list_all, context);
					citylistview.setAdapter(adapter);
					
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
}
