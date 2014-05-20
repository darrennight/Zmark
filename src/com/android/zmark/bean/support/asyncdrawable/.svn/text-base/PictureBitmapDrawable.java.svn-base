package com.android.zmark.bean.support.asyncdrawable;

import java.lang.ref.WeakReference;

import android.graphics.drawable.ColorDrawable;

/**
 * User: qii Date: 12-9-5
 */
public class PictureBitmapDrawable extends ColorDrawable {
	private final WeakReference<IPictureWorker> bitmapDownloaderTaskReference;

	public PictureBitmapDrawable(IPictureWorker bitmapDownloaderTask) {
		bitmapDownloaderTaskReference = new WeakReference<IPictureWorker>(
				bitmapDownloaderTask);
	}

	public IPictureWorker getBitmapDownloaderTask() {
		return bitmapDownloaderTaskReference.get();
	}
}
