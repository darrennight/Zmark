package com.android.zmark.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;
import com.android.zmark.bean.IntentInterface;
import com.android.zmark.wigets.OnViewChangeListener;
import com.android.zmark.wigets.PullScrollLayout;

@SuppressWarnings("unused")
public class LogoSwitchActivity extends Activity implements
		OnViewChangeListener {

	private PullScrollLayout mScrollLayout;
	private int count;
	private int currentItem;
	private Button startBtn;
	private RelativeLayout mainRLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.logoswitch_activity);
		initView();
	}

	private void initView() {
		mScrollLayout = (PullScrollLayout) findViewById(R.id.ScrollLayout);
		mainRLayout = (RelativeLayout) findViewById(R.id.mainRLayout);
		startBtn = (Button) findViewById(R.id.startBtn);
		startBtn.setOnClickListener(onClick);
		findViewById(R.id.registerBtn).setOnClickListener(onClick);
		count = mScrollLayout.getChildCount();
		currentItem = 0;
		mScrollLayout.SetOnViewChangeListener(this);
		if (!hasShortcut()) {
			new AlertDialog.Builder(this)
					.setMessage("创建桌面图标？")
					.setNegativeButton("取消", null)
					.setNeutralButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									addShortcut();

								}
							}).show();
		}
	}

	private View.OnClickListener onClick = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == R.id.startBtn) {
				Intent intent = new Intent(LogoSwitchActivity.this,
						MainActivity.class);
				intent.putExtra("flag", 1); // 1为普通intent,2为推送
				LogoSwitchActivity.this.startActivity(intent);
				LogoSwitchActivity.this.finish();
			} else {
				Intent intent = new Intent(LogoSwitchActivity.this,
						MainActivity.class);
				intent.putExtra("flag", 1); // 1为普通intent,2为推送
				LogoSwitchActivity.this.startActivity(intent);

				if (!ZmarkApplication.getInstance().userIsOline()) {
					IntentInterface.IntentAll(LogoSwitchActivity.this, null,
							LoginActivity.class, -1);
				}
				LogoSwitchActivity.this.finish();
			}
		}
	};

	@Override
	public void OnViewChange(int position) {
		setcurrentPoint(position);
	}

	private void setcurrentPoint(int position) {
		if (position < 0 || position > count - 1 || currentItem == position) {
			return;
		}
		currentItem = position;
	}

	public boolean hasShortcut() {
		boolean result = false;
		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = this.getPackageManager();
			title = pm.getApplicationLabel(
					pm.getApplicationInfo(this.getPackageName(),
							PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
		}

		final String uriStr;
		if (android.os.Build.VERSION.SDK_INT < 8) {
			uriStr = "content://com.android.launcher.settings/favorites?notify=true";
		} else {
			uriStr = "content://com.android.launcher2.settings/favorites?notify=true";
		}
		final Uri CONTENT_URI = Uri.parse(uriStr);
		final Cursor c = this.getContentResolver().query(CONTENT_URI, null,
				"title=?", new String[] { title }, null);
		if (c != null && c.getCount() > 0) {
			result = true;
		}
		return result;
	}

	public void addShortcut() {
		Intent shortcut = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");

		Intent shortcutIntent = this.getPackageManager()
				.getLaunchIntentForPackage(this.getPackageName());
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		// 获取当前应用名称
		String title = null;
		try {
			final PackageManager pm = this.getPackageManager();
			title = pm.getApplicationLabel(
					pm.getApplicationInfo(this.getPackageName(),
							PackageManager.GET_META_DATA)).toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 快捷方式名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
		// 不允许重复创建（不一定有效）
		shortcut.putExtra("duplicate", false);
		// 快捷方式的图标
		Parcelable iconResource = Intent.ShortcutIconResource.fromContext(this,
				R.drawable.icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconResource);
		this.sendBroadcast(shortcut);
	}

}