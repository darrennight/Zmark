package com.android.zmark.bean.db;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 本地存储
 * 
 * @author yateng
 * 
 */
public class SP {

	public static SharedPreferences getSp(Context context) {
		return context.getSharedPreferences("setting", Context.MODE_PRIVATE);
	}

	public static SharedPreferences.Editor getSpEdit(Context context) {
		return context.getSharedPreferences("setting", Context.MODE_PRIVATE).edit();
	}

}
