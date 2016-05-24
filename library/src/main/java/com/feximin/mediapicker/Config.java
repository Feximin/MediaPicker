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

    Config() { }


    public Request getRequest(MediaEntity entity){
        return  mRequestMap.get(entity.getType());
    }

    public Map<Integer, Request> getRequestMap(){
        return mRequestMap;
    }

    public List<String> getSuffixList(){
        return mSuffixList;
    }



    public static class Builder{
        List<String> suffixList = new ArrayList<>(8);
        private Map<Integer, Request> requestMap = new HashMap<>(3);

        public Builder(){
            suffixList.add("jpg");
            suffixList.add("jpeg");
            suffixList.add("png");
            suffixList.add("mp4");
            suffixList.add("3gp");
            suffixList.add("avi");
            suffixList.add("amr");

        }
        public Builder image(Request request){
            if (request != null){
                requestMap.put(MediaEntity.IMAGE, request);
            }
            return this;
        }

        public Builder video(Request request){
            if (request != null){
                requestMap.put(MediaEntity.VIDEO, request);
            }

            return this;
        }

        public Builder audio(Request request){
            if (request != null){
                requestMap.put(MediaEntity.AUDIO, request);
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


    static class Request {


        protected int take;
        protected int maxSize;
        protected int maxDuration;
        protected int minDuration;

        public Request(int take, int maxSize) {
            this.take = take;
            this.maxSize = maxSize;
        }

        public Request(int take, int maxSize, int maxDuration, int minDuration) {
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
