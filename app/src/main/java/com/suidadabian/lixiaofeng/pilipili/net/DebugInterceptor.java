package com.suidadabian.lixiaofeng.pilipili.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class DebugInterceptor implements Interceptor {
    private static final String TAG = DebugInterceptor.class.getSimpleName();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = chain.proceed(request);
        String body = response.body().string();
        Log.d(TAG, body);
        return response.newBuilder().build();
    }
}
