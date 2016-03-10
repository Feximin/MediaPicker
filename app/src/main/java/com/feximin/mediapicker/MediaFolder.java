package com.feximin.mediapicker;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neo on 16/1/29.
 * 文件夹
 */
public class MediaFolder implements Parcelable {
    private String path;
    private int num;
    private String name;
    private String albumPath ;   // 第一张图片作为封面
    private List<MediaEntity> children = new ArrayList<>(1);

    private Type type;

    public MediaFolder(){}

    public MediaFolder(String name){
        this.name = name;
    }
    public MediaFolder(String name, Type type){
        this.name = name;
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
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
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeInt(this.num);
        dest.writeString(this.name);
        dest.writeString(this.albumPath);
        dest.writeTypedList(children);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    }

    protected MediaFolder(Parcel in) {
        this.path = in.readString();
        this.num = in.readInt();
        this.name = in.readString();
        this.albumPath = in.readString();
        this.children = in.createTypedArrayList(MediaEntity.CREATOR);
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
    }

    public static final Creator<MediaFolder> CREATOR = new Creator<MediaFolder>() {
        public MediaFolder createFromParcel(Parcel source) {
            return new MediaFolder(source);
        }

        public MediaFolder[] newArray(int size) {
            return new MediaFolder[size];
        }
    };
}
