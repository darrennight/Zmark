package com.android.zmark.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.pulltorefresh.widgets.PullToRefreshBase;
import com.android.pulltorefresh.widgets.PullToRefreshBase.OnRefreshListener;
import com.android.pulltorefresh.widgets.PullToRefreshListView;
import com.android.zmark.R;
import com.android.zmark.bean.UIRunableControl;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public abstract class AbstractListActivity<T> extends AbstractActivity
		implements OnItemClickListener, OnRefreshListener, UIRunableControl {

	protected final static int LOADER_LIST_DATA = 0x111;
	protected ListView actualListView;
	protected PullToRefreshListView mPullRefreshListView;
	protected View progressView;
	protected TextView emptyView, messageView;
	protected ProgressBar progressBar;
	private int mIndex = 0;
	private static int mRefreshIndex = 0;
	protected boolean mIsStart = true, more = true;
	@Override
	public void showEmptyMessage(String text) {
		if (emptyView != null && !TextUtils.isEmpty(text)
				&& !emptyView.getText().toString().equals(text))
			emptyView.setText(text);
		if (emptyView != null && emptyView.getVisibility() != View.VISIBLE)
			emptyView.setVisibility(View.VISIBLE);
		if (actualListView != null && actualListView.getEmptyView() == null)
			actualListView.setEmptyView(emptyView);
	}

	@Override
	public void hideEmptyMessage() {
		if (emptyView != null && emptyView.getVisibility() == View.VISIBLE)
			emptyView.setVisibility(View.GONE);
	}

	@Override
	public void setEmptyMessage(String text) {
		if (progressView != null)
			progressView.setVisibility(View.VISIBLE);
		if (emptyView != null && !TextUtils.isEmpty(text))
			emptyView.setText(text);
		if (emptyView != null)
			emptyView.setVisibility(View.VISIBLE);
		if (actualListView != null)
			actualListView.setEmptyView(emptyView);
	}

	@Override
	public void showProgressBar(String text) {
		if (progressView != null)
			progressView.setVisibility(View.VISIBLE);

		if (messageView != null && !TextUtils.isEmpty(text))
			messageView.setText(text);
	}

	@Override
	public void hideProgressBar(String text) {
		if (progressView != null)
			progressView.setVisibility(View.GONE);
		if (messageView != null && !TextUtils.isEmpty(text))
			messageView.setText(text);
	}

	@Override
	public void showLoadingView(String text) {
		if (progressView != null)
			progressView.setVisibility(View.VISIBLE);
		if (messageView != null && !TextUtils.isEmpty(text))
			messageView.setText(text);
	}

	public void netWorkLoadError(String text) {
		if (progressView != null)
			progressView.setVisibility(View.GONE);
		if (progressBar != null)
			progressBar.setVisibility(View.GONE);
		if (messageView != null)
			messageView.setVisibility(View.VISIBLE);
		if (messageView != null && !TextUtils.isEmpty(text))
			messageView.setText(text);
		else
			messageView.setText(getResources().getString(
					R.string.net_error_retry));
		initAdapterOrNotifyDataSet(true);
	}

	@Override
	public void netWorkLoadError() {
		netWorkLoadError(null);
	}

	@Override
	public void hideLoadingView() {
		if (progressView != null
				&& progressView.getVisibility() == View.VISIBLE)
			progressView.setVisibility(View.GONE);
	}

	protected void initListView(int emptyViewId, int messageViewId,
			int progressViewId, int progressBarId, int listViewId) {
		initListView(emptyViewId, messageViewId, progressViewId, progressBarId,
				listViewId, true);
	}

	ViewGroup.MarginLayoutParams params;
//	public int LISTVIEW_SELECTOR_DEFAULT = R.drawable.listview_selector_select;
//	public int LISTVIEW_SELECTOR_SOCIAL = R.drawable.listview_selector_select2;

	protected void initListView(int emptyViewId, int messageViewId,
			int progressViewId, int progressBarId, int listViewId,
			boolean startFresh) {
		if (emptyViewId != 0)
			emptyView = (TextView) findViewById(emptyViewId);
		if (messageViewId != 0)
			messageView = (TextView) findViewById(messageViewId);
		if (messageViewId != 0)
			progressView = findViewById(progressViewId);
		if (progressBarId != 0)
			progressBar = (ProgressBar) findViewById(progressBarId);
		if (listViewId != 0)
			mPullRefreshListView = (PullToRefreshListView) findViewById(listViewId);
		if (mPullRefreshListView == null)
			throw new NullPointerException("mPullRefreshListView can not empty");
		mPullRefreshListView.setPullLoadEnabled(false);
		mPullRefreshListView.setScrollLoadEnabled(true);
		actualListView = mPullRefreshListView.getRefreshableView();
		mPullRefreshListView.setOnRefreshListener(this);
		actualListView.setOnItemClickListener(this);
		actualListView.setFadingEdgeLength(0);
		actualListView.setCacheColorHint(0);
//		actualListView.setDivider(getResources().getDrawable(
//				R.drawable.ic_tag_horizontal_line));
//		actualListView.setSelector(getResources().getDrawable(
//				LISTVIEW_SELECTOR_DEFAULT));
		params = (MarginLayoutParams) mPullRefreshListView.getLayoutParams();
		if (startFresh)
			mPullRefreshListView.doPullRefreshing(true, 500);

	}

	protected void setListViewLeftMargin(int flag0) {
		if (params != null)
			params.leftMargin = flag0;
	}

	protected void setListViewtMargin(int left, int top, int right, int bottom) {
		if (params != null) {
			params.leftMargin = left;
			params.rightMargin = right;
			params.bottomMargin = bottom;
			params.topMargin = top;

		}
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

	protected String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	// /加载更多
	protected abstract void loadMoreData(Object object);

	protected abstract void loadNewData(Object object);

	protected void initAdapterOrNotifyDataSet(boolean hasmore) {
		onPullDownUpRefreshComplete(hasmore);
	}

	private void onPullDownUpRefreshComplete(boolean hasmore) {
		// 刷新结束的时候
		// 这两个的顺序不能交换，注意!!!!!
		onPullDownUpRefreshComplete();
		mPullRefreshListView.setHasMoreData(hasmore);

	}

	protected void onPullDownUpRefreshComplete() {
		// 刷新结束的时候
		mPullRefreshListView.onPullDownRefreshComplete();
		mPullRefreshListView.onPullUpRefreshComplete();
		mPullRefreshListView.setLastUpdatedLabel(getTime());
	}

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
