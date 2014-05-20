/** 手机阅读器 android版
 *   v 1.0.0
 *
 * 作者:杨雯方
 * 时间: 2014  2014年3月27日   下午9:58:13
 * 描述:TODO
 */
package com.android.zmark.bean.support.http;

import android.content.res.Resources;
import android.text.TextUtils;

/**
 * 手机阅读器 android版 v 1.0.0
 * 
 * 作者:杨雯方 时间: 2014 2014年3月27日 下午9:58:13 描述:TODO
 */
public class ApiException extends Exception {
	private static final long serialVersionUID = 1L;
	private String error_msg;
	private String oriError;
	private int error_code;
	private int error_action;

	public String getError() {

		String result;

		if (!TextUtils.isEmpty(error_msg)) {
			result = error_msg;
		} else {

			String name = "code" + error_code;
			try {
				result = "com.android.zmark";
			} catch (Resources.NotFoundException e) {

				if (!TextUtils.isEmpty(oriError)) {
					result = oriError;
				} else {
					result = "未知错误代码" + error_code;
				}
			}
		}

		return result;
	}

	public ApiException(String msg, String oriError, int error_code,
			int error_action) {
		super();
		this.error_msg = msg;
		this.oriError = oriError;
		this.error_code = error_code;
		this.error_action = error_action;
	}

	@Override
	public String getMessage() {
		return getError();
	}

	public void setError_code(int error_code) {
		this.error_code = error_code;
	}

	public int getError_action() {
		return error_action;
	}

	public void setError_action(int error_action) {
		this.error_action = error_action;
	}

	public int getError_code() {
		return error_code;
	}

	public ApiException() {

	}

	public ApiException(String detailMessage) {
		error_msg = detailMessage;
	}

	public ApiException(String detailMessage, Throwable throwable) {
		error_msg = detailMessage;
	}

	public void setOriError(String oriError) {
		this.oriError = oriError;
	}

}
