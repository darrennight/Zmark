package com.android.zmark;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpConnection {

	/**
	 * 下面是一个开源的异步网路请求接口，可以使用 API地址爲：http://loopj.com/android-async-http/
	 */
	public static final int TIMEOUT = 30 * 1000;

	public static void AsyncGet(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient client = new AsyncHttpClient(TIMEOUT);
		if (null == url)
			throw new NullPointerException("url can not null");
		if (params == null)
			client.get(url, responseHandler);
		else
			client.get(url, params, responseHandler);
	}

	public static void AsyncGet(String url,
			AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient client = new AsyncHttpClient(TIMEOUT);
		if (null == url)
			throw new NullPointerException("url can not null");
		client.get(url, responseHandler);
	}

	public static void AsyncPost(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		AsyncHttpClient client = new AsyncHttpClient(TIMEOUT);
		if (null == url)
			throw new NullPointerException("url can not null");
		if (params == null)
			client.post(url, responseHandler);
		else
			client.post(url, params, responseHandler);
	}

	public static void AsyncPost(String url,
			AsyncHttpResponseHandler responseHandler) {
		if (null == url)
			throw new NullPointerException("url can not null");
		AsyncHttpClient client = new AsyncHttpClient(TIMEOUT);
		client.post(url, responseHandler);
	}
}
