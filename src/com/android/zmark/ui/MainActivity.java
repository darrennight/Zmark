package com.android.zmark.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.android.zmark.R;
import com.android.zmark.ZmarkApplication;
import com.android.zmark.bean.ApkUpdate;
import com.android.zmark.bean.CommonLog;
import com.android.zmark.bean.FragmentControlCenter;
import com.android.zmark.bean.FragmentModel;
import com.android.zmark.bean.IntentInterface;
import com.android.zmark.bean.LogFactory;
import com.android.zmark.bean.SettingUtility;
import com.android.zmark.bean.util.Utility;
import com.android.zmark.ui.fragment.MenuLeftFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 * 首页 基类
 * 
 * Activity
 * 
 * @author yangwenfang
 * 
 */
public class MainActivity extends SlidingFragmentActivity implements
		OnClickListener {

	private static final CommonLog log = LogFactory.createLog();

	private String mTitle;
	private Fragment mContent;

	private ImageView mLeftIcon;
	private ImageView mRightIcon;
	private TextView mTitleTextView;
	Context context;

	ZmarkApplication app;
	private FragmentControlCenter mControlCenter;
	ApkUpdate mApkUpdate;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this;
		mControlCenter = FragmentControlCenter.getInstance(this);
		setAlias();
		setupViews();
		// 检测apk更新
		if (mApkUpdate == null)
			mApkUpdate = new ApkUpdate(this, Utility.DisplayPixels(this));
		mApkUpdate.checkApk(false);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	private void setAlias() {

		String alias = SettingUtility.getDefaultUserId();
		if (TextUtils.isEmpty(alias)) {
			return;
		}
		// 调用JPush API设置Alias
		JPushInterface.setAliasAndTags(getApplicationContext(), alias, null);
	}

	private void setupViews() {

		setContentView(R.layout.main_slidemenu_layout);

		// initActionBar();

		initSlideMenu();

	}

	private MenuLeftFragment menuFragment;

	private void initSlideMenu() {
		FragmentModel fragmentModel = mControlCenter.getBlogFragmentModel();
		switchContent(fragmentModel);

		if (app == null) {
			app = ZmarkApplication.getInstance();
		}
		SlidingMenu sm = getSlidingMenu();
		app.sm = sm;
		sm.setMode(SlidingMenu.LEFT_RIGHT);

		setBehindContentView(R.layout.left_menu_frame);
		sm.setSlidingEnabled(true);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		// getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (menuFragment == null)
			menuFragment = new MenuLeftFragment(context);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.left_menu_frame, menuFragment).commit();
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setBehindScrollScale(0);
		sm.setFadeDegree(0.25f);
	}

	public void intentSetting() {
		if (menuFragment == null)
			return;
		menuFragment.changgeitem(3);
		menuFragment.goSetFragment();
	}

	public void setCollectNum(int num) {
		if (menuFragment == null)
			return;
		menuFragment.setCollectNum(num);

	}

	public void getHistoryNum() {
		if (menuFragment == null)
			return;
		menuFragment.getHistoryNum();
	}

	public void intentSearch() {
		startActivity(new Intent(this, SearchActivity.class));
	}

	public void intentHomePage() {
		if (menuFragment == null)
			return;
		if (menuFragment.flag == 1) {
		} else {
			menuFragment.changgeitem(0);
			menuFragment.goBoKeFragment();
		}
	}

	public void intentActivity() {
		if (!ZmarkApplication.getInstance().userIsOline()) {
			startActivity(new Intent(context, LoginActivity.class));
		} else {
			IntentInterface.IntentAll(this, null, ActivitiesListActivity.class,
					-1);
		}
	}

	public void switchContent(final FragmentModel fragment) {
		mTitle = fragment.mTitle;
		mContent = fragment.mFragment;

		getSupportFragmentManager().beginTransaction()
				.replace(R.id.content_frame, mContent).commit();
		Handler h = new Handler();
		h.postDelayed(new Runnable() {
			public void run() {
				getSlidingMenu().showContent();
			}
		}, 50);

		// mTitleTextView.setText(mTitle);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.iv_left_icon:
			toggle();
			break;
		case R.id.iv_right_icon:
			break;

		}
	}

}
