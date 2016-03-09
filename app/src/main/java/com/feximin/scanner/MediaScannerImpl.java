package com.feximin.scanner;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.IntDef;

import com.feximin.mediapicker.MediaEntity;
import com.feximin.mediapicker.MediaFolder;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Neo on 16/1/30.
 */
public abstract class MediaScannerImpl<T extends AbstractMediaPeanut> implements IMediaScanner {

    private ContentResolver mContentResolver;



    public static final int NONE = 0;
    public static final int REFRESHING = 1;
    public static final int REFRESH_COMPLETED = 2;

    protected ScannerConfig mScannerConfig;
    public MediaScannerImpl(){
        mScannerConfig = onCreate();
    }

    protected abstract ScannerConfig onCreate();

    @IntDef({NONE, REFRESHING, REFRESH_COMPLETED})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status{}

    @Status
    private int mCurStatus = NONE;

    @Override
    public void refresh(Context context) {

    }

    @Override
    public void refreshImmediately(Context context) {

    }

    private void refreshData(Context context){
        this.mCurStatus = REFRESHING;
        mExistMediaFolder.clear();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver
                .query(mScannerConfig.uri,
                mScannerConfig.columns,
                mScannerConfig.whereClause,
                mScannerConfig.args,
                mScannerConfig.order);

        if(cursor == null) return;
        try {
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            while (cursor.moveToNext()) {
                String path = cursor.getString(index); // 文件地址
                if(isFileValid(path)){
                    MediaEntity entity = new MediaEntity(path);
//                    mFolderAll.add(entity);
                    getFolder(path).add(entity);
                }
            }
        }finally {
            cursor.close();
//            mExistMediaFolder.put("所有图片", mFolderAll);
//            this.mCurStatus = REFRESH_COMPLETED;
//            if(mOnRefreshListener != null){
//                mOnRefreshListener.onRefreshCompleted();
//            }
        }
//        Toast.makeText(context, mAll.size() + "" , Toast.LENGTH_SHORT).show();
    }

    private Map<String, MediaFolder> mExistMediaFolder = new HashMap<>();
    private MediaFolder getFolder(String path){
        String parentDir;
        int last = path.lastIndexOf("/");
        if(last == -1){
            parentDir = "/";
        }else{
            parentDir = path.substring(0, last);
        }
        MediaFolder folder = mExistMediaFolder.get(parentDir);
        if(folder == null){
            folder = new MediaFolder();
            folder.setPath(parentDir);
            String name = parentDir.substring(parentDir.lastIndexOf("/")+1);
            folder.setName(name);
            folder.setAlbumPath(path);
            mExistMediaFolder.put(parentDir, folder);
        }
        return folder;
    }

    private boolean isFileValid(String path){
        if(path == null || path.length() == 0) return false;
        File f = new File(path);
        return f.exists() && f.length() > 0;
    }
    protected void ensureContentResolver(Context context){
        if(mContentResolver == null) mContentResolver = context.getContentResolver();
    }
}
