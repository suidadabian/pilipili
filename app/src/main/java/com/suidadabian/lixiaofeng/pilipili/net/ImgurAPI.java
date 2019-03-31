package com.suidadabian.lixiaofeng.pilipili.net;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImgurAPI {
    String SERVER = "https://api.imgur.com";

    /**
     * 完成
     *
     * @param auth
     * @param file
     * @return
     */
    @Multipart
    @POST("/3/image")
    Observable<ImgurServer.UploadPicture> postImage(
            @Header("Authorization") String auth,
            @Part("image") RequestBody file
    );
}
