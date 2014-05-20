package com.android.zmark.bean.support.http;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;

import com.android.zmark.bean.LogHelper;

/**
 * oblivion
 */
public class FHttpLoader<T> extends

AbstractAsyncNetRequestTaskLoader<HttpResponseBean> {
	private String url;
	public FHttpLoader(Context context, String url) {
		super(context);
		this.url = url;
	}

	@Override
	protected HttpResponseBean loadData() throws ApiException {
		String jsonData = null;
		try {
			URL uri = new URL(url);
			URLConnection ucon = uri.openConnection();
			InputStream is = ucon.getInputStream();

			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayBuffer baf = new ByteArrayBuffer(100);
			int current = 0;
			/* 一直读到文件结束 */
			while ((current = bis.read()) != -1) {
				baf.append((byte) current);
			}
			jsonData = new String(baf.toByteArray());
		} catch (Exception e) {
			jsonData = e.getMessage();
		}
		LogHelper.i("" + jsonData);
		if (TextUtils.isEmpty(jsonData))
			return null;
		jsonData = jsonData.trim();
		HttpResponseBean value = new HttpResponseBean();
		try {
			JSONObject json = new JSONObject(jsonData);
			value.setType(0x002);
			value.setData(json.optJSONObject("data"));
			value.setStates(json.optInt("status"));
			value.setMessage(json.optString("message"));
			value.setStr(jsonData);
			return value;
		} catch (JSONException e) {
			LogHelper.e(e.getMessage());
			value.setStates(HttpResponseBean.HTTP_404);
			value.setMessage("服务器错误");
		}
		return value;
	}

	/**
	 * Interface definition for a callback to be invoked when a view is clicked.
	 */
	public interface HttpLoaderParserCallback {
		void onParser(JSONObject data, HttpResponseBean httpBean);
	}

}