package com.android.zmark.bean.support.http;

import java.util.LinkedList;

import org.codehaus.jackson.map.util.JSONPObject;
import org.json.JSONObject;

import android.os.Bundle;

public class HttpResponseBean<T> {

	public static final int HTTP_OK = 255, HTTP_ERROR = 0, HTTP_404 = 404,
			HTTP_500 = 500, HTTP_NOMORE = 3;

	private int type;
	private int states;
	private String message;
	private JSONObject data;
	private String jsonStr;
	private Bundle bundle;
	private boolean more = true;
	public LinkedList<T> mLinkedList;

	public String getStr() {
		return jsonStr;
	}

	public void setStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public boolean HasMore() {
		return more;
	}

	public void setMore(boolean more) {
		this.more = more;
	}

	public Bundle getBundle() {
		return bundle;
	}

	public void setBundle(Bundle bundle) {
		this.bundle = bundle;
	}

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStates() {
		return states;
	}

	public void setStates(int states) {
		this.states = states;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
