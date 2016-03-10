package com.feximin.mediapicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by Neo on 16/3/8.
 */
public class ActivityImageShower extends ActivityShower implements ViewPager.OnPageChangeListener {

    private ViewPager mPager;
    private AdapterGallery mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int curIndex = getIntent().getIntExtra(CUR_INDEX, 0);
        MediaFolder mCurMediaFolder = getIntent().getParcelableExtra(MEDIA_FOLDER);

        this.mPager = (ViewPager) findViewById(R.id.view_pager);
        this.mPager.addOnPageChangeListener(this);
        this.mAdapter = new AdapterGallery(this);
        this.mAdapter.add(mCurMediaFolder.getChildren());
        this.mPager.setAdapter(mAdapter);
        this.mPager.setCurrentItem(curIndex);
    }

    private void setTitleText(int cur){
        this.mTxtTitle.setText(String.format("%s/%s", cur + 1, mAdapter.getCount()));
    }


    @Override
    protected void toggle(){
        MediaEntity entity = mAdapter.getItem(mPager.getCurrentItem());
        mImgCheck.setSelected(mMediaManager.toggle(entity)== MediaManager.Status.Add);
    }

    @Override
    protected Type getCurType() {
        return Type.Image;
    }

    public static final String MEDIA_FOLDER = "media_folder";
    public static final String CUR_INDEX = "cur_index";
    public static void startActivity(Activity activity, MediaFolder folder, int curIndex){
        Intent intent = new Intent(activity, ActivityImageShower.class);
        intent.putExtra(CUR_INDEX, curIndex);
        intent.putExtra(MEDIA_FOLDER, folder);
        activity.startActivity(intent);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setTitleText(position);
        MediaEntity entity = mAdapter.getItem(position);
        mImgCheck.setSelected(mMediaManager.isSelected(entity));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_image_shower;
    }

    class AdapterGallery extends PagerAdapter {
        private List<MediaEntity> mData = new ArrayList<>();
        private Activity mActivity;

        public AdapterGallery(Activity activity) {
            mActivity = activity;
        }

        public void add(MediaEntity path) {
            mData.add(path);
            notifyDataSetChanged();
        }

        public void add(List<MediaEntity> list) {
            mData.addAll(list);
            notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView photoView = new PhotoView(mActivity);
            Glide.with(mActivity).load(mData.get(position).getPath()).crossFade().into(photoView);
            container.addView(photoView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return photoView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        public MediaEntity getItem(int position){
            return mData.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
