package com.android.zmark.bean.http;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;


public class DownloadManager {

	private Stack<DownloadRef> mDownloadQueue = new Stack<DownloadRef>();
	private Queue<DownloadRef> mDownloadRequestQueue = new LinkedList<DownloadRef>();
	private Handler mDownloadHandler;
	private boolean isDownloaderIdle = true;

	private final int SHOW_PROGRESS = 1;
	private final int DOWNLOAD_OVER = 2;

	private DownloadListener mListener;

	public void download(DownloadRef downloadRef) {
		Iterator<DownloadRef> iterator = mDownloadRequestQueue.iterator();
		while (iterator.hasNext()) {
			if (iterator.next().encodeKey.equals(downloadRef.encodeKey)) {
				iterator.remove();
			}
		}
		// 添加请求
		mDownloadQueue.push(downloadRef);
		sendDownloadRequest();
	}

	private void sendDownloadRequest() {
		// 开启下载线程
		if (mDownloadHandler == null) {
			HandlerThread downloader = new HandlerThread("dowload_manager");
			downloader.start();
			mDownloadHandler = new DownloadHandler(downloader.getLooper());
		}

		// 发送请求
		if (isDownloaderIdle && mDownloadQueue.size() > 0) {
			DownloadRef ref = mDownloadQueue.pop();
			mListener = ref.listener;
			Message message = mDownloadHandler.obtainMessage(0, ref);
			mDownloadHandler.sendMessage(message);
			isDownloaderIdle = false;
			mDownloadRequestQueue.add(ref);
		} else {
			mListener = null;
		}
	}

	public void stopDownload() {
		// 清空请求队列
		mDownloadQueue.clear();
	}

	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_PROGRESS:
				if (mListener != null) {
					mListener.progress(msg.arg1);
				}
				break;
			case DOWNLOAD_OVER:
				if (mListener != null) {
					mListener.result(msg.arg1, (String) msg.obj);
				}
				isDownloaderIdle = true;
				sendDownloadRequest();
				break;
			}
		}
	};

	private class DownloadHandler extends Handler {

		public DownloadHandler(Looper looper) {
			super(looper);
		}

		@Override
		public void handleMessage(Message msg) {
			DownloadRef ref = (DownloadRef) msg.obj;
			boolean error = true;
			String message = "下载失败";
			try {
				if (ref.downloadUrl == null) {
					message = "未指定文件地址";
				} else if (ref.saveFile == null) {
					message = "未指定保存文件";
				} else if (!PhoneManage.isSdCardExit()) {
					message = "SD卡未插入，无法保存文件";
				} else if (ref.saveFile.exists()) {
					error = false;
					message = ref.saveFile.getAbsolutePath();
				} else {
					File dir = ref.saveFile.getParentFile();
					if (!dir.exists()) {
						dir.mkdirs();
					}
					URL url = new URL(ref.downloadUrl);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					int fileSize = connection.getContentLength();
					if (fileSize <= 0) {
						message = "无法获取文件大小";
					} else {
						InputStream inputStream = connection.getInputStream();
						if (inputStream == null) {
							message = "无法获取文件";
						} else {
							File saveFile = ref.saveFile;
							FileOutputStream outputStream = new FileOutputStream(saveFile);
							byte[] buffer = new byte[1024];
							int len = 0;
							int finished = 0;
							int lastProgress = 0;
							int progress = 0;
							while ((len = inputStream.read(buffer)) != -1) {
								outputStream.write(buffer, 0, len);
								finished += len;
								progress = (finished * 100) / fileSize;
								if (progress > lastProgress) {
									lastProgress = progress;
									Message pMessage = handler.obtainMessage(SHOW_PROGRESS, progress, 0);
									handler.sendMessage(pMessage);
								}
							}
							outputStream.flush();
							outputStream.close();
							inputStream.close();
							saveFile.setLastModified(System.currentTimeMillis());
							message = saveFile.getAbsolutePath();
							error = false;
						}
					}
					connection.disconnect();
				}
			} catch (MalformedURLException e) {
				message = "文件地址错误";
				e.printStackTrace();
			} catch (IOException e) {
				message = "下载失败";
				e.printStackTrace();
			} finally {
				int code = error ? DownloadListener.ERROR : DownloadListener.OK;
				Message rMessage = handler.obtainMessage();
				rMessage.what = DOWNLOAD_OVER;
				rMessage.arg1 = code;
				rMessage.obj = message;
				handler.sendMessage(rMessage);
			}
		}
	}
}
