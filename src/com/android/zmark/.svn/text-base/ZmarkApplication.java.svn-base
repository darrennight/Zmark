package com.android.zmark;

import java.util.HashMap;
import java.util.List;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.android.zmark.bean.LocationProvider;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.support.CachePath;
import com.android.zmark.bean.util.Utility;
import com.baidu.location.BDLocation;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class ZmarkApplication extends Application {

	public static ZmarkApplication newInstence = null;

	public static int width;
	public static int height;
	public static float density; // 屏幕密度（像素比例：0.75/1.0/1.5/2.0）
	public static int densityDPI;
	public static int messagenum = 0;
	public static int firstlogin = 0;// 判断是否是第一次登陆
	public String cord;
	public String city="北京";
	public String mycity;
	public double Latitude;
	public double Longitude;
	public static SlidingMenu sm;

	public static List<HashMap<String, Object>> list;// 百度地图中

	public static List<HashMap<String, Boolean>> list_check;

	// 地图相关
	public boolean m_bKeyRight = true;

	public BMapManager mBMapManager = null;
	public BDLocation nowLoaction;
	public static final String strKey = "5ReL3GqeQ0QgMkSAO3IzO5Pq";
	private LruCache<String, Bitmap> avatarCache = null;
	LocationProvider mProvider = null;

	public LocationProvider getLocationProvider() {
		return mProvider;
	}

	public synchronized LruCache<String, Bitmap> getAvatarCache() {
		if (avatarCache == null) {
			buildCache();
		}
		return avatarCache;
	}

	private void buildCache() {
		if (!Utility.isExist(CachePath.photocachePic))
			Utility.mkDirs(CachePath.photocachePic);
		if (!Utility.isExist(CachePath.cacheApk))
			Utility.mkDirs(CachePath.cacheApk);
		if (!Utility.isExist(CachePath.cacheVideo))
			Utility.mkDirs(CachePath.cacheVideo);
		if (!Utility.isExist(CachePath.cacheTemp))
			Utility.mkDirs(CachePath.cacheTemp);
		int memClass = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE))
				.getMemoryClass();

		int cacheSize = Math.max(1024 * 1024 * 8, 1024 * 1024 * memClass / 5);

		avatarCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getRowBytes() * bitmap.getHeight();
			}
		};
	}

	@Override
	public void onCreate() {
		super.onCreate();
		if (Config.DEVELOPER_MODE
				&& Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectAll().penaltyDeath().build());
		}
		initImageLoader(getApplicationContext());
		newInstence = this;
		cord = SettingUtility.getLocationInfo();
		mProvider = new LocationProvider(this);
		this.initEngineManager(this);
		buildCache();
		JPushInterface.setDebugMode(true);
		JPushInterface.init(this);

	}

	public static ZmarkApplication getInstance() {
		return newInstence;
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPoolSize(3)
				.threadPriority(Thread.NORM_PRIORITY - 1)
				.denyCacheImageMultipleSizesInMemory()

				.memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	public static class Config {
		public static final boolean DEVELOPER_MODE = false;
	}

	public void onTerminate() {
		if (mBMapManager != null) {
			mBMapManager.destroy();
			mBMapManager = null;
		}
		super.onTerminate();
	}

	public boolean userIsOline() {
		return !TextUtils.isEmpty(SettingUtility.getUserInfo());
	}

	public void initEngineManager(Context context) {
		if (mBMapManager == null) {
			mBMapManager = new BMapManager(context);
		}

		if (mBMapManager.init(strKey, new MyGeneralListener())) {
			/*
			 * Toast.makeText(ZmarkApplication.getInstance().getApplicationContext
			 * (), "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
			 */
		}
	}

	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
	public static class MyGeneralListener implements MKGeneralListener {

		@Override
		public void onGetNetworkState(int iError) {
			if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
				Toast.makeText(
						ZmarkApplication.getInstance().getApplicationContext(),
						"您的网络出错啦！", Toast.LENGTH_LONG).show();
			} else if (iError == MKEvent.ERROR_NETWORK_DATA) {
				Toast.makeText(
						ZmarkApplication.getInstance().getApplicationContext(),
						"输入正确的检索条件！", Toast.LENGTH_LONG).show();
			}
			// ...
		}

		@Override
		public void onGetPermissionState(int iError) {
			if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
				// 授权Key错误：
				Toast.makeText(
						ZmarkApplication.getInstance().getApplicationContext(),
						"请在 DemoApplication.java文件输入正确的授权Key！",
						Toast.LENGTH_LONG).show();
				ZmarkApplication.getInstance().m_bKeyRight = false;
			}
		}
	}
}
