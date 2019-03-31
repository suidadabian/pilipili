package com.suidadabian.lixiaofeng.pilipili.net;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.suidadabian.lixiaofeng.pilipili.PiliPiliApplication;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

public class CheckConnectInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (isConnected()) {
            return chain.proceed(chain.request());
        }

        throw new NoConnectException();
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) PiliPiliApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }

        return false;
    }
}
