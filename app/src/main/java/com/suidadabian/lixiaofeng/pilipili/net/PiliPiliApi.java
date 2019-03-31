package com.suidadabian.lixiaofeng.pilipili.net;

import com.suidadabian.lixiaofeng.pilipili.model.Comment;
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;
import com.suidadabian.lixiaofeng.pilipili.model.Reply;
import com.suidadabian.lixiaofeng.pilipili.model.User;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 *
 */
public interface PiliPiliApi {
    String SERVER = "http://140.82.45.203:8080/PiliPiliWeb_Tomcat_war/";

    /**
     * 完成
     *
     * @param account
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login/login")
    Observable<User> login(
            @Field("account") String account,
            @Field("password") String password
    );

    /**
     * 完成
     *
     * @param account
     * @param username
     * @param password
     * @return
     */
    @FormUrlEncoded
    @POST("login/registered")
    Observable<User> registered(
            @Field("account") String account,
            @Field("username") String username,
            @Field("password") String password
    );

    /**
     * 完成
     *
     * @param id
     * @return
     */
    @GET("user/getUser")
    Observable<User> getUser(
            @Query("id") long id
    );

    /**
     * 完成
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("infoPicture/getInfoPictures")
    Observable<List<InfoPicture>> getInfoPictures(
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize
    );

    /**
     * 完成
     *
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("lightPicture/getLightPictures")
    Observable<List<LightPicture>> getLightPictures(
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize
    );

    /**
     * 完成
     *
     * @param id
     * @return
     */
    @GET("infoPicture/getInfoPicture")
    Observable<InfoPicture> getInfoPicture(
            @Query("id") long id
    );

    @GET("lightPicture/getLightPicture")
    Observable<LightPicture> getLightPicture(
            @Query("id") long id
    );

    /**
     * 完成
     *
     * @param infoPictureId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("comment/getComments")
    Observable<List<Comment>> getComments(
            @Query("infoPictureId") long infoPictureId,
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize);

    /**
     * 完成
     *
     * @param id
     * @return
     */
    @GET("comment/getComment")
    Observable<Comment> getComment(
            @Query("id") long id
    );

    /**
     * 完成
     *
     * @param commentId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @GET("reply/getReplies")
    Observable<List<Reply>> getReplies(
            @Query("commentId") long commentId,
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize);

    /**
     * 完成
     *
     * @param infoPictureId
     * @return
     */
    @GET("infoPicture/getRecommendInfoPictures")
    Observable<List<InfoPicture>> getRecommendInfoPictures(
            @Query("infoPictureId") long infoPictureId
    );

    /**
     * 完成
     *
     * @param comment
     * @return
     */
    @POST("comment/sendComment")
    Observable<Comment> sendComment(
            @Body Comment comment
    );

    /**
     * 完成
     *
     * @param reply
     * @return
     */
    @POST("reply/sendReply")
    Observable<Reply> sendReply(
            @Body Reply reply
    );

    /**
     * 完成
     *
     * @param infoPicture
     * @return
     */
    @POST("infoPicture/uploadInfoPicture")
    Observable<InfoPicture> uploadInfoPicture(
            @Body InfoPicture infoPicture
    );

    /**
     * 完成
     *
     * @param lightPicture
     * @return
     */
    @POST("lightPicture/uploadLightPicture")
    Observable<LightPicture> uploadLightPicture(
            @Body LightPicture lightPicture
    );

    /**
     * 完成
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param self
     * @return
     */
    @GET("infoPicture/getUserInfoPictures")
    Observable<List<InfoPicture>> getInfoPictures(
            @Query("userId") long userId,
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize,
            @Query("self") boolean self
    );

    /**
     * 完成
     *
     * @param userId
     * @param pageNo
     * @param pageSize
     * @param self
     * @return
     */
    @GET("lightPicture/getUserLightPictures")
    Observable<List<LightPicture>> getLightPictures(
            @Query("userId") long userId,
            @Query("pageNo") int pageNo,
            @Query("pageSize") int pageSize,
            @Query("self") boolean self
    );

    /**
     * 完成
     *
     * @param userId
     * @param infoPictureId
     * @return
     */
    @GET("infoPicture/deleteInfoPicture")
    Observable<InfoPicture> deleteInfoPicture(
            @Query("userId") long userId,
            @Query("id") long infoPictureId
    );

    /**
     * 完成
     *
     * @param userId
     * @param lightPictureId
     * @return
     */
    @GET("lightPicture/deleteLightPicture")
    Observable<LightPicture> deleteLightPicture(
            @Query("userId") long userId,
            @Query("id") long lightPictureId
    );

    /**
     * 完成
     *
     * @param user
     * @return
     */
    @POST("user/modifyUserInfo")
    Observable<User> modifyUserInfo(
            @Body User user
    );
}