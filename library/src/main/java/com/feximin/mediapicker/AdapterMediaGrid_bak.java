//package com.feximin.mediapicker;
//
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.RecyclerView.LayoutParams;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static android.support.v7.widget.RecyclerView.ViewHolder;
//
///**
// * Created by Neo on 16/1/30.
// */
//public class AdapterMediaGrid_bak extends RecyclerView.Adapter<AdapterMediaGrid_bak.OpViewHolder> {
//
//    private ActivityPicker mActivity;
//    private List<MediaEntity> mDada;
//    private MediaFolder mDataFolder;
//    private LayoutInflater mInflater;
//    private int mItemWidth;
//    private int mItemPadding = 6;
//    private MediaManager mediaManager;
//    public AdapterMediaGrid_bak(ActivityPicker activity){
//        this.mActivity = activity;
//        this.mDada = new ArrayList<>();
//        this.mInflater = LayoutInflater.from(mActivity);
//        DisplayMetrics metrics = mActivity.getResources().getDisplayMetrics();
//        mItemPadding *= metrics.density;
//        mItemWidth = (metrics.widthPixels - 2 * mItemPadding) / 3;
//        mediaManager = MediaManager.getInstance(mActivity);
//    }
//
//
//
//    public void setMediaFolder(MediaFolder folder){
//        mDataFolder = folder;
//        mDada.clear();
//        mDada.addAll(folder.getChildren());
//
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public OpViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        Log.v("onCreateViewHolder", "onCreateViewHolder + type" + viewType);
//        if (viewType == Type.Image.ordinal()){
//            return new ImageViewHolder(mInflater.inflate(R.layout.item_grid, null));
//        }else if (viewType == Type.Video.ordinal()){
//            return new VideoViewHolder(mInflater.inflate(R.layout.item_grid_video, null));
//
//        }else if (viewType == TYPE_OP){
//            return new OpViewHolder(mInflater.inflate(R.layout.item_op, null));
//        }
//        else {
//            throw new IllegalArgumentException("no this kind of viewType");
//        }
//    }
//
//    @Override
//    public void onBindViewHolder(OpViewHolder h, int position) {
//        LayoutParams lp = (LayoutParams) h.itemView.getLayoutParams();
//        if(lp == null){
//            lp = new LayoutParams(mItemWidth, mItemWidth);
//            lp.topMargin = position < 3?0:mItemPadding;
//            lp.rightMargin = position % 3 == 2?0:mItemPadding;
//            h.itemView.setLayoutParams(lp);
//        }
//
//
//        Type type = mDataFolder.getType();
//        if (position == 0){
//            if (type == Type.Image){
//                h.img.setImageResource(R.mipmap.img_op_camera);
//                h.img.setOnClickListener(v -> mActivity.toTakePhoto());
//            }else if (type == Type.Video){
//                h.img.setImageResource(R.mipmap.img_op_video);
//                h.img.setOnClickListener(v -> mActivity.toMakeVideo());
//            }
//        }else {
//            ImageViewHolder holder = (ImageViewHolder) h;
//            int realPosition = position - 1;
//            MediaEntity en = mDada.get(realPosition);
//            holder.imgCheckBox.setSelected(mediaManager.isSelected(en));
//            holder.imgCheckBox.setOnClickListener(v1 -> mediaManager.toggle(en));
//
//
//            Glide.with(mActivity).load(en.getPath()).crossFade().into(holder.img);
//            if (type == Type.Image){
//                holder.foreView.setOnClickListener(v -> ActivityImageShower.startActivity(mActivity, mDataFolder, realPosition));
//            }else if (type == Type.Video){
//                ((VideoViewHolder)holder).txtTime.setText(getSecond(en.getDuration()));
//                holder.foreView.setOnClickListener(v -> ActivityVideoShower.startActivity(mActivity, en));
//            }
//        }
//    }
//
//    private StringBuilder getSecond(long milli){
//        long second = milli/1000;
//        long hour = second / 3600;
//        long minute = second % 3600 / 60;
//        second = second % 3600 % 60;
//        StringBuilder sb = new StringBuilder();
//        if (hour > 0){
//            if (hour < 10){
//                sb.append(0);
//            }
//            sb.append(hour).append(":");
//        }
//        if (minute < 10){
//            sb.append("0");
//        }
//        sb.append(minute).append(":");
//        if (second < 10){
//            sb.append(0);
//        }
//        sb.append(second);
//        return sb;
//    }
//
//    private static final int TYPE_OP = 1024;        //第一个item，表示拍照或者录像
//
//    @Override
//    public int getItemViewType(int position) {
//        if (position == 0) return TYPE_OP;
//        return mDataFolder.getType().ordinal();
//    }
//
//
//
//    @Override
//    public int getItemCount() {
//        int count = mDada.size();
//        if (count > 0) count ++;
//        return count;
//    }
//
//    public static class OpViewHolder extends ViewHolder{
//        protected ImageView img;
//        public OpViewHolder(View itemView) {
//            super(itemView);
//            img = (ImageView) itemView.findViewById(R.id.img);
//        }
//    }
//
//    public static class ImageViewHolder extends OpViewHolder {
//
//        protected ImageView imgCheckBox;
//        protected View foreView;
//        public ImageViewHolder(View itemView) {
//            super(itemView);
//            imgCheckBox = (ImageView) itemView.findViewById(R.id.img_check_box);
//            foreView = itemView.findViewById(R.id.mask_foreground);
//
//        }
//    }
//
//    public static class VideoViewHolder extends ImageViewHolder{
//
//        TextView txtTime;
//        public VideoViewHolder(View itemView) {
//            super(itemView);
//            txtTime = (TextView) itemView.findViewById(R.id.txt_time);
//        }
//    }
//}
