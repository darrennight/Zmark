package com.android.zmark.bean.support.http;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;

/**
 * User: qii Date: 13-5-15
 */
public abstract class AbstractAsyncNetRequestTaskLoader<T> extends
		AsyncTaskLoader<AsyncTaskLoaderResult<T>> {
	protected static Lock lock = new ReentrantLock();
	private AsyncTaskLoaderResult<T> result;
	private Bundle args;
	public boolean isFresh;
	public AbstractAsyncNetRequestTaskLoader(Context context) {
		super(context);
	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		if (result == null) {
			forceLoad();
		} else {
			deliverResult(result);
		}
	}

	@Override
	public AsyncTaskLoaderResult<T> loadInBackground() {

		T data = null;
		ApiException exception = null;

		try {
			data = loadData();
		} catch (ApiException e) {
			exception = e;
		}

		result = new AsyncTaskLoaderResult<T>();
		result.data = data;
		result.exception = exception;
		result.args = this.args;

		return result;
	}

	protected abstract T loadData() throws ApiException;

	public void setArgs(Bundle args) {
		if (result != null) {
			throw new IllegalArgumentException(
					"can't setArgs after loader executes");
		}
		this.args = args;
	}

}
