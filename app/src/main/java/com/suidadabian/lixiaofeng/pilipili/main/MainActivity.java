package com.suidadabian.lixiaofeng.pilipili.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserManager;
import com.suidadabian.lixiaofeng.pilipili.download.DownloadActivity;
import com.suidadabian.lixiaofeng.pilipili.model.User;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.setting.SettingActivity;
import com.suidadabian.lixiaofeng.pilipili.setting.UserInfoActivity;
import com.suidadabian.lixiaofeng.pilipili.upload.UploadInfoPictureActivity;
import com.suidadabian.lixiaofeng.pilipili.upload.UploadLightPictureActivity;
import com.suidadabian.lixiaofeng.pilipili.user.UserActivity;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private CircleImageView mToolAvatarCiv;
    private TextView mToolUsernameTv;
    private FloatingActionButton mAddFab;
    private InfoPicturesFragment mInfoPicturesFragment;
    private LightPicturesFragment mLightPicturesFragment;
    private MainPagerAdapter mMainPagerAdapter;
    private BroadcastReceiver mUserInfoModifyBroadcastReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initData();
        initView();
        initBroadcastReceiver();
    }

    private void initView() {
        mToolbar = findViewById(R.id.main_toolbar);
        mViewPager = findViewById(R.id.main_view_pager);
        mTabLayout = findViewById(R.id.main_tab_layout);
        mAddFab = findViewById(R.id.main_add_fab);

        View toolbarContentView = LayoutInflater.from(this).inflate(R.layout.layout_main_toolbar_content, null);
        mToolAvatarCiv = toolbarContentView.findViewById(R.id.main_toolbar_avatar_civ);
        mToolUsernameTv = toolbarContentView.findViewById(R.id.main_toolbar_username_tv);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        toolbarContentView.setLayoutParams(layoutParams);
        mToolbar.addView(toolbarContentView);
        mToolAvatarCiv.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UserActivity.class);
            intent.putExtra(UserActivity.KEY_USER_ID, UserManager.getInstance().getUser().getId());
            startActivity(intent);
        });
        mToolbar.inflateMenu(R.menu.menu_main);
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_main_setting: {
                    Intent intent = new Intent(MainActivity.this, SettingActivity.class);
                    startActivity(intent);
                    break;
                }
                case R.id.item_main_download: {
                    Intent intent = new Intent(MainActivity.this, DownloadActivity.class);
                    startActivity(intent);
                    break;
                }
                default: {
                    break;
                }
            }
            return true;
        });

        mInfoPicturesFragment = new InfoPicturesFragment();
        mLightPicturesFragment = new LightPicturesFragment();
        mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(),
                new MainPagerAdapter.FragmentPage(mInfoPicturesFragment, "重图"),
                new MainPagerAdapter.FragmentPage(mLightPicturesFragment, "轻图"));
        mViewPager.setAdapter(mMainPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mAddFab.setOnClickListener(v -> {
            Fragment fragment = mMainPagerAdapter.getItem(mViewPager.getCurrentItem());
            if (fragment == mInfoPicturesFragment) {
                Intent intent = new Intent(MainActivity.this, UploadInfoPictureActivity.class);
                startActivity(intent);
            } else if (fragment == mLightPicturesFragment) {
                Intent intent = new Intent(MainActivity.this, UploadLightPictureActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData() {
        PiliPiliServer.getInstance().getUser(UserManager.getInstance().getUser().getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> initDataSuccess(user), throwable -> initDataFail(throwable));
    }

    private void initBroadcastReceiver() {
        mUserInfoModifyBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                User user = intent.getParcelableExtra(UserInfoActivity.KEY_USER);

                if (user == null) {
                    return;
                }

                mToolUsernameTv.setText(user.getName());
                GlideApp.with(MainActivity.this)
                        .load(user.getAvatarUrl())
                        .placeholder(R.drawable.icon_placeholder)
                        .error(R.drawable.icon_error)
                        .into(mToolAvatarCiv);
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UserInfoActivity.ACTION_USER_INFO_MODIFY);
        LocalBroadcastManager.getInstance(this).registerReceiver(mUserInfoModifyBroadcastReceiver, intentFilter);
    }

    private void initDataSuccess(User user) {
        mToolUsernameTv.setText(user.getName());
        GlideApp.with(MainActivity.this)
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(mToolAvatarCiv);
    }

    private void initDataFail(Throwable throwable) {
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mUserInfoModifyBroadcastReceiver);
    }
}
