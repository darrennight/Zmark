package com.android.zmark.bean;

import android.content.Context;
import android.text.TextUtils;

import com.android.zmark.ZmarkApplication;

public class SettingUtility {
	private static final String tag = "SettingUtility";
	private static final String FIRSTSTART = "firststart";
	private static final String USERINFO = "userinfo";
	private static final String USERID = "userid";
	private static final String TOKEN = "token";
	private static final String PHONE = "phone";
	private static final String LOCATION = "location";
	private static final String FIRSTLANCHER = "firstlancher";
	private static final String LOCATION_CITY = "city";

	private SettingUtility() {
		super();
	}

	public static void setUserInfo(String flag) {
		if (TextUtils.isEmpty(flag)) {
			SettingHelper.setEditor(getContext(), USERINFO, "");
			return;
		}
		SettingHelper.setEditor(getContext(), USERINFO, flag);
	}

	private static Context getContext() {
		return ZmarkApplication.getInstance();
	}

	public static String getUserInfo() {
		return SettingHelper.getSharedPreferences(getContext(), USERINFO, "");
	}

	public static String getLocationInfo() {
		return SettingHelper.getSharedPreferences(getContext(), LOCATION, "");
	}

	public static String getLocationCity() {
		return SettingHelper.getSharedPreferences(getContext(), LOCATION_CITY,
				"");
	}

	public static void setLocationInfo(String location) {
		SettingHelper.setEditor(getContext(), LOCATION, location);
	}

	public static void setLocationCity(String city) {
		SettingHelper.setEditor(getContext(), LOCATION_CITY, city);
	}

	public static void setDefaultUserId(String id) {
		SettingHelper.setEditor(getContext(), USERID, id);
	}

	public static void setDefaultPhone(String phone) {
		SettingHelper.setEditor(getContext(), PHONE, phone);
	}

	public static void setFirstLancher(boolean flag) {
		SettingHelper.setEditor(getContext(), FIRSTLANCHER, flag);
	}

	public static void setDefaultToken(String token) {
		SettingHelper.setEditor(getContext(), TOKEN, token);
	}

	public static boolean isFirstLancher() {
		return SettingHelper.getSharedPreferences(getContext(), FIRSTLANCHER,
				true);

	}

	public static String getDefaultToken() {
		return SettingHelper.getSharedPreferences(getContext(), TOKEN, "");
	}

	public static String getDefaultPhone() {
		return SettingHelper.getSharedPreferences(getContext(), PHONE, "");
	}

	public static String getDefaultUserId() {
		return SettingHelper.getSharedPreferences(getContext(), USERID, "-1");
	}

}
