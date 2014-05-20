/**
 * @author ChenDu GOGO  Administrator
 *	时间   2013 上午11:12:52
 *  包名：com.gogotown.ui
            工程名：GoGoCity
 */
package com.android.zmark.wigets;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.zmark.R;

/**
 * @类名 CommonDaligo.java
 * @时间 2013-1-11 上午11:12:52
 * @描述 通用型的对话框
 */
public class ApkUpdateDialog extends Dialog {

	private boolean mTitle = true;
	public static int FLAG_ONLEY_POSITIVE = 0; // 只有确定按钮
	public static int FLAG_POSITIVE_NEGATIVE = 1; // 有确定也有取消
	private int flag = FLAG_ONLEY_POSITIVE; //
	private String positive = "确定";
	private String negative = "取消";
	private View view;
	private Context context;
	private TextView txTitle, txContent;
	private Button btPositive, btNegative;
	private String title;
	private String text;
	private int[] disylayPix;
	private View contentView;
	private LayoutInflater mLayoutInflater;

	public ApkUpdateDialog(Context context, int[] disylayPix) {
		super(context, R.style.progressdialog);
		this.context = context;
		init();
		this.disylayPix = disylayPix;
	}

	public ApkUpdateDialog(Context context) {
		super(context, R.style.progressdialog);
		this.context = context;
		init();
	}

	public ApkUpdateDialog(Context context, int[] disylayPix, View view) {
		this(context, disylayPix);
		this.view = view;
		init();

	}

	public void setPositiveButton(String positive,
			View.OnClickListener clickListener) {
		this.positive = positive;
		view.findViewById(R.id.absolute_button).setVisibility(View.VISIBLE);
		if (null != btPositive) {
			btPositive.setText(positive);
			btPositive.setVisibility(View.VISIBLE);
		}
		if (clickListener != null) {
			btPositive.setOnClickListener(clickListener);
		} else {
			btNegative.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();

				}
			});
		}

	}

	public void setNegativeButton(String negative,
			View.OnClickListener clickListener) {
		this.negative = negative;
		view.findViewById(R.id.absolute_button).setVisibility(View.VISIBLE);
		if (null != btNegative) {
			btNegative.setText(negative);
			btNegative.setVisibility(View.VISIBLE);
		}
		if (clickListener != null) {
			btNegative.setOnClickListener(clickListener);
		} else {
			btNegative.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					dismiss();

				}
			});
		}

	}

	public void setIContentView(View view) {
		if (null == view)
			return;
		if (null != this.view) {
			this.contentView = view;
			LinearLayout content = (LinearLayout) this.view
					.findViewById(R.id.absolute_content);
			content.removeAllViews();
			content.addView(view);
		}
	}

	public void setIContentView(int resId) {
		if (-1 == resId)
			return;
		if (null != this.view) {
			this.contentView = view;
			LinearLayout content = (LinearLayout) this.view
					.findViewById(R.id.absolute_content);
			content.removeAllViews();
			if (mLayoutInflater != null)
				content.addView(mLayoutInflater.inflate(resId, null));
		}
	}

	public View getIContentView() {
		return contentView;
	}

	/**
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
		if (null != txContent)
			txContent.setText(text);
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public void setTitle(String title) {
		this.title = title;
		super.setTitle(null);
		if (null != txTitle)
			txTitle.setText(title);
	}

	private void init() {
		if (null == this.view) {
			mLayoutInflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			this.view = mLayoutInflater.inflate(R.layout.apkupdate_dialog_view,
					null);
		}
		setContentView(view);
		if (!mTitle) {
			this.view.findViewById(R.id.absolute_title)
					.setVisibility(View.GONE);
		} else {
			txTitle = (TextView) this.view.findViewById(R.id.tx_title);
			txTitle.setText(title);
		}

		txContent = (TextView) this.view.findViewById(R.id.tx_absolute_content);
		txContent.setText(text);
		btNegative = (Button) this.view.findViewById(R.id.bt_absolute_negative);
		btPositive = (Button) this.view.findViewById(R.id.bt_absolute_positive);
		btPositive.setText(this.positive);
		btNegative.setText(this.negative);
		if (this.flag != FLAG_POSITIVE_NEGATIVE) {
			btNegative.setVisibility(View.GONE);
		}

	}

	/**
	 * @param positive
	 *            the positive to set
	 */
	public void setPositiveText(String positive) {
		this.positive = positive;
		if (null != btPositive)
			btPositive.setText(positive);
	}

	/**
	 * @param negative
	 *            the negative to set
	 */
	public void setNegativeText(String negative) {
		this.negative = negative;
		if (null != btNegative)
			btNegative.setText(negative);
	}

	@Override
	public void show() {
		super.show();
		Window w = getWindow();
		WindowManager.LayoutParams params = w.getAttributes();
		if (disylayPix != null) {
			params.width = (int) (disylayPix[0] * 0.9);
			params.height = params.WRAP_CONTENT;
		}
		w.setAttributes(params);
	}
}