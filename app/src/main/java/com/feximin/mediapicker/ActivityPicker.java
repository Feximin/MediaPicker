package com.feximin.mediapicker;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Neo on 16/1/29.
 */
public class ActivityPicker extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private TextView mTxtTitle;
    private ListView mListViewFolder;
    private View mMask;
    private ImageView mImgIndicator;
    private MediaFinder mMediaFinder;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picker);
        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mTxtTitle.setOnClickListener(this);

        mListViewFolder = (ListView) findViewById(R.id.list_view_folder);
        mListViewFolder.setOnItemClickListener(this);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int h = metrics.heightPixels / 2;
        mListViewFolder.getLayoutParams().height = h;
        mListViewFolder.requestLayout();

        mMask = findViewById(R.id.mask);
        mMask.setOnClickListener(v -> doToggleListFolder());

        mImgIndicator = (ImageView) findViewById(R.id.img_indicator);
        mMediaFinder = MediaFinder.getInstance();
        mMediaFinder.refreshImmediately(this);
    }

    private static final int STATUS_GONE = 0;
    private static final int STATUS_VISIBLE = 1;
    private static final int STATUS_ACTIVE = 2;

    @Status
    private int mCurStatus = STATUS_GONE;

    @IntDef({STATUS_GONE, STATUS_VISIBLE, STATUS_ACTIVE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status{}

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.txt_title:
                doToggleListFolder();
                break;
        }
    }

    private void doToggleListFolder(){
        if(mCurStatus == STATUS_ACTIVE) return;
        if(mCurStatus == STATUS_GONE){
            show();
        }else if(mCurStatus == STATUS_VISIBLE){
            dismiss();
        }
    }

    OvershootInterpolator interpolator = new OvershootInterpolator();
    AnticipateInterpolator anticipateInterpolator = new AnticipateInterpolator();
    private void show(){
        mCurStatus =STATUS_ACTIVE;
        mMask.setVisibility(View.VISIBLE);
        mListViewFolder.setVisibility(View.VISIBLE);
        mImgIndicator.setImageResource(R.mipmap.img_arrow_up);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mMask, "alpha", 0f, 1f).setDuration(300);
        int height = mListViewFolder.getHeight();
        if(height == 0){
                Animation translationAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_up_down_in);
                translationAnimation.setInterpolator(interpolator);
                mListViewFolder.startAnimation(translationAnimation);
                set.playTogether(alpha);
        }else{
            ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mListViewFolder, "translationY", -height, 0).setDuration(300);
            translationAnimator.setInterpolator(interpolator);
            set.playTogether(alpha, translationAnimator);
        }
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurStatus = STATUS_VISIBLE;
                mListViewFolder.clearAnimation();
                mImgIndicator.setImageResource(R.mipmap.img_arrow_up);
            }
        });
        set.start();
    }

    private void dismiss(){
        mCurStatus =STATUS_ACTIVE;
        AnimatorSet set = new AnimatorSet();
        int height = mListViewFolder.getHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mMask, "alpha", 1f, 0f);
        ObjectAnimator translation = ObjectAnimator.ofFloat(mListViewFolder, "translationY", 0, -height);
        translation.setInterpolator(anticipateInterpolator);
        set.playTogether(alpha, translation);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mMask.setVisibility(View.GONE);
                mListViewFolder.setVisibility(View.GONE);
                mImgIndicator.setImageResource(R.mipmap.img_arrow_down);
                mCurStatus = STATUS_GONE;
            }
        });
        set.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
