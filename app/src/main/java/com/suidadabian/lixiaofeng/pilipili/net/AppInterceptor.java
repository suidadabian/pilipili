package com.suidadabian.lixiaofeng.pilipili.net;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AppInterceptor implements Interceptor {
    private JsonParser jsonParser;

    public AppInterceptor() {
        jsonParser = new JsonParser();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String url = request.url().toString();
        if (url.contains(PiliPiliApi.SERVER)) {
            Response response = chain.proceed(request);
            String body = response.body().string();
            JsonObject jsonObject = jsonParser.parse(body).getAsJsonObject();
            int code = jsonObject.get("code").getAsInt();

            if (code != Code.SUCCESS) {
                throw new ServerException(code);
            }

            String data = jsonObject.get("data").toString();
            ResponseBody dataResponseBody = ResponseBody.create(response.body().contentType(), data);
            return response.newBuilder().body(dataResponseBody).build();
        } else if (url.contains(ImgurAPI.SERVER)) {
            Response response = chain.proceed(request);
            String body = response.body().string();
            JsonObject jsonObject = jsonParser.parse(body).getAsJsonObject();
            boolean success = jsonObject.get("success").getAsBoolean();

            if (!success) {
                throw new ServerException(Code.UNKNOWN_ERROR);
            }

            String data = jsonObject.get("data").toString();
            ResponseBody dataResponseBody = ResponseBody.create(response.body().contentType(), data);
            return response.newBuilder().body(dataResponseBody).build();
        }

        return chain.proceed(request);
    }
}
