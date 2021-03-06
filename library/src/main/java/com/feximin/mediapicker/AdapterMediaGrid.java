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
            int realPosition = position - 1;
            MediaEntity en = mDada.get(realPosition);
            imgCheck.setSelected(mediaManager.isSelected(en));
            imgCheck.setOnClickListener(v1 -> mediaManager.toggle(en));


            Glide.with(mActivity).load(en.getPath()).crossFade().into(imgCover);
            if (type == MediaEntity.IMAGE){
                imgCover.setOnClickListener(v -> {
                    if (mOnMediaClickListener == null){
                        ActivityImageShower.startActivity(mActivity, mDataFolder, realPosition);
                    }else {
                        mOnMediaClickListener.onMediaClick(en, realPosition);
                    }
                });
            }else if (type == MediaEntity.VIDEO){
                TextView txtTime = (TextView) convertView.findViewById(R.id.txt_time);
                txtTime.setText(getSecond(en.getDuration()));
                imgCover.setOnClickListener(v -> {
                    if (mOnMediaClickListener == null){
                        ActivityVideoShower.startActivity(mActivity, en);
                    }else {
                        mOnMediaClickListener.onMediaClick(en, realPosition);
                    }

                });
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

    public interface OnMediaClickListener{
        void onMediaClick(MediaEntity entity, int position);
    }

    private OnMediaClickListener mOnMediaClickListener;
    public void setOnItemClickListener(OnMediaClickListener listener){
        mOnMediaClickListener = listener;
    }

    @Override
    public int getViewTypeCount() {
        return 4;
    }

}
