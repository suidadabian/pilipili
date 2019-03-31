package com.suidadabian.lixiaofeng.pilipili.net;

import com.suidadabian.lixiaofeng.pilipili.common.Constant;
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;

import java.io.File;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ImgurServer {
    private static ImgurServer instance;
    private Retrofit rerofit;
    private ImgurAPI imgurAPI;

    private ImgurServer() {

    }

    public static ImgurServer getInstance() {
        if (instance == null) {
            instance = new ImgurServer();
        }

        return instance;
    }

    public void init(OkHttpClient okHttpClient) {
        rerofit = new Retrofit.Builder()
                .baseUrl(ImgurAPI.SERVER)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        imgurAPI = rerofit.create(ImgurAPI.class);
    }

    public Observable<UploadPicture> postImage(String url) {
        if (url == null) {
            return null;
        }

        File file = new File(url);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return imgurAPI.postImage(Constant.CLIENT_AUTH, requestBody);
    }

    public static class UploadPicture {
        private int width;
        private int height;
        private String link;
        private String deletehash;

        public int getWidht() {
            return width;
        }

        public int getHeight() {
            return height;
        }

        public String getLink() {
            return link;
        }

        public String getDeletehash() {
            return deletehash;
        }
    }
}
