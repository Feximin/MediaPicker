package com.feximin.mediapicker;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neo on 16/1/29.
 * 文件夹
 */
public class MediaFolder {
    private String path;
    private int num;
    private String name;
    private String albumPath ;   // 第一张图片作为封面

    public static final int IMAGE = 0;
    public static final int AUDIO = 1;
    public static final int VIDEO = 2;

    @IntDef({IMAGE, AUDIO, VIDEO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type{}


    private int type;
    private List<MediaEntity> children = new ArrayList<>(1);

    public MediaFolder(){}

    public MediaFolder(String name){
        this.name = name;
    }
    public MediaFolder(String name, int type){
        this.name = name;
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbumPath() {
        return albumPath;
    }

    public void setAlbumPath(String albumPath) {
        this.albumPath = albumPath;
    }

    public List<MediaEntity> getChildren() {
        return children;
    }

    public void setChildren(List<MediaEntity> mChildren) {
        this.children = mChildren;
    }

    public void add(MediaEntity entity){
        children.add(entity);
        num ++;
    }

    public void clear(){
        children.clear();
        num = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaFolder that = (MediaFolder) o;

        return path != null ? path.equals(that.path) : that.path == null;

    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }
}
