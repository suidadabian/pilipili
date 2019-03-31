package com.suidadabian.lixiaofeng.pilipili.util;

import android.view.Window;

import com.suidadabian.lixiaofeng.pilipili.PiliPiliApplication;

public final class ScreenUtil {
    private ScreenUtil() {

    }

    public static int getScreenWidth() {
        return PiliPiliApplication.getContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getWindowHeight(Window window) {
        return PiliPiliApplication.getContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static int dpToPx(int dp) {
        return Math.round(PiliPiliApplication.getContext().getResources().getDisplayMetrics().density * dp);
    }

    public static int pxToDp(int px) {
        return Math.round(px / PiliPiliApplication.getContext().getResources().getDisplayMetrics().density);
    }
}
