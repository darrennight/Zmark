package com.android.zmark.bean.http;

import java.lang.reflect.Field;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

public class PhoneManage {

	/**
	 * sd卡是否插入
	 * 
	 * @return
	 */
	public static boolean isSdCardExit() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	/**
	 * 获取SD卡根目录路径
	 * 
	 * @return
	 */
	public static String getSdCardRootPath() {
		return Environment.getExternalStorageDirectory().getPath();
	}

	/**
	 * 获取app安装根目录
	 * 
	 * @param context
	 * @return
	 */
	public static String getAppRootPath(Context context) {
		return "/data/data/" + context.getPackageName();
	}

	public static int getAvailableSize(String rootPath) {
		StatFs inStatFs = new StatFs(rootPath);
		long blockSize = inStatFs.getBlockSize();
		long availaBlocks = inStatFs.getAvailableBlocks();
		int availaSize = (int) (availaBlocks * blockSize / 1024 / 1024);
		return availaSize;
	}

	public static int getStatusBarHigh(Context context) { // 获取状态栏高度
		Class<?> c = null;
		Object obj = null;
		Field field = null;
		int x = 0, sbar = 0;
		try {
			c = Class.forName("com.android.internal.R$dimen");
			obj = c.newInstance();
			field = c.getField("status_bar_height");
			x = Integer.parseInt(field.get(obj).toString());
			sbar = context.getResources().getDimensionPixelSize(x);
			return sbar;
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return 0;
	}
}
