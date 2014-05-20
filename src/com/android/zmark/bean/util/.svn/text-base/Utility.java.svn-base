package com.android.zmark.bean.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.opengl.GLES10;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.SoundEffectConstants;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.zmark.bean.LogHelper;

public class Utility {

	private Utility() {
	}
	public static String getFormatTime(long time) {
		if (time == 0) {
			return "";
		}
		Timestamp timestamp = new Timestamp(time);
		SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
		String str = df.format(timestamp);
		return str;
	}

	
	public static String encodeUrl(Map<String, String> param) {
		if (param == null) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		Set<String> keys = param.keySet();
		boolean first = true;

		for (String key : keys) {
			String value = param.get(key);
			// pain...EditMyProfileDao params' values can be empty
			if (!TextUtils.isEmpty(value) || key.equals("description")
					|| key.equals("url")) {
				if (first)
					first = false;
				else
					sb.append("&");
				try {
					sb.append(URLEncoder.encode(key, "UTF-8")).append("=")
							.append(URLEncoder.encode(param.get(key), "UTF-8"));
				} catch (UnsupportedEncodingException e) {

				}
			}

		}
		LogHelper.i("请求参数---" + sb.toString());
		return sb.toString();
	}

	public static Bundle decodeUrl(String s) {
		Bundle params = new Bundle();
		if (s != null) {
			String array[] = s.split("&");
			for (String parameter : array) {
				String v[] = parameter.split("=");
				try {
					params.putString(URLDecoder.decode(v[0], "UTF-8"),
							URLDecoder.decode(v[1], "UTF-8"));
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();

				}
			}
		}
		return params;
	}

	public static void closeSilently(Closeable closeable) {
		if (closeable != null)
			try {
				closeable.close();
			} catch (IOException ignored) {

			}
	}

	/**
	 * Parse a URL query and fragment parameters into a key-value bundle.
	 */
	public static Bundle parseUrl(String url) {
		url = url.replace("weiboconnect", "http");
		try {
			URL u = new URL(url);
			Bundle b = decodeUrl(u.getQuery());
			b.putAll(decodeUrl(u.getRef()));
			return b;
		} catch (MalformedURLException e) {
			return new Bundle();
		}
	}

	public static int length(String paramString) {
		int i = 0;
		for (int j = 0; j < paramString.length(); j++) {
			if (paramString.substring(j, j + 1).matches("[Α-￥]"))
				i += 2;
			else
				i++;
		}
		if (i % 2 > 0) {
			i = 1 + i / 2;
		} else {
			i = i / 2;
		}

		return i;
	}

	public static boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();

		return networkInfo != null && networkInfo.isConnected();
	}

	public static boolean isWifi(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	public static int getNetType(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			return networkInfo.getType();
		}
		return -1;
	}

	public static boolean isGprs(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = cm.getActiveNetworkInfo();
		if (networkInfo != null && networkInfo.isConnected()) {
			if (networkInfo.getType() != ConnectivityManager.TYPE_WIFI) {
				return true;
			}
		}
		return false;
	}

	public static boolean isSystemRinger(Context context) {
		AudioManager manager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
		return manager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL;
	}

	// public static void configVibrateLedRingTone(Notification.Builder builder)
	// {
	// configRingTone(builder);
	// configLed(builder);
	// configVibrate(builder);
	// }

	// private static void configVibrate(Notification.Builder builder) {
	// if (SettingUtility.allowVibrate()) {
	// long[] pattern = { 0, 200, 500 };
	// builder.setVibrate(pattern);
	// }
	// }

	// private static void configRingTone(Notification.Builder builder) {
	// Uri uri = null;
	//
	// if (!TextUtils.isEmpty(SettingUtility.getRingtone())) {
	// uri = Uri.parse(SettingUtility.getRingtone());
	// }
	//
	// if (uri != null && isSystemRinger(GoGoApp.getInstance())) {
	// builder.setSound(uri);
	// }
	// }

	// private static void configLed(Notification.Builder builder) {
	// if (SettingUtility.allowLed()) {
	// builder.setLights(Color.WHITE, 2000, 2000);
	// }
	//
	// }

	public static String getPicPathFromUri(Uri uri, Activity activity) {
		String value = uri.getPath();

		if (value.startsWith("/external")) {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else {
			return value;
		}
	}

	public static boolean isAllNotNull(Object... obs) {
		for (int i = 0; i < obs.length; i++) {
			if (obs[i] == null) {
				return false;
			}
		}
		return true;
	}

	public static boolean isIntentSafe(Activity activity, Uri uri) {
		Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
		PackageManager packageManager = activity.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				mapCall, 0);
		return activities.size() > 0;
	}

	public static boolean isIntentSafe(Activity activity, Intent intent) {
		PackageManager packageManager = activity.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				intent, 0);
		return activities.size() > 0;
	}

	public static boolean isGooglePlaySafe(Activity activity) {
		Uri uri = Uri
				.parse("http://play.google.com/store/apps/details?id=com.google.android.gms");
		Intent mapCall = new Intent(Intent.ACTION_VIEW, uri);
		mapCall.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		mapCall.setPackage("com.android.vending");
		PackageManager packageManager = activity.getPackageManager();
		List<ResolveInfo> activities = packageManager.queryIntentActivities(
				mapCall, 0);
		return activities.size() > 0;
	}

	public static Rect locateView(View v) {
		int[] location = new int[2];
		if (v == null)
			return null;
		try {
			v.getLocationOnScreen(location);
		} catch (NullPointerException npe) {
			return null;
		}
		Rect locationRect = new Rect();
		locationRect.left = location[0];
		locationRect.top = location[1];
		locationRect.right = locationRect.left + v.getWidth();
		locationRect.bottom = locationRect.top + v.getHeight();
		return locationRect;
	}

	public static int countWord(String content, String word, int preCount) {
		int count = preCount;
		int index = content.indexOf(word);
		if (index == -1) {
			return count;
		} else {
			count++;
			return countWord(content.substring(index + word.length()), word,
					count);
		}
	}

	public static String getIdFromWeiboAccountLink(String url) {
		String id = url.substring(19);
		id = id.replace("/", "");
		return id;
	}

	public static String getDomainFromWeiboAccountLink(String url) {
		String domain = url.substring(17);
		domain = domain.replace("/", "");
		return domain;
	}

	public static boolean isWeiboAccountIdLink(String url) {
		return !TextUtils.isEmpty(url) && url.startsWith("http://weibo.com/u/");
	}

	public static boolean isWeiboAccountDomainLink(String url) {
		if (TextUtils.isEmpty(url)) {
			return false;
		} else {
			boolean a = url.startsWith("http://weibo.com/");
			boolean b = !url.contains("?");
			return a && b;
		}
	}

	public static void playClickSound(View view) {
		view.playSoundEffect(SoundEffectConstants.CLICK);
	}

	public static View getListViewItemViewFromPosition(ListView listView,
			int position) {
		return listView.getChildAt(position
				- listView.getFirstVisiblePosition());
	}

	private static Dialog mProgressDialog;

	public static final void showProgressDialog(Context context, String title,
			String message) {
		dismissDialog();
		if (TextUtils.isEmpty(title)) {
			title = "请稍候";
		}
		if (TextUtils.isEmpty(message)) {
			message = "正在加载...";
		}
		mProgressDialog = ProgressDialog.show(context, title, message);
	}

	public static final void dismissDialog() {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	public static int getMaxLeftWidthOrHeightImageViewCanRead(int heightOrWidth) {
		// 1pixel==4bytes
		// http://stackoverflow.com/questions/13536042/android-bitmap-allocating-16-bytes-per-pixel
		// http://stackoverflow.com/questions/15313807/android-maximum-allowed-width-height-of-bitmap
		int[] maxSizeArray = new int[1];
		GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);

		if (maxSizeArray[0] == 0) {
			GLES10.glGetIntegerv(GL11.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);
		}
		int maxHeight = maxSizeArray[0];
		int maxWidth = maxSizeArray[0];

		return (maxHeight * maxWidth) / heightOrWidth;
	}

	// sometime can get value, sometime can't, so I define it is 2048x2048
	public static int getBitmapMaxWidthAndMaxHeight() {
		int[] maxSizeArray = new int[1];
		GLES10.glGetIntegerv(GL10.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);

		if (maxSizeArray[0] == 0) {
			GLES10.glGetIntegerv(GL11.GL_MAX_TEXTURE_SIZE, maxSizeArray, 0);
		}
		// return maxSizeArray[0];
		return 2048;
	}

	/**
	 * 网络是否可用，包括3G和WIFI中的任意一种
	 */
	public static boolean NetworkAvailable(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null || !info.isConnected()) {
				return false;
			}
			if (info.isRoaming()) {
				return true;
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return true;
	}

	public static int NetworkType(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info == null)
				return -1;
			return info.getType();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * GPRS/3G 是否可用
	 */
	public static boolean GprsNetworkAvailable(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity == null) {
				return false;
			} else {
				NetworkInfo info = connectivity.getActiveNetworkInfo();
				if (info == null) {
					return false;
				} else {
					if (info.isAvailable()) {
						return true;
					}
				}
			}
		} catch (NullPointerException e) {
			e.printStackTrace();
			return false;
		} catch (RuntimeException e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * wifi 是否可用
	 */
	public static boolean WifiNetworkAvailable(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			WifiInfo wifiInfo = wifiManager.getConnectionInfo();
			int ipAddress = wifiInfo == null ? 0 : wifiInfo.getIpAddress();//

			if (wifiManager.isWifiEnabled() && ipAddress != 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	// public static int[] DisplayPixels(Activity context) {
	// DisplayMetrics dm = new DisplayMetrics();
	// context.getWindowManager().getDefaultDisplay().getMetrics(dm);
	// int[] display = new int[2];
	// display[0] = dm.widthPixels;
	// display[1] = dm.heightPixels;
	// return display;
	// }
	public static int[] DisplayPixels(Context context) {
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		int width = wm.getDefaultDisplay().getWidth();
		int height = wm.getDefaultDisplay().getHeight();
		// DisplayMetrics dm = new DisplayMetrics();
		// context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int[] display = new int[2];
		display[0] = width;
		display[1] = height;
		return display;
	}

	public static boolean GPSOpening(Context mContext) {
		LocationManager alm = (LocationManager) mContext
				.getSystemService(Context.LOCATION_SERVICE);
		if (alm.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			return true;
		} else {
			// Intent myIntent = new Intent(Settings.ACTION_SECURITY_SETTINGS);
			// mContext.startActivity(myIntent);
			return false;
		}
	}

	/**
	 * 获取SD卡剩余空间，单位 MB
	 */
	public static long getSDAvailaleSpace() {

		File path = Environment.getExternalStorageDirectory(); // 取得sdcard文件路径
		StatFs stat = new StatFs(path.getPath());
		long blockSize = stat.getBlockSize();
		long availableBlocks = stat.getAvailableBlocks();
		return (availableBlocks * blockSize) / 1024 / 1024;
		// (availableBlocks * blockSize)/1024 KIB 单位
		// (availableBlocks * blockSize)/1024 /1024 MIB单位

	}

	/**
	 * @根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * @得到手机的dpi
	 */
	public static float getdpi(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	/**
	 * @根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 将px值转换为sp值，保证文字大小不变
	 * 
	 * @param pxValue
	 * @param fontScale
	 *            （DisplayMetrics类中属性scaledDensity）
	 * @return
	 */
	public static int px2sp(Context context, float pxValue) {
		final float fontScale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / fontScale + 0.5f);
	}

	public static String getVersion(Context ctx) {
		if (ctx == null)
			return "";
		String versionName = "";
		PackageInfo packageInfo;
		try {
			packageInfo = ctx.getPackageManager().getPackageInfo(
					ctx.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return versionName;
	}

	public static int getVersionCode(Context ctx) {
		if (ctx == null)
			return 0;
		try {
			return ctx.getPackageManager().getPackageInfo(ctx.getPackageName(),
					0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public static String getDeviceId(Context ctx) {
		if (ctx == null)
			return "";
		return ((TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
	}

	public static String readFileData(String fileName) {
		try {
			File urlFile = new File(fileName);
			InputStreamReader isr = new InputStreamReader(new FileInputStream(
					urlFile), "UTF-8");
			BufferedReader br = new BufferedReader(isr);
			StringBuffer str = new StringBuffer();
			String mimeTypeLine = null;
			while ((mimeTypeLine = br.readLine()) != null) {
				str.append(mimeTypeLine);
			}
			return str.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * 加密过后的设备号
	 */
	public static String getPhoneSerialNumber(Context mContext) {
		final TelephonyManager tm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		final String tmDevice, tmSerial, androidId;
		tmDevice = "" + tm.getDeviceId();
		tmSerial = "" + tm.getSimSerialNumber();
		androidId = ""
				+ android.provider.Settings.Secure.getString(
						mContext.getContentResolver(),
						android.provider.Settings.Secure.ANDROID_ID);
		UUID deviceUuid = new UUID(androidId.hashCode(),
				((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
		return deviceUuid.toString();

	}

	public static void recycleViewGroupAndChildViews(ViewGroup viewGroup,
			boolean recycleBitmap) {
		for (int i = 0; i < viewGroup.getChildCount(); i++) {

			View child = viewGroup.getChildAt(i);

			if (child instanceof WebView) {
				WebView webView = (WebView) child;
				webView.loadUrl("about:blank");
				webView.stopLoading();
				continue;
			}

			if (child instanceof ViewGroup) {
				recycleViewGroupAndChildViews((ViewGroup) child, true);
				continue;
			}

			if (child instanceof ImageView) {
				ImageView iv = (ImageView) child;
				Drawable drawable = iv.getDrawable();
				if (drawable instanceof BitmapDrawable) {
					BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
					Bitmap bitmap = bitmapDrawable.getBitmap();
					if (recycleBitmap && bitmap != null) {
						bitmap.recycle();
					}
				}
				iv.setImageBitmap(null);
				iv.setBackgroundDrawable(null);
				continue;
			}

			child.setBackgroundDrawable(null);

		}

		viewGroup.setBackgroundDrawable(null);
	}

	// ///////////////////////////////////file Utility/////////////////////

	public static long MB = 1024 * 1024;
	private final static int FILESIZE = 4 * 1024;

	/**
	 * 从输入流中获取数据
	 * 
	 * @param inStream
	 *            输入流
	 * @return
	 * @throws Exception
	 */
	public static byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		inStream.close();
		return outStream.toByteArray();
	}

	/**
	 * 写入文件
	 */
	public static void writeToFile(File file, Bitmap data) throws Exception {

		FileOutputStream fOut = null;
		try {
			if (!file.exists())
				file.createNewFile();
			fOut = new FileOutputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		data.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return;
	}

	/**
	 * 根据URL得到输入流
	 * 
	 * @param urlStr
	 * @return
	 */
	public static InputStream getInputFileStreamFromURL(String urlStr) {
		HttpURLConnection urlConn = null;
		InputStream inputStream = null;
		try {
			URL url = new URL(urlStr);
			urlConn = (HttpURLConnection) url.openConnection();
			inputStream = urlConn.getInputStream();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return inputStream;
	}

	public static String writeToFile(Bitmap data) throws Exception {
		File file = File.createTempFile(
				String.valueOf(System.currentTimeMillis()), ".png");
		FileOutputStream fOut = null;
		try {
			if (!file.exists())
				file.createNewFile();
			fOut = new FileOutputStream(file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		data.compress(Bitmap.CompressFormat.JPEG, 100, fOut);// 把Bitmap对象解析成流
		try {
			fOut.flush();
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file.getAbsolutePath();
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public static File write2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			mkDirs(path);
			file = createSDFile(path, fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[FILESIZE];
			while ((input.read(buffer)) != -1) {
				output.write(buffer);
			}
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 将一个InputStream里面的数据写入到SD卡中
	 * 
	 * @param path
	 * @param fileName
	 * @param input
	 * @return
	 */
	public static File writeGuideImg2SDFromInput(String path, String fileName,
			InputStream input) {
		File file = null;
		OutputStream output = null;
		try {
			mkDirs(path);
			file = createSDFile(path, fileName);
			output = new FileOutputStream(file);
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = input.read(buffer)) != -1) {
				output.write(buffer, 0, len);
			}
			input.close();
			output.close();
			output.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}

	/**
	 * 在SD卡上创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static File createSDFile(String path, String fileName)
			throws IOException {
		File file = new File(path, fileName);
		file.createNewFile();
		return file;
	}

	public static File createFile(String path, String fileName)
			throws IOException {
		File file = new File(path, fileName);
		file.createNewFile();
		return file;
	}

	public static boolean mkDir(String dirPath) {
		File mPath = new File(dirPath);
		if (mPath.exists()) {
			return false;
		}
		return mPath.mkdir();

	}

	public static boolean mkDirs(String dirPath) {

		File mPath = new File(dirPath);
		if (mPath.exists()) {
			return false;
		}
		return mPath.mkdirs();

	}

	/*
	 * 判断SD卡是否存在
	 */
	public static boolean isSDExist() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {

			return true;
		}

		return false;
	}

	public static boolean isExist(String path) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			File f = new File(path);
			if (f.exists()) {
				return true;
			}
		}

		return false;
	}

	public static long getFileSize(String path) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return new File(path).length();
		}
		return 0;
	}

	public static void deleteFile(String path) {
		if (TextUtils.isEmpty(path))
			return;
		File fleFile = new File(path);
		deleteFile(fleFile);
		fleFile = null;
	}

	/**
	 * 递归删除文件和文件夹，注意，别乱用，防止誤殺
	 */
	public static void deleteFile(File file) {
		if (file.isFile()) {
			file.deleteOnExit();
			return;
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.deleteOnExit();
				return;
			}
			for (File f : childFile) {
				deleteFile(f);
			}
			file.deleteOnExit();
		}
	}

	public static void writeFile(String info, String filePath) {
		File targetFile = new File(filePath);
		if (targetFile != null) {
			if (targetFile.exists()) {
				targetFile.delete();
			}

			if (!targetFile.getParentFile().exists())
				mkDirs(targetFile.getParentFile().getAbsolutePath());
			OutputStreamWriter osw;
			try {
				osw = new OutputStreamWriter(new FileOutputStream(targetFile),
						"utf-8");
				try {
					osw.write(info);
					osw.flush();
					osw.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 获取文件大小
	 * 
	 * @param length
	 * @return
	 */
	public static String formatFileSize(long length) {
		String result = null;
		int sub_string = 0;
		if (length >= 1073741824) {
			sub_string = String.valueOf((float) length / 1073741824).indexOf(
					".");
			result = ((float) length / 1073741824 + "000").substring(0,
					sub_string + 3) + "GB";
		} else if (length >= 1048576) {
			sub_string = String.valueOf((float) length / 1048576).indexOf(".");
			result = ((float) length / 1048576 + "000").substring(0,
					sub_string + 3) + "MB";
		} else if (length >= 1024) {
			sub_string = String.valueOf((float) length / 1024).indexOf(".");
			result = ((float) length / 1024 + "000").substring(0,
					sub_string + 3) + "KB";
		} else if (length < 1024)
			result = Long.toString(length) + "B";
		return result;
	}

	public static String GetFileName(String URL, String type) {
		try {

			int end = URL.indexOf(type);
			if (end != -1) {
				String temp = URL.substring(0, end + 3);
				int start = temp.lastIndexOf("/");

				return (temp.substring(start + 1));
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;

	}

	public static boolean downGuideImgLoadFile(String url, String path,
			String filename) {
		try {
			InputStream inputStream = null;
			try {

				if (!Utility.isExist(path))
					Utility.mkDirs(path);
				if (Utility.isExist(TextUtils.isEmpty(filename) ? path
						+ File.separator + url + ".jpg" : path + File.separator
						+ filename)) {
					return false;
				} else {
					inputStream = Utility.getInputFileStreamFromURL(url);
					if (inputStream != null)
						Utility.writeGuideImg2SDFromInput(path, TextUtils
								.isEmpty(filename) ? url + ".jpg" : filename,
								inputStream);
				}
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			} finally {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;

	}

	/**
	 * 隐藏键盘
	 */
	public static void onHideInputSoftKeyboard(View editeText) {
		try {
			InputMethodManager imm = (InputMethodManager) editeText
					.getContext()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editeText.getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static Bitmap decodeBitmapFromSDCard(String path, int reqWidth,
			int reqHeight) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(path, options);

	}

	private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {

		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (height > reqHeight && reqHeight != 0) {
				inSampleSize = (int) Math.floor((double) height
						/ (double) reqHeight);
			}

			int tmp = 0;

			if (width > reqWidth && reqWidth != 0) {
				tmp = (int) Math.floor((double) width / (double) reqWidth);
			}

			inSampleSize = Math.max(inSampleSize, tmp);

		}
		int roundedSize;
		if (inSampleSize <= 8) {
			roundedSize = 1;
			while (roundedSize < inSampleSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (inSampleSize + 7) / 8 * 8;
		}

		return roundedSize;
	}

}
