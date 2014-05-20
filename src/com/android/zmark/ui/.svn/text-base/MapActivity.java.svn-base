package com.android.zmark.ui;
import java.io.IOException;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.zmark.ZmarkApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKGeocoderAddressComponent;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.android.zmark.R;

public class MapActivity extends Activity implements OnClickListener{
	LinearLayout layout;
	TextView title,content,map_info_info,map_info_num,map_info_title;
	RelativeLayout map_info_infos;
	//ImageView call;
	static MapView mMapView = null;	
	Context context;
	private MapController mMapController = null;
	public MKMapViewListener mMapListener = null;
	FrameLayout mMapViewContainer = null;
	String[] str1={"地址：中烟有限公司\n","地址：中烟有限公司",
			};
	String name;//名称
	String address;//地址
	String str[]={"中烟"};
	double lat[]={30.660739};//分店坐标
	double lon[]={104.085396};
	double mylat=0.0;//我的位置
	double mylon=0.0;
	MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
    public NotifyLister mNotifyer=null;	
	ImageView testUpdateButton = null;	
	EditText indexText = null;
	MyLocationOverlay myLocationOverlay = null;
	int dianIndex =0;//当前选中
	//LinearLayout menban;	
	HashMap<String,Object> hashmap= new HashMap<String, Object>();
	LocationData locData = null;
	ImageView btn_left,mian_title_img;
	Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
           
        };
    };
    ZmarkApplication app;
    String[] strgps;
    String city="曲靖";
    
    int flags=1;
   /* LinearLayout mian_title;
	LinearLayout main_title_lin;
	TextView main_text_home, main_text_news, main_text_shop, main_text_product,
			main_text_set, main_text_user;*/
	
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	super.onCreate(savedInstanceState);
    	Intent intent= getIntent();
        hashmap=(HashMap<String, Object>) intent.getSerializableExtra("mothinfo");
    	lon[0]=Double.parseDouble(hashmap.get("lon2").toString());
    	lat[0]=Double.parseDouble(hashmap.get("lat2").toString());
    	 name=hashmap.get("title").toString();
    	if(name!=null && !"".equals(name)){
    		str[0]=name;
    	}
    	 address=hashmap.get("address").toString();
    	if(address!=null && !"".equals(address)){
    		str1[0]=address;
    	}
    	
    	 
        
        /*GeoPoint point = new GeoPoint((int) (39.915 * 1E6),
				(int) (116.404 * 1E6));*/
		// 用给定的经纬度构造一个GeoPoint，单位是微度 (度 * 1E6)
		//mMapController.setCenter(point);
        app = (ZmarkApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(ZmarkApplication.strKey,new ZmarkApplication.MyGeneralListener());
        }
        setContentView(R.layout.mymap_act);
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapController = mMapView.getController();       
        initMapView();//设置        
        initlocation();//定位		
		mMapView.refresh();
		initlistener();//监听
		
		initOverlay();
		
		initbusline();//路线
		
		context=this;
		
	    layout=(LinearLayout)findViewById(R.id.layout);
	    title=(TextView)findViewById(R.id.title);
	    content=(TextView)findViewById(R.id.content);
	    map_info_info=(TextView) findViewById(R.id.map_info_info);
	    if(hashmap.get("address")!=null){
	    	map_info_info.setText(hashmap.get("address").toString());
	    }
	    
	    map_info_num=(TextView) findViewById(R.id.map_info_num);
	    if(hashmap.get("Mobilephone")!=null){
	    	 map_info_num.setText(hashmap.get("Mobilephone").toString());
	    }
	   
	    
	    map_info_title=(TextView) findViewById(R.id.map_info_title);
	    if(hashmap.get("title")!=null){
	    	map_info_title.setText(hashmap.get("title").toString());
	    }
	    
	    map_info_infos = (RelativeLayout) findViewById(R.id.map_relative);
		map_info_infos.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Uri uri = Uri.parse("tel:"
						+ hashmap.get("Mobilephone").toString());
				Intent intent = new Intent(Intent.ACTION_DIAL, uri);
				startActivity(intent);
			}
		});
    }
    
    
    public void initbusline(){//路线
    	// 初始化搜索模块，注册事件监听
        mSearch = new MKSearch();
        mSearch.init(app.mBMapManager, new MKSearchListener(){

            @Override
            public void onGetPoiDetailSearchResult(int type, int error) {
            }

            @Override
			public void onGetDrivingRouteResult(MKDrivingRouteResult res,
					int error) {
            	 try {
  			    	mMapView.getOverlays().remove(routetOverlay);
  			    	mMapView.getOverlays().remove(routedOverlay);
  				} catch (Exception e) {
  				}
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				routedOverlay = new RouteOverlay(MapActivity.this, mMapView);
			    // 此处仅展示一个方案作为示例
				routedOverlay.setData(res.getPlan(0).getRoute(0));
			   // mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(routedOverlay);
			    mMapView.refresh();
			    // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			    mMapView.getController().zoomToSpan(routedOverlay.getLatSpanE6(), routedOverlay.getLonSpanE6());
			    mMapView.getController().animateTo(res.getStart().pt);
			}
            RouteOverlay routedOverlay;
            TransitOverlay routetOverlay;
            @Override
			public void onGetTransitRouteResult(MKTransitRouteResult res,
					int error) {
            	 try {
 			    	mMapView.getOverlays().remove(routetOverlay);
 			    	mMapView.getOverlays().remove(routedOverlay);
 				} catch (Exception e) {
 				}
				if (error != 0 || res == null) {
					Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				routetOverlay = new TransitOverlay (MapActivity.this, mMapView);
			    // 此处仅展示一个方案作为示例
				routetOverlay.setData(res.getPlan(0));
			   
			    mMapView.getOverlays().add(routetOverlay);
			    mMapView.refresh();
			    // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			    mMapView.getController().zoomToSpan(routetOverlay.getLatSpanE6(), routetOverlay.getLonSpanE6());
			    mMapView.getController().animateTo(res.getStart().pt);
			}
            @Override
			public void onGetWalkingRouteResult(MKWalkingRouteResult res,
					int error) {
				if (error != 0 || res == null) {
					Toast.makeText(MapActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
					return;
				}
				RouteOverlay routeOverlay = new RouteOverlay(MapActivity.this, mMapView);
			    // 此处仅展示一个方案作为示例
				routeOverlay.setData(res.getPlan(0).getRoute(0));
			    mMapView.getOverlays().clear();
			    mMapView.getOverlays().add(routeOverlay);
			    mMapView.refresh();
			    // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
			    mMapView.getController().zoomToSpan(routeOverlay.getLatSpanE6(), routeOverlay.getLonSpanE6());
			    mMapView.getController().animateTo(res.getStart().pt);
			    
			}
			public void onGetAddrResult(MKAddrInfo result, int error) {
				MKGeocoderAddressComponent kk = result.addressComponents;
				city = kk.city;
			}
			public void onGetPoiResult(MKPoiResult res, int arg1, int arg2) {
			}
			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1,
					int arg2) {
				// TODO Auto-generated method stub
				
			}

        });
        
      
    }

    public void initlocation(){
    	mLocClient = new LocationClient( this );
        mLocClient.registerLocationListener( myListener );
        
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
	    myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
    }
    private void initMapView() {
        mMapView.setLongClickable(true);
        mMapView.getController().setZoom(15);
        mMapView.getController().enableClick(true);       
    }
    public void initlistener(){
        btn_left=(ImageView)findViewById(R.id.back);
        btn_left.setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				back();
			}
		});
    	testUpdateButton = (ImageView)findViewById(R.id.button1);
		OnClickListener clickListener = new OnClickListener(){
				public void onClick(View v) {
					testUpdateClick();
				}
	        };
	    testUpdateButton.setOnClickListener(clickListener);
    }
    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }
    
    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }
    
    
    @Override
    protected void onDestroy() {
        if (mLocClient != null)
            mLocClient.stop();
       /* mMapView.destroy();
        ZmarkApplication app = (ZmarkApplication)this.getApplication();
        if (app.mBMapManager != null) {
            app.mBMapManager.destroy();
            app.mBMapManager = null;
        }*/
        super.onDestroy();
    }
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	mMapView.onSaveInstanceState(outState);
    	
    }
    
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
    	super.onRestoreInstanceState(savedInstanceState);
    	mMapView.onRestoreInstanceState(savedInstanceState);
    }
    
    public void testUpdateClick(){
        mLocClient.requestLocation();
        mMapView.refresh();
        mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)), mHandler.obtainMessage(1));
    }
  
   


	
	/**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中 
     */
    public class MyLocationListenner implements BDLocationListener {
    	
    	@Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            mylat=location.getLatitude();
            mylon=location.getLongitude();
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            myLocationOverlay.setData(locData);
            if(mSearch!=null){
				mSearch.reverseGeocode(new GeoPoint((int) (mylat * 1000000),
						(int) (mylon * 1000000)));
			}
            if(flags==1){
            	testUpdateClick();
            	flags=0;
            }
            
        }
        
        public void onReceivePoi(BDLocation poiLocation) {
            if (poiLocation == null){
                return ;
            }
        }
    }
    
    public class NotifyLister extends BDNotifyListener{
        public void onNotify(BDLocation mlocation, float distance) {
        }
    }
    /* 
     * 要处理overlay点击事件时需要继承ItemizedOverlay 
     * 不处理点击事件时可直接生成ItemizedOverlay. 
     */ 
    public class OverlayTest extends ItemizedOverlay<OverlayItem> { 
    	//用MapView构造ItemizedOverlay  
    	private Context mContext = null;
        PopupOverlay pop = null;

        public OverlayTest(Drawable mark,MapView mapView,Context ctx){  
                super(mark,mapView); 
                mContext=ctx;
                pop = new PopupOverlay( MapActivity.mMapView,new PopupClickListener() {
        			
        			@Override
        			public void onClickedPopup(int index) {
        				switch (index) {
						case 0:
							if(mylat!=0.0){
								MKPlanNode start = new MKPlanNode();
						    	start.pt = new GeoPoint((int) (mylat * 1E6), (int) (mylon * 1E6));
						    	MKPlanNode end = new MKPlanNode();
						    	end.pt = new GeoPoint((int)(lat[dianIndex]* 1E6), (int)(lon[dianIndex]* 1E6));// 设置驾车路线搜索策略，时间优先、费用最少或距离最短
						    	mSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);
						    	mSearch.drivingSearch(null, start, null, end);
							}else{
								testUpdateClick();
								Toast.makeText(MapActivity.this, "定位失败，请重新定位", 4000);
							}
							
							break;
						case 1:
							if(mylat!=0.0){
								MKPlanNode start1 = new MKPlanNode();
						    	start1.pt = new GeoPoint((int) (mylat * 1E6), (int) (mylon * 1E6));
						    	MKPlanNode end1 = new MKPlanNode();
						    	end1.pt = new GeoPoint((int)(lat[dianIndex]* 1E6), (int)(lon[dianIndex]* 1E6));// 设置驾车路线搜索策略，时间优先、费用最少或距离最短
						    	mSearch.setTransitPolicy(MKSearch.ECAR_TIME_FIRST);
						    	mSearch.transitSearch(city, start1, end1);
							}else{
								testUpdateClick();
								Toast.makeText(MapActivity.this, "定位失败，请重新定位", 4000);
							}
							break;
						case 2:
							title.setText(str[dianIndex]);
							content.setText(str1[dianIndex]);
							layout.setVisibility(View.VISIBLE);
							break;

						default:
							break;
						}
        			}
        		});
        } 
        
        protected boolean onTap(int index) {  
            //在此处理item点击事件  
        	dianIndex=index;
        	Bitmap[] bmps = new Bitmap[3];
     	        try {
     	            bmps[0] = BitmapFactory.decodeStream(mContext.getAssets().open("marker1.png"));
     	            bmps[1] = BitmapFactory.decodeStream(mContext.getAssets().open("marker2.png"));
     	            bmps[2] = BitmapFactory.decodeStream(mContext.getAssets().open("marker3.png"));
     	        } catch (IOException e) {
     	            e.printStackTrace();
     	        }
             
     	       layout.setVisibility(View.GONE);
     	    pop.showPopup(bmps, getItem(index).getPoint(), 60);
            Toast.makeText(mContext, getItem(index).getTitle(), Toast.LENGTH_SHORT).show();
     		
     		
     		return true; 
        }  
            public boolean onTap(GeoPoint pt, MapView mapView){  
                    //在此处理MapView的点击事件，当返回 true时  
	            	if (pop != null){
	        			pop.hidePop();
	        		}   
	            	layout.setVisibility(View.GONE);
	            	super.onTap(pt,mapView); 
                    return false;  
            }  
    }
   
    OverlayTest itemOverlay;
    public void initOverlay(){

    	HashMap<String, Object> map=new HashMap<String, Object>();
    	HashMap<String, Object> overitemap=new HashMap<String, Object>();
  	    	
    	for(int i=0;i<lat.length;i++){
    		map.put(""+i, new GeoPoint((int) (lat[i] * 1E6), (int) (lon[i] * 1E6)));
    		overitemap.put(""+i, new OverlayItem((GeoPoint)map.get(""+i),str[i],str1[i])); 
    	}    
    	//准备overlay图像数据，根据实情情况修复  
    	Drawable mark= getResources().getDrawable(R.drawable.icon_marka);  
    	//用OverlayItem准备Overlay数据  
    	//使用setMarker()方法设置overlay图片,如果不设置则使用构建ItemizedOverlay时的默认设置  
    	((OverlayItem) overitemap.get(""+0)).setMarker(mark);    	   
    	//创建IteminizedOverlay  
    	itemOverlay = new OverlayTest(mark, mMapView,MapActivity.this);

    	//将IteminizedOverlay添加到MapView中      	  
    	//mMapView.getOverlays().clear();
    	//mMapView.getOverlays().remove(itemOverlay);
    	//现在所有准备工作已准备好，使用以下方法管理overlay.  
    	//添加overlay, 当批量添加Overlay时使用addItem(List<OverlayItem>)效率更高   
    	for(int i=0;i<lat.length;i++){
    		itemOverlay.addItem((OverlayItem)overitemap.get(""+i));
    	}
    	mMapView.getOverlays().add(itemOverlay); 
    	mMapView.refresh();  
    	//删除overlay .  
    	//itemOverlay.removeItem(itemOverlay.getItem(0));  
    	//mMapView.refresh();  
    	//清除overlay  
    	// itemOverlay.removeAll();  
    	// mMapView.refresh();  
    }
   
    
    /*返回键start*/
    @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		// TODO Auto-generated method stub

		if(event.getKeyCode()==KeyEvent.KEYCODE_BACK)
		{
			if(event.getKeyCode()==KeyEvent.KEYCODE_BACK&&
					event.getAction()==KeyEvent.ACTION_DOWN){
				back();
			return super.dispatchKeyEvent(event);
		  }
		}
		if (event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
			return super.dispatchKeyEvent(event);
		}

		return true;
	}
    void back(){
    	MapActivity.this.finish();
    }


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent intent;
		switch (v.getId()) {
		
		default:
			break;
		}
	}
}


