package com.suidadabian.lixiaofeng.pilipili.detail;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.Constant;
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.model.User;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.user.UserActivity;
import com.suidadabian.lixiaofeng.pilipili.watch.PictureWatchActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class InfoPictureActivity extends AppCompatActivity {
    public static final String kEY_INFO_PICTURE_ID = "info_picture_id";
    private RelativeLayout mRootRelativeLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ImageView mToolbarAvatarIv;
    private TextView mToolbarUsernameTv;
    private ImageView mInfoPictureIv;
    private ImageButton mFullScreenIbtn;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private RelativeLayout mContainerRelativeLayout;
    private TextView mInputTextTv;
    private ImageButton mSendIbtn;
    private InputTextPopupWindow mInputTextPopupWindow;
    private InfoPicture mInfoPicture;
    private InfoPictureFragmentPagerAdapter mInfoPictureFragmentPagerAdapter;
    private InfoPictureIntroFragment mInfoPictureIntroFragment;
    private InfoPictureCommentFragment mInfoPictureCommentFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_info_picture);
        initView();
        initData();
    }

    private void initView() {
        mRootRelativeLayout = findViewById(R.id.info_picture_root_relative_layout);
        mAppBarLayout = findViewById(R.id.info_picture_app_bar_layout);
        mToolbar = findViewById(R.id.info_picture_toolbar);
        mInfoPictureIv = findViewById(R.id.info_picture_riv);
        mFullScreenIbtn = findViewById(R.id.info_picture_full_screen_ibtn);
        mTabLayout = findViewById(R.id.info_picture_tab_layout);
        mViewPager = findViewById(R.id.info_picture_view_pager);
        mContainerRelativeLayout = findViewById(R.id.info_picture_input_text_container);
        mInputTextTv = findViewById(R.id.include_input_text_tv);
        mSendIbtn = findViewById(R.id.include_input_text_ibtn);

        View toolbarContentView = getLayoutInflater().inflate(R.layout.layout_info_picture_toolbar_content, null);
        mToolbarAvatarIv = toolbarContentView.findViewById(R.id.info_picture_toolbar_avatar_civ);
        mToolbarUsernameTv = toolbarContentView.findViewById(R.id.info_picture_toolbar_username_tv);
        mToolbar.addView(toolbarContentView);
        mToolbar.setNavigationOnClickListener(v -> finish());
        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
                mToolbarAvatarIv.setVisibility(View.GONE);
                mToolbarUsernameTv.setVisibility(View.GONE);
            } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                mToolbarAvatarIv.setVisibility(View.VISIBLE);
                mToolbarUsernameTv.setVisibility(View.VISIBLE);
            }
        });
        mToolbarAvatarIv.setOnClickListener(v -> {
            if (mInfoPicture == null) {
                return;
            }

            Intent intent = new Intent(InfoPictureActivity.this, UserActivity.class);
            intent.putExtra(UserActivity.KEY_USER_ID, mInfoPicture.getUserId());
            startActivity(intent);
        });

        mFullScreenIbtn.setOnClickListener(v -> {
            if (mInfoPicture == null) {
                return;
            }

            Intent intent = new Intent(InfoPictureActivity.this, PictureWatchActivity.class);
            intent.putExtra(PictureWatchActivity.KEY_PICTURE_URL, mInfoPicture.getUrl());
            intent.putExtra(PictureWatchActivity.KEY_FLAG_DOWNLOAD_ENABLE, true);
            startActivity(intent);
        });

        long infoPictureId = getIntent().getLongExtra(kEY_INFO_PICTURE_ID, 0);
        mInfoPictureIntroFragment = InfoPictureIntroFragment.newInstance(infoPictureId);
        mInfoPictureCommentFragment = InfoPictureCommentFragment.newInstance(infoPictureId);
        mInfoPictureFragmentPagerAdapter = new InfoPictureFragmentPagerAdapter(getSupportFragmentManager(),
                new InfoPictureFragmentPagerAdapter.FragmentPage(mInfoPictureIntroFragment, "简介"),
                new InfoPictureFragmentPagerAdapter.FragmentPage(mInfoPictureCommentFragment, "评论"));
        mViewPager.setAdapter(mInfoPictureFragmentPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mContainerRelativeLayout.setVisibility(mInfoPictureFragmentPagerAdapter.getItem(position) == mInfoPictureCommentFragment ? View.VISIBLE : View.GONE);
            }
        });
        mTabLayout.setupWithViewPager(mViewPager);

        mContainerRelativeLayout.setVisibility(View.GONE);
        mInputTextTv.setOnClickListener(v -> {
            if (mInputTextPopupWindow == null) {
                initInputTextPopupWindow();
            }
            mInputTextPopupWindow.showAtLocation(mRootRelativeLayout, Gravity.BOTTOM, 0, 0);
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 0.3f;
            getWindow().setAttributes(params);
        });
        mSendIbtn.setOnClickListener(v -> {
            if (mInfoPictureCommentFragment != null) {
                mInfoPictureCommentFragment.sendComment(mInputTextTv.getText().toString());
            }
        });
    }

    private void initData() {
        PiliPiliServer.getInstance().getInfoPicture(getIntent().getLongExtra(kEY_INFO_PICTURE_ID, 0))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(infoPicture -> {
                    getInfoPictureSuccess(infoPicture);
                    return infoPicture.getUserId();
                })
                .observeOn(Schedulers.io())
                .flatMap(userId -> PiliPiliServer.getInstance().getUser(userId))
                .observeOn(AndroidSchedulers.mainThread())
                .map(user -> {
                    getUserSuccess(user);
                    return Constant.NULL;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object -> initDataSuccess(), throwable -> onInitDataFail(throwable));
    }

    private void getInfoPictureSuccess(InfoPicture infoPicture) {
        mInfoPicture = infoPicture;

        GlideApp.with(this)
                .load(infoPicture.getUrl())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(mInfoPictureIv);
    }

    private void getUserSuccess(User user) {
        mToolbarUsernameTv.setText(user.getName());
        GlideApp.with(InfoPictureActivity.this)
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(mToolbarAvatarIv);
    }

    private void initDataSuccess() {
    }

    private void onInitDataFail(Throwable throwable) {
        new NetExceptionHandler().handleException(this, throwable);
    }

    private void initInputTextPopupWindow() {
        View view = getLayoutInflater().inflate(R.layout.popup_input_text, null);
        mInputTextPopupWindow = new InputTextPopupWindow(view, MATCH_PARENT, WRAP_CONTENT);
        mInputTextPopupWindow.setOutsideTouchable(true);
        mInputTextPopupWindow.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.half_transparent_black, null)));
        mInputTextPopupWindow.setCallback(inputText -> {
            if (mInfoPictureCommentFragment != null) {
                mInfoPictureCommentFragment.sendComment(inputText);
            }
        });
        mInputTextPopupWindow.setOnDismissListener(() -> {
            mInputTextTv.setText(mInputTextPopupWindow.getInputText());
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.alpha = 1.0f;
            getWindow().setAttributes(params);
        });
    }
}
