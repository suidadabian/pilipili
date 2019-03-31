package com.suidadabian.lixiaofeng.pilipili.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toolbar;

import com.suidadabian.lixiaofeng.pilipili.R;

public class SettingActivity extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.setting_toolbar);

        mToolbar.setTitle("设置");
        mToolbar.setNavigationOnClickListener(v -> finish());

        getFragmentManager().beginTransaction()
                .replace(R.id.setting_container_fl, new SettingFragment(), SettingFragment.class.getSimpleName())
                .commit();
    }
}
