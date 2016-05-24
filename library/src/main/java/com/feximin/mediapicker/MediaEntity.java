package com.feximin.mediapicker;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Neo on 16/1/29.
 */
public class MediaEntity implements Parcelable {
    private String path;
    private int type;
    private int duration;
    private String thumb;

    public static final int IMAGE = 1;
    public static final int VIDEO = 2;
    public static final int AUDIO = 3;

    @IntDef({IMAGE, VIDEO, AUDIO})
    @Retention(RetentionPolicy.SOURCE)
    public static @interface Type{}

    public MediaEntity(String path) {
        this.path = path;
    }

    public MediaEntity(String path, @Type int type) {
        this.path = path;
        this.type = type;
    }

    public MediaEntity(String path, int duration, @Type int type) {
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

    public @Type int getType() {
        return type;
    }

    public void setType(@Type int type) {
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

        return path != null ? path.equals(entity.path) : entity.path == null;
    }

    @Override
    public int hashCode() {
        return path != null ? path.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeInt(this.type);
        dest.writeInt(this.duration);
        dest.writeString(this.thumb);
    }

    protected MediaEntity(Parcel in) {
        this.path = in.readString();
        this.type = in.readInt();
        this.duration = in.readInt();
        this.thumb = in.readString();
    }

    public static final Creator<MediaEntity> CREATOR = new Creator<MediaEntity>() {
        @Override
        public MediaEntity createFromParcel(Parcel source) {
            return new MediaEntity(source);
        }

        @Override
        public MediaEntity[] newArray(int size) {
            return new MediaEntity[size];
        }
    };
}
