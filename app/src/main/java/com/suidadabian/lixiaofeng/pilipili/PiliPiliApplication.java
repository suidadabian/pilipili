package com.suidadabian.lixiaofeng.pilipili;

import android.app.Application;
import android.content.Context;

import com.suidadabian.lixiaofeng.pilipili.net.AppInterceptor;
import com.suidadabian.lixiaofeng.pilipili.net.CheckConnectInterceptor;
import com.suidadabian.lixiaofeng.pilipili.net.ImgurServer;
import com.suidadabian.lixiaofeng.pilipili.net.OkHttpManager;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;

public class PiliPiliApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();


        initOkHttpManger();
        initPiliPiliServer();
        initImgurServer();
    }

    public static Context getContext() {
        return context;
    }

    private void initOkHttpManger() {
        OkHttpManager.getInstance().init(new CheckConnectInterceptor(), new AppInterceptor());
    }

    private void initPiliPiliServer() {
        PiliPiliServer.getInstance().init(OkHttpManager.getInstance().getOkHttpClient());
    }

    private void initImgurServer() {
        ImgurServer.getInstance().init(OkHttpManager.getInstance().getOkHttpClient());
    }


}
