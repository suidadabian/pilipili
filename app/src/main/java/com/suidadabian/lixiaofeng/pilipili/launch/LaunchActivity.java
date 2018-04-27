package com.suidadabian.lixiaofeng.pilipili.launch;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserSpManager;
import com.suidadabian.lixiaofeng.pilipili.login.LoginActivity;
import com.suidadabian.lixiaofeng.pilipili.main.MainActivity;

public class LaunchActivity extends AppCompatActivity {
    private static long LEAST_JUMP_DELAY = 5000L;
    private long mStartTime;
    private ImageView mLaunchIv;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launch);
        mLaunchIv = findViewById(R.id.launch_iv);

        GlideApp.with(this)
                // TODO: 2018/4/23 更换图片
                .load(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mLaunchIv);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mStartTime = System.currentTimeMillis();
    }

    private void init() {
        // TODO: 2018/4/23 进行应用的初始化
        long delay = System.currentTimeMillis() - mStartTime;
        delay = Math.min(delay, LEAST_JUMP_DELAY);
        if (delay < 0) {
            delay = 0;
        }
        new Handler().postDelayed(() -> {
            Intent intent;
            if (!UserSpManager.getInstance().isLogin()) {
                // TODO: 2018/4/23 未登录逻辑
                intent = new Intent(LaunchActivity.this, LoginActivity.class);
            } else {
                // TODO: 2018/4/23 已登录逻辑
                intent = new Intent(LaunchActivity.this, MainActivity.class);
            }
            startActivity(intent);
            finish();
        }, delay);
    }
}
