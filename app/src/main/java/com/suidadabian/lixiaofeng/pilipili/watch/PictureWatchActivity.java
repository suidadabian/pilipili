package com.suidadabian.lixiaofeng.pilipili.watch;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.github.chrisbanes.photoview.PhotoView;
import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;

public class PictureWatchActivity extends AppCompatActivity {
    public static String KEY_PICTURE_URL = "picture_url";
    private String mPictureUrl;
    private PhotoView mPhotoView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picture_watch);
        mPhotoView = findViewById(R.id.photo_view);

        mPictureUrl = getIntent().getStringExtra(KEY_PICTURE_URL);
        GlideApp.with(this)
                .load(mPictureUrl)
                // TODO: 2018/4/23 更换图片
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mPhotoView);
    }
}
