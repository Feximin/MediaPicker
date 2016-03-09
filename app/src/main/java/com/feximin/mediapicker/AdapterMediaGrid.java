package com.feximin.mediapicker;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neo on 16/1/30.
 */
public class AdapterMediaGrid extends RecyclerView.Adapter<AdapterMediaGrid.ImageViewHolder> {

    private Activity mActivity;
    private List<MediaEntity> mDada;
    private MediaFolder mDataFolder;
    private LayoutInflater mInflater;
    private int mItemWidth;
    private int mItemPadding = 6;
    public AdapterMediaGrid(Activity activity){
        this.mActivity = activity;
        this.mDada = new ArrayList<>();
        this.mInflater = LayoutInflater.from(mActivity);
        DisplayMetrics metrics = mActivity.getResources().getDisplayMetrics();
        mItemPadding *= metrics.density;
        mItemWidth = (metrics.widthPixels - 2 * mItemPadding) / 3;
    }



    public void setMediaFolder(MediaFolder folder){
        mDataFolder = folder;
        mDada.clear();
        mDada.addAll(folder.getChildren());

        notifyDataSetChanged();
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.v("onCreateViewHolder", "onCreateViewHolder + type" + viewType);
        if (viewType == MediaFolder.IMAGE){
            return new ImageViewHolder(mInflater.inflate(R.layout.item_grid, null));
        }else if (viewType == MediaFolder.VIDEO){
            return new VideoViewHolder(mInflater.inflate(R.layout.item_grid_video, null));

        }else {
            throw new IllegalArgumentException("no this kind of viewType");
        }
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        MediaEntity en = mDada.get(position);
        RecyclerView.LayoutParams lp = (RecyclerView.LayoutParams) holder.itemView.getLayoutParams();
        if(lp == null){
            lp = new RecyclerView.LayoutParams(mItemWidth, mItemWidth);
            lp.topMargin = mItemPadding;
            holder.itemView.setLayoutParams(lp);
        }

        if(position % 3 == 2){
            lp.rightMargin = 0;
        }else{
            lp.rightMargin = mItemPadding;
        }

        Glide.with(mActivity).load(en.getPath()).crossFade().into(holder.img);
        int type = mDataFolder.getType();
        if (type == MediaFolder.IMAGE){
            holder.foreView.setOnClickListener(v -> {});
        }else if (type == MediaFolder.VIDEO){
            ((VideoViewHolder)holder).txtTime.setText(getSceond(((MediaEntity.VideoEntity)en).getDuration()));
            holder.foreView.setOnClickListener(v -> {
                ActivityVideoShower.startActivity(mActivity, en);
            });
        }
    }

    private StringBuilder getSceond(long milli){
        long second = milli/1000;
        long hour = second / 3600;
        long minute = second % 3600 / 60;
        second = second % 3600 % 60;
        StringBuilder sb = new StringBuilder();
        if (hour > 0){
            if (hour < 10){
                sb.append(0);
            }
            sb.append(hour).append(":");
        }
        if (minute < 10){
            sb.append("0");
        }
        sb.append(minute).append(":");
        if (second < 10){
            sb.append(0);
        }
        sb.append(second);
        return sb;
    }

    @Override
    public int getItemViewType(int position) {
        return mDataFolder.getType();
    }



    @Override
    public int getItemCount() {
        return mDada.size();
    }

    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        protected ImageView img;
        protected ImageView imgCheckBox;
        protected View foreView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
            imgCheckBox = (ImageView) itemView.findViewById(R.id.img_check_box);
            foreView = itemView.findViewById(R.id.mask_foreground);

        }
    }

    public static class VideoViewHolder extends ImageViewHolder{

        TextView txtTime;
        public VideoViewHolder(View itemView) {
            super(itemView);
            txtTime = (TextView) itemView.findViewById(R.id.txt_time);
        }
    }
}
