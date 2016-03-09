package com.feximin.mediapicker;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Neo on 16/1/29.
 */
public class MediaEntity implements Parcelable {
    private String path;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public MediaEntity(String path) {
        this.path = path;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public static class ImageEntity extends MediaEntity{

        public ImageEntity(String path) {
            super(path);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
        }

        protected ImageEntity(Parcel in) {
            super(in);
        }

        public static final Creator<ImageEntity> CREATOR = new Creator<ImageEntity>() {
            public ImageEntity createFromParcel(Parcel source) {
                return new ImageEntity(source);
            }

            public ImageEntity[] newArray(int size) {
                return new ImageEntity[size];
            }
        };
    }

    public static class AudioEntity extends MediaEntity{

        private int duration;
        public AudioEntity(String path, int duration) {
            super(path);
            this.duration = duration;
        }

        public int getDuration() {
            return duration;
        }

        public void setDuration(int duration) {
            this.duration = duration;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(this.duration);
        }

        protected AudioEntity(Parcel in) {
            super(in);
            this.duration = in.readInt();
        }

        public static final Creator<AudioEntity> CREATOR = new Creator<AudioEntity>() {
            public AudioEntity createFromParcel(Parcel source) {
                return new AudioEntity(source);
            }

            public AudioEntity[] newArray(int size) {
                return new AudioEntity[size];
            }
        };
    }

    public static class VideoEntity extends MediaEntity{

        private String thumb;
        private int duration;
        public VideoEntity(String path, int duration) {
            super(path);
            thumb = "";
            this.duration = duration;
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
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeString(this.thumb);
            dest.writeInt(this.duration);
        }

        protected VideoEntity(Parcel in) {
            super(in);
            this.thumb = in.readString();
            this.duration = in.readInt();
        }

        public static final Creator<VideoEntity> CREATOR = new Creator<VideoEntity>() {
            public VideoEntity createFromParcel(Parcel source) {
                return new VideoEntity(source);
            }

            public VideoEntity[] newArray(int size) {
                return new VideoEntity[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeByte(selected ? (byte) 1 : (byte) 0);
    }

    protected MediaEntity(Parcel in) {
        this.path = in.readString();
        this.selected = in.readByte() != 0;
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
