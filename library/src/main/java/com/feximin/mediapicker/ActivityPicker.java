package com.feximin.mediapicker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.io.File;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Neo on 16/1/29.
 */
public abstract class ActivityPicker extends Activity implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        MediaFinder.OnRefreshListener,
        AdapterListFolder.OnFolderSelectListener,
        MediaManager.OnMediaSelectListener{
    private TextView mTxtTitle;
    private View mMask;
    private ImageView mImgIndicator;
    private RecyclerView mListViewFolder;
    private GridView mGridView;
    protected AdapterMediaGrid mAdapter;
    private AdapterListFolder mAdapterListFolder;
    protected MediaManager mMediaManager;
    private TextView mTxtSelectCount;
    private String mDestPhotoUri;
    private String mDestVideoUri;
    private String mDestCropPhotoUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_picker);
        if (savedInstanceState != null){
            mDestPhotoUri = savedInstanceState.getString(DEST_PHOTO_PATH);
            mDestVideoUri = savedInstanceState.getString(DEST_VIDEO_PATH);
            mDestCropPhotoUri = savedInstanceState.getString(DEST_CROP_PHOTO_PATH);
        }

        mTxtTitle = (TextView) findViewById(R.id.txt_title);
        mTxtTitle.setOnClickListener(this);
        mTxtTitle.setText("所有图片");
        mTxtTitle.setEnabled(false);

        mTxtSelectCount = (TextView) findViewById(R.id.txt_right);
        mTxtSelectCount.setOnClickListener(v -> onNextStep());
        mListViewFolder = (RecyclerView) findViewById(R.id.recycler_view_folder);
        mListViewFolder.setLayoutManager(new LinearLayoutManager(this));
        mAdapterListFolder = new AdapterListFolder(this);
        mAdapterListFolder.setOnFolderSelectListener(this);
        mListViewFolder.setAdapter(mAdapterListFolder);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int h = (int) (metrics.heightPixels / 1.6f);
        mListViewFolder.getLayoutParams().height = h;
        mListViewFolder.requestLayout();

        mMask = findViewById(R.id.mask);
        mMask.setOnClickListener(v -> doToggleListFolder());

        mImgIndicator = (ImageView) findViewById(R.id.img_indicator);

        this.mGridView = (GridView) findViewById(R.id.grid_view);
        this.mGridView.setOnItemClickListener(this);
        this.mAdapter = new AdapterMediaGrid(this);
        this.mGridView.setAdapter(this.mAdapter);

        mMediaManager = MediaManager.getInstance(this);
        mMediaManager.initConfig(getConfig());
        mMediaManager.setOnRefreshListener(this);
        mMediaManager.refreshImmediately();
        mMediaManager.addOnMediaSelectListener(this);
    }
    private final SimpleDateFormat sSimpleDateFormat = new SimpleDateFormat("yyyyMMdd__HHmmss");

    protected abstract Config getConfig();

    protected void onNextStep(){

    }
    public String getMediaOutputUri(String suffix){
        String time = sSimpleDateFormat.format(new Date());
        File file = new File(getExternalCacheDir(),time + "." + suffix);
        String uri = file.getAbsolutePath();
        return uri;
    }
    private static final int STATUS_GONE = 0;
    private static final int STATUS_VISIBLE = 1;
    private static final int STATUS_ACTIVE = 2;

    @Status
    private int mCurStatus = STATUS_GONE;

    public void startActivity(Class<? extends Activity> activity){
        startActivity(new Intent(this, activity));
    }

    @Override
    public void onRefreshCompleted(List<MediaFolder> folderList) {
        mTxtTitle.setEnabled(true);
        mCurMediaFolder = folderList.get(0);
        mAdapter.setMediaFolder(mCurMediaFolder);
        mAdapterListFolder.add(folderList);
    }

    private MediaFolder mCurMediaFolder;
    @Override
    public void onFolderSelect(MediaFolder folder, int position) {
        if(mCurStatus != STATUS_VISIBLE) return;
        mCurMediaFolder = folder;
        doToggleListFolder();
        mTxtTitle.setText(folder.getName());
        mAdapter.setMediaFolder(folder);
        onMediaSelect(folder.getType(), mMediaManager.getSelectedCount(folder));
    }

    @Override
    public void onMediaSelect(@MediaEntity.Type int type, int count) {
        if (type == mCurMediaFolder.getType()){
            mTxtSelectCount.setEnabled(count > 0);
            mTxtSelectCount.setText(String.format("下一步(%s)", count));
            mAdapter.notifyDataSetChanged();
        }
    }

    @IntDef({STATUS_GONE, STATUS_VISIBLE, STATUS_ACTIVE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Status{}

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.txt_title) {
            doToggleListFolder();
        }
    }

    private void doToggleListFolder(){
        if(mCurStatus == STATUS_ACTIVE) return;
        if(mCurStatus == STATUS_GONE){
            show();
        }else if(mCurStatus == STATUS_VISIBLE){
            dismiss();
        }
    }

    private final int REQUEST_TAKE_PHOTO = 2048;
    private final int REQUEST_MAKE_VIDEO = 2049;
    private final int REQUEST_CROP_PHOTO = 2050;
    private final String DEST_PHOTO_PATH = "dest_photo_path";
    private final String DEST_VIDEO_PATH = "dest_video_path";
    private final String DEST_CROP_PHOTO_PATH = "dest_crop_photo_path";

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mDestPhotoUri != null) outState.putString(DEST_PHOTO_PATH, mDestPhotoUri);
        if (mDestVideoUri != null) outState.putString(DEST_VIDEO_PATH, mDestVideoUri);
        if (mDestCropPhotoUri != null) outState.putString(DEST_CROP_PHOTO_PATH, mDestCropPhotoUri);
    }

    public void toTakePhoto(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        if (mDestPhotoUri == null) mDestPhotoUri = getMediaOutputUri("jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mDestPhotoUri)));
        try {
            startActivityForResult(intent, REQUEST_TAKE_PHOTO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(ActivityPicker.this, "没有拍照应用", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ActivityPicker.this, "拍照失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void toMakeVideo(){
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_VIDEO_CAPTURE);
        if (mDestVideoUri == null) mDestVideoUri = getMediaOutputUri("mp4");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(mDestVideoUri)));
        try {
            startActivityForResult(intent, REQUEST_MAKE_VIDEO);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(ActivityPicker.this, "没有录像应用", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(ActivityPicker.this, "录制失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == REQUEST_TAKE_PHOTO){
                Config.Crop crop = mMediaManager.getCropInfo();
                if (isValidFile(mDestPhotoUri)) {
                    MediaEntity entity = new MediaEntity(mDestPhotoUri, MediaEntity.IMAGE);
                    mMediaManager.toggle(entity);
                    if (crop == null) {
                        onTakePhoto(mDestPhotoUri);
                    } else {
                        doCropPicture(mDestPhotoUri, crop);
                    }
                }else {
                    Toast.makeText(ActivityPicker.this, "拍照失败", Toast.LENGTH_SHORT).show();
                }

            }else if (requestCode == REQUEST_MAKE_VIDEO){
                if (isValidFile(mDestVideoUri)){
                    MediaEntity entity = new MediaEntity(mDestVideoUri, MediaEntity.VIDEO);
                    mMediaManager.toggle(entity);
                    onMakeVideo(mDestVideoUri);
                }else{
                    Toast.makeText(ActivityPicker.this, "录制失败", Toast.LENGTH_SHORT).show();
                }

            }else if (requestCode == REQUEST_CROP_PHOTO){
                if (isValidFile(mDestPhotoUri)){
                    onCropFinish(mDestCropPhotoUri);
                }else{
                    Toast.makeText(ActivityPicker.this, "裁剪失败", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

    protected void onTakePhoto(String path){ }

    protected void onMakeVideo(String path){}

    protected void onCropFinish(String path){}

    public void doCropPicture(String from, Config.Crop crop) {

        Uri uri = Uri.fromFile(new File(from));
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // crop为true是设置在开启的intent中设置显示的view可以剪裁
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1 * 100);
        intent.putExtra("aspectY", (int) ((1 / crop.cropScale) * 100));

        // outputX,outputY 是剪裁图片的宽高
        intent.putExtra("outputX", crop.outWidth);
        intent.putExtra("outputY", (int) (crop.outWidth / crop.cropScale));
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra("scaleUpIfNeeded", true); //黑边
        intent.putExtra("output", Uri.fromFile(new File(mDestCropPhotoUri = getMediaOutputUri("jpg"))));
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_CROP_PHOTO);
    }

    public static boolean isValidFile(String path){
        if (TextUtils.isEmpty(path)) return false;
        File file = new File(path);
        return isValidFile(file);
    }

    public static boolean isValidFile(File f){
        return f != null && f.exists() && f.length() > 0;
    }
    OvershootInterpolator interpolator = new OvershootInterpolator();
    AnticipateInterpolator anticipateInterpolator = new AnticipateInterpolator();
    private void show(){
        mCurStatus =STATUS_ACTIVE;
        mMask.setVisibility(View.VISIBLE);
        mListViewFolder.setVisibility(View.VISIBLE);
        mImgIndicator.setImageResource(R.mipmap.img_arrow_up);
        AnimatorSet set = new AnimatorSet();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mMask, "alpha", 0f, 1f).setDuration(300);
        int height = mListViewFolder.getHeight();
        if(height == 0){
                Animation translationAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_up_down_in);
                translationAnimation.setInterpolator(interpolator);
                mListViewFolder.startAnimation(translationAnimation);
                set.playTogether(alpha);
        }else{
            ObjectAnimator translationAnimator = ObjectAnimator.ofFloat(mListViewFolder, "translationY", -height, 0).setDuration(300);
            translationAnimator.setInterpolator(interpolator);
            set.playTogether(alpha, translationAnimator);
        }
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurStatus = STATUS_VISIBLE;
                mListViewFolder.clearAnimation();
                mImgIndicator.setImageResource(R.mipmap.img_arrow_up);
            }
        });
        set.start();
    }

    private void dismiss(){
        mCurStatus =STATUS_ACTIVE;
        AnimatorSet set = new AnimatorSet();
        int height = mListViewFolder.getHeight();
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mMask, "alpha", 1f, 0f);
        ObjectAnimator translation = ObjectAnimator.ofFloat(mListViewFolder, "translationY", 0, -height);
        translation.setInterpolator(anticipateInterpolator);
        set.playTogether(alpha, translation);
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mMask.setVisibility(View.GONE);
                mListViewFolder.setVisibility(View.GONE);
                mImgIndicator.setImageResource(R.mipmap.img_arrow_down);
                mCurStatus = STATUS_GONE;
            }
        });
        set.start();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMediaManager.removeOnMediaSelectListener(this);
    }
}
