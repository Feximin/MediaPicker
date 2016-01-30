package com.feximin.mediapicker;

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
    private List<MediaEntity> mChildren = new ArrayList<>(1);

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

    public void add(MediaEntity entity){
        mChildren.add(entity);
        num ++;
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
