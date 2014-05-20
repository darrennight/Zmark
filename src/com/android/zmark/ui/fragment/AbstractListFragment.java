package com.android.zmark.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.pulltorefresh.widgets.PullToRefreshBase;
import com.android.pulltorefresh.widgets.PullToRefreshBase.OnRefreshListener;
import com.android.pulltorefresh.widgets.PullToRefreshListView;
import com.android.zmark.R;
import com.android.zmark.bean.LogHelper;
import com.android.zmark.bean.UIRunableControl;

public abstract class AbstractListFragment<T> extends AbstractFragment
		implements OnItemClickListener, OnRefreshListener, UIRunableControl {
	protected String tag = AbstractListFragment.class.getName();
	protected PullToRefreshListView mPullRefreshListView;
	protected ListView actualListView;
	protected LayoutInflater inflater;
	protected View contentView, progressView;
	protected TextView emptyView, messageView;
	protected ProgressBar progressBar;
	protected boolean more = true;
	protected boolean start = true;
	protected Dialog webDialog;
	protected int page = 1, size = 10;

	public AbstractListFragment() {
		super();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		LogHelper.i(tag, "onCreateView");
		this.inflater = inflater;
		contentView = inflater.inflate(R.layout.listview_layout, null);
		return contentView;

	}

	/**
	 * 没有数据时的提示信息
	 */
	@Override
	public void showEmptyMessage(String text) {
		if (!TextUtils.isEmpty(text)
				&& !emptyView.getText().toString().equals(text))
			emptyView.setText(text);
		if (emptyView.getVisibility() != View.VISIBLE)
			emptyView.setVisibility(View.VISIBLE);
		if (actualListView.getEmptyView() == null)
			actualListView.setEmptyView(emptyView);
	}

	@Override
	public void hideEmptyMessage() {
		if (emptyView.getVisibility() == View.VISIBLE)
			emptyView.setVisibility(View.GONE);
	}

	@Override
	public void setEmptyMessage(String text) {
		progressView.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(text))
			emptyView.setText(text);
		emptyView.setVisibility(View.VISIBLE);
		actualListView.setEmptyView(emptyView);
	}

	@Override
	public void showProgressBar(String text) {
		progressBar.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(text))
			messageView.setText(text);
	}

	@Override
	public void hideProgressBar(String text) {
		progressBar.setVisibility(View.GONE);
		if (!TextUtils.isEmpty(text))
			messageView.setText(text);
	}

	@Override
	public void showLoadingView(String text) {
		if (progressView.getVisibility() != View.VISIBLE)
			progressView.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(text))
			messageView.setText(text);
	}

	@Override
	public void hideLoadingView() {
		if (progressView.getVisibility() == View.VISIBLE)
			progressView.setVisibility(View.GONE);
	}

	@Override
	public void netWorkLoadError(String text) {
		progressView.setVisibility(View.GONE);
		progressView.setVisibility(View.VISIBLE);
		if (!TextUtils.isEmpty(text))
			messageView.setText(text);
	}

	@Override
	public void netWorkLoadError() {
		progressBar.setVisibility(View.GONE);
		progressView.setVisibility(View.VISIBLE);

		messageView.setText(getResources().getString(R.string.net_error_retry));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		initListView();
	}
	private void initListView() {
		emptyView = (TextView) contentView.findViewById(R.id.empty);
		messageView = (TextView) contentView.findViewById(R.id.tv_message);
		progressView = contentView.findViewById(R.id.include_progress);
		progressBar = (ProgressBar) contentView.findViewById(R.id.pb_progress);
		mPullRefreshListView = (PullToRefreshListView) contentView
				.findViewById(R.id.listView);
		mPullRefreshListView.setPullLoadEnabled(false);
		mPullRefreshListView.setScrollLoadEnabled(true);
		actualListView = mPullRefreshListView.getRefreshableView();
		mPullRefreshListView.setOnRefreshListener(this);
		actualListView.setOnItemClickListener(this);
		actualListView.setFadingEdgeLength(0);
		actualListView.setCacheColorHint(0);
		// actualListView.setSelector(getResources().getDrawable(
		// LISTVIEW_SELECTOR_DEFAULT));
		// actualListView.setDivider(getResources().getDrawable(
		// R.drawable.ic_tag_horizontal_line));
		// mPullRefreshListView.setRefreshing(false); 这句是一进去显示上面刷新的圈圈
		if (start)
			mPullRefreshListView.doPullRefreshing(true, 500);
		params = (MarginLayoutParams) mPullRefreshListView.getLayoutParams();
	}

	ViewGroup.MarginLayoutParams params;

	protected void setListViewLeftMargin(int flag0) {
		if (params != null)
			params.leftMargin = flag0;
	}

	protected void setListViewRightMargin(int flag0) {
		if (params != null)
			params.rightMargin = flag0;
	}

	protected void setListViewTopMargin(int flag0) {
		if (params != null)
			params.topMargin = flag0;
	}

	protected void setListViewBottomMargin(int flag0) {
		if (params != null)
			params.bottomMargin = flag0;
	}

	protected void setListViewMargin(int left, int top, int right, int bottom) {
		if (params != null) {
			params.bottomMargin = bottom;
			params.topMargin = top;
			params.leftMargin = left;
			params.rightMargin = right;
		}

	}

	protected void setListViewMargin(int flag) {
		if (params != null) {
			params.bottomMargin = flag;
			params.topMargin = flag;
			params.leftMargin = flag;
			params.rightMargin = flag;
		}

	}

	// /加载更多
	protected abstract void loadMoreData(Object object);

	protected void initAdapterOrNotifyDataSet(boolean hasmore) {
		onPullDownUpRefreshComplete(hasmore);
	}

	private void onPullDownUpRefreshComplete(boolean hasmore) {
		// 刷新结束的时候
		onPullDownUpRefreshComplete();
		mPullRefreshListView.setHasMoreData(hasmore);
	}

	protected void onPullDownUpRefreshComplete() {
		// 刷新结束的时候
		mPullRefreshListView.onPullDownRefreshComplete();
		mPullRefreshListView.onPullUpRefreshComplete();
		mPullRefreshListView.setLastUpdatedLabel(getTime());
	}

	protected String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	protected boolean mIsStart = true;

	@Override
	public void onPullDownToRefresh(PullToRefreshBase refreshView) {
		mIsStart = true;
		loadNewData(null);
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase refreshView) {
		mIsStart = false;
		loadMoreData(null);
	}
}
