package com.suidadabian.lixiaofeng.pilipili.net;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

public class OkHttpManager {
    private static final long DEFALUT_CONNECT_TIMEOUT = 60000;
    private static final long DEFALUT_WRITE_TIMEOUT = 60000;
    private static final long DEFALUT_READ_TIMEOUT = 60000;
    private static OkHttpManager instance;
    private OkHttpClient okHttpClient;

    private OkHttpManager() {

    }

    public static OkHttpManager getInstance() {
        if (instance == null) {
            instance = new OkHttpManager();
        }

        return instance;
    }

    public void init(Interceptor... interceptors) {
        init(DEFALUT_CONNECT_TIMEOUT, DEFALUT_WRITE_TIMEOUT, DEFALUT_READ_TIMEOUT, interceptors);
    }

    public void init(long connectTimeout, long writeTimeout, long readTimeout, Interceptor... interceptors) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS);

        for (Interceptor interceptor : interceptors) {
            builder.addInterceptor(interceptor);
        }

        okHttpClient = builder.build();
    }

    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
}
