package com.feximin.mediapicker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Neo on 16/1/29.
 */
public class MediaEntity implements Parcelable {
    private String path;
    private Type type;
    private int duration;
    private String thumb;

    public MediaEntity(String path) {
        this.path = path;
    }

    public MediaEntity(String path, Type type) {
        this.path = path;
        this.type = type;
    }

    public MediaEntity(String path, int duration, Type type) {
        this.duration = duration;
        this.path = path;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MediaEntity entity = (MediaEntity) o;

        if (path != null ? !path.equals(entity.path) : entity.path != null) return false;
        return type == entity.type;

    }

    @Override
    public int hashCode() {
        int result = path != null ? path.hashCode() : 0;
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
        dest.writeInt(this.duration);
        dest.writeString(this.thumb);
    }

    protected MediaEntity(Parcel in) {
        this.path = in.readString();
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : Type.values()[tmpType];
        this.duration = in.readInt();
        this.thumb = in.readString();
    }

    public static final Creator<MediaEntity> CREATOR = new Creator<MediaEntity>() {
        public MediaEntity createFromParcel(Parcel source) {
            return new MediaEntity(source);
        }

        public MediaEntity[] newArray(int size) {
            return new MediaEntity[size];
        }
    };
}
