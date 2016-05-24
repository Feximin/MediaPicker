package com.feximin.mediapicker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import static android.support.v7.widget.RecyclerView.ViewHolder;

/**
 * Created by Neo on 16/1/30.
 */
public class AdapterMediaGrid extends BaseAdapter {

    private ActivityPicker mActivity;
    private List<MediaEntity> mDada;
    private MediaFolder mDataFolder;
    private LayoutInflater mInflater;
    private MediaManager mediaManager;
    public AdapterMediaGrid(ActivityPicker activity){
        this.mActivity = activity;
        this.mDada = new ArrayList<>();
        this.mInflater = LayoutInflater.from(mActivity);
        mediaManager = MediaManager.getInstance(mActivity);
    }



    public void setMediaFolder(MediaFolder folder){
        mDataFolder = folder;
        mDada.clear();
        mDada.addAll(folder.getChildren());

        notifyDataSetChanged();
    }

    private StringBuilder getSecond(long milli){
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
    public int getCount() {
        int count = mDada.size();
        if (count > 0) count ++;
        return count;
    }

    @Override
    public MediaEntity getItem(int position) {
        if (position == 0) return null;
        return mDada.get(position - 1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        if (convertView == null){
            if (type == 0){
                convertView = mInflater.inflate(R.layout.item_op, null);
            }else if (type == MediaEntity.IMAGE){
                convertView = mInflater.inflate(R.layout.item_grid, null);
            }else if (type == MediaEntity.AUDIO){
                convertView = mInflater.inflate(R.layout.item_grid, null);
            }else if (type == MediaEntity.VIDEO){
                convertView = mInflater.inflate(R.layout.item_grid_video, null);
            }else{
                throw new IllegalArgumentException("no this kind of viewType");
            }
        }

        int folderType = mDataFolder.getType();
        if (position == 0){
            ImageView img = (ImageView) convertView;
            if (folderType == MediaEntity.IMAGE){
                img.setImageResource(R.mipmap.img_op_camera);
                img.setOnClickListener(v -> mActivity.toTakePhoto());
            }else if (type == MediaEntity.VIDEO){
                img.setImageResource(R.mipmap.img_op_video);
                img.setOnClickListener(v -> mActivity.toMakeVideo());
            }
        }else {
            ImageView imgCover = (ImageView) convertView.findViewById(R.id.img);
            ImageView imgCheck = (ImageView) convertView.findViewById(R.id.img_check_box);
            View foreView = convertView.findViewById(R.id.mask_foreground);
            int realPosition = position - 1;
            MediaEntity en = mDada.get(realPosition);
            imgCheck.setSelected(mediaManager.isSelected(en));
            imgCheck.setOnClickListener(v1 -> mediaManager.toggle(en));


            Glide.with(mActivity).load(en.getPath()).crossFade().into(imgCover);
            if (type == MediaEntity.IMAGE){
                foreView.setOnClickListener(v -> ActivityImageShower.startActivity(mActivity, mDataFolder, realPosition));
            }else if (type == MediaEntity.VIDEO){
                TextView txtTime = (TextView) convertView.findViewById(R.id.txt_time);
                txtTime.setText(getSecond(en.getDuration()));
                foreView.setOnClickListener(v -> ActivityVideoShower.startActivity(mActivity, en));
            }
        }
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;
        MediaEntity entity = mDada.get(position - 1);
        return entity.getType();
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

    public static class OpViewHolder extends ViewHolder{
        protected ImageView img;
        public OpViewHolder(View itemView) {
            super(itemView);
            img = (ImageView) itemView.findViewById(R.id.img);
        }
    }

    public static class ImageViewHolder extends OpViewHolder {

        protected ImageView imgCheckBox;
        protected View foreView;
        public ImageViewHolder(View itemView) {
            super(itemView);
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
