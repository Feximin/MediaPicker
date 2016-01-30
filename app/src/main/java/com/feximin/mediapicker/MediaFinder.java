package com.feximin.mediapicker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neo on 16/1/30.
 */
public class MediaFinder {
    private List<MediaEntity> mAll = new ArrayList<>();

    private MediaFinder() {}

    private static MediaFinder sMediaFinder = new MediaFinder();

    public static MediaFinder getInstance() {
        return sMediaFinder;
    }

    private boolean mIsRefreshing;          //是否正在刷新数据
    public void refresh(Context context) {
        if(mIsRefreshing) return;
        mIsRefreshing = true;
        refreshData(context);
    }

    public void refreshImmediately(Context context){
        refreshData(context);
    }
    private Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

    private String mQueryImageArgs = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE +"=?";
    private void refreshData(Context context){
        mExistMediaFolder.clear();
        mAll.clear();
        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(uri, null, mQueryImageArgs, new String[]{"image/jpeg", "image/png"}, MediaStore.Images.Media.DATE_MODIFIED);
        if(cursor == null) return;
        try {
            while (cursor.moveToNext()) {
                int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                String path = cursor.getString(index); // 文件地址
                if(isFileValid(path)){
                    MediaEntity entity = new MediaEntity(path);
                    mAll.add(entity);
                    getFolder(path).add(entity);
                }
            }
        }finally {
            cursor.close();
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



}
