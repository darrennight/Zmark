package com.android.zmark.bean.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TaskManger {

	
	private ExecutorService mTaskPool;//线程池
	private final int MIN_POOL_SIZE = 2;// 最小并发线程数
	
	public TaskManger() {
		mTaskPool=  getExecutorService();
	}
	
	/**
	 * 添加任务
	 * @return
	 */
	
	public synchronized boolean addTask(Runnable runnable){
		if(runnable!=null){
			mTaskPool.execute(runnable);
			return true;
		}
		
		return false;
	}
	
	
	/**
	 * 销毁线程池
	 * @return
	 */
	
	public void destroyTaskPool(){
		if(mTaskPool!=null){
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
		
		mTaskPool=null;
	}
	
	public ExecutorService getExecutorService(){
		int cupnum=Runtime.getRuntime().availableProcessors();
		if(cupnum<0){
			cupnum=1;
		}
		return Executors.newFixedThreadPool(cupnum*MIN_POOL_SIZE);
	}
}
