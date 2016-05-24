package com.feximin.mediapicker;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Neo on 16/3/14.
 * 图片宽高比固定
 */
public class RatioImageView extends ImageView {
    private float mRatio = 1;
    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
            mRatio = ta.getFloat(R.styleable.RatioImageView_ratio, 1);
            ta.recycle();
        }
        if (mRatio < 0) mRatio = 1;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int height = (int) (getMeasuredWidth() / mRatio);
        if (height != getMeasuredHeight()) {
            setMeasuredDimension(getMeasuredWidth(), height);
        }
    }

    public void setRatio(float ratio){
        if (ratio > 0 && ratio != mRatio){
            mRatio = ratio;
            requestLayout();
        }
    }
}
