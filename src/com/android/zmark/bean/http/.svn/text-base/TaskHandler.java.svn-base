package com.android.zmark.bean.http;

import android.os.Handler;
import android.os.Message;

public abstract class TaskHandler extends Handler {
	public static final int OK = 0;
	public static final int ERROR = 1;

	@Override
	public void handleMessage(Message msg) {
		if (msg != null) {
			result(msg.what, (String) msg.obj);
		}
	}

	public void goToResult(int code, String resMsg) {
		Message message = obtainMessage();
		message.what = code;
		message.obj = resMsg;
		sendMessage(message);
	}

	public abstract void result(int code, String resMsg);
}
