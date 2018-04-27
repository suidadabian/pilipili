package com.suidadabian.lixiaofeng.pilipili.net;

import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;
import com.suidadabian.lixiaofeng.pilipili.model.User;

import java.util.List;

import io.reactivex.Observable;

public interface PiliPiliServer {
    Observable<User> login(String account, String password);

    Observable<User> registered(String account, String userName, String password);

    Observable<User> getUser(long id);

    Observable<List<InfoPicture>> getInfoPictures(int pageNo, int pageSize);

    Observable<List<LightPicture>> getLightPictures(int pageNo, int pageSize);

    Observable<InfoPicture> getInfoPicture(long id);

    Observable<LightPicture> getLightPicture(long id);
}
