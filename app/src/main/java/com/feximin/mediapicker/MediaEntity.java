package com.feximin.mediapicker;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Neo on 16/1/29.
 */
public class MediaEntity {
    private String path;
    private MediaFolder mParentFolder;

    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_AUDIO = 2;

    @IntDef({TYPE_IMAGE, TYPE_VIDEO, TYPE_AUDIO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type{}

    private int type;

    public MediaEntity(String path) {
        this.path = path;
    }

    @Type
    public int getType() {
        return type;
    }

    public void setType(@Type int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public MediaFolder getmParentFolder() {
        return mParentFolder;
    }

    public void setmParentFolder(MediaFolder mParentFolder) {
        this.mParentFolder = mParentFolder;
    }
}
