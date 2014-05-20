package com.android.zmark.wigets;



import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.zmark.R;

/**
 * com.app.android.widget.PullListView
 * 
 * @author lc
 * 
 */
public class PullListView extends ListView implements OnScrollListener {
	private static final float OFFSET_RADIO = 1.8f;
	private static final int AUTO_SCROLL_DISTANCE = 100;
	private static final int AUTO_REFRESH_SCROLL = 0;
	private static final int AUTO_LOADMORE_SCROLL = 1;
	private static final int AUTO_SCROLL_UPDATE = 2;

	private static final int NONE = 0;
	private static final int RELESE_TO_REFRESH = 1;
	private static final int REFRESHING = 2;
	private static final int REFRESH_NORMAL = 3;
	private static final int REFRESH_NONE = 4;
	private static final int RELESE_TO_LOADMORE = 5;
	private static final int PUSH_TO_LOADMORE_ANIM = 6;
	private static final int LOADINGMORE = 7;
	private static final int PUSH_TO_LOADMORE = 8;
	private static final int LOADMORE_NONE = 9;

	private View footer;
	private View header;
	private TextView tv_refresh_time;
	private TextView tv_refresh_msg;
	private ImageView iv_refresh_arrow;
	private ProgressBar pb_refresh;
	private TextView tv_load_msg;
	private ImageView iv_load_arrow;
	private ProgressBar pb_loadmore;
	private RelativeLayout rl_loadmore;

	private int refreshBarPadding = 0;
	private int loadBarPadding = 0;
	private float lastY;
	private int status = NONE;
	private int lastStatus = NONE;
	private boolean isFirstItemVisible = false;
	private boolean isLastItemVisible = false;
	private boolean hasSetFooter = false;
	private boolean isAutoScroll = false;

	private String mLoadMoreMsg;
	private int mMinPadding;
	private int mMaxPadding;
	private int mRefreshingPadding;
	private Animation mRotateUpAnim;
	private Animation mRotateDownAnim;
	private OnActionListener mListener;

	public PullListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public PullListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public PullListView(Context context) {
		super(context);
		init(context, null);
	}

	@SuppressLint("NewApi")
	private void init(Context context, AttributeSet attrs) {
		final int apiLevel = Build.VERSION.SDK_INT;
		if (apiLevel >= 9) {
			setOverScrollMode(View.OVER_SCROLL_NEVER);
		}
		DisplayMetrics metrics = getResources().getDisplayMetrics();

		mMinPadding = -(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, metrics);
		mMaxPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 110, metrics);
		mRefreshingPadding = mMaxPadding + mMinPadding;
		refreshBarPadding = mMinPadding;
		loadBarPadding = 0;

		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		header = mInflater.inflate(R.layout.pull_listview_header, null);
		tv_refresh_msg = (TextView) header.findViewById(R.id.tv_refresh_msg);
		tv_refresh_time = (TextView) header.findViewById(R.id.tv_refresh_time);
		iv_refresh_arrow = (ImageView) header.findViewById(R.id.iv_refresh_arrow);
		pb_refresh = (ProgressBar) header.findViewById(R.id.pb_refresh);
		tv_refresh_time.setText("刷新时间");
		tv_refresh_msg.setText(R.string.pull_listview_refresh_normal);
		header.setPadding(0, refreshBarPadding, 0, 0);
		addHeaderView(header);

		footer = mInflater.inflate(R.layout.pull_listview_footer, null);
		tv_load_msg = (TextView) footer.findViewById(R.id.tv_load_msg);
		iv_load_arrow = (ImageView) footer.findViewById(R.id.iv_load_arrow);
		pb_loadmore = (ProgressBar) footer.findViewById(R.id.pb_loadmore);
		rl_loadmore = (RelativeLayout) footer.findViewById(R.id.rl_loadmore);
		mLoadMoreMsg = getResources().getString(R.string.pull_listview_loadmore_normal);
		tv_load_msg.setText(mLoadMoreMsg);
		rl_loadmore.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				updateStatus(LOADINGMORE);
				if (mListener != null) {
					mListener.loadMoreAction();
				}
			}
		});
		footer.setPadding(0, 0, 0, loadBarPadding);

		mRotateUpAnim = new RotateAnimation(0.0f, -180.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateUpAnim.setDuration(200);
		mRotateUpAnim.setFillAfter(true);
		mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		mRotateDownAnim.setDuration(200);
		mRotateDownAnim.setFillAfter(true);

		super.setOnScrollListener(this);
	}

	@Override
	public void setAdapter(ListAdapter adapter) {
		if (!hasSetFooter) {
			hasSetFooter = true;
			addFooterView(footer);
		}
		super.setAdapter(adapter);
	}

	public void setOverScrollDistance(int inPx) {
		this.mMaxPadding = inPx;
	}

	public void setOnActionListener(OnActionListener l) {
		this.mListener = l;
	}

	public void startRefresh() {
		if (status == NONE) {
			updateStatus(REFRESHING);
		}
	}

	public void completeRefresh() {
		if (status == REFRESHING) {
			if (isAutoScroll) {
				lastStatus = REFRESH_NONE;
			} else {
				updateStatus(REFRESH_NONE);
			}
		}
	}

	public void startLoadmore() {
		if (status == NONE) {
			updateStatus(LOADINGMORE);
		}
	}

	public void comleteLoadmore() {
		if (status == LOADINGMORE) {
			if (isAutoScroll) {
				lastStatus = LOADMORE_NONE;
			} else {
				updateStatus(LOADMORE_NONE);
			}
		}
	}

	public interface OnActionListener {
		public void refreshAction();

		public void loadMoreAction();
	}

	@Override
	public void setOnScrollListener(OnScrollListener l) {
		// super.setOnScrollListener(l);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		if (isAutoScroll) {
			return super.onTouchEvent(ev);
		}
		final float y = ev.getRawY();
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			lastY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			float delta = y - lastY;
			if (isFirstItemVisible) {
				updateRefreshBarHeight(delta / OFFSET_RADIO);
			} else if (isLastItemVisible) {
				updateLoadmoreBarHeight(delta);
			}
			lastY = y;
			break;
		case MotionEvent.ACTION_UP:
			if (isFirstItemVisible) {
				new Thread(new AutoRefreshScrollRunnable()).start();
			} else if (isLastItemVisible) {
				new Thread(new AutoLoadmoreScrollRunnable()).start();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	private void updateRefreshBarHeight(float dy) {
		refreshBarPadding += dy;
		int min;
		switch (status) {
		case RELESE_TO_REFRESH:
			if (refreshBarPadding > mMaxPadding) {
				refreshBarPadding = mMaxPadding;
			} else if (refreshBarPadding < mRefreshingPadding) {
				updateStatus(REFRESH_NORMAL);
			}
			min = mMinPadding;
			break;
		case REFRESHING:
			if (refreshBarPadding > mMaxPadding) {
				refreshBarPadding = mMaxPadding;
			} else if (refreshBarPadding < 0) {
				refreshBarPadding = 0;
			}
			min = 0;
			break;
		default:
			if (refreshBarPadding < mMinPadding) {
				refreshBarPadding = mMinPadding;
			} else if (refreshBarPadding > mRefreshingPadding) {
				updateStatus(RELESE_TO_REFRESH);
			}
			min = mMinPadding;
			break;
		}
		header.setPadding(0, refreshBarPadding, 0, 0);
		if (refreshBarPadding != min) {
			setSelection(0);
		}
	}

	private void updateLoadmoreBarHeight(float dy) {
		loadBarPadding -= dy;
		switch (status) {
		case PUSH_TO_LOADMORE:
			if (loadBarPadding > mRefreshingPadding) {
				updateStatus(RELESE_TO_LOADMORE);
			}
			break;
		case RELESE_TO_LOADMORE:
			if (loadBarPadding > mMaxPadding) {
				loadBarPadding = mMaxPadding;
			} else if (loadBarPadding < mRefreshingPadding) {
				updateStatus(PUSH_TO_LOADMORE_ANIM);
			}
			break;
		case LOADINGMORE:
			if (loadBarPadding > mMaxPadding) {
				loadBarPadding = mMaxPadding;
			} else if (loadBarPadding < 0) {
				loadBarPadding = 0;
			}
			break;
		default:
			if (loadBarPadding > 0) {
				updateStatus(PUSH_TO_LOADMORE);
			} else {
				loadBarPadding = 0;
			}
			break;
		}
		footer.setPadding(0, 0, 0, loadBarPadding);
	}

	private void updateStatus(int s) {
		if (status != s) {
			status = s;
			switch (status) {
			case REFRESHING:
				tv_refresh_msg.setText(R.string.pull_listview_refresing);
				iv_refresh_arrow.setVisibility(View.GONE);
				iv_refresh_arrow.clearAnimation();
				pb_refresh.setVisibility(View.VISIBLE);
				refreshBarPadding = 0;
				header.setPadding(0, refreshBarPadding, 0, 0);
				setSelection(0);
				if (mListener != null) {
					mListener.refreshAction();
				}
				break;
			case RELESE_TO_REFRESH:
				tv_refresh_msg.setText(R.string.pull_listview_refresh_relese);
				iv_refresh_arrow.startAnimation(mRotateUpAnim);
				break;
			case REFRESH_NORMAL:
				tv_refresh_msg.setText(R.string.pull_listview_refresh_normal);
				iv_refresh_arrow.startAnimation(mRotateDownAnim);
				break;
			case REFRESH_NONE:
				tv_refresh_msg.setText(R.string.pull_listview_refresh_normal);
				iv_refresh_arrow.setVisibility(View.VISIBLE);
				pb_refresh.setVisibility(View.GONE);
				refreshBarPadding = mMinPadding;
				header.setPadding(0, refreshBarPadding, 0, 0);
				setSelection(0);
				status = NONE;
				break;
			case PUSH_TO_LOADMORE:
				tv_load_msg.setText(R.string.pull_listview_loadmore_pull);
				iv_load_arrow.setVisibility(View.VISIBLE);
				break;
			case PUSH_TO_LOADMORE_ANIM:
				tv_load_msg.setText(R.string.pull_listview_loadmore_pull);
				iv_load_arrow.startAnimation(mRotateDownAnim);
				break;
			case RELESE_TO_LOADMORE:
				tv_load_msg.setText(R.string.pull_listview_loadmore_relese);
				iv_load_arrow.startAnimation(mRotateUpAnim);
				break;
			case LOADINGMORE:
				iv_load_arrow.clearAnimation();
				iv_load_arrow.setVisibility(View.GONE);
				pb_loadmore.setVisibility(View.VISIBLE);
				tv_load_msg.setText(R.string.pull_listview_loading);
				footer.setPadding(0, 0, 0, 0);
				if (mListener != null) {
					mListener.loadMoreAction();
				}
				break;
			case LOADMORE_NONE:
				loadBarPadding = 0;
				iv_load_arrow.clearAnimation();
				iv_load_arrow.setVisibility(View.GONE);
				pb_loadmore.setVisibility(View.GONE);
				tv_load_msg.setText(mLoadMoreMsg);
				footer.setPadding(0, 0, 0, 0);
				status = NONE;
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		isFirstItemVisible = (totalItemCount > 0 && firstVisibleItem == 0 && status != LOADINGMORE);
		if (!isFirstItemVisible) {
			isLastItemVisible = (totalItemCount > visibleItemCount
					&& firstVisibleItem + visibleItemCount == totalItemCount && status != REFRESHING);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	private class AutoRefreshScrollRunnable implements Runnable {

		public AutoRefreshScrollRunnable() {
			isAutoScroll = true;
		}

		@Override
		public void run() {
			int mTagPadding;
			switch (status) {
			case REFRESHING:
				lastStatus = status;
				mTagPadding = 0;
				break;
			default:
				if (refreshBarPadding > mRefreshingPadding) {
					mTagPadding = 0;
					lastStatus = REFRESHING;
				} else {
					mTagPadding = mMinPadding;
					lastStatus = REFRESH_NONE;
				}
				break;
			}

			AccelerateInterpolator interpolator = new AccelerateInterpolator();
			while (true) {
				int deltaPadding = (int) (AUTO_SCROLL_DISTANCE * interpolator
						.getInterpolation(refreshBarPadding > mRefreshingPadding ? 0.9f : 0.5f));
				refreshBarPadding -= deltaPadding;
				if (refreshBarPadding <= mTagPadding) {
					refreshBarPadding = mTagPadding;
				}
				mHandler.sendEmptyMessage(AUTO_REFRESH_SCROLL);
				if (refreshBarPadding == mTagPadding) {
					break;
				} else {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
			}
			if (lastStatus != status) {
				mHandler.sendEmptyMessage(AUTO_SCROLL_UPDATE);
			}
			isAutoScroll = false;
		}
	}

	private class AutoLoadmoreScrollRunnable implements Runnable {

		public AutoLoadmoreScrollRunnable() {
			isAutoScroll = true;
		}

		@Override
		public void run() {
			int mTagPadding = 0;
			switch (status) {
			case LOADINGMORE:
				lastStatus = status;
				break;
			default:
				if (loadBarPadding > mRefreshingPadding) {
					lastStatus = LOADINGMORE;
				} else {
					lastStatus = LOADMORE_NONE;
				}
				break;
			}

			AccelerateInterpolator interpolator = new AccelerateInterpolator();
			while (true) {
				int deltaPadding = (int) (AUTO_SCROLL_DISTANCE * interpolator
						.getInterpolation(loadBarPadding > mRefreshingPadding ? 0.9f : 0.5f));
				loadBarPadding -= deltaPadding;
				if (loadBarPadding <= mTagPadding) {
					loadBarPadding = mTagPadding;
				}
				mHandler.sendEmptyMessage(AUTO_LOADMORE_SCROLL);
				if (loadBarPadding == mTagPadding) {
					break;
				} else {
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					continue;
				}
			}
			if (lastStatus != status) {
				mHandler.sendEmptyMessage(AUTO_SCROLL_UPDATE);
			}
			isAutoScroll = false;
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case AUTO_REFRESH_SCROLL:
				header.setPadding(0, refreshBarPadding, 0, 0);
				setSelection(0);
				break;
			case AUTO_LOADMORE_SCROLL:
				footer.setPadding(0, 0, 0, loadBarPadding);
				break;
			case AUTO_SCROLL_UPDATE:
				updateStatus(lastStatus);
				break;
			default:
				break;
			}
		}
	};
}
