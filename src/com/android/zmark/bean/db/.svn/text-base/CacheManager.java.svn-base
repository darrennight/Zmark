package com.android.zmark.bean.db;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

import android.os.Environment;
import android.text.TextUtils;

public class CacheManager {

	// define the cache root path
	public static String getRootPath() {

		// MLog.i("图片存储路径：" + Environment.getExternalStorageDirectory() + "/MicMgr");

		return Environment.getExternalStorageDirectory() + "/Tcscd";
	}

	public static String getImgFileName(String url) {
		return getRootPath() + File.separator + URLEncoder.encode(url).replace("*", "%%");
	}

	public static boolean isSdCardMounted() {
		return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
	}

	public static byte[] readCacheData(String url) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (TextUtils.isEmpty(url)) {
				return null;
			}
			FileInputStream fis = null;
			String fileName = getImgFileName(url);
			File file = new File(fileName);
			if (file.exists()) {
				try {
					fis = new FileInputStream(file);
					byte[] bytes = new byte[fis.available()];
					fis.read(bytes);
					return bytes;
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			file.delete();
		}
		return null;
	}

	public static Boolean writeCacheData(String url, byte[] bytes) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (TextUtils.isEmpty(url)) {
				return false;
			}

			String pathName = getRootPath();
			File dir = new File(pathName);
			if (!dir.exists()) {
				dir.mkdirs();
			}

			String cacheFileName = getImgFileName(url);
			File cacheFile = new File(cacheFileName);
			try {
				cacheFile.createNewFile();

				FileOutputStream fos = new FileOutputStream(cacheFile);
				fos.write(bytes);
				fos.close();

				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			cacheFile.delete();
		}
		return false;
	}

	public static boolean existCacheObjectData(String url) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (TextUtils.isEmpty(url)) {
				return false;
			}
			String fileName = getImgFileName(url);
			File file = new File(fileName);
			if (file.exists()) {
				return true;
			}
		}
		return false;
	}

	public static Boolean clearCacheObjectData(String url) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (TextUtils.isEmpty(url)) {
				return false;
			}

			String pathName = getRootPath();
			File file = new File(pathName);
			if (!file.exists()) {
				file.mkdirs();
				return true;
			}
			if (file.isFile()) {
				file.delete();
				file.mkdirs();
				return true;
			}

			String cacheFileName = getImgFileName(url);
			File cacheFile = new File(cacheFileName);
			if (cacheFile.exists()) {
				cacheFile.delete();
			}
			return true;
		}
		return false;
	}

	public static Boolean clearCacheObjectDataByPath(String pathName) {
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			if (TextUtils.isEmpty(pathName)) {
				return false;
			}
			String basePathName = getRootPath();
			File dir = new File(basePathName);
			if (!dir.exists()) {
				dir.mkdirs();
				return true;
			}

			String subPathName = getRootPath() + File.separator + pathName;
			File path = new File(subPathName);
			if (!path.exists()) {
				path.mkdirs();
				return true;
			}

			// delete all under the path as pathName specified
			return clearPathAndFile(subPathName, false);
		}
		return false;
	}

	private static boolean clearPathAndFile(String pathName, boolean delPath) {
		if (TextUtils.isEmpty(pathName)) {
			return false;
		}
		File path = new File(pathName);
		if (path.exists()) {
			return false;
		}
		if (path.isDirectory()) {
			String[] files = path.list();
			for (int i = 0; i < files.length; i++) {
				clearPathAndFile(pathName + File.separator + files[i], true);
			}
			if (delPath) {
				path.delete();
			}
		} else {
			path.delete();
		}
		return true;
	}

}
