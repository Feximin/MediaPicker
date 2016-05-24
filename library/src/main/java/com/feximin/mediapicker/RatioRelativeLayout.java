package com.feximin.mediapicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 宽高比固定的RelativeLayout
 */
public class RatioRelativeLayout extends RelativeLayout {
	private float mRatio = 1;
	public RatioRelativeLayout(Context context) {
		this(context, null);
	}
	public RatioRelativeLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public RatioRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (attrs != null) {
			TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatioRelativeLayout);
			mRatio = ta.getFloat(R.styleable.RatioRelativeLayout_ratio, 1);
			ta.recycle();
		}
		if (mRatio < 0) mRatio = 1;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = View.getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
		int height = (int) (width / mRatio);
		heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY);
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public void setRatio(float ratio){
		if (ratio > 0 && ratio != mRatio){
			mRatio = ratio;
			requestLayout();
		}
	}

}