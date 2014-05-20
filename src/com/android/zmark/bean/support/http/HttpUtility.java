package com.android.zmark.bean.support.http;

import java.util.Map;

import com.android.zmark.bean.support.asyncdrawable.FileDownloaderHttpHelper;
import com.android.zmark.bean.support.asyncdrawable.FileUploaderHttpHelper;

public class HttpUtility {

	private static HttpUtility httpUtility = new HttpUtility();

	private HttpUtility() {
	}

	public static HttpUtility getInstance() {
		return httpUtility;
	}

	public String executeNormalTask(HttpMethod httpMethod, String url,
			Map<String, String> param) throws ApiException {
		return new JavaHttpUtility().executeNormalTask(httpMethod, url, param);
	}

	public String executeNormalTask(HttpMethod httpMethod, String url,
			Map<String, String> param, String tag, String path)
			throws ApiException {
		return new JavaHttpUtility().executeNormalTask(httpMethod, url, param,
				tag, path);
	}

	public boolean executeDownloadTask(String url, String path,
			FileDownloaderHttpHelper.DownloadListener downloadListener) {
		return !Thread.currentThread().isInterrupted()
				&& new JavaHttpUtility().doGetSaveFile(url, path,
						downloadListener);
	}

	public boolean executeUploadTask(String url, Map<String, String> param,
			String path, String imageParamName,
			FileUploaderHttpHelper.ProgressListener listener)
			throws ApiException {
		return !Thread.currentThread().isInterrupted()
				&& new JavaHttpUtility().doUploadFile(url, param, path,
						imageParamName, listener);
	}
}
