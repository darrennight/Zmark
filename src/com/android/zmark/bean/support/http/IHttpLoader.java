package com.android.zmark.bean.support.http;

import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.zmark.bean.LogHelper;

/**
 * oblivion
 */
public class IHttpLoader<T> extends

AbstractAsyncNetRequestTaskLoader<HttpResponseBean> {
	Map<String, String> param;
	private String url;
	private HttpMethod mHttpMethod = HttpMethod.Get;
	private Bundle args;
	HttpLoaderParserCallback callback;

	public IHttpLoader(Context context, String URL, Map<String, String> param,
			boolean fresh, HttpLoaderParserCallback callback) {
		this(context, URL, param, fresh);
		this.callback = callback;
	}

	public IHttpLoader(Context context, String URL, Map<String, String> param) {
		this(context, URL, param, false);
	}

	public IHttpLoader(Context context, String URL, Map<String, String> param,
			boolean isfresh) {
		super(context);
		this.param = param;
		this.url = URL;
		this.isFresh = isfresh;
	}

	public IHttpLoader(Context context, String Method,
			Map<String, String> param, HttpMethod mHttpMethod) {
		this(context, Method, param, mHttpMethod, null);
	}

	public IHttpLoader(Context context, String URL, Map<String, String> param,
			HttpMethod mHttpMethod, Bundle args) {
		super(context);
		this.param = param;
		this.url = URL;
		this.mHttpMethod = mHttpMethod;
		this.args = args;
	}

	String filetag;
	String filepath;

	public IHttpLoader(Context context, String URL, Map<String, String> param,
			String filetag, String filepath) {
		super(context);
		this.param = param;
		this.url = URL;
		this.filetag = filetag;
		this.filepath = filepath;
	}

	@Override
	protected HttpResponseBean loadData() throws ApiException {
		String jsonData = null;
		if (!TextUtils.isEmpty(filetag) && !TextUtils.isEmpty(filepath)) {
			jsonData = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.ANDPOST, url, param, filetag, filepath);
		} else {
			if (null != mHttpMethod) {
				jsonData = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, url, param, null, null);
			} else
				jsonData = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, url, param);
		}
		LogHelper.i("" + jsonData);
		if (TextUtils.isEmpty(jsonData))
			return null;
		jsonData = jsonData.trim();
		HttpResponseBean value = new HttpResponseBean();
		value.setBundle(args);
		value.setStr(jsonData);
		if (callback != null) {
			if (value.mLinkedList == null)
				value.mLinkedList = new LinkedList<Object>();
			callback.onParser(jsonData, value);
		}
		param = null;
		return value;
	}

	/**
	 * Interface definition for a callback to be invoked when a view is clicked.
	 */
	public interface HttpLoaderParserCallback {
		void onParser(Object data, HttpResponseBean httpBean);
	}

}