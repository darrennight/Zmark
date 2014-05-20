package com.android.zmark.bean.http;

import java.io.File;
import java.util.HashMap;

import com.android.zmark.bean.content.FrameConstant;


public class Parameter {
	private HashMap<String, Object> mParams;
	private String mRequestUrl;

	public Parameter(String url) {
		mParams = new HashMap<String, Object>();
		mRequestUrl = FrameConstant.URL_EXTRA + url;
	}

	public void setParam(String key, String value) {
		if (key != null && value != null) {
			mParams.put(key, value);
		}
	}

	public void setParam(String key, File file) {
		if (key != null && file != null) {
			mParams.put(key, file);
		}
	}

	public HashMap<String, Object> getParamMap() {
		return mParams;
	}

	public String getRequestUrl() {
		return mRequestUrl;
	}
}
