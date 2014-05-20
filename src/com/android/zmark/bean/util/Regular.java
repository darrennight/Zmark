/**
 * @author ChenDu GOGO  Administrator
 *	时间   2012 下午3:06:49
 *  包名：com.gogotown.bean.util
            工程名：GoGoCity
 */
package com.android.zmark.bean.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * 类名 Regular.java 时间 2012-9-18 下午3:06:49 描述
 */
public class Regular {
	private static String EMAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";
	private static String CELLPHONE = "^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$";
	private static String USERNAME = "^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$"; // 只含有汉字、数字、字母、下划线不能以下划线开头和结尾

	private static boolean regularFormat(String arg0, String arg1) {
		Pattern pattern = Pattern.compile(arg0);
		Matcher mc = pattern.matcher(arg1);
		return mc.matches();
	}

	/*
	 * 验证邮箱
	 */
	public static boolean checkEmail(String arg0) {
		if (TextUtils.isEmpty(arg0))
			return false;
		return regularFormat(EMAIL, arg0);

	}

	public static boolean checkUserName(String arg0) {
		if (TextUtils.isEmpty(arg0))
			return false;
		return regularFormat(USERNAME, arg0);

	}

	public static boolean checkCellPhone(String arg0) {
		if (TextUtils.isEmpty(arg0))
			return false;
		// 匹配号码有：
		// 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		//
		// 联通：130、131、132、152、155、156、185、186、182
		//
		// 电信：133、153、180、189、（1349卫通）
		return regularFormat(CELLPHONE, arg0);
	}

	/*
	 * 判断是否是数字
	 */
	public static boolean checkNumer(String str) {
		if (TextUtils.isEmpty(str))
			return false;
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (isNum.matches()) {
			return true;
		} else {
			return false;
		}
	}

	// 身份证号码
	public static boolean checkIdCard(String arg0) {
		if (TextUtils.isEmpty(arg0))
			return false;
		Pattern p1 = Pattern.compile("\\d{15}|\\d{18}|\\d{17}+[[0-9],x,X]"); // 正则表达式
		Matcher m2 = p1.matcher(arg0); // 操作的字符串
		return m2.matches();

	}
	/*
	 * Url类型判断
	 */

	enum UrlType {
		everday, topic, merchant, social, other
	}

	public static UrlType checkUrl(String Url) {
		if (TextUtils.isEmpty(Url))
			return UrlType.other;
		// /#!/daily/866
		if (Url.contains("/#!/daily/"))
			return UrlType.everday;
		if (Url.contains("/#!/lady"))
			return UrlType.social;
		if (Url.contains("/#!/delicious"))
			return UrlType.social;
		if (Url.contains("/#!/leisure"))
			return UrlType.social;
		
		return UrlType.other;
	}
}
