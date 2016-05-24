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
    private Map<Integer, Request> mRequestMap = new HashMap<>(3);
    private Crop crop;

    public Config() {

        mSuffixList.add("jpg");
        mSuffixList.add("jpeg");
        mSuffixList.add("png");
        mSuffixList.add("mp4");
        mSuffixList.add("3gp");
        mSuffixList.add("avi");
        mSuffixList.add("amr");
    }


    public Request getRequest(MediaEntity entity){
        return  mRequestMap.get(entity.getType());
    }

    public Request getRequest(@MediaEntity.Type int type){
        return mRequestMap.get(type);
    }

    public Map<Integer, Request> getRequestMap(){
        return mRequestMap;
    }

    public List<String> getSuffixList(){
        return mSuffixList;
    }


    public Crop getCrop(){
        return crop;
    }

    public Config image(Request request){
        if (request != null){
            mRequestMap.put(MediaEntity.IMAGE, request);
        }
        return this;
    }

    public Config video(Request request){
        if (request != null){
            mRequestMap.put(MediaEntity.VIDEO, request);
        }

        return this;
    }

    public Config audio(Request request){
        if (request != null){
            mRequestMap.put(MediaEntity.AUDIO, request);
        }
        return this;
    }

    public Config crop(Crop crop){
        this.crop = crop;
        return this;
    }
    public Config clearSuffix(){
        mSuffixList.clear();
        return this;
    }

    public Config suffix(String...suffixes){
        for (String suf : suffixes){
            if (! mSuffixList.contains(suf)){
                mSuffixList.add(suf);
            }
        }
        return this;
    }

    public static class Crop{
        public float cropScale = 1;
        public int outWidth;

        public Crop(float cropScale, int outWidth) {
            this.cropScale = cropScale;
            this.outWidth = outWidth;
        }
    }

    public static class Request {


        protected int take;
        protected int maxSize;              //字节
        protected int maxDuration;          //毫秒
        protected int minDuration;

        public Request(int take) {              //不限制大小
            this.take = take;
        }
        public Request(int take, int maxSize) {
            this.take = take;
            this.maxSize = maxSize;
        }

        public Request(int take, int maxSize, int minDuration, int maxDuration) {
            this(take, maxSize);
            this.maxDuration = maxDuration;
            this.minDuration = minDuration;
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

}
