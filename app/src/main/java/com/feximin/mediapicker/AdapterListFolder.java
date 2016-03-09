package com.feximin.mediapicker;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
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
public class AdapterListFolder extends RecyclerView.Adapter<AdapterListFolder.ViewHolder> {

    private final LayoutInflater mInflater;
    private List<MediaFolder> mData;

    private Activity mActivity;
    public AdapterListFolder(Activity activity){
        this.mActivity = activity;
        this.mInflater = LayoutInflater.from(mActivity);
        this.mData = new ArrayList<>();
    }

    public void add(List<MediaFolder> list){
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.item_list_folder, null));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        MediaFolder folder = mData.get(position);
        Glide.with(mActivity).load(folder.getAlbumPath()).crossFade().into(holder.imgAlbum);
        holder.txtName.setText(String.format("%s(%d)", folder.getName(), folder.getNum()));
        holder.itemView.setOnClickListener(v -> {
            if(mOnItemClickListener != null) mOnItemClickListener.onFolderClick(folder, position);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView imgAlbum;
        TextView txtName;
        public ViewHolder(View itemView) {
            super(itemView);
            imgAlbum = (ImageView) itemView.findViewById(R.id.img_album);
            txtName = (TextView) itemView.findViewById(R.id.txt_name_and_count);
        }
    }

    private OnFolderClickListener mOnItemClickListener;

    public void setOnFolderClickListener(OnFolderClickListener listener){
        this.mOnItemClickListener = listener;
    }

    public interface OnFolderClickListener{
        void onFolderClick(MediaFolder folder, int position);
    }
}
