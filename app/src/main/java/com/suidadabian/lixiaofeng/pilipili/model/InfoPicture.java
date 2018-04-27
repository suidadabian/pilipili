package com.suidadabian.lixiaofeng.pilipili.model;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class InfoPicture implements Picture {
    @Id(autoincrement = true)
    private long id;
    @NonNull
    private long userId;
    @NonNull
    private String url;
    @NonNull
    private Date date;
    private String tags;
    @NonNull
    private boolean share;
    @NonNull
    private String title;
    private String intro;
    @NonNull
    private int thumbsUpNumber;
    @NonNull
    private int collectNumber;
    @NonNull
    private int commentNumber;

    @Generated(hash = 1910362547)
    public InfoPicture(long id, long userId, @NonNull String url,
            @NonNull Date date, String tags, boolean share, @NonNull String title,
            String intro, int thumbsUpNumber, int collectNumber,
            int commentNumber) {
        this.id = id;
        this.userId = userId;
        this.url = url;
        this.date = date;
        this.tags = tags;
        this.share = share;
        this.title = title;
        this.intro = intro;
        this.thumbsUpNumber = thumbsUpNumber;
        this.collectNumber = collectNumber;
        this.commentNumber = commentNumber;
    }

    @Generated(hash = 259319555)
    public InfoPicture() {
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Date getDate() {
        return date;
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getTags() {
        return tags;
    }

    @Override
    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share = share;
    }

    public boolean getShare() {
        return this.share;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntro() {
        return this.intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public int getThumbsUpNumber() {
        return this.thumbsUpNumber;
    }

    public void setThumbsUpNumber(int thumbsUpNumber) {
        this.thumbsUpNumber = thumbsUpNumber;
    }

    public int getCollectNumber() {
        return this.collectNumber;
    }

    public void setCollectNumber(int collectNumber) {
        this.collectNumber = collectNumber;
    }

    public int getCommentNumber() {
        return this.commentNumber;
    }

    public void setCommentNumber(int commentNumber) {
        this.commentNumber = commentNumber;
    }
}
