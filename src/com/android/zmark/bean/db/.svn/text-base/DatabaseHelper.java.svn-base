package com.android.zmark.bean.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.zmark.ZmarkApplication;

public class DatabaseHelper extends SQLiteOpenHelper {
	private final static String TABLE_NAME = "data";
	public String configtable = "ZmarkConfig"; // 配置表
	public String createconfigtable = "create table " + configtable
			+ "(uid text," //
			+ "classify text," // 分类区域
			+ "showguide integer," // 每次检查是否显示过头条
			+ "menuepaper integer," // 第一次启动手势教程
			+ "orderhelp integer," // 预定教程
			+ "firstused integer," // 第一次安装
			+ "guideImgId integer," //
			+ "guidepageid integer," //
			+ "guidetype integer," //
			+ "guideNowImgId integer," //
			+ "guideurl text," //
			+ "guidetitle text," //
			+ "other integer);"; //
	public DatabaseHelper(Context context) {
		super(context, "cachedata.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_NAME
				+ " (id INTEGER PRIMARY key AUTOINCREMENT, key CHAR, cache_data VARCHAR, time LONG)");
		db.execSQL(createconfigtable);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		deleteAllTable(db);
		onCreate(db);
	}
	static DatabaseHelper  singleton;
	public static synchronized DatabaseHelper getInstance() {
		if (singleton == null) {
			singleton = new DatabaseHelper(ZmarkApplication.getInstance());
		}
		return singleton;
	}

	private void deleteAllTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + configtable);
	}
	/**
	 * 获取缓存数据
	 * 
	 * @param dataKey
	 * @return
	 */
	
	public String getCacheData(String dataKey) {
		String data = null;
		if (dataKey != null) {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db
					.rawQuery("SELECT cache_data FROM " + TABLE_NAME + " WHERE key = '" + dataKey + "'", null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				data = cursor.getString(cursor.getColumnIndex("cache_data"));
			}
			cursor.close();
			cursor = null;
			db.close();
		}
		return data;
	}

	/**
	 * 获取缓存数据时间
	 * 
	 * @param dataKey
	 * @return
	 */
	public long getCacheTime(String dataKey) {
		long time = 0;
		if (dataKey != null) {
			SQLiteDatabase db = getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT time FROM " + TABLE_NAME + " WHERE key = '" + dataKey + "'", null);
			if (cursor.getCount() > 0) {
				cursor.moveToFirst();
				time = cursor.getLong(cursor.getColumnIndex("time"));
			}
			cursor.close();
			cursor = null;
			db.close();
		}
		return time;
	}

	/**
	 * 删除缓存数据
	 * 
	 * @param dataKey
	 * @return
	 */
	public boolean deleteCacheData(String dataKey) {
		if (dataKey != null) {
			SQLiteDatabase db = getWritableDatabase();
			db.delete(TABLE_NAME, "key=?", new String[] { dataKey });
			db.close();
			return true;
		}
		return false;
	}

	/**
	 * 更新缓存数据
	 * 
	 * @param dataKey
	 * @param data
	 * @return
	 */
	public boolean updateCacheData(String dataKey, String data) {
		if (dataKey != null && data != null) {
			SQLiteDatabase db = getWritableDatabase();
			Cursor cursor = db.rawQuery("SELECT id FROM " + TABLE_NAME + " WHERE key = '" + dataKey + "'", null);
			boolean exit = cursor.getCount() > 0;
			cursor.close();
			cursor = null;
			ContentValues values = new ContentValues();
			values.put("cache_data", data);
			values.put("time", System.currentTimeMillis());
			if (exit) {
				db.update(TABLE_NAME, values, "key=?", new String[] { dataKey });
			} else {
				values.put("key", dataKey);
				db.insert(TABLE_NAME, null, values);
			}
			db.close();
			return true;
		}
		return false;
	}

}
