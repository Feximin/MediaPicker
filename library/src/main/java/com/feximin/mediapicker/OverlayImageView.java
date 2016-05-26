package com.feximin.mediapicker;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Neo on 16/3/12.
 * 点击之后有一个遮罩效果
 */
public class OverlayImageView extends ImageView {
    private int mOverlayColor = 0x60000000;
    public OverlayImageView(Context context) {
        this(context, null);
    }

    public OverlayImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setClickable(true);         //必须得是clickable，否则不会执行到action_up
    }

    public void setOverlayColor(int color){
        this.mOverlayColor = color;
    }

    @Override
    protected void dispatchSetPressed(boolean pressed) {
        Drawable d = getDrawable();
        if (d != null && !d.isStateful()) {
            if (pressed){
                d.setColorFilter(new PorterDuffColorFilter(mOverlayColor, PorterDuff.Mode.DARKEN));
            }else{
                d.clearColorFilter();
            }
            invalidate();
        }
    }
}
