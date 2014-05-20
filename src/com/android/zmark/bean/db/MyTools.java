package com.android.zmark.bean.db;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.TextUtils;
import android.util.Log;


public class MyTools {

	private static final String TAG = "MyTools";
	
	/**
	 * 获取远程字符串
	 * 
	 * @param mUrlStr
	 * @return String
	 */
	public static String getRemoteStr(String mUrlStr) {
		InputStream is = null;
		StringBuffer sb = new StringBuffer();
		try {
			URL url = new URL(mUrlStr);
			is = url.openConnection().getInputStream();
			byte[] bytes = new byte[128];
			int n = 0;
			while ((n = is.read(bytes)) != -1) {
				sb.append(new String(bytes, 0, n, "GBK"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (is != null)
					is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	
	/**
	 * Post提交
	 */
	
	public static String Post(String url,NameValuePair...params){
		List<NameValuePair> formparams= new ArrayList<NameValuePair>();
		for(NameValuePair p:params){
			formparams.add(p);
			Log.i("==par", ""+p.getName()+"   "+p.getValue());
		}
		UrlEncodedFormEntity entity = null;
		try {
			entity= new UrlEncodedFormEntity(formparams,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.i(TAG, "编码错误");
			return null;
		}
		HttpPost post= new HttpPost(url);
		post.setEntity(entity);
		BasicHttpParams httpParams= new BasicHttpParams();
		HttpClient client= new DefaultHttpClient(httpParams);
		HttpResponse response = null;
		try {
			response=client.execute(post);
		} catch (ClientProtocolException e) {
			Log.i("=====", "客户端异常");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			Log.i("=====", "客户端异常");
			e.printStackTrace();
			return null;
		}
		
		if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK) {
			Log.e(TAG, "response.getStatusLine().getStatusCode():" + response.getStatusLine().getStatusCode());
			throw new RuntimeException("");
		}
		
		HttpEntity httpentity= response.getEntity();
		try {
			return (httpentity == null) ? null : EntityUtils.toString(httpentity, "UTF-8");
		} catch (ParseException e) {
			e.printStackTrace();
			Log.e(TAG, "类型转换出错");
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			Log.i(TAG, "类型转换出错");
			return null;
		}
		
	}
	
	/**
	 * Get数据提交
	 */
	
	public static String getURLDate(String url){
		HttpResponse response= null;
		String result=null;
		HttpGet httpget= new HttpGet(url);
		try {
			response= new DefaultHttpClient().execute(httpget);
			if (response.getStatusLine().getStatusCode() == 200) {
				result = EntityUtils.toString(response.getEntity(), "GB2312");
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
	/**
	 * 编码网址中的中文
	 * 
	 * @param url
	 * @return 成功返回编码后的网址，失败返回null
	 */
	public static String encodeURLChinese(String url) {
		if (TextUtils.isEmpty(url)) {
			return null;
		}
		String result = null;

		String[] array = url.split("/");
		String last = array[array.length - 1];
		String[] array1 = last.split("\\.");
		String chinese = array1[0];
		try {
			String newStr = URLEncoder.encode(chinese, "utf-8");
			result = url.replaceAll(chinese, newStr);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 检测网络是否连接(如果检测不可以连接，肯定就不可以。检测了可以连接，不一定可以)
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isConnect(Context context) {
		// 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity != null) {
			// 获取网络连接管理的对象
			NetworkInfo info = connectivity.getActiveNetworkInfo();
			if (info != null && info.isConnected()) {
				// 判断当前网络是否已经连接
				if (info.getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}

		return false;
	}

	
}
