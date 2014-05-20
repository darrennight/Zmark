package com.android.zmark.bean.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


import android.content.Context;
import android.os.Environment;

public class CachePath {
	private final static String splitpath = String.format(".%s",
			"zmark");

	public static String getCachMainPath() {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return Environment.getExternalStorageDirectory().getPath()
					+ File.separator + splitpath;
		}
		final List<String> dirNames = new LinkedList<String>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader("/proc/self/mounts"));
			String line;
			while ((line = reader.readLine()) != null) {
				final String[] parts = line.split("\\s+");
				if (parts.length >= 4
						&& parts[2].toLowerCase().indexOf("fat") >= 0
						&& parts[3].indexOf("rw") >= 0) {
					final File fsDir = new File(parts[1]);
					if (fsDir.isDirectory() && fsDir.canWrite()) {
						dirNames.add(fsDir.getPath());
					}
				}
			}
		} catch (Throwable e) {
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
				}
			}
		}

		for (String dir : dirNames) {
			if (dir.toLowerCase().indexOf("media") > 0) {
				return dir + File.separator + splitpath;
			}
		}
		if (dirNames.size() > 0) {
			return dirNames.get(0) + File.separator + splitpath;
		}
		return Environment.getExternalStorageDirectory().getPath()
				+ File.separator + splitpath;
	}

	public final static String photocachePic = getCachMainPath() + "/Image";

	public final static String cacheApk = getCachMainPath() + File.separator
			+ "UpdateApk";

	public final static String cacheVideo = getCachMainPath() + File.separator
			+ "Video";
	public final static String cacheTemp = getCachMainPath() + File.separator
			+ "TempCache";
	public final static String cacheBook = getCachMainPath() + File.separator
			+ "Book";

	public static File getImageCache(Context context) {
		if (!Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return context.getCacheDir();
		}
		File file = new File(getCachMainPath());
		if (!file.exists())
			file.mkdirs();
		return file;
	}
}
