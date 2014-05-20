package com.android.zmark.wigets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @author oblivion
 */
public class ImageViewBorder extends ImageView {

	/**
	 * @param context
	 */

	public ImageViewBorder(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ImageViewBorder(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public ImageViewBorder(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// @Override
	// protected void onDraw(Canvas canvas) {
	// Path clipPath = new Path();
	// int w = this.getWidth();
	// int h = this.getHeight();
	// float temp = (w >= 90 ? 10.0f : 8.0f);
	// // float temp = w / 2f;
	// // clipPath.addCircle(w / 2, w / 2, 360, Path.Direction.CW);
	// clipPath.addRoundRect(new RectF(0, 0, w, h), temp, temp,
	// Path.Direction.CW);
	// canvas.clipPath(clipPath);
	// super.onDraw(canvas);
	//
	// }

	@Override
	protected void onDraw(Canvas canvas) {
		Path clipPath = new Path();
		int w = this.getWidth();
		int h = this.getHeight();
		clipPath.addCircle(w / 2, h / 2, w / 2, Path.Direction.CCW);
		canvas.clipPath(clipPath);
		canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG
				| Paint.FILTER_BITMAP_FLAG));
		super.onDraw(canvas);

	}
}
