package com.suidadabian.lixiaofeng.pilipili.detail;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class InfoPictureFragmentPagerAdapter extends FragmentPagerAdapter {
    private FragmentPage[] mFragmentPages;

    public InfoPictureFragmentPagerAdapter(FragmentManager fragmentManager, FragmentPage... fragmentPages) {
        super(fragmentManager);
        mFragmentPages = fragmentPages;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentPages[position].fragment;
    }

    @Override
    public int getCount() {
        return mFragmentPages == null ? 0 : mFragmentPages.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentPages[position].title;
    }

    public static class FragmentPage {
        private Fragment fragment;
        private String title;

        public FragmentPage(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }
    }
}
