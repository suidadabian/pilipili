package com.suidadabian.lixiaofeng.pilipili.model;

import android.support.annotation.NonNull;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Reply {
    @Id(autoincrement = true)
    private long id;
    @NonNull
    private long userId;
    @NonNull
    private long commentId;
    @NonNull
    private String reply;
    @NonNull
    private Date date;
    @NonNull
    private int index;

    @Generated(hash = 439285326)
    public Reply(long id, long userId, long commentId, @NonNull String reply,
                 @NonNull Date date, int index) {
        this.id = id;
        this.userId = userId;
        this.commentId = commentId;
        this.reply = reply;
        this.date = date;
        this.index = index;
    }

    @Generated(hash = 1831839081)
    public Reply() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCommentId() {
        return this.commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public String getReply() {
        return this.reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getIndex() {
        return this.index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
