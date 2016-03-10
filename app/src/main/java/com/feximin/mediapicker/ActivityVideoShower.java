package com.feximin.mediapicker;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.VideoView;

/**
 * Created by Neo on 16/3/8.
 */
public class ActivityVideoShower extends ActivityShower implements MediaManager.OnMediaSelectListener {

    private MediaEntity mCurEntity;
    private VideoView mVideoView;
    private View mRootView;
    private MediaPlayer mMediaPlayer;
    private View mCoverView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mCurEntity = getIntent().getParcelableExtra(ENTITY);
        if (mCurEntity == null){
            finish();
        }
        mRootView = findViewById(R.id.root);
        mCoverView = findViewById(R.id.view_cover);

        mVideoView = (VideoView) findViewById(R.id.video_view);
        mVideoView.setVideoURI(Uri.parse(mCurEntity.getPath()));
        ViewTreeObserver vto = mRootView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                resizeViewVideoSize();
                mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mMediaPlayer = mp;
                resizeViewVideoSize();
            }
        });
        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                int i = what;
                int e = extra;
                return false;
            }
        });

    }

    protected void toggle(){
        mImgCheck.setSelected(mMediaManager.toggle(mCurEntity) == MediaManager.Status.Add);
    }

    @Override
    protected Type getCurType() {
        return Type.Video;
    }

    //貌似设置ViewVideo的高度无效
    private void resizeViewVideoSize(){
        float w = mRootView.getWidth();
        if (w == 0 || mMediaPlayer == null) return;

        float h = mRootView.getHeight();
        float videoW = mMediaPlayer.getVideoWidth();
        float videoH = mMediaPlayer.getVideoHeight();

        float destW = w;
        float destH = w / videoW * videoH;

        if (destH > h) destW = h / destH * w;

        mCoverView.setVisibility(View.GONE);
        mVideoView.getLayoutParams().width = (int) destW;
        mVideoView.requestLayout();
        mVideoView.start();
    }

    public static final String ENTITY = "video_path";
    public static void startActivity(Activity activity, MediaEntity entity){
        if (entity == null) return;
        Intent intent = new Intent(activity, ActivityVideoShower.class);
        intent.putExtra(ENTITY, entity);
        activity.startActivity(intent);
    }


    @Override
    protected int getLayoutResId() {
        return R.layout.activity_video_shower;
    }
}
