package com.android.zmark.bean.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class ISFirstUntil {

	private final static String IS_FRIST_USE="is_frist_use";
	private final static String FIRST_USE_FLAG="frist_use_flag";
	
	public static boolean isFirstUse(Context context){
		SharedPreferences shared=context.getSharedPreferences(IS_FRIST_USE, Activity.MODE_PRIVATE);
		return shared.getBoolean(FIRST_USE_FLAG, true);
		
	}
	
	public static void saveFirstUseInfo(Context context){
		SharedPreferences shared= context.getSharedPreferences(IS_FRIST_USE, Activity.MODE_PRIVATE);
		SharedPreferences.Editor editer=shared.edit();
		editer.putBoolean(FIRST_USE_FLAG, false);
		editer.commit();
	}
	
	
//	public static String isGetUserInfo(Context context){
//		SharedPreferences shared=context.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
//		return shared.getString("string", null);
//		
//	}
	
	public static void saveGetUseInfo(Context context,String str){
		//手机号,密码,手机唯一码,Id,唯一Token
		SharedPreferences shared= context.getSharedPreferences("userinfo", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editer=shared.edit();
		editer.putString("string",str);
		editer.commit();
	}

	
	public static String isGetProductInfo(Context context){
		SharedPreferences shared=context.getSharedPreferences("productinfo", Activity.MODE_PRIVATE);
		return shared.getString("string", null);
		
	}
	
	public static void saveProductInfo(Context context,String str){
		SharedPreferences shared= context.getSharedPreferences("productinfo", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editer=shared.edit();
		editer.putString("string",str);
		editer.commit();
	}
	
	
	public static String isScoreInfo(Context context){
		SharedPreferences shared=context.getSharedPreferences("scoreinfo", Activity.MODE_PRIVATE);
		return shared.getString("string", null);
		
	}
	
	public static void saveScoreInfo(Context context,String str){
		//用户名 等级 积分
		SharedPreferences shared= context.getSharedPreferences("scoreinfo", Activity.MODE_PRIVATE);
		SharedPreferences.Editor editer=shared.edit();
		editer.putString("string",str);
		editer.commit();
	}
	
	
}
