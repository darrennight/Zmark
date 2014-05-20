package com.android.zmark.bean.http;

import android.os.Handler;
import android.os.Message;

public abstract class TaskThreadHandler extends Handler {
	public static final int OK = 0;
	public static final int ERROR = 1;
	private String resMsg;

	@Override
	public void handleMessage(Message msg) {
		if (msg != null) {
			result(msg.what, resMsg, msg.obj);
		}
	}

	public void goToResult(int code, String resMsg, Object object) {
		this.resMsg = resMsg;
		Message message = obtainMessage();
		message.what = code;
		message.obj = object;
		sendMessage(message);
	}

	public abstract void resultInThread(String resMsg);

	public abstract void result(int code, String resMsg, Object object);
}
