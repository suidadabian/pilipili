package com.suidadabian.lixiaofeng.pilipili.model;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class LightPicture implements Picture {
    @Id(autoincrement = true)
    private long id;
    @NonNull
    private long userId;
    @NonNull
    private String url;
    @NonNull
    private Date date;
    @NonNull
    private String tags;
    @NonNull
    private boolean share;

    @Generated(hash = 1023949667)
    public LightPicture(long id, long userId, @NonNull String url,
            @NonNull Date date, @NonNull String tags, boolean share) {
        this.id = id;
        this.userId = userId;
        this.url = url;
        this.date = date;
        this.tags = tags;
        this.share = share;
    }

    @Generated(hash = 761205405)
    public LightPicture() {
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
}
