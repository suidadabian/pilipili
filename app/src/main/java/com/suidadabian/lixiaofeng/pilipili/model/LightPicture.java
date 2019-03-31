package com.suidadabian.lixiaofeng.pilipili.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.JsonAdapter;

import java.util.Date;

public class LightPicture implements Picture, Parcelable {
    private long id;
    private long userId;
    private String url;
    @JsonAdapter(DateConverter.class)
    private Date date;
    private float scale;
    private boolean share;
    private String deletehash;

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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    @Override
    public boolean isShare() {
        return share;
    }

    @Override
    public void setShare(boolean share) {
        this.share = share;
    }

    @Override
    public String getDeletehash() {
        return deletehash;
    }

    @Override
    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.userId);
        dest.writeString(this.url);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeFloat(this.scale);
        dest.writeByte(this.share ? (byte) 1 : (byte) 0);
        dest.writeString(this.deletehash);
    }

    public LightPicture() {
    }

    protected LightPicture(Parcel in) {
        this.id = in.readLong();
        this.userId = in.readLong();
        this.url = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.scale = in.readFloat();
        this.share = in.readByte() != 0;
        this.deletehash = in.readString();
    }

    public static final Parcelable.Creator<LightPicture> CREATOR = new Parcelable.Creator<LightPicture>() {
        @Override
        public LightPicture createFromParcel(Parcel source) {
            return new LightPicture(source);
        }

        @Override
        public LightPicture[] newArray(int size) {
            return new LightPicture[size];
        }
    };
}
