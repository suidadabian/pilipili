package com.suidadabian.lixiaofeng.pilipili.download;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import com.suidadabian.lixiaofeng.pilipili.R;

public class DownloadActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_download);
        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.download_toolbar);

        mToolbar.setTitle("图片缓存");
        mToolbar.setNavigationOnClickListener(v -> finish());

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.download_container_fl, DownloadFragment.newInstance(), DownloadFragment.class.getSimpleName())
                .commit();
    }
}
