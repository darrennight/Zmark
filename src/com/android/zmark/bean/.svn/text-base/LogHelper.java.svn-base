package com.android.zmark.bean;

import android.util.Log;

public class LogHelper {

	public static boolean isDebug = true;
	private static final String TAG = "com.android.zmark";

	public static int d(String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.d(TAG, msg);
		}
		return 0;

	}

	public static int d(String tag, String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.d(tag, msg);
		}
		return 0;
	}

	public static int i(String tag, String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.i( tag, msg);
		}
		return 0;
	}

	public static int i(String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.i(TAG + ".", msg);
		} else {
			return 0;
		}
	}

	public static int e(String tag, String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.e(tag, msg);
		}
		return 0;
	}

	public static int e(String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.e(TAG, msg);
		}
		return 0;
	}

	public static int w(String tag, String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.w(tag, msg);
		}
		return 0;
	}

	public static int v(String tag, String msg) {
		if (null == msg)
			return 0;
		if (isDebug) {
			return Log.v(tag, msg);
		}
		return 0;
	}

	public static int w(String tag, Exception e) {
		if (null == e)
			return 0;
		if (isDebug) {
			return Log.w(TAG + "." + tag, e.getMessage());
		}
		return 0;
	}

	public static int e(String tag, String msg, Exception e) {
		if (null == msg && null == e)
			return 0;
		if (isDebug) {
			return Log.e(tag, msg + "." + e.getMessage());
		}
		return 0;
	}
}
