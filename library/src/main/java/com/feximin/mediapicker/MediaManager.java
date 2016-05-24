package com.feximin.mediapicker;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.feximin.mediapicker.MediaEntity.Type;

import java.util.ArrayList;
import java.util.List;

import static com.feximin.mediapicker.MediaFinder.OnRefreshListener;

/**
 * Created by Neo on 16/3/9.
 */
public class MediaManager {
    private static final MediaManager sManager = new MediaManager();
    private List<MediaEntity> mSelectedList = new ArrayList<>(5);
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
     * 如果已经选中，则取消，否则选中
     * @param entity
     * @return 选中返回true，取消选中返回false,不能选中也返回false
     */
    public Status toggle(MediaEntity entity){
        Status status;
        if (isSelected(entity)){
            mSelectedList.remove(entity);
            status = Status.Remove;
        }else{
            int selectCount = getSelectedCount(entity);
            int requestCount = getRequestCount(entity);
            if (selectCount < requestCount){
                mSelectedList.add(entity);
                status = Status.Add;
            }else{
                showNoMoreThanHint(entity);
                status = Status.Full;
            }
        }
        if (status != Status.Full) {
            for (OnMediaSelectListener listener : mOnMediaSelectListenerList) {
                listener.onMediaSelect(entity.getType(), getSelectedCount(entity));
            }
        }
        return status;
    }


    public interface OnMediaSelectListener{
        void onMediaSelect(@Type int type, int count);
    }

    private List<OnMediaSelectListener> mOnMediaSelectListenerList = new ArrayList<>(2);
    public void addOnMediaSelectListener(OnMediaSelectListener listener){
        if (listener != null && !mOnMediaSelectListenerList.contains(listener)){
            mOnMediaSelectListenerList.add(listener);
        }
    }

    public void removeOnMediaSelectListener(OnMediaSelectListener listener){
        if (listener != null && mOnMediaSelectListenerList.contains(listener)){
            mOnMediaSelectListenerList.remove(listener);
        }
    }

    public enum Status{
        Add, Remove, Full
    }

    /**
     * 是否已经选中，根据路径判断
     * @param entity
     * @return
     */
    public boolean isSelected(MediaEntity entity){
        boolean selected = mSelectedList.contains(entity);
        return selected;
    }

    private void showNoMoreThanHint(MediaEntity entity){
        @Type int type = entity.getType();
        String format = "";
        if (type == MediaEntity.IMAGE){
            format = "最多只能选择%d张图片";
        }else if (type == MediaEntity.VIDEO){
            format = "最多只能选择%d个视频";
        }else if (type == MediaEntity.AUDIO){
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

    public int getSelectedCount(MediaEntity entity){
        return getSelectedCount(entity.getType());
    }
    public int getSelectedCount(@Type int type){
        int count = 0;
        for (MediaEntity en : mSelectedList){
            if (en.getType() == type){
                count ++ ;
            }
        }
        return count;
    }

    public int getSelectedCount(MediaFolder folder){
        return getSelectedCount(folder.getType());
    }

    public void setOnRefreshListener(OnRefreshListener listener){
        mMediaFinder.setOnRefreshListener(listener);
    }

    public void refresh(){
        mMediaFinder.refresh(mContext);
    }

    public void refreshImmediately(){
        mMediaFinder.refreshImmediately(mContext);
    }



}
