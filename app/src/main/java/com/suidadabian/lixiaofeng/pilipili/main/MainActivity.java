package com.suidadabian.lixiaofeng.pilipili.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.suidadabian.lixiaofeng.pilipili.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout mDrawerLayout;
    private AppBarLayout mAppBarLayout;
    private Toolbar mToolbar;
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;
    private NavigationView mNavigationView;
    private CircleImageView mToolAvatarCiv;
    private TextView mToolUsernameTv;
    private CircleImageView mNavigationAvatarCiv;
    private TextView mNavigationUsernameTv;
    private InfoPicturesFragment mInfoPicturesFragment;
    private LightPicturesFragment mLightPicturesFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mDrawerLayout = findViewById(R.id.main_drawer_layout);
        mAppBarLayout = findViewById(R.id.main_app_bar_layout);
        mToolbar = findViewById(R.id.main_toolbar);
        mViewPager = findViewById(R.id.main_view_pager);
        mBottomNavigationView = findViewById(R.id.main_bottom_navigation_view);
        mNavigationView = findViewById(R.id.main_navigation_view);
        mNavigationAvatarCiv = findViewById(R.id.main_navigation_header_avatar_civ);
        mNavigationUsernameTv = findViewById(R.id.main_navigation_header_username_tv);


        mToolbar.setNavigationIcon(R.drawable.icon_main_menu);
        View toolbarContentView = LayoutInflater.from(this).inflate(R.layout.layout_main_toolbar_content, null);
        mToolAvatarCiv = toolbarContentView.findViewById(R.id.main_toolbar_avatar_civ);
        mToolUsernameTv = toolbarContentView.findViewById(R.id.main_toolbar_username_tv);
        Toolbar.LayoutParams layoutParams = new Toolbar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER_VERTICAL;
        toolbarContentView.setLayoutParams(layoutParams);
        mToolbar.addView(toolbarContentView);
        // TODO: 2018/4/25 设置用户名
        // TODO: 2018/4/25 设置头像
        mToolAvatarCiv.setOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));
        mToolbar.setNavigationOnClickListener(v -> mDrawerLayout.openDrawer(GravityCompat.START));

        mInfoPicturesFragment = new InfoPicturesFragment();
        mLightPicturesFragment = new LightPicturesFragment();
        mViewPager.setAdapter(new MainPagerAdapter(getSupportFragmentManager(), new Fragment[]{mInfoPicturesFragment, mLightPicturesFragment}));
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mAppBarLayout.setExpanded(true);
                mBottomNavigationView.setSelectedItemId(mBottomNavigationView.getMenu().getItem(position).getItemId());
            }
        });
        mBottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Menu menu = mBottomNavigationView.getMenu();
            for (int i = 0; i < menu.size(); i++) {
                if (menu.getItem(i) != item) {
                    continue;
                }

                mAppBarLayout.setExpanded(true);
                mViewPager.setCurrentItem(i);
                return true;
            }

            return false;
        });
    }
}
