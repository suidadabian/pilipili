package com.suidadabian.lixiaofeng.pilipili.net;

import android.content.Context;
import android.content.SharedPreferences;

import com.suidadabian.lixiaofeng.pilipili.PiliPiliApplication;
import com.suidadabian.lixiaofeng.pilipili.model.Comment;
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;
import com.suidadabian.lixiaofeng.pilipili.model.Reply;
import com.suidadabian.lixiaofeng.pilipili.model.User;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class PiliPiliServer implements PiliPiliApi {
    private static final String SP_NAME = "ip_sp";
    private static final String KEY_IP = "ip";
    private static final String DEFAULT_IP = "140.82.45.203:8080";
    private static final String WAR = "PiliPiliWeb_Tomcat_war";
    private static PiliPiliServer instance;
    private SharedPreferences mSharedPrefereces;
    private Retrofit retrofit;
    private PiliPiliApi piliPiliApi;

    private PiliPiliServer() {
        mSharedPrefereces = PiliPiliApplication.getContext().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
    }

    public static PiliPiliServer getInstance() {
        if (instance == null) {
            instance = new PiliPiliServer();
        }

        return instance;
    }

    public void change(OkHttpClient okHttpClient, String ip) {
        mSharedPrefereces.edit().putString(KEY_IP, ip).apply();
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + ip + "/" + WAR + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        piliPiliApi = retrofit.create(PiliPiliApi.class);
    }

    public void init(OkHttpClient okHttpClient) {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://" + mSharedPrefereces.getString(KEY_IP, DEFAULT_IP) + "/" + WAR + "/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();

        piliPiliApi = retrofit.create(PiliPiliApi.class);
    }

    @Override
    public Observable<User> login(String account, String password) {
        return piliPiliApi.login(account, password);
    }

    @Override
    public Observable<User> registered(String account, String username, String password) {
        return piliPiliApi.registered(account, username, password);
    }

    @Override
    public Observable<User> getUser(long id) {
        return piliPiliApi.getUser(id);
    }

    @Override
    public Observable<List<InfoPicture>> getInfoPictures(int pageNo, int pageSize) {
        return piliPiliApi.getInfoPictures(pageNo, pageSize);
    }

    @Override
    public Observable<List<LightPicture>> getLightPictures(int pageNo, int pageSize) {
        return piliPiliApi.getLightPictures(pageNo, pageSize);
    }

    @Override
    public Observable<InfoPicture> getInfoPicture(long id) {
        return piliPiliApi.getInfoPicture(id);
    }

    @Override
    public Observable<LightPicture> getLightPicture(long id) {
        return piliPiliApi.getLightPicture(id);
    }

    @Override
    public Observable<List<Comment>> getComments(long infoPictureId, int pageNo, int pageSize) {
        return piliPiliApi.getComments(infoPictureId, pageNo, pageSize);
    }

    @Override
    public Observable<Comment> getComment(long id) {
        return piliPiliApi.getComment(id);
    }

    @Override
    public Observable<List<Reply>> getReplies(long commentId, int pageNo, int pageSize) {
        return piliPiliApi.getReplies(commentId, pageNo, pageSize);
    }

    @Override
    public Observable<List<InfoPicture>> getRecommendInfoPictures(long infoPictureId) {
        return piliPiliApi.getRecommendInfoPictures(infoPictureId);
    }

    @Override
    public Observable<Comment> sendComment(Comment comment) {
        return piliPiliApi.sendComment(comment);
    }

    @Override
    public Observable<Reply> sendReply(Reply reply) {
        return piliPiliApi.sendReply(reply);
    }

    @Override
    public Observable<InfoPicture> uploadInfoPicture(InfoPicture infoPicture) {
        return piliPiliApi.uploadInfoPicture(infoPicture);
    }

    @Override
    public Observable<LightPicture> uploadLightPicture(LightPicture lightPicture) {
        return piliPiliApi.uploadLightPicture(lightPicture);
    }

    @Override
    public Observable<List<InfoPicture>> getInfoPictures(long userId, int pageNo, int pageSize, boolean self) {
        return piliPiliApi.getInfoPictures(userId, pageNo, pageSize, self);
    }

    @Override
    public Observable<List<LightPicture>> getLightPictures(long userId, int pageNo, int pageSize, boolean self) {
        return piliPiliApi.getLightPictures(userId, pageNo, pageSize, self);
    }

    @Override
    public Observable<InfoPicture> deleteInfoPicture(long userId, long infoPictureId) {
        return piliPiliApi.deleteInfoPicture(userId, infoPictureId);
    }

    @Override
    public Observable<LightPicture> deleteLightPicture(long userId, long lightPictureId) {
        return piliPiliApi.deleteLightPicture(userId, lightPictureId);
    }

    @Override
    public Observable<User> modifyUserInfo(User user) {
        return piliPiliApi.modifyUserInfo(user);
    }
}
