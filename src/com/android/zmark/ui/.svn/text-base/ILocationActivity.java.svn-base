package com.android.zmark.ui;


import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.zmark.ZmarkApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.BDNotifyListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.android.zmark.R;




public class ILocationActivity extends Activity {

	double lon1;
	double lat1;
	static double DEF_PI = 3.14159265359; // PI
	static double DEF_2PI= 6.28318530712; // 2*PI
	static double DEF_PI180= 0.01745329252; // PI/180.0
	static double DEF_R =6370693.5; // radius of earth
	
	static MapView mMapView = null;	// 地图View
	MKSearch mSearch = null;	// 搜索模块，也可去掉地图模块独立使用
	
	private MapController mMapController = null;

	public MKMapViewListener mMapListener = null;
	FrameLayout mMapViewContainer = null;
	
	// 定位相关
	LocationClient mLocClient;
	public MyLocationListenner myListener = new MyLocationListenner();
    public NotifyLister mNotifyer=null;
	
	Button testUpdateButton = null;
	
	EditText indexText = null;
	MyLocationOverlay myLocationOverlay = null;//定位层
	int index =0;
	LocationData locData = null;
	
	
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ZmarkApplication app = (ZmarkApplication)this.getApplication();
        if (app.mBMapManager == null) {
            app.mBMapManager = new BMapManager(this);
            app.mBMapManager.init(ZmarkApplication.strKey,new ZmarkApplication.MyGeneralListener());
        }
        setContentView(R.layout.baidumap);
        mMapView = (MapView)findViewById(R.id.bmapView);
        mMapController = mMapView.getController();
        
        
       
        initMapView();
        
        mLocClient = new LocationClient( this );
        mLocClient.registerLocationListener( myListener );
        
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);//打开gps
        option.setCoorType("bd09ll");     //设置坐标类型
        option.setScanSpan(5000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        mMapView.getController().setZoom(12);
        
        mMapView.getController().enableClick(true);
        
        mMapView.setBuiltInZoomControls(true);
        
        mMapListener = new MKMapViewListener() {
			
			@Override
			public void onMapMoveFinish() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				String title = "";
				if (mapPoiInfo != null){
					title = mapPoiInfo.strText;
					Toast.makeText(ILocationActivity.this,title,Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onGetCurrentMap(Bitmap b) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onMapLoadFinish() {
				// TODO Auto-generated method stub
				
			}
		};
		
		mMapView.regMapViewListener(ZmarkApplication.getInstance().mBMapManager, mMapListener);
		myLocationOverlay = new MyLocationOverlay(mMapView);
		locData = new LocationData();
	    myLocationOverlay.setData(locData);
		mMapView.getOverlays().add(myLocationOverlay);
		myLocationOverlay.enableCompass();
		
		//需要标记的位置
		
		Drawable mark=getResources().getDrawable(R.drawable.redmark);
		//mMapView.getOverlays().add(new OverItemTs(mark, this));
		
		mMapView.refresh();
		
		
		
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
        mMapView.destroy();
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
    }
    private void initMapView() {
        mMapView.setLongClickable(true);
       
    }
   


	
    public double GetShortDistance(double lon1, double lat1, double lon2, double lat2)
     {
     double ew1, ns1, ew2, ns2;
      double dx, dy, dew;
     double distance;
      // 角度转换为弧度
      ew1 = lon1 * DEF_PI180;
    ns1 = lat1 * DEF_PI180;
     ew2 = lon2 * DEF_PI180;
     ns2 = lat2 * DEF_PI180;
   // 经度差
     dew = ew1 - ew2;
      // 若跨东经和西经180 度，进行调整
      if (dew > DEF_PI)
      dew = DEF_2PI - dew;
      else if (dew < -DEF_PI)
      dew = DEF_2PI + dew;
      dx = DEF_R * Math.cos(ns1) * dew; // 东西方向长度(在纬度圈上的投影长度)
      dy = DEF_R * (ns1 - ns2); // 南北方向长度(在经度圈上的投影长度)
      // 勾股定理求斜边长
      distance = Math.sqrt(dx * dx + dy * dy);
      return distance;
     }
	/**
     * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
     */
    public class MyLocationListenner implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            
            locData.latitude = location.getLatitude();
            locData.longitude = location.getLongitude();
            locData.accuracy = location.getRadius();
            locData.direction = location.getDerect();
            myLocationOverlay.setData(locData);
            lon1=locData.longitude;
            lat1=locData.latitude;
            
             double lat2 = 30.635497; // point1纬度
        	 double lon2 = 104.08069799999998;  // point1经度
           double s= GetShortDistance(lon1, lat1, lon2, lat2);
            if(mMapView!=null){
            	synchronized (mMapView) {
            		mMapView.refresh();
				}
            }
            mMapController.animateTo(new GeoPoint((int)(locData.latitude* 1e6), (int)(locData.longitude *  1e6)));
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
    
    public double GetLongDistance(double lon1, double lat1, double lon2, double lat2)
     {
      double ew1, ns1, ew2, ns2;
      double distance;
      // 角度转换为弧度
      ew1 = lon1 * DEF_PI180;
      ns1 = lat1 * DEF_PI180;
     ew2 = lon2 * DEF_PI180;
     ns2 = lat2 * DEF_PI180;
      // 求大圆劣弧与球心所夹的角(弧度)
      distance = Math.sin(ns1) * Math.sin(ns2) + Math.cos(ns1) * Math.cos(ns2) * Math.cos(ew1 - ew2);
      // 调整到[-1..1]范围内，避免溢出
      if (distance > 1.0)
           distance = 1.0;
      else if (distance < -1.0)
            distance = -1.0;
      // 求大圆劣弧长度
      distance = DEF_R * Math.acos(distance);
      return distance;
     }
     double mLat1 = 39.90923; // point1纬度
     double mLon1 = 116.357428; // point1经度
     double mLat2 = 39.90923;// point2纬度
     double mLon2 = 116.397428;// point2经度
     double distance = GetShortDistance(mLon1, mLat1, mLon2, mLat2);
    

}
