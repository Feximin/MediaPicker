package com.feximin.mediapicker;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Neo on 16/3/9.
 */
public abstract class ActivityShower extends Activity  implements MediaManager.OnMediaSelectListener{
    protected TextView mTxtSelectCount;
    protected MediaManager mMediaManager;
    protected ImageView mImgCheck;
    protected TextView mTxtTitle;
    protected Type mCurType;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        this.mCurType = getCurType();
        this.mMediaManager = MediaManager.getInstance(this);

        this.mTxtTitle = (TextView) findViewById(R.id.txt_title);
        this.mImgCheck = (ImageView) findViewById(R.id.img_check_box);
        this.mImgCheck.setOnClickListener(v -> toggle());
        this.mTxtSelectCount = (TextView) findViewById(R.id.txt_right);
        mMediaManager.addOnMediaSelectListener(this);
        onMediaSelect(mCurType, mMediaManager.getSelectedCount(mCurType));
    }

    protected abstract void toggle();

    protected abstract Type getCurType();


    @Override
    public void onMediaSelect(Type type, int count) {
        if (type == mCurType){
            mTxtSelectCount.setEnabled(count>0);
            mTxtSelectCount.setText(String.format("下一步(%s)", count));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaManager.removeOnMediaSelectListener(this);
    }
    protected abstract int getLayoutResId();
}
