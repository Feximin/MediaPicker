package com.feximin.mediapicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Neo on 16/3/7.
 */
public class Config  {

    private List<String> mSuffixList = new ArrayList<>(3);
    private Map<Class<?>, Request> mRequestMap = new HashMap<>(3);

    Config() { }


    public <T extends Request> T getRequest(MediaEntity entity){
        return (T) mRequestMap.get(entity.getClass());
    }

    public Map<Class<?>, Request> getRequestMap(){
        return mRequestMap;
    }

    public List<String> getSuffixList(){
        return mSuffixList;
    }



    public static class Builder{
        List<String> suffixList = new ArrayList<>(8);
        private Map<Class<?>, Request> requestMap = new HashMap<>(3);

        public Builder(){
            suffixList.add("jpg");
            suffixList.add("jpeg");
            suffixList.add("png");
            suffixList.add("mp4");
            suffixList.add("3gp");
            suffixList.add("avi");
            suffixList.add("amr");

        }
        public Builder image(ImageRequest request){
            if (request != null){
                requestMap.put(MediaEntity.ImageEntity.class, request);
            }
            return this;
        }

        public Builder video(VideoRequest request){
            if (request != null){
                requestMap.put(MediaEntity.VideoEntity.class, request);
            }

            return this;
        }

        public Builder audio(AudioRequest request){
            if (request != null){
                requestMap.put(MediaEntity.AudioEntity.class, request);
            }
            return this;
        }

        public Builder clearSuffix(){
            suffixList.clear();
            return this;
        }

        public Builder suffix(String...suffixes){
            for (String suf : suffixes){
                if (! suffixList.contains(suf)){
                    suffixList.add(suf);
                }
            }
            return this;
        }

        public Config build(){
            Config config = new Config();
            config.mSuffixList.addAll(suffixList);
            config.mRequestMap.putAll(requestMap);
            return config;
        }
    }


    abstract static class Request {


        protected int take;
        protected int maxSize;

        public Request(int take, int maxSize) {
            this.take = take;
            this.maxSize = maxSize;
        }

        public int getMaxSize() {
            return maxSize;
        }

        public void setMaxSize(int maxSize) {
            this.maxSize = maxSize;
        }

        public int getTake() {
            return take;
        }

        public void setTake(int take) {
            this.take = take;
        }

    }

    public static class ImageRequest extends Request{
        public ImageRequest(int take, int maxSize) {
            super(take, maxSize);
        }
    }

    public static class VideoRequest extends Request{

        protected int maxDuration;
        protected int minDuration;

        public VideoRequest(int take, int maxSize, int maxDuration, int minDuration) {
            super(take, maxSize);
            this.maxDuration = maxDuration;
            this.minDuration = minDuration;
        }

        public int getMaxDuration() {
            return maxDuration;
        }

        public void setMaxDuration(int maxDuration) {
            this.maxDuration = maxDuration;
        }

        public int getMinDuration() {
            return minDuration;
        }

        public void setMinDuration(int minDuration) {
            this.minDuration = minDuration;
        }
    }

    public static class AudioRequest extends VideoRequest{
        public AudioRequest(int take, int maxSize, int maxDuration, int minDuration) {
            super(take, maxSize, maxDuration, minDuration);
        }
    }
}
