package com.suidadabian.lixiaofeng.pilipili.user;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.model.User;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserActivity extends AppCompatActivity {
    public static final String KEY_USER_ID = "user_id";
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private TextView mToolbarUsernameTv;
    private ImageView mAvatarIv;
    private TextView mUsernameTv;
    private TextView mSignTv;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private UserFragmentPagerAdapter mUserFragmentPagerAdapter;
    private UserInfoPictureFragment mUserInfoPictureFragment;
    private UserLightPictureFragment mUserLightPictureFragment;
    private long mUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user);
        initData();
        initView();
    }

    private void initView() {
        mAppBarLayout = findViewById(R.id.user_app_bar_layout);
        mToolbar = findViewById(R.id.user_toolbar);
        mAvatarIv = findViewById(R.id.user_avatar_civ);
        mUsernameTv = findViewById(R.id.user_name_tv);
        mSignTv = findViewById(R.id.user_sign_tv);
        mTabLayout = findViewById(R.id.user_tab_layout);
        mViewPager = findViewById(R.id.user_view_pager);

        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                mToolbarUsernameTv.setVisibility(View.GONE);
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                mToolbarUsernameTv.setVisibility(View.VISIBLE);
            }
        });
        View toolbarContentView = getLayoutInflater().inflate(R.layout.layout_user_toolbar_content, null);
        mToolbarUsernameTv = toolbarContentView.findViewById(R.id.user_toolbar_username_tv);
        mToolbar.addView(toolbarContentView);
        mToolbar.setNavigationOnClickListener(v -> finish());

        mUserInfoPictureFragment = UserInfoPictureFragment.newInstance(mUserId);
        mUserLightPictureFragment = UserLightPictureFragment.newInstance(mUserId);
        mUserFragmentPagerAdapter = new UserFragmentPagerAdapter(getSupportFragmentManager(),
                new UserFragmentPagerAdapter.FragmentPage(mUserInfoPictureFragment, "重图"),
                new UserFragmentPagerAdapter.FragmentPage(mUserLightPictureFragment, "轻图"));
        mViewPager.setAdapter(mUserFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initData() {
        mUserId = getIntent().getLongExtra(KEY_USER_ID, 0);

        PiliPiliServer.getInstance().getUser(mUserId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> initDataSuccess(user), throwable -> initDataFail(throwable));
    }

    private void initDataSuccess(User user) {
        mToolbarUsernameTv.setText(user.getName());
        mUsernameTv.setText(user.getName());
        mSignTv.setText(user.getSign());
        GlideApp.with(UserActivity.this)
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(mAvatarIv);
    }

    private void initDataFail(Throwable throwable) {
        new NetExceptionHandler().handleException(this, throwable);
    }
}
