package com.suidadabian.lixiaofeng.pilipili.net;

import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;
import com.suidadabian.lixiaofeng.pilipili.model.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;

public final class FakeServer implements PiliPiliServer {
    private static final User TEST_USER_1;
    private static final User TEST_USER_2;

    private static final InfoPicture TEST_INFO_PICTURE_1;
    private static final InfoPicture TEST_INFO_PICTURE_2;
    private static final InfoPicture TEST_INFO_PICTURE_3;
    private static final InfoPicture TEST_INFO_PICTURE_4;
    private static final InfoPicture TEST_INFO_PICTURE_5;
    private static final InfoPicture TEST_INFO_PICTURE_6;

    private static final LightPicture TEST_LIGHT_PICTURE_1;
    private static final LightPicture TEST_LIGHT_PICTURE_2;
    private static final LightPicture TEST_LIGHT_PICTURE_3;
    private static final LightPicture TEST_LIGHT_PICTURE_4;
    private static final LightPicture TEST_LIGHT_PICTURE_5;
    private static final LightPicture TEST_LIGHT_PICTURE_6;

    private static FakeServer instance;

    static {
        TEST_USER_1 = new User();
        TEST_USER_1.setId(1);
        TEST_USER_1.setAccount("111");
        TEST_USER_1.setPassword("111");
        TEST_USER_1.setName("1号测试员");
        TEST_USER_1.setAvatarUrl("https://i0.hdslb.com/bfs/bangumi/81a79b71fe48d803c039f6ecfa86e8a64809ccb9.png@99w_99h.webp");
        TEST_USER_1.setSex(User.Sex.MALE);

        TEST_USER_2 = new User();
        TEST_USER_2.setId(2);
        TEST_USER_2.setAccount("222");
        TEST_USER_2.setPassword("222");
        TEST_USER_2.setName("2号测试员");
        TEST_USER_2.setAvatarUrl("https://i0.hdslb.com/bfs/bangumi/c75d5d1a7394dbc62aa4148c9f36fd0bdabc75dc.jpg@99w_99h.webp");
        TEST_USER_2.setSex(User.Sex.FEMALE);

        TEST_INFO_PICTURE_1 = new InfoPicture();
        TEST_INFO_PICTURE_1.setId(1);
        TEST_INFO_PICTURE_1.setUserId(TEST_USER_1.getId());
        TEST_INFO_PICTURE_1.setUrl("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike272%2C5%2C5%2C272%2C90/sign=08156d34444a20a425133495f13bf347/2934349b033b5bb582e915d83dd3d539b600bcdd.jpg");
        TEST_INFO_PICTURE_1.setDate(new Date(System.currentTimeMillis()));
        TEST_INFO_PICTURE_1.setTags("tag1,tag2");
        TEST_INFO_PICTURE_1.setShare(true);
        TEST_INFO_PICTURE_1.setTitle("1号测试图片");
        TEST_INFO_PICTURE_1.setIntro("这是1号测试图片");

        TEST_INFO_PICTURE_2 = new InfoPicture();
        TEST_INFO_PICTURE_2.setId(2);
        TEST_INFO_PICTURE_2.setUserId(TEST_USER_2.getId());
        TEST_INFO_PICTURE_2.setUrl("https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/crop%3D0%2C4%2C500%2C330%3Bc0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4437d31ac91b9d169e88c021ceee98bb/6d81800a19d8bc3e83c4470f858ba61ea9d34522.jpg");
        TEST_INFO_PICTURE_2.setDate(new Date(System.currentTimeMillis()));
        TEST_INFO_PICTURE_2.setTags("tag1,tag2");
        TEST_INFO_PICTURE_2.setShare(true);
        TEST_INFO_PICTURE_2.setTitle("2号测试图片");
        TEST_INFO_PICTURE_2.setIntro("这是2号测试图片");


        TEST_INFO_PICTURE_3 = new InfoPicture();
        TEST_INFO_PICTURE_3.setId(3);
        TEST_INFO_PICTURE_3.setUserId(TEST_USER_1.getId());
        TEST_INFO_PICTURE_3.setUrl("https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4e53cee38db1cb132a643441bc3d3d2b/a8773912b31bb05182e21624367adab44bede0fc.jpg");
        TEST_INFO_PICTURE_3.setDate(new Date(System.currentTimeMillis()));
        TEST_INFO_PICTURE_3.setTags("tag1,tag2");
        TEST_INFO_PICTURE_3.setShare(true);
        TEST_INFO_PICTURE_3.setTitle("3号测试图片");
        TEST_INFO_PICTURE_3.setIntro("这是3号测试图片");


        TEST_INFO_PICTURE_4 = new InfoPicture();
        TEST_INFO_PICTURE_4.setId(4);
        TEST_INFO_PICTURE_4.setUserId(TEST_USER_2.getId());
        TEST_INFO_PICTURE_4.setUrl("https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=feba77710d55b31988f48a2722c0e943/faf2b2119313b07e9bf96b780bd7912397dd8c7d.jpg");
        TEST_INFO_PICTURE_4.setDate(new Date(System.currentTimeMillis()));
        TEST_INFO_PICTURE_4.setTags("tag1,tag2");
        TEST_INFO_PICTURE_4.setShare(true);
        TEST_INFO_PICTURE_4.setTitle("4号测试图片");
        TEST_INFO_PICTURE_4.setIntro("这是4号测试图片");


        TEST_INFO_PICTURE_5 = new InfoPicture();
        TEST_INFO_PICTURE_5.setId(5);
        TEST_INFO_PICTURE_5.setUserId(TEST_USER_1.getId());
        TEST_INFO_PICTURE_5.setUrl("https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike180%2C5%2C5%2C180%2C60/sign=f9b4bc0544a98226accc2375ebebd264/0b7b02087bf40ad170a9f993502c11dfa9ecce74.jpg");
        TEST_INFO_PICTURE_5.setDate(new Date(System.currentTimeMillis()));
        TEST_INFO_PICTURE_5.setTags("tag1,tag2");
        TEST_INFO_PICTURE_5.setShare(true);
        TEST_INFO_PICTURE_5.setTitle("5号测试图片");
        TEST_INFO_PICTURE_5.setIntro("这是5号测试图片");


        TEST_INFO_PICTURE_6 = new InfoPicture();
        TEST_INFO_PICTURE_6.setId(6);
        TEST_INFO_PICTURE_6.setUserId(TEST_USER_2.getId());
        TEST_INFO_PICTURE_6.setUrl("https://gss0.bdstatic.com/-4o3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=1546c8c45b6034a83defb0d3aa7a2231/4b90f603738da977b571c7eeb751f8198718e39d.jpg");
        TEST_INFO_PICTURE_6.setDate(new Date(System.currentTimeMillis()));
        TEST_INFO_PICTURE_6.setTags("tag1,tag2");
        TEST_INFO_PICTURE_6.setShare(true);
        TEST_INFO_PICTURE_6.setTitle("6号测试图片");
        TEST_INFO_PICTURE_6.setIntro("这是6号测试图片");

        TEST_LIGHT_PICTURE_1 = new LightPicture();
        TEST_LIGHT_PICTURE_1.setId(1);
        TEST_LIGHT_PICTURE_1.setUserId(TEST_USER_1.getId());
        TEST_LIGHT_PICTURE_1.setUrl("https://gss3.bdstatic.com/-Po3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike272%2C5%2C5%2C272%2C90/sign=08156d34444a20a425133495f13bf347/2934349b033b5bb582e915d83dd3d539b600bcdd.jpg");
        TEST_LIGHT_PICTURE_1.setDate(new Date(System.currentTimeMillis()));
        TEST_LIGHT_PICTURE_1.setTags("tag1,tag2");
        TEST_LIGHT_PICTURE_1.setShare(true);

        TEST_LIGHT_PICTURE_2 = new LightPicture();
        TEST_LIGHT_PICTURE_2.setId(2);
        TEST_LIGHT_PICTURE_2.setUserId(TEST_USER_2.getId());
        TEST_LIGHT_PICTURE_2.setUrl("https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/crop%3D0%2C4%2C500%2C330%3Bc0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4437d31ac91b9d169e88c021ceee98bb/6d81800a19d8bc3e83c4470f858ba61ea9d34522.jpg");
        TEST_LIGHT_PICTURE_2.setDate(new Date(System.currentTimeMillis()));
        TEST_LIGHT_PICTURE_2.setTags("tag1,tag2");
        TEST_LIGHT_PICTURE_2.setShare(true);

        TEST_LIGHT_PICTURE_3 = new LightPicture();
        TEST_LIGHT_PICTURE_3.setId(3);
        TEST_LIGHT_PICTURE_3.setUserId(TEST_USER_1.getId());
        TEST_LIGHT_PICTURE_3.setUrl("https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=4e53cee38db1cb132a643441bc3d3d2b/a8773912b31bb05182e21624367adab44bede0fc.jpg");
        TEST_LIGHT_PICTURE_3.setDate(new Date(System.currentTimeMillis()));
        TEST_LIGHT_PICTURE_3.setTags("tag1,tag2");
        TEST_LIGHT_PICTURE_3.setShare(true);

        TEST_LIGHT_PICTURE_4 = new LightPicture();
        TEST_LIGHT_PICTURE_4.setId(4);
        TEST_LIGHT_PICTURE_4.setUserId(TEST_USER_2.getId());
        TEST_LIGHT_PICTURE_4.setUrl("https://gss1.bdstatic.com/9vo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=feba77710d55b31988f48a2722c0e943/faf2b2119313b07e9bf96b780bd7912397dd8c7d.jpg");
        TEST_LIGHT_PICTURE_4.setDate(new Date(System.currentTimeMillis()));
        TEST_LIGHT_PICTURE_4.setTags("tag1,tag2");
        TEST_LIGHT_PICTURE_4.setShare(true);

        TEST_LIGHT_PICTURE_5 = new LightPicture();
        TEST_LIGHT_PICTURE_5.setId(5);
        TEST_LIGHT_PICTURE_5.setUserId(TEST_USER_1.getId());
        TEST_LIGHT_PICTURE_5.setUrl("https://gss2.bdstatic.com/9fo3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike180%2C5%2C5%2C180%2C60/sign=f9b4bc0544a98226accc2375ebebd264/0b7b02087bf40ad170a9f993502c11dfa9ecce74.jpg");
        TEST_LIGHT_PICTURE_5.setDate(new Date(System.currentTimeMillis()));
        TEST_LIGHT_PICTURE_5.setTags("tag1,tag2");
        TEST_LIGHT_PICTURE_5.setShare(true);

        TEST_LIGHT_PICTURE_6 = new LightPicture();
        TEST_LIGHT_PICTURE_6.setId(4);
        TEST_LIGHT_PICTURE_6.setUserId(TEST_USER_2.getId());
        TEST_LIGHT_PICTURE_6.setUrl("https://gss0.bdstatic.com/-4o3dSag_xI4khGkpoWK1HF6hhy/baike/c0%3Dbaike80%2C5%2C5%2C80%2C26/sign=1546c8c45b6034a83defb0d3aa7a2231/4b90f603738da977b571c7eeb751f8198718e39d.jpg");
        TEST_LIGHT_PICTURE_6.setDate(new Date(System.currentTimeMillis()));
        TEST_LIGHT_PICTURE_6.setTags("tag1,tag2");
        TEST_LIGHT_PICTURE_6.setShare(true);
    }

    private FakeServer() {
    }

    public static FakeServer getInstance() {
        if (instance == null) {
            instance = new FakeServer();
        }

        return instance;
    }

    @Override
    public Observable<User> login(String account, String password) {
        User user = null;

        if (account.equals(TEST_USER_1.getAccount()) && password.equals(TEST_USER_1.getPassword())) {
            user = TEST_USER_1;
        } else if (account.equals(TEST_USER_2.getAccount()) && password.equals(TEST_USER_2.getPassword())) {
            user = TEST_USER_2;
        }

        return Observable.just(user);
    }

    @Override
    public Observable<User> registered(String account, String userName, String password) {
        User user = null;

        if (account.equals(TEST_USER_1.getAccount()) && password.equals(TEST_USER_1.getPassword())) {
            user = TEST_USER_1;
        } else if (account.equals(TEST_USER_2.getAccount()) && password.equals(TEST_USER_2.getPassword())) {
            user = TEST_USER_2;
        }

        return Observable.just(user);
    }

    @Override
    public Observable<User> getUser(long id) {
        User user = null;

        if (id == TEST_USER_1.getId()) {
            user = TEST_USER_1;
        } else if (id == TEST_USER_2.getId()) {
            user = TEST_USER_2;
        }

        return Observable.just(user);
    }

    @Override
    public Observable<List<InfoPicture>> getInfoPictures(int pageNo, int pageSize) {
        List<InfoPicture> infoPictures = new ArrayList<>();
        infoPictures.add(TEST_INFO_PICTURE_1);
        infoPictures.add(TEST_INFO_PICTURE_2);
        infoPictures.add(TEST_INFO_PICTURE_3);
        infoPictures.add(TEST_INFO_PICTURE_4);
        infoPictures.add(TEST_INFO_PICTURE_5);
        infoPictures.add(TEST_INFO_PICTURE_6);
        return Observable.just(infoPictures);
    }

    @Override
    public Observable<List<LightPicture>> getLightPictures(int pageNo, int pageSize) {
        List<LightPicture> lightPictures = new ArrayList<>();
        lightPictures.add(TEST_LIGHT_PICTURE_1);
        lightPictures.add(TEST_LIGHT_PICTURE_2);
        lightPictures.add(TEST_LIGHT_PICTURE_3);
        lightPictures.add(TEST_LIGHT_PICTURE_4);
        lightPictures.add(TEST_LIGHT_PICTURE_5);
        lightPictures.add(TEST_LIGHT_PICTURE_6);
        return Observable.just(lightPictures);
    }

    @Override
    public Observable<InfoPicture> getInfoPicture(long id) {
        InfoPicture infoPicture = null;

        if (id == TEST_INFO_PICTURE_1.getId()) {
            infoPicture = TEST_INFO_PICTURE_1;
        } else if (id == TEST_INFO_PICTURE_2.getId()) {
            infoPicture = TEST_INFO_PICTURE_2;
        } else if (id == TEST_INFO_PICTURE_3.getId()) {
            infoPicture = TEST_INFO_PICTURE_3;
        } else if (id == TEST_INFO_PICTURE_4.getId()) {
            infoPicture = TEST_INFO_PICTURE_4;
        } else if (id == TEST_INFO_PICTURE_5.getId()) {
            infoPicture = TEST_INFO_PICTURE_5;
        } else if (id == TEST_INFO_PICTURE_6.getId()) {
            infoPicture = TEST_INFO_PICTURE_6;
        }

        return Observable.just(infoPicture);
    }

    @Override
    public Observable<LightPicture> getLightPicture(long id) {
        LightPicture lightPicture = null;

        if (id == TEST_LIGHT_PICTURE_1.getId()) {
            lightPicture = TEST_LIGHT_PICTURE_1;
        } else if (id == TEST_LIGHT_PICTURE_2.getId()) {
            lightPicture = TEST_LIGHT_PICTURE_2;
        } else if (id == TEST_LIGHT_PICTURE_3.getId()) {
            lightPicture = TEST_LIGHT_PICTURE_3;
        } else if (id == TEST_LIGHT_PICTURE_4.getId()) {
            lightPicture = TEST_LIGHT_PICTURE_4;
        } else if (id == TEST_LIGHT_PICTURE_5.getId()) {
            lightPicture = TEST_LIGHT_PICTURE_5;
        } else if (id == TEST_LIGHT_PICTURE_6.getId()) {
            lightPicture = TEST_LIGHT_PICTURE_6;
        }

        return Observable.just(lightPicture);
    }
}
