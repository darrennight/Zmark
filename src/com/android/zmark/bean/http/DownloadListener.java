package com.android.zmark.bean.http;

public interface DownloadListener {
	public static final int OK = 0;
	public static final int ERROR = 1;

	public void progress(int p);

	public void result(int code, String msg);
}
