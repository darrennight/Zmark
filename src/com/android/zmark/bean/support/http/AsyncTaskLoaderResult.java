package com.android.zmark.bean.support.http;

import android.os.Bundle;

/**
 */
public class AsyncTaskLoaderResult<T> {
	public T data;
	public ApiException exception;
	public Bundle args;
}
