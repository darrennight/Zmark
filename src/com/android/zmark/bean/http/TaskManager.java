package com.android.zmark.bean.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskManager {
	private ExecutorService mTaskPool;// 线程池
	private final int MIN_POOL_SIZE = 2;// 最小并发线程数

	public TaskManager() {
		mTaskPool = getExecutorService();
	}

	/**
	 * 添加线程任务
	 * 
	 * @param run
	 */
	public synchronized boolean addTask(Runnable run) {
		if (run != null) {
			mTaskPool.execute(run);
			return true;
		}
		return false;
	}

	public void destroyTaskPool() {
		if (mTaskPool != null) {
			mTaskPool.shutdown();
			try {
				if (!mTaskPool.awaitTermination(60, TimeUnit.SECONDS)) {
					mTaskPool.shutdownNow();
				}
			} catch (InterruptedException ie) {
				mTaskPool.shutdownNow();
				Thread.currentThread().interrupt();
			}
		}
		mTaskPool = null;
	}

	private ExecutorService getExecutorService() {
		int cpuNums = Runtime.getRuntime().availableProcessors();// 根据cpu数确定最大并发线程数
		if (cpuNums <= 0) {
			cpuNums = 1;
		}
		return Executors.newFixedThreadPool(cpuNums * MIN_POOL_SIZE);
	}
}
