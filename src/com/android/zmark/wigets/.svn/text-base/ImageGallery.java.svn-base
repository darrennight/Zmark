package com.android.zmark.wigets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;

public class ImageGallery extends Gallery {

	public ImageGallery(Context context, AttributeSet paramAttributeSet) {

		super(context, paramAttributeSet);

	}

	@Override
	public boolean onFling(MotionEvent paramMotionEvent1,

	MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {

		return false;

	}

	@Override
	public boolean onScroll(MotionEvent paramMotionEvent1,
			MotionEvent paramMotionEvent2, float paramFloat1, float paramFloat2) {

		float f = 1.4F * paramFloat1;

		return super.onScroll(paramMotionEvent1, paramMotionEvent2, f,
				paramFloat2);

	}

	@Override
	protected boolean getChildStaticTransformation(View child, Transformation t) {
		return true;

	}

}
