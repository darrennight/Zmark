package com.android.zmark.ui.fragment;

import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;
import com.android.zmark.adapter.MyLoveAdapter;
import com.android.zmark.bean.CommonLog;
import com.android.zmark.bean.LogFactory;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.db.DateInfo;
import com.android.zmark.bean.util.ISFirstUntil;
import com.android.zmark.entities.FoodEntity;
import com.android.zmark.ui.LoginActivity;
import com.android.zmark.ui.MainActivity;
import com.android.zmark.ui.MerchantInfoActivity;
import com.android.zmark.ui.UserCenterActivity;
/**收藏
 * 
 * 我的
 * 杨雯方
 * @author yangwenfang
 *
 */
public class CollectionFragment extends AbstractFragment implements OnClickListener{

	private static final CommonLog log = LogFactory.createLog();
	
	private Context context;
	private ListView listview;
	private ProgressDialog progressDialog;
	List<FoodEntity> listfoodall;
	List<FoodEntity> listfood;
	MyLoveAdapter adapter;
	int page=1;
	private View moreView; //加载更多页面
	private TextView tvLoadMore;
	ImageView back;
	View view;
	ZmarkApplication app;
	
	public CollectionFragment(Context context) {
		this.context=context;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		log.e("YuleFragment onCreate");
		if(app!=null){
			
		}else{
			app= ZmarkApplication.getInstance();
		}
		super.onCreate(savedInstanceState);
	}

	public  void updateMyLove(int index){
		Log.i("=======", "刷新");
		listfoodall.remove(index);
		adapter.notifyDataSetChanged();
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = LayoutInflater.from(context).inflate(R.layout.collection_fragment_layout, null);
		listview = (ListView)view.findViewById(R.id.mylove_list);
		back=(ImageView) view.findViewById(R.id.back);
		moreView = LayoutInflater.from(context).inflate(R.layout.footer_loadmoer_layout, null);
		tvLoadMore = (TextView) moreView.findViewById(R.id.tv_loadmore_text);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				app.sm.toggle();
			}
		});
		
		moreView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				page++;
				loadMoreData();
			}
		});
		
		
		
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent= new Intent(context, MerchantInfoActivity.class);
				Bundle bundle= new Bundle();
				bundle.putSerializable("info", listfoodall.get(arg2));
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
		});
		addDate();
		view.findViewById(R.id.bt_search).setOnClickListener(this);
		view.findViewById(R.id.bt_mycenter).setOnClickListener(this);
		view.findViewById(R.id.bt_more).setOnClickListener(this);
		view.findViewById(R.id.bt_activity).setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		log.e("YuleFragment onActivityCreated");
		
		
	}
	
	
	
	@Override
	public void onDestroy() {
		log.e("YuleFragment onDestroy");
		super.onDestroy();
	}
	
	
	public void addDate(){
		page=1;
		if(ZmarkApplication.getInstance().userIsOline()){
			String cord=null;
			if(app.cord==null){
					cord="0,0";
				}else{
					cord=app.cord;
				}
			GetMyLovetask task= new GetMyLovetask();
			task.execute(SettingUtility.getDefaultUserId(),""+page,cord);
		}else{
			Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show();
		}
		
	}
	
	
	private void loadMoreData() { // 加载更多数据
		// 当所有数据加载完成后，currentPage设为0
		if(progressDialog!=null){
			progressDialog.show();
		}
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				
				if (page == 1 && listfoodall!=null) {
					listfoodall.clear();
				}
				
				listfood = (List<FoodEntity>) getNewsListData(page);
				mHandler.sendEmptyMessage(4);
			}
		}).start();
		progressDialog.dismiss();
		
		
		
		
	}
	
	
	public List<?> getNewsListData(int index) {
	String cord=null;
	if(app.cord==null){
			cord="0,0";
		}else{
			cord=app.cord;
		}
	   return DateInfo.getDataFromSer(context, DateInfo.GetMyCollections,
			   SettingUtility.getDefaultUserId(),""+index, cord, null);
		 
	}
	
	
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
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
								addApater();
							} else {
								adapter.notifyDataSetChanged();
							}

							
						} else {
							//addApapter();
						}

					} else {
						//addApapter();
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			default:
				break;
			}
		}

	};
	
	class GetMyLovetask extends AsyncTask<String, Void, List<?>> {

		@Override
		protected List<?> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return DateInfo.getDataFromSer(context, DateInfo.GetMyCollections,
					arg0[0], arg0[1], arg0[2], null);
		}

		@Override
		protected void onPostExecute(List<?> result) {
			// TODO Auto-generated method stub
			if (result!=null) {
				String res=result.toString();
				if(res.substring(1, res.length()-1)!=null && !"".equals(res.substring(1, res.length()-1))){
					listfoodall=(List<FoodEntity>) result;
					listfood=listfoodall;
					
					if (listfoodall.size() >=10) {
						listview.addFooterView(moreView);
					}
					
					addApater();
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
	
	public void addApater(){
		if(listfoodall!=null){
			adapter= new MyLoveAdapter(context, listfoodall);
			listview.setAdapter(adapter);
		}else{
			Toast.makeText(context, "你的收藏暂无数据", Toast.LENGTH_SHORT).show();
		}
	}
	
	
	
	
	public void deleteImage(int position){
		
		/*DeleteMyLovetask task= new DeleteMyLovetask();
		task.execute("");*/
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_activity:
			if (getActivity() == null)
				return;
			((MainActivity) getActivity()).intentActivity();
			break;
		case R.id.bt_search:
			if (getActivity() == null)
				return;
			((MainActivity) getActivity()).intentSearch();
			break;
		case R.id.bt_mycenter:
			Intent intent;
			if (!ZmarkApplication.getInstance().userIsOline()) {
				intent = new Intent(context, LoginActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(context, UserCenterActivity.class);
				context.startActivity(intent);
			}
			intent=null;
			break;
		case R.id.bt_more:
			if (getActivity() == null)
				return;
			((MainActivity) getActivity()).intentSetting();
			break;

		default:
			break;
		}
		
	}
}
