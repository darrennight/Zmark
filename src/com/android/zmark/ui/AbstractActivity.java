package com.android.zmark.ui;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;

import com.android.zmark.R;
import com.android.zmark.bean.support.BitmapDownloader;
import com.android.zmark.wigets.LoadingDialog;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

public abstract class AbstractActivity extends FragmentActivity {
	protected final static int LOADER_INITDATA = 0x100;
	protected String tag = this.getClass().getName();
	public final int LOADING = 0, LOADSUCCESS = 1, LOADFAILURE = -1;
	public Context mContext;
	protected BitmapDownloader commander;
	private LoadingDialog mProgressDialog;
	protected View mProgressView;
	protected Dialog pDialogView;

	public View getProgressView() {
		return mProgressView;
	}

	public BitmapDownloader getCommander() {
		return commander;
	}

	public void ShowDialog(String title) {
		ShowDialog(title, true);
	}

	public void ShowDialog(String title, boolean cancleable) {
		if (TextUtils.isEmpty(title)) {
			title = "加载中...";
		}
		if (mProgressDialog == null) {
			mProgressDialog = new LoadingDialog(this, title, cancleable);
			mProgressDialog.setCancelable(cancleable);

		} else {
			mProgressDialog.setCancelable(cancleable);
			mProgressDialog.setText(title);
		}
		if (mProgressDialog.isShowing()) {
			mProgressDialog.setText(title);
		} else
			mProgressDialog.show();

	}

	public void DismissDialog() {
		if (mProgressDialog == null || !mProgressDialog.isShowing())
			return;
		if (mContext == null)
			return;
		mProgressDialog.dismiss();
	}

	public void SetDialogMessage(String title) {
		if (mProgressDialog == null)
			return;
		mProgressDialog.setText(title);
	}

	public boolean DialogisShowing() {
		if (mProgressDialog == null)
			return false;
		return mProgressDialog.isShowing();
	}

	public void onBackPressed(int backid) {
		if (backid != -1)
			findViewById(backid).setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
	}

	protected ImageLoader imageLoader = null;
	protected DisplayImageOptions options = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		if (android.os.Build.VERSION.SDK_INT > 8) {
			/*
			 * 线程监测
			 */
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
					.detectDiskReads().detectDiskWrites().detectNetwork()
					.penaltyLog().build());
			/*
			 * 虚拟机监测
			 */
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
					.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
					.penaltyLog() // 打印logcat
					.penaltyDeath().build());
		}
		mContext = this;
		commander = BitmapDownloader.getInstance();
		super.onCreate(savedInstanceState);
		BitmapDownloader.refreshThemePictureBackground();
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.main_baby)
				.showImageOnFail(R.drawable.main_baby).cacheInMemory(true)
				.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(300)).build();
	}

}
