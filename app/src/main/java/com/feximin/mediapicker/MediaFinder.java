package com.feximin.mediapicker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.IntDef;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neo on 16/1/30.
 */
public class MediaFinder {
    private MediaFolder mAllImageFolder = new MediaFolder("所有图片", MediaFolder.IMAGE);
    private MediaFolder mAllAudioFolder = new MediaFolder("所有音频", MediaFolder.AUDIO);
    private MediaFolder mAllVideoFolder = new MediaFolder("所有视频", MediaFolder.VIDEO);
    private Config mConfig;

    public void setConfig(Config config){
        this.mConfig = config;
    }

    void refresh(Context context) {
        if(this.mCurStatus == REFRESHING) return;
        refreshData(context);
    }

    public void refreshImmediately(Context context){
        refreshData(context);
    }
    private Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
    private Uri mAudioUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
    private Uri mVideoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;

    private void refreshData(Context context){
        this.mCurStatus = REFRESHING;
        clearAndEnsureFullFolder();
        ContentResolver resolver = context.getContentResolver();
        if (needRefresh(MediaFolder.IMAGE)) refreshImage(resolver);
        if (needRefresh(MediaFolder.AUDIO))refreshAudio(resolver);
        if (needRefresh(MediaFolder.VIDEO))refreshVideo(resolver);
        this.mCurStatus = REFRESH_COMPLETED;
        if (mOnRefreshListener != null) mOnRefreshListener.onRefreshCompleted(getAllFolder());
    }

    private void clearAndEnsureFullFolder(){
        mAllImageFolder.clear();
        mAllAudioFolder.clear();
        mAllVideoFolder.clear();
        mExistMediaFolder.clear();

        if (needRefresh(MediaFolder.IMAGE)) mExistMediaFolder.put(mAllImageFolder.getName(), mAllImageFolder);
        if (needRefresh(MediaFolder.AUDIO)) mExistMediaFolder.put(mAllAudioFolder.getName(), mAllAudioFolder);
        if (needRefresh(MediaFolder.VIDEO)) mExistMediaFolder.put(mAllVideoFolder.getName(), mAllVideoFolder);
    }

    //如果config中包含了就需要刷新，否则不需要
    private boolean needRefresh(@MediaFolder.Type int type){
        Class<?> key = null;
        if (type == MediaFolder.IMAGE){
            key = MediaEntity.ImageEntity.class;
        }else if (type == MediaFolder.VIDEO){
            key = MediaEntity.VideoEntity.class;
        }else if (type == MediaFolder.AUDIO){
            key = MediaEntity.AudioEntity.class;
        }
        return mConfig.getRequestMap().containsKey(key);
    }
    private List<MediaFolder> getAllFolder(){
        List<MediaFolder> folders = new ArrayList<>(mExistMediaFolder.size());
        for(String key : mExistMediaFolder.keySet()){
            folders.add(mExistMediaFolder.get(key));
        }
        return folders;
    }

    public static final int NONE = 0;
    public static final int REFRESHING = 1;
    public static final int REFRESH_COMPLETED = 2;

    @IntDef({NONE, REFRESHING, REFRESH_COMPLETED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status{}

    @Status
    private int mCurStatus = NONE;

    //我了个擦，还是需要用map来表示的，需要根据路径名去获取对应的folder，只有在添加图片的时候才使用到，
    private Map<String, MediaFolder> mExistMediaFolder = new LinkedHashMap<>();
    private MediaFolder getFolder(String path){
        String parentDir;
        int last = path.lastIndexOf("/");
        if(last <= 0){
            parentDir = "/";
        }else{
            parentDir = path.substring(0, last);
        }
        MediaFolder folder = mExistMediaFolder.get(parentDir);
        if(folder == null){
            folder = new MediaFolder();
            folder.setPath(parentDir);
            folder.setType(MediaFolder.IMAGE);
            String name = parentDir.substring(parentDir.lastIndexOf("/")+1);
            folder.setName(name);
            folder.setAlbumPath(path);
            mExistMediaFolder.put(parentDir, folder);
        }
        return folder;
    }

    private boolean isValidFile(String path){
        if(path == null || path.length() == 0) return false;
        File f = new File(path);
        return f.exists() && f.length() > 0;
    }

    private OnRefreshListener mOnRefreshListener;
    public void setOnRefreshListener(OnRefreshListener listener){
        this.mOnRefreshListener = listener;
    }

    public interface OnRefreshListener{
        void onRefreshCompleted(List<MediaFolder> folderList);
    }


    private void refreshImage( ContentResolver contentResolver){
        Cursor cursor = contentResolver.query(mImageUri, null, null, null, MediaStore.Images.Media.DATE_MODIFIED + " DESC");
        if(cursor != null) {
            try {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                while (cursor.moveToNext()) {
                    String path = cursor.getString(index); // 文件地址
                    if (isValidSuffix(path) && isValidFile(path)) {
                        MediaEntity.ImageEntity entity = new MediaEntity.ImageEntity(path);
                        if (mAllImageFolder.getChildren().size() == 0) mAllImageFolder.setAlbumPath(path);
                        mAllImageFolder.add(entity);
                        getFolder(path).add(entity);
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }

    private void refreshAudio(ContentResolver contentResolver){
        Cursor cursor = contentResolver.query(mAudioUri, null, null, null, MediaStore.Audio.Media.DATE_MODIFIED + " DESC");
        if(cursor != null) {
            try {
                int pathIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                int durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION);
                while (cursor.moveToNext()) {
                    String path = cursor.getString(pathIndex); // 文件地址
                    if (isValidSuffix(path) && isValidFile(path)) {
                        int duration = cursor.getInt(durationIndex);
                        MediaEntity.AudioEntity audio = new MediaEntity.AudioEntity(path, duration);
                        mAllAudioFolder.add(audio);
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }

    private void refreshVideo(ContentResolver contentResolver){
        Cursor cursor = contentResolver.query(mVideoUri, null, null, null, MediaStore.Video.Media.DATE_MODIFIED + " DESC");
        if(cursor != null) {
            try {
                int pathIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                int durationIndex = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION);
                while (cursor.moveToNext()) {
                    String path = cursor.getString(pathIndex); // 文件地址
                    if (isValidSuffix(path) && isValidFile(path)) {
                        int duration = cursor.getInt(durationIndex);
                        MediaEntity.VideoEntity video = new MediaEntity.VideoEntity(path, duration);
                        if (mAllVideoFolder.getChildren().size() == 0) mAllVideoFolder.setAlbumPath(path);
                        mAllVideoFolder.add(video);
                    }
                }
            } finally {
                cursor.close();
            }
        }
    }

    private boolean isValidSuffix(String path){
        for (String suf : mConfig.getSuffixList()){
            if (path.endsWith("."+suf)){
                return true;
            }
        }
        return false;
    }

}
