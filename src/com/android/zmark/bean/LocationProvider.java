/**
 * @author ChenDu GOGO  oblivion
 *	时间   2013 上午11:17:05
 *  包名：com.gogotown.core.bean
            工程名：GoGoCity
 */
package com.android.zmark.bean;

import org.json.JSONObject;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.zmark.ZmarkApplication;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * @类名 LocationProvider.java
 * @时间 2013-4-11 上午11:17:05
 * @描述 定位类
 */
public class LocationProvider implements BDLocationListener, MKGeneralListener,
		MKSearchListener {
	private final String TAG = "LocationProvider";
	private int defauldScanSpan = 1000 * 60 * 3; // 定位间隔时间
	private LocationClient mLocationClient = null; // 百度定位模块
	private Application mContext;
	private boolean mIsStart = false;
	private LocationListener mLocationListener;
	private double mLatitude = 0.0d, mLongitude = 0.0d;
	private String mAddress;
	public static final double LocationError = 0.0d;
	private String lastLocation;
	private BMapManager mBMapManager = null;
	private MKSearch mMKSearch;
	private static LocationProvider instance;

	public LocationProvider(Application mContext) {
		super();
		this.mContext = mContext;
		init();
		instance = this;
	}

	public static LocationProvider getInstance() {
		if (instance == null)
			instance = new LocationProvider(ZmarkApplication.getInstance());
		return instance;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public double getLongitude() {
		return mLongitude;
	}

	public LocationListener getLocationListener() {
		return mLocationListener;
	}

	public void setLocationListener(LocationListener locationListener) {
		this.mLocationListener = locationListener;
	}

	// 是否已经定位成功,
	public enum LocationType {
		UNKOWN, SCUCESS, FAIL, UNFITECITY
	}

	public LocationType isSuccessfulPositioning(boolean mCurrentCity) {
		if (TextUtils.isEmpty(lastLocation))
			return LocationType.FAIL;
		// 解析
		// {"date": "2013-12-19 13:15:14","time": 1387430114950,"locType":
		// "161","latitude": 30.646376,"lontitude": 104.099282,"address":
		// "四川省成都市锦江区水碾河南路65-10号","poi": "noPoi information"}
		try {
			JSONObject loacationJS = new JSONObject(lastLocation);
			long last = loacationJS.optLong("time");
			if ((System.currentTimeMillis() - last) > 60 * 60 * 1000) // 大于一小时就重新定位
				return LocationType.FAIL;
			mLatitude = loacationJS.optDouble("latitude");
			mLongitude = loacationJS.optDouble("lontitude");
			if (mLatitude == LocationError || mLongitude == LocationError)
				return LocationType.FAIL;
			// if (mCurrentCity
			// && !SettingUtility.getCurrentCity().equals(
			// loacationJS.optString("city")))
			// return LocationType.UNFITECITY;
			return LocationType.SCUCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return LocationType.FAIL;
	}

	// 设置定位关参数
	public void init() {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(mContext);
		}
		if (!mBMapManager.init(ZmarkApplication.strKey, this)) {
			LogHelper.w(TAG, "百度地图相关初始化错误！");
		}
		lastLocation = SettingUtility.getLocationInfo();
		mLocationClient = new LocationClient(mContext);
		// mLocationClient.setAK(Constants.baiduMapKey);//4.0使用
		mLocationClient.registerLocationListener(this);
		// mGeofenceClient = new GeofenceClient(mContext);
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true); // 打开gps
		option.setCoorType("bd0911"); // 设置坐标类型
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
		// /option.setPriority(LocationClientOption.GpsFirst); //GPS有先
		option.setScanSpan(defauldScanSpan);
		option.setPoiNumber(10); // 周边热点个数
		option.disableCache(true);
		mLocationClient.setLocOption(option);
		mMKSearch = new MKSearch();
		mMKSearch.init(mBMapManager, this);
	}

	public void onStart() {
		if (!mIsStart) {
			mLocationClient.start();
			mIsStart = true;

		}
	}

	public void onStop() {
		if (mIsStart) {
			mLocationClient.stop();
			mIsStart = false;

		}
	}

	public void onRequestLocation() {
		if (mLocationClient != null && mLocationClient.isStarted()) {
			mLocationClient.requestLocation();
		} else {
			onStart();
		}
	}

	/**
	 * 定位相关
	 */
	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null) {
			if (this.mLocationListener != null) {
				this.mLocationListener.onFailLocation();
				this.mLocationListener = null;
			}
			return;
		}
		mlocation = location;
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
		mAddress = location.getAddrStr();
		/** 查询该经纬度值所对应的地址位置信息 */
		int longitude = (int) (1000000 * location.getLongitude());
		int latitude = (int) (1000000 * location.getLatitude());
		mMKSearch.reverseGeocode(new GeoPoint(latitude, longitude));

	}

	BDLocation mlocation;

	@Override
	public void onReceivePoi(BDLocation location) {
		if (location == null) {
			if (this.mLocationListener != null) {
				this.mLocationListener.onFailLocation();
				this.mLocationListener = null;
			}
			return;
		}
		mlocation = location;
		mLatitude = location.getLatitude();
		mLongitude = location.getLongitude();
		mAddress = location.getAddrStr();
		int longitude = (int) (1000000 * location.getLongitude());
		int latitude = (int) (1000000 * location.getLatitude());
		mMKSearch.reverseGeocode(new GeoPoint(latitude, longitude));

	}

	private void saveLocation(BDLocation location, String city) {
		if (location == null)
			return;

		StringBuffer sb = new StringBuffer();
		sb.append("{");
		sb.append("\"date\": ");
		sb.append("\"" + location.getTime() + "\"");
		sb.append(",");
		sb.append("\"time\": ");
		sb.append(System.currentTimeMillis());
		sb.append(",");
		sb.append("\"locType\": ");
		sb.append("\"" + location.getLocType() + "\"");
		sb.append(",");
		sb.append("\"latitude\": ");
		sb.append(location.getLatitude());
		sb.append(",");
		sb.append("\"lontitude\": ");
		sb.append(location.getLongitude());
		sb.append(",");
		sb.append("\"radius\": ");
		sb.append(location.getRadius());
		sb.append(",");
		sb.append("\"city\": ");
		sb.append(TextUtils.isEmpty(city) ? "" : city);
		sb.append(",");
		if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
			sb.append("\"address\": ");
			sb.append("\"" + location.getAddrStr() + "\"");
			sb.append(",");
		} else {
			sb.append("\"address\": ");
			sb.append("\"" + "" + "\"");
			sb.append(",");
		}

		if (location.hasPoi()) {
			sb.append("\"poi\": ");
			sb.append("\"" + location.getPoi() + "\"");
		} else {
			sb.append("\"poi\": ");
			sb.append("\"" + "noPoi information" + "\"");
		}
		sb.append("}");
		lastLocation = sb.toString();
		LogHelper.i(TAG, lastLocation);
		SettingUtility.setLocationInfo(lastLocation);
	}

	public interface LocationListener {
		public void onReceiveLocation(double Latitude, double Longitude,
				String city, String address);

		public void onFailLocation();
	}

	@Override
	public void onGetNetworkState(int arg0) {
		if (arg0 == MKEvent.ERROR_NETWORK_CONNECT) {
			LogHelper.w(TAG, "您的网络出错啦");
		} else if (arg0 == MKEvent.ERROR_NETWORK_DATA) {
			LogHelper.w(TAG, "输入正确的检索条件");
		}
	}

	@Override
	public void onGetPermissionState(int iError) {
		// 非零值表示key验证未通过
		if (iError != 0) {
			// 授权Key错误：
			LogHelper.w(TAG, "输入正确的授权Key,并检查您的网络连接是否正常！");
		} else {
			LogHelper.i(TAG, "百度地图key认证成功");
		}
	}

	@Override
	public void onGetAddrResult(MKAddrInfo result, int arg1) {
		if (this.mLocationListener != null) {
			this.mLocationListener.onReceiveLocation(mlocation.getLatitude(),
					mlocation.getLongitude(), result == null ? ""
							: result.addressComponents.city, mlocation
							.getAddrStr());
		}
		if (arg1 != 0 || result == null) {
			saveLocation(mlocation, null);
		} else {
			Log.i("json", "result= " + result);
			// //////保存坐标
			saveLocation(mlocation, result.addressComponents.city);
		}
	}

	public void onWalk(String city, String location, String destination) {
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = location;
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = destination;
		// 实际使用中请对起点终点城市进行正确的设定
		mMKSearch.walkingSearch(city, stNode, city, enNode);
	}

	public void onWalk(String city, GeoPoint location, GeoPoint destination) {
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode stNode = new MKPlanNode();
		stNode.pt = location;
		MKPlanNode enNode = new MKPlanNode();
		enNode.pt = destination;
		// 实际使用中请对起点终点城市进行正确的设定
		mMKSearch.walkingSearch(city, stNode, city, enNode);
	}

	public void onDriving(String city, String location, String destination) {
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = location;
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = destination;
		// 实际使用中请对起点终点城市进行正确的设定
		mMKSearch.drivingSearch(city, stNode, city, enNode);
	}

	public void onDriving(String city, GeoPoint location, GeoPoint destination) {
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode stNode = new MKPlanNode();
		stNode.pt = location;
		MKPlanNode enNode = new MKPlanNode();
		enNode.pt = destination;
		// 实际使用中请对起点终点城市进行正确的设定
		mMKSearch.drivingSearch(city, stNode, city, enNode);
	}

	public void onBus(String city, String location, String destination) {
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode stNode = new MKPlanNode();
		stNode.name = location;
		MKPlanNode enNode = new MKPlanNode();
		enNode.name = destination;
		// 实际使用中请对起点终点城市进行正确的设定
		mMKSearch.transitSearch(city, stNode, enNode);
	}

	public void onBus(String city, GeoPoint location, GeoPoint destination) {
		// 对起点终点的name进行赋值，也可以直接对坐标赋值，赋值坐标则将根据坐标进行搜索
		MKPlanNode stNode = new MKPlanNode();
		stNode.pt = location;
		MKPlanNode enNode = new MKPlanNode();
		enNode.pt = destination;
		// 实际使用中请对起点终点城市进行正确的设定
		mMKSearch.transitSearch(city, stNode, enNode);
	}

	private GetRouteResultListener resultListener;

	public void setRouteResultListener(GetRouteResultListener resultListener) {
		this.resultListener = resultListener;
	}

	public interface GetRouteResultListener {
		public void onDrivingRouteResult(MKDrivingRouteResult arg0, int error);

		public void onBusRouteResult(MKTransitRouteResult arg0, int error);

		public void onWalkingRouteResult(MKWalkingRouteResult arg0, int error);
	}

	@Override
	public void onGetBusDetailResult(MKBusLineResult arg0, int arg1) {
	}

	@Override
	public void onGetDrivingRouteResult(MKDrivingRouteResult arg0, int error) {
		if (resultListener != null)
			resultListener.onDrivingRouteResult(arg0, error);
	}

	@Override
	public void onGetPoiDetailSearchResult(int arg0, int arg1) {

	}

	@Override
	public void onGetPoiResult(MKPoiResult arg0, int arg1, int arg2) {

	}

	@Override
	public void onGetShareUrlResult(MKShareUrlResult arg0, int arg1, int arg2) {

	}

	@Override
	public void onGetSuggestionResult(MKSuggestionResult arg0, int arg1) {

	}

	@Override
	public void onGetTransitRouteResult(MKTransitRouteResult arg0, int arg1) {
		if (resultListener != null)
			resultListener.onBusRouteResult(arg0, arg1);
	}

	@Override
	public void onGetWalkingRouteResult(MKWalkingRouteResult arg0, int arg1) {

		if (resultListener != null)
			resultListener.onWalkingRouteResult(arg0, arg1);

	}
}
