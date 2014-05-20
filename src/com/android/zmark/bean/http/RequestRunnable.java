package com.android.zmark.bean.http;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Iterator;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import com.android.zmark.bean.content.FrameConstant;


public class RequestRunnable implements Runnable {
	public static final int POST = 10;
	public static final int GET = 11;
	private Parameter mParameter;
	private TaskHandler mTaskHandler;
	private TaskThreadHandler mTaskThreadHandler;
	private int mRequestType;

	public RequestRunnable(Parameter p, TaskHandler h, int type) {
		mParameter = p;
		mTaskHandler = h;
		mRequestType = type;
	}

	public RequestRunnable(Parameter p, TaskThreadHandler h, int type) {
		mParameter = p;
		mTaskThreadHandler = h;
		mRequestType = type;
	}

	@Override
	public void run() {
		boolean error = true;
		String message = "访问失败";
		try {
			if (mParameter != null) {
				String url = mParameter.getRequestUrl();
				if (url == null || "".equals(url)) {
					message = "URL错误";
				} else {
					HttpUriRequest request;
					if (mRequestType == GET) {
						request = getGetRequest(url);
					} else {
						request = getPostRequest(url);
						if (request == null) {
							message = "上传的文件不存在";
						}
					}
					if (request != null) {
						HttpClient httpClient = new DefaultHttpClient();
						httpClient.getParams().setParameter("http.protocol.content-charset",
								FrameConstant.DEFAULT_HTTP_CHARSET);
						httpClient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
						HttpResponse response = httpClient.execute(request);
						if (response != null) {
							int statusCode = response.getStatusLine().getStatusCode();
							if (statusCode == HttpURLConnection.HTTP_OK) {
								HttpEntity httpEntity = response.getEntity();
								message = EntityUtils.toString(httpEntity, FrameConstant.DEFAULT_HTTP_CHARSET);
								httpEntity.consumeContent();
								Log.e("response", message);
								error = false;
							} else {
								message = "服务器异常";
							}
						} else {
							message = "服务器无响应";
						}
						httpClient.getConnectionManager().shutdown();
					}
				}
			} else {
				message = "参数错误";
			}
		} catch (UnsupportedEncodingException e) {
			message = "编码错误";
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			message = "服务器连接失败";
			e.printStackTrace();
		} catch (IOException e) {
			message = "无网络连接";
			e.printStackTrace();
		} finally {
			if (mTaskHandler != null) {
				int code = error ? TaskHandler.ERROR : TaskHandler.OK;
				mTaskHandler.goToResult(code, message);
			} else if (mTaskThreadHandler != null) {
				if (error) {
					mTaskThreadHandler.goToResult(TaskHandler.ERROR, message, null);
				} else {
					mTaskThreadHandler.resultInThread(message);
				}
			}
		}
	}

	private HttpUriRequest getPostRequest(String url) throws UnsupportedEncodingException {
		boolean fileError = false;
		MultipartEntity mEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
		Iterator<java.util.Map.Entry<String, Object>> paramIterator = mParameter.getParamMap().entrySet().iterator();
		while (paramIterator.hasNext()) {
			java.util.Map.Entry<String, Object> entry = paramIterator.next();
			String key = entry.getKey();
			if (entry.getValue() instanceof File) {
				File file = (File) entry.getValue();
				if (file.exists()) {
					FileBody fileBody = new FileBody(file);
					mEntity.addPart(key, fileBody);
				} else {
					fileError = true;
					break;
				}
			} else if (entry.getValue() instanceof String) {
				String value = (String) entry.getValue();
				StringBody stringBody = new StringBody(value, Charset.forName(FrameConstant.DEFAULT_HTTP_CHARSET));
				mEntity.addPart(encodeString(key), stringBody);
			}
		}
		if (!fileError) {
			HttpPost httpPost = new HttpPost(url);
			httpPost.setEntity(mEntity);
			return httpPost;
		} else {
			return null;
		}
	}

	private HttpUriRequest getGetRequest(String url) throws UnsupportedEncodingException {
		StringBuilder sbUri = new StringBuilder();
		sbUri.append(url);
		if (!url.endsWith("?")) {
			sbUri.append("?");
		}
		Iterator<java.util.Map.Entry<String, Object>> paramIterator = mParameter.getParamMap().entrySet().iterator();
		boolean isFirst = true;
		while (paramIterator.hasNext()) {
			java.util.Map.Entry<String, Object> entry = paramIterator.next();
			if (entry.getValue() instanceof String) {
				String key = entry.getKey();
				String value = (String) entry.getValue();
				if (isFirst) {
					isFirst = false;
				} else {
					sbUri.append("&");
				}
				sbUri.append(encodeString(key));
				sbUri.append("=");
				sbUri.append(encodeString(value));
			}
		}

		return new HttpGet(sbUri.toString());
	}

	private String encodeString(String str) throws UnsupportedEncodingException {
		return URLEncoder.encode(str, FrameConstant.DEFAULT_HTTP_CHARSET);
	}

}
