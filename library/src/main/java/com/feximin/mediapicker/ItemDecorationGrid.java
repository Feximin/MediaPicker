package com.feximin.mediapicker;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.LayoutParams;
import android.view.View;

/**
 * itemDecoration是一个大坑, 设置之后并不是真正的为每一项,坑啊。。。。
 */
public class ItemDecorationGrid extends RecyclerView.ItemDecoration {

    private Drawable mDivider;

    public ItemDecorationGrid(int color, int value) {
        ShapeDrawable divider = new ShapeDrawable();
        divider.setIntrinsicHeight(value);
        divider.setIntrinsicWidth(value);
        divider.getPaint().setColor(color);
        mDivider = divider;
    }

    public ItemDecorationGrid(Drawable divider){
        this.mDivider = divider;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        drawHorizontal(c, parent);
//        drawVertical(c, parent);
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {

            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    public void drawHorizontal(Canvas c, RecyclerView parent) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
//            if (!isLastRow(parent, child)) {
                final LayoutParams params = (LayoutParams) child.getLayoutParams();
                final int left = child.getLeft() - params.leftMargin;
                final int right = child.getRight() + params.rightMargin + mDivider.getIntrinsicWidth();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
//            }
        }
    }

    public void drawVertical(Canvas c, RecyclerView parent) {
        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
//            if (!isLastColumn(parent, child)) {
                final LayoutParams params = (LayoutParams) child.getLayoutParams();
                final int top = child.getTop() - params.topMargin;
                final int bottom = child.getBottom() + params.bottomMargin;
                final int left = child.getRight() + params.rightMargin;
                final int right = left + mDivider.getIntrinsicWidth();

                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
//            }
        }
    }

    private boolean isLastColumn(RecyclerView parent, View child) {

        int pos = ((LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
        int spanCount = getSpanCount(parent);
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if ((pos + 1) % spanCount == 0){// 如果是最后一列，则不需要绘制右边
                return true;
            }
        }
        return false;
    }
    private boolean isFirstColumn(RecyclerView parent, View child) {

        int pos = ((LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
        int spanCount = getSpanCount(parent);
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
                return pos % spanCount == 0;
        }
        return false;
    }

    private boolean isLastRow(RecyclerView parent, View child) {
        int pos = ((LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
        int spanCount = getSpanCount(parent);
        int childCount = parent.getAdapter().getItemCount();
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            childCount = childCount - childCount % spanCount;
            if (pos >= childCount)// 如果是最后一行，则不需要绘制底部
                return true;
        }
        return false;
    }
    private boolean isFirstRow(RecyclerView parent, View child) {
        int pos = ((LayoutParams) child.getLayoutParams()).getViewLayoutPosition();
        int spanCount = getSpanCount(parent);
        LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return pos<spanCount;
        }
        return false;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        if (isLastRow(parent, view)){// 如果是最后一行，则不需要绘制底部
            if (isLastColumn(parent, view)){
                outRect.set(0, 0, 0, 0);
            }else{
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }else {
            if (isLastColumn(parent, view)){
                outRect.set(0, 0, 0, mDivider.getIntrinsicHeight());
            }else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), mDivider.getIntrinsicHeight());
            }
        }
//        int h13 = mDivider.getIntrinsicHeight() / 3;
//        int h23 = mDivider.getIntrinsicHeight() - h13;
//        int w13 = mDivider.getIntrinsicWidth() / 3;
//        int w23 = mDivider.getIntrinsicWidth() - w13;
//        if (isFirstRow(parent, view)){
//            if (isFirstColumn(parent, view)){
//                outRect.set(0, 0, w23, h23);
//            }if (isLastColumn(parent, view)){
//                outRect.set(w23, 0, 0, h23);
//            }else{
//                outRect.set(w13, 0, w13, h23);
//            }
//        }else if (isLastRow(parent, view)){
//            if (isFirstColumn(parent, view)){
//                outRect.set(0, h23, w23, 0);
//            }if (isLastColumn(parent, view)){
//                outRect.set(w23, h23, 0, 0);
//            }else{
//                outRect.set(w13, h23, h23, 0);
//            }
//
//        }else{
//            if (isFirstColumn(parent, view)){
//                outRect.set(0, h23, w23, h13);
//            }if (isLastColumn(parent, view)){
//                outRect.set(w23, h23, w23, h13);
//            }else{
//                outRect.set(0, h23, w23, h13);
//            }
//
//        }
//        outRect.set(0, 0, isLastColumn(parent, view)?0:mDivider.getIntrinsicWidth(), 0);
    }
}