package com.android.zmark.bean.http;

import java.io.File;

public class NetWork {
	private static TaskManager mTaskManager;
	private static DownloadManager mDownloadManager;

	public static void doPost(Parameter p, TaskHandler h) {
		addTask(new RequestRunnable(p, h, RequestRunnable.POST));
	}

	public static void doPost(Parameter p, TaskThreadHandler h) {
		addTask(new RequestRunnable(p, h, RequestRunnable.POST));
	}

	public static void doGet(Parameter p, TaskHandler h) {
		addTask(new RequestRunnable(p, h, RequestRunnable.GET));
	}

	public static void doGet(Parameter p, TaskThreadHandler h) {
		addTask(new RequestRunnable(p, h, RequestRunnable.GET));
	}

	public static void download(String url, File saveFile, DownloadListener l) {
		if (mDownloadManager == null) {
			mDownloadManager = new DownloadManager();
		}
		DownloadRef ref = new DownloadRef(url, saveFile, l);
		mDownloadManager.download(ref);
	}

	public static void destroy() {
		if (mTaskManager != null) {
			mTaskManager.destroyTaskPool();
		}
		mTaskManager = null;
	}

	private static void addTask(Runnable run) {
		if (mTaskManager == null) {
			mTaskManager = new TaskManager();
		}
		mTaskManager.addTask(run);
	}

}
