/**
 * @author ChenDu GOGO  Administrator
 *	时间   2012 下午3:04:14
 *  包名：com.gogotown.bean
            工程名：GoGoCity
 */
package com.android.zmark.bean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.android.zmark.R;
import com.android.zmark.bean.support.CachePath;
import com.android.zmark.bean.support.http.AsyncTaskLoaderResult;
import com.android.zmark.bean.support.http.FHttpLoader;
import com.android.zmark.bean.support.http.HttpResponseBean;
import com.android.zmark.bean.util.Utility;
import com.android.zmark.entities.ApkGrade;
import com.android.zmark.wigets.ApkUpdateDialog;
import com.android.zmark.wigets.ProgressBarDialog;

/**
 * 类名 ApkUpdate.java 时间 2012-7-23 下午3:04:14 描述
 */
public class ApkUpdate<T> {
	private final int LOADER_CHECK = 0x001;
	/**
	 * 0代表没有更新 1代表有更新 2代表更新出现异常
	 */
	String TAG = "ApkUpdate";
	private Activity mContext;
	// private JSONObject updateObject = null;
	private ProgressBarDialog mApkDownLoadDialog;
	// ///////////////////////////
	public final static int TAG_APKNEW = 0x00001; // apk更新
	public final static int TAG_APKDOWNPOS = 0x00002; // apk更新位置
	public final static int TAG_APKSIZE = 0x00003; // apk大小
	public final static int TAG_UPDATEAPKSUC = 0x11; // 升级成功
	public final static int TAG_UPDATEAPKFAILE = 0x10; // 升级失败
	public final static int TAG_UPDATEAPKEXCEPTION = 0x13; // 升级异常
	public final static int TAG_NOUPDATE = 0x12; // 没有更新
	int EXCEPTION = -1, FAILED = 0, SCU = 1, NOTUPDATE = 2;
	private boolean isAutoHint = true; // 没有更新，是否自动提示
	private int[] disylayPix;
	onCheckVersionListener versionlistener;

	/**
	 * @param mContext
	 * @param mHandler
	 */
	public ApkUpdate(Activity mContext, int[] disylayPix) {
		super();
		this.mContext = mContext;
		this.disylayPix = disylayPix;
	}

	public interface onCheckVersionListener {
		public void onNewVresion(int version, String verso);

		public void onNoNewVresion();
	}

	public onCheckVersionListener getOnCheckVersionListener() {
		return versionlistener;
	}

	public void setOnCheckVersionListener(onCheckVersionListener versionlistener) {
		this.versionlistener = versionlistener;
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (mContext == null)
				return;
			switch (msg.what) {
			case ApkUpdate.TAG_APKNEW: // apk更新
				final ApkGrade apk = (ApkGrade) msg.obj;
				final ApkUpdateDialog daligo = new ApkUpdateDialog(mContext,
						disylayPix);
				daligo.setTitle("发现新版本");
				daligo.setText(apk.getUpdateInfo() + "\n");
				daligo.setPositiveButton("立即更新", new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						if (!Utility.isSDExist()) {
							Toast.makeText(mContext, "SD卡不存在，请检查SD卡",
									Toast.LENGTH_SHORT).show();
							daligo.dismiss();
							return;
						}
						if (Utility.getSDAvailaleSpace() < 10) {
							Toast.makeText(mContext, "SD卡剩余存储空间不足",
									Toast.LENGTH_SHORT).show();
							daligo.dismiss();
							return;
						}
						/**
						 * 如果APK已经存在，那么直接安装
						 */
						String filePath = checkApkExsit(apk.getApkUrl());
						if (filePath != null) {
							IntentInterface
									.IntentApkInstall(mContext, filePath);
							if (mApkDownLoadDialog != null
									&& mApkDownLoadDialog.isShowing())
								mApkDownLoadDialog.dismiss();
							daligo.dismiss();
							return;
						}
						// 开始更新
						if (mApkDownLoadDialog == null) {
							mApkDownLoadDialog = new ProgressBarDialog(mContext);
							mApkDownLoadDialog.setMessage("更新中...");
							mApkDownLoadDialog.setCancelable(false);
						}
						mApkDownLoadDialog.show();
						// 开始更新
						new apkUpdateAsyncTask().execute(apk.getApkUrl());
						daligo.dismiss();

					}
				});
				if (apk.isCompulsory() == true) // 强制更新
				{
					daligo.setCancelable(false);

				} else
					daligo.setNegativeButton("取消", null);
				daligo.show();

				break;
			case ApkUpdate.TAG_APKSIZE: // apk大小
				mApkDownLoadDialog
						.setDMax(Integer.parseInt(msg.obj.toString()));
				break;
			case ApkUpdate.TAG_APKDOWNPOS: // apk大小
				mApkDownLoadDialog.setDProgress(Integer.parseInt(msg.obj
						.toString()));
				break;
			case ApkUpdate.TAG_UPDATEAPKSUC: // 升级成功
				IntentInterface.IntentApkInstall(mContext, msg.obj.toString());
				if (mApkDownLoadDialog != null
						&& mApkDownLoadDialog.isShowing())
					mApkDownLoadDialog.dismiss();
				break;
			case ApkUpdate.TAG_UPDATEAPKFAILE: // 升级失败
				if (mApkDownLoadDialog != null
						&& mApkDownLoadDialog.isShowing())
					mApkDownLoadDialog.dismiss();
				Toast.makeText(mContext,
						"下载失败" + "请访问 http://www.gogo.cn/ 最新版本!", 1).show();
				break;
			case ApkUpdate.TAG_UPDATEAPKEXCEPTION: // 升级异常
				if (!isAutoHint)
					return;
				if (versionlistener != null) {
					versionlistener.onNoNewVresion();
					return;
				}
				Toast.makeText(mContext, "检查更新失败!", 1).show();
				break;
			case ApkUpdate.TAG_NOUPDATE: // 没有更新
				if (!isAutoHint)
					return;
				if (mApkDownLoadDialog != null
						&& mApkDownLoadDialog.isShowing())
					mApkDownLoadDialog.dismiss();
				if (versionlistener != null) {
					versionlistener.onNoNewVresion();
					return;
				}
				Toast.makeText(mContext, "已经是最新版本", 1).show();
				break;

			default:
				break;
			}
		}

	};

	public String checkApkExsit(String apkUrl) {
		if (!Utility.isExist(CachePath.cacheApk))
			Utility.mkDirs(CachePath.cacheApk);
		final String fileName = Utility.GetFileName(apkUrl, "apk"); // 获取文件名
		if (fileName == null)
			return null;
		String filePath = CachePath.cacheApk + File.separator + fileName;
		if (Utility.isExist(filePath) && checkApkRight(filePath))
			return filePath;
		return null;
	}

	/**
	 * 检查apk是否是完整的
	 */
	public boolean checkApkRight(String filePath) {
		boolean result = false;
		try {
			PackageManager pm = mContext.getPackageManager();
			PackageInfo info = pm.getPackageArchiveInfo(filePath,
					PackageManager.GET_ACTIVITIES);
			if (info != null) {
				result = true;
			}
		} catch (Exception e) {
			result = false;
		}
		return result;
	}

	/**
	 * 检查apk
	 */
	public void checkApk(boolean isAutoHint) {
		this.isAutoHint = isAutoHint;
		if (mContext instanceof FragmentActivity)
			((FragmentActivity) mContext).getSupportLoaderManager()
					.restartLoader(LOADER_CHECK, null, httpCallback);
		return;
	}

	// private void checkApk() {
	// int versionCode = Utility.getVersionCode(mContext);
	// RequestParams params = new RequestParams();
	// params.put("versionid", String.valueOf(versionCode));
	// params.put("sofewareid", "1");
	// AsyncHttpResponseHandler responseHandler = new AsyncHttpResponseHandler()
	// {
	// @Override
	// public void onFailure(Throwable error, String content) {
	// super.onFailure(error, content);
	// }
	//
	// @Override
	// public void onSuccess(String content) {
	// super.onSuccess(content);
	// if (TextUtils.isEmpty(content))
	// return;
	// try {
	// JSONObject json = new JSONObject(content);
	// if (json != null) {
	// JSONObject obj = json.optJSONObject("data");
	// if (obj != null) {
	// ApkGrade apk = new ApkGrade();
	// apk.setApkUrl(obj.optString("ApkUrl"));
	// apk.setCompulsory(obj.optBoolean("IsCompulsory"));
	// apk.setUpdateInfo(obj.optString("UpdateInfo"));
	// apk.setVerName(obj.optString("VerName"));
	// apk.setVerCode(Integer.parseInt(obj
	// .optString("VerCode")));
	// if (versionlistener != null) {
	// versionlistener.onNewVresion(apk.getVerCode(),
	// apk.getVerName());
	// }
	// Message msg = mHandler.obtainMessage(TAG_APKNEW,
	// apk);
	// mHandler.sendMessage(msg);
	// return;
	// }
	// }
	//
	// } catch (JSONException e) {
	// LogHelper.e(e.getMessage());
	// }
	// if (isAutoHint)
	// Toast.makeText(mContext, "获取更新信息失败", Toast.LENGTH_SHORT)
	// .show();
	//
	// }
	//
	// };
	// HttpConnection.AsyncPost(ApiServerPath.SYSYTEM_APKUPDATE, params,
	// responseHandler);
	// }

	/**
	 * 返回 -1,为更新失败，成功为apk的大小
	 */
	public Object upGradeApk(String apkUrl, Handler mHandler) {

		try {
			if (apkUrl == null)
				return FAILED;
			if (!Utility.isExist(CachePath.cacheApk))
				Utility.mkDir(CachePath.cacheApk);
			final String fileName = Utility.GetFileName(apkUrl, "apk"); // 获取文件名
			if (fileName == null)
				return FAILED;
			if (downLoadFile(apkUrl, CachePath.cacheApk + File.separator
					+ fileName, mHandler))
				return CachePath.cacheApk + File.separator + fileName;
			else
				return FAILED;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return EXCEPTION;
	}

	public boolean downLoadFile(String urlstr, String path, Handler mHandler) {

		try {
			int mProgressPos = 0;
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(30 * 1000);
			connection.setConnectTimeout(45 * 1000);
			InputStream input = connection.getInputStream();
			int size = connection.getContentLength();
			mHandler.sendMessage(mHandler.obtainMessage(TAG_APKSIZE, size));
			OutputStream output = null;
			try {
				File file = new File(path); // 创建文件
				try {
					if (!file.exists())
						file.createNewFile();
					else
						file.delete(); // 原先有的删掉，说明原来的apk包不完整
				} catch (Exception e) {
					e.printStackTrace();
				}

				output = new FileOutputStream(file);
				byte[] buffer = new byte[4 * 1024];
				int temp;
				while ((temp = input.read(buffer)) != -1) {
					output.write(buffer, 0, temp);
					mProgressPos += temp;
					mHandler.sendMessage(mHandler.obtainMessage(TAG_APKDOWNPOS,
							mProgressPos));
				}
				output.flush();
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					output.close();
				} catch (Exception e2) {
					e2.printStackTrace();
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	protected LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>> httpCallback = new LoaderManager.LoaderCallbacks<AsyncTaskLoaderResult<T>>() {

		@Override
		public Loader<AsyncTaskLoaderResult<T>> onCreateLoader(int arg0,
				Bundle arg1) {

			// 接口名称 app更新
			// 地址 /Api/Api/checkupdate
			// 请求参数 sofewareid * versionid *
			// 请求方式 POST
			int versionCode = Utility.getVersionCode(mContext);
			Map<String, String> param = new HashMap<String, String>();
			param.put("versionid", String.valueOf(versionCode));
			param.put("sofewareid", "1");

			return new FHttpLoader(mContext,
					"http://app10014.yunbosoft.com:9090/update.txt");
		}

		@Override
		public void onLoadFinished(Loader<AsyncTaskLoaderResult<T>> loder,
				AsyncTaskLoaderResult<T> result) {
			T data = result != null ? result.data : null;
			if (data == null) { // 网络加载失败
				if (isAutoHint)
					Toast.makeText(
							mContext,
							mContext.getResources().getString(
									R.string.net_unconnect), Toast.LENGTH_SHORT)
							.show();
				return;
			}
			HttpResponseBean responseBean = (HttpResponseBean) data;
			if (responseBean.getStates() == HttpResponseBean.HTTP_OK) {
				// 检查更新成功///
				try {
					ApkGrade apk = new ApkGrade();
					JSONObject obj = new JSONObject(responseBean.getStr())
							.optJSONObject("data");
					apk.setApkUrl(obj.optString("ApkUrl"));
					apk.setCompulsory(obj.optBoolean("IsCompulsory"));
					apk.setUpdateInfo(obj.optString("UpdateInfo"));
					apk.setVerName(obj.optString("VerName"));
					apk.setVerCode(Integer.parseInt(obj.optString("VerCode")));
					if (apk.getVerCode() > Utility.getVersionCode(mContext)
							&& !TextUtils.isEmpty(apk.getApkUrl())) {
						Message msg = mHandler.obtainMessage(TAG_APKNEW, apk);
						if (versionlistener != null) {
							versionlistener.onNewVresion(apk.getVerCode(),
									apk.getVerName());
						}
						mHandler.sendMessage(msg);
					} else {
						if (isAutoHint)
							Toast.makeText(
									mContext,
									TextUtils.isEmpty(responseBean.getMessage()) ? "获取更新信息失败"
											: responseBean.getMessage(),
									Toast.LENGTH_SHORT).show();
					}

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				if (isAutoHint)
					Toast.makeText(
							mContext,
							TextUtils.isEmpty(responseBean.getMessage()) ? "获取更新信息失败"
									: responseBean.getMessage(),
							Toast.LENGTH_SHORT).show();
			}
		}

		@Override
		public void onLoaderReset(Loader<AsyncTaskLoaderResult<T>> arg0) {

		}
	};

	class apkUpdateAsyncTask extends AsyncTask<Object, Integer, Object> {

		@Override
		protected Object doInBackground(Object... params) {

			if (params != null && params[0] instanceof String) { // 开始更新
				return upGradeApk(params[0].toString(), mHandler);
			}
			return params;

		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onPostExecute(Object result) {
			if (result instanceof Integer) {
				try {
					if (Integer.parseInt(result.toString()) == FAILED)
						mHandler.sendEmptyMessage(TAG_UPDATEAPKFAILE);
					else if (Integer.parseInt(result.toString()) == NOTUPDATE)
						mHandler.sendEmptyMessage(TAG_NOUPDATE);
					else if (Integer.parseInt(result.toString()) == EXCEPTION)
						mHandler.sendEmptyMessage(TAG_UPDATEAPKEXCEPTION);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (result instanceof String) {
				String filePath = result.toString();
				if (filePath.endsWith("apk"))
					mHandler.sendMessage(mHandler.obtainMessage(
							TAG_UPDATEAPKSUC, filePath));
			}

		}
	}
}