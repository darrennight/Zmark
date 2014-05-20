package com.android.zmark.bean.http;

import java.io.File;

import com.android.zmark.bean.code.MD5;

public class DownloadRef {
	DownloadListener listener;
	String encodeKey;
	String downloadUrl;
	File saveFile;

	public DownloadRef(String url, File file, DownloadListener l) {
		downloadUrl = url;
		saveFile = file;
		encodeKey = MD5.md5Encode16(url);
		listener = l;
	}
}
