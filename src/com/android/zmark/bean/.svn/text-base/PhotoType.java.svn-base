/**
 * @author ChenDu GOGO  Administrator
 *	时间   2012 下午2:22:14
 *  包名：com.gogotown.util
            工程名：GoGoCity
 */
package com.android.zmark.bean;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * 类名 PhotoType.java
 */
public class PhotoType {
	public static final int TYPE_PHOTOCAMRA = 0x901; // 照相机
	public static final int TYPE_GALLERY = 0x902; // 本地相册
	public static final int TYPE_PHOTOCROP = 0x903; // 切图

	public static String getPhotoAbsPath(Context mContext, Uri uri) {
		ContentResolver cr = mContext.getContentResolver();
		Cursor cursor = cr.query(uri, null, null, null, null);
		cursor.moveToFirst();
		if (cursor.getColumnCount() >= 2)
			return cursor.getString(1);

		return null;
	}
}