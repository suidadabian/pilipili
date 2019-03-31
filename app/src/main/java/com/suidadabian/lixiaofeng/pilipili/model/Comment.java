package com.suidadabian.lixiaofeng.pilipili.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import java.util.Date;

public class Comment implements Parcelable {
    private long id;
    private long userId;
    private long infoPictureId;
    private String comment;
    @JsonAdapter(DateConverter.class)
    private Date date;
    private int replyNum;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getInfoPictureId() {
        return infoPictureId;
    }

    public void setInfoPictureId(long infoPictureId) {
        this.infoPictureId = infoPictureId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(int replyNum) {
        this.replyNum = replyNum;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.userId);
        dest.writeLong(this.infoPictureId);
        dest.writeString(this.comment);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeInt(this.replyNum);
    }

    public Comment() {
    }

    protected Comment(Parcel in) {
        this.id = in.readLong();
        this.userId = in.readLong();
        this.infoPictureId = in.readLong();
        this.comment = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.replyNum = in.readInt();
    }

    public static final Parcelable.Creator<Comment> CREATOR = new Parcelable.Creator<Comment>() {
        @Override
        public Comment createFromParcel(Parcel source) {
            return new Comment(source);
        }

        @Override
        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}