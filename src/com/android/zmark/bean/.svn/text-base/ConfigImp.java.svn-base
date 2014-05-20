package com.android.zmark.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.android.zmark.ZmarkApplication;
import com.android.zmark.bean.db.DatabaseHelper;

public class ConfigImp {
	// 创建SQLiteDatabase对象
	private SQLiteDatabase db = null;
	// 相当于创建连接
	private DatabaseHelper dbhelper = null;
	private Context context;
	public String configtable = "ZmarkConfig"; // 配置表
	private static ConfigImp instance = null;

	private ConfigImp() {
		// do something
		context = ZmarkApplication.getInstance();
		dbhelper = DatabaseHelper.getInstance();
		if (db == null) {
			db = dbhelper.getWritableDatabase();
		}
	}

	public static synchronized ConfigImp getInstance() {
		if (instance == null) {
			instance = new ConfigImp();
		}
		return instance;
	}
	/**
	 * 是否是第一次使用软件 0表示第一次 其他则不是
	 */
	public boolean setFirstUsed(Context mContext, int flag) {
		try {
			ContentValues mContentValues = new ContentValues();
			mContentValues.put("firstused", flag);
			db.update(configtable, mContentValues, null, null);
			return true;
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return false;
	}
	/**
	 * 得到是否是第一次使用
	 */
	public int getFirstUsed(Context context) {
		int flag = 0;
		try {
			String sql = "SELECT firstused FROM " + configtable;
			Cursor cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				flag = cursor.getInt(0);
				if (!cursor.isClosed()) {
					cursor.close();
				}
			} else {
				if (!cursor.isClosed()) {
					cursor.close();
				}
			}
		} catch (SQLiteException e) {
			e.printStackTrace();
		}
		return flag;
	}
}
