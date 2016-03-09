package com.feximin.mediapicker;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.feximin.mediapicker.MediaFinder.*;

/**
 * Created by Neo on 16/3/9.
 */
public class MediaManager {
    private static final MediaManager sManager = new MediaManager();
    private List<MediaEntity> mSelectedList = new ArrayList<>(5);
    private Map<String, MediaFolder> mExistFolderList = new LinkedHashMap<>();
    private Config mConfig;
    private Context mContext;
    private MediaFinder mMediaFinder = new MediaFinder();
    private MediaManager(){
    }

    public static MediaManager getInstance(Activity activity){
        sManager.mContext = activity.getApplicationContext();
        return sManager;
    }

    public void clear(){
        if (mConfig != null) mConfig = null;
    }

    public void initConfig(Config config){
        this.mConfig = config;
        this.mMediaFinder.setConfig(config);
    }

    /**
     * @param entity
     * @return 如果成功添加则返回true
     */
    public boolean add(MediaEntity entity){

        int selectCount = getSelectedCount(entity);
        int requestCount = getRequestCount(entity);
        boolean ok = selectCount < requestCount;
        if (ok){
            entity.setSelected(true);
            mSelectedList.add(entity);
        }else{
            showNoMoreThanHint(entity);
        }
        return ok;
    }

    public void remove(MediaEntity entity){
        if (entity == null) return;
        entity.setSelected(false);
        mSelectedList.remove(entity);
    }

    private void showNoMoreThanHint(MediaEntity entity){
        Class<?> clazz = entity.getClass();
        String format = "";
        if (clazz == MediaEntity.ImageEntity.class){
            format = "最多只能选择%d张图片";
        }else if (clazz == MediaEntity.VideoEntity.class){
            format = "最多只能选择%d个视频";
        }else if (clazz == MediaEntity.AudioEntity.class){
            format = "最多只能选择%d条音频";
        }else {
            throw  new IllegalArgumentException("no this kind of media entity !!");
        }
        String toast = String.format(format, getRequestCount(entity));
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }

    public int getRequestCount(MediaEntity entity){
        return mConfig.getRequest(entity).getTake();
    }

    private int getSelectedCount(MediaEntity entity){
        Class<?> clazz = entity.getClass();
        int count = 0;
        for (MediaEntity en : mSelectedList){
            if (en.getClass() == clazz){
                count ++ ;
            }
        }
        return count;
    }

    private OnRefreshListener mOnRefreshListener;

    public void setOnRefreshListener(OnRefreshListener listener){
        this.mOnRefreshListener = listener;
    }

    public void refresh(){
        mMediaFinder.refresh(mContext);
    }

    public void refreshImmediately(){
        mMediaFinder.refreshImmediately(mContext);
    }



}
