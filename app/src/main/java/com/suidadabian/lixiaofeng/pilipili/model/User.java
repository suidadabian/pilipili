package com.suidadabian.lixiaofeng.pilipili.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class User implements Parcelable {
    private long id;
    private String account;
    private String password;
    private String avatarUrl;
    private String name;
    @JsonAdapter(SexTypeAdapter.class)
    private Sex sex;
    private String sign;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Sex getSex() {
        return sex;
    }

    public void setSex(Sex sex) {
        this.sex = sex;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public enum Sex {
        MALE(1, "男"), FEMALE(2, "女"), SECRET(3, "保密");

        private int id;
        private String info;

        Sex(int id, String info) {
            this.id = id;
            this.info = info;
        }

        public static Sex getSex(int id) {
            Sex[] sexes = Sex.values();
            for (Sex sex : sexes) {
                if (sex.id == id) {
                    return sex;
                }
            }
            return null;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

    public static class SexTypeAdapter extends TypeAdapter<Sex> {
        @Override
        public void write(JsonWriter out, Sex value) throws IOException {
            out.value(value == null ? Sex.SECRET.getId() : value.getId());
        }

        @Override
        public Sex read(JsonReader in) throws IOException {
            if (in.peek() == JsonToken.NULL) {
                in.nextNull();
                return Sex.SECRET;
            }

            return Sex.getSex(in.nextInt());
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.account);
        dest.writeString(this.password);
        dest.writeString(this.avatarUrl);
        dest.writeString(this.name);
        dest.writeInt(this.sex == null ? -1 : this.sex.ordinal());
        dest.writeString(this.sign);
    }

    public User() {
    }

    protected User(Parcel in) {
        this.id = in.readLong();
        this.account = in.readString();
        this.password = in.readString();
        this.avatarUrl = in.readString();
        this.name = in.readString();
        int tmpSex = in.readInt();
        this.sex = tmpSex == -1 ? null : Sex.values()[tmpSex];
        this.sign = in.readString();
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel source) {
            return new User(source);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };
}
