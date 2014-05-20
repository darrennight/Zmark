package com.android.zmark.bean.support.http;

import java.util.LinkedList;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;

import com.android.zmark.ApiServerPath;
import com.android.zmark.bean.LogHelper;

/**
 * oblivion
 */
public class HttpLoader<T> extends

AbstractAsyncNetRequestTaskLoader<HttpResponseBean> {
	Map<String, String> param;
	private String Method;
	private HttpMethod mHttpMethod = HttpMethod.Get;
	private Bundle args;
	HttpLoaderParserCallback callback;

	public HttpLoader(Context context, String Method,
			Map<String, String> param, boolean fresh,
			HttpLoaderParserCallback callback) {
		this(context, Method, param, fresh);
		this.callback = callback;
	}

	public HttpLoader(Context context, String Method, Map<String, String> param) {
		this(context, Method, param, false);
	}

	public HttpLoader(Context context, String Method,
			Map<String, String> param, boolean isfresh) {
		super(context);
		this.param = param;
		this.Method = Method;
		this.isFresh = isfresh;
	}

	public HttpLoader(Context context, String Method,
			Map<String, String> param, HttpMethod mHttpMethod) {
		this(context, Method, param, mHttpMethod, null);
	}

	public HttpLoader(Context context, String Method,
			Map<String, String> param, HttpMethod mHttpMethod, Bundle args) {
		super(context);
		this.param = param;
		this.Method = Method;
		this.mHttpMethod = mHttpMethod;
		this.args = args;
	}
	String filetag;
	String filepath;
	public HttpLoader(Context context, String Method,
			Map<String, String> param, String filetag, String filepath) {
		super(context);
		this.param = param;
		this.Method = Method;
		this.filetag = filetag;
		this.filepath = filepath;
	}

	@Override
	protected HttpResponseBean loadData() throws ApiException {
		String jsonData = null;
		if (param != null)
			param.put("method", Method);
		if (!TextUtils.isEmpty(filetag) && !TextUtils.isEmpty(filepath)) {
			jsonData = HttpUtility.getInstance().executeNormalTask(
					HttpMethod.ANDPOST, ApiServerPath.SERVERHOST, param,
					filetag, filepath);
		} else {
			if (null != mHttpMethod) {
				jsonData = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, ApiServerPath.SERVERHOST, param, null,
						null);
			} else
				jsonData = HttpUtility.getInstance().executeNormalTask(
						HttpMethod.Get, ApiServerPath.SERVERHOST, param);
		}
		LogHelper.i("" + jsonData);
		if (TextUtils.isEmpty(jsonData))
			return null;
		jsonData = jsonData.trim();
		HttpResponseBean value = new HttpResponseBean();
		value.setBundle(args);
		try {
			JSONObject json = new JSONObject(jsonData);
			value.setType(0x002);
			value.setData(json.optJSONObject("data"));
			value.setStates(json.optInt("status"));
			value.setMessage(json.optString("message"));
			if (value.mLinkedList == null)
				value.mLinkedList = new LinkedList<Object>();
			if (callback != null)
				callback.onParser(json.optJSONObject("data"), value);
			return value;
		} catch (JSONException e) {
			LogHelper.e(e.getMessage());
			value.setStates(HttpResponseBean.HTTP_404);
			value.setMessage("服务器错误");
		}
		param = null;
		return value;
	}

	/**
	 * Interface definition for a callback to be invoked when a view is clicked.
	 */
	public interface HttpLoaderParserCallback {
		void onParser(JSONObject data, HttpResponseBean httpBean);
	}

}