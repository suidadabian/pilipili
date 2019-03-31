package com.suidadabian.lixiaofeng.pilipili.watch;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toolbar;

import com.github.chrisbanes.photoview.PhotoView;
import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.download.DownloadManager;

import java.io.File;

public class PictureWatchActivity extends AppCompatActivity {
    public static String KEY_PICTURE_URL = "picture_url";
    public static String KEY_FLAG_DOWNLOAD_ENABLE = "download_enable";
    private String mPictureUrl;
    private PhotoView mPhotoView;
    private Toolbar mToolbar;
    private ImageButton mDownloadIbtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picture_watch);
        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.picture_watch_toolbar);
        mPhotoView = findViewById(R.id.picture_watch_photo_view);
        mDownloadIbtn = findViewById(R.id.picture_watch_download_ibtn);

        mToolbar.setNavigationOnClickListener(v -> finish());
        mDownloadIbtn.setVisibility(getIntent().getBooleanExtra(KEY_FLAG_DOWNLOAD_ENABLE, false) ? View.VISIBLE : View.GONE);
        mDownloadIbtn.setOnClickListener(v -> {
            DownloadManager.getInstance().downloadPicture(mPictureUrl, new DownloadManager.Callback() {
                @Override
                public void onSuccess(String pictureUrl, File file) {
                    // do nothing
                }

                @Override
                public void onFail(String pictureUrl, Exception e) {
                    // do nothing
                }
            });
        });

        mPhotoView.setOnClickListener(v -> {
            mToolbar.setVisibility(mToolbar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            mDownloadIbtn.setVisibility(mDownloadIbtn.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        });
        mPictureUrl = getIntent().getStringExtra(KEY_PICTURE_URL);
        GlideApp.with(this)
                .load(mPictureUrl)
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(mPhotoView);

    }
}
