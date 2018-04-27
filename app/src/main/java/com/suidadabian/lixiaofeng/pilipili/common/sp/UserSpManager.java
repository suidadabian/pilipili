package com.suidadabian.lixiaofeng.pilipili.common.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.suidadabian.lixiaofeng.pilipili.PiliPiliApplication;

public class UserSpManager {
    private static final String SP_NAME = "user_sp";
    private static final String KEY_LOGIN = "login";
    private static final String KEY_USER_ID = "user_id";
    private static UserSpManager instance;
    private SharedPreferences sharedPreferences;
    private boolean login;
    private long userId;

    private UserSpManager() {
        init(PiliPiliApplication.getContext());
    }

    public static UserSpManager getInstance() {
        if (instance == null) {
            instance = new UserSpManager();
        }

        return instance;
    }

    private void init(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        login = sharedPreferences.getBoolean(KEY_LOGIN, false);
        userId = sharedPreferences.getLong(KEY_USER_ID, 0);
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
        sharedPreferences.edit().putBoolean(KEY_LOGIN, login).apply();
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
        sharedPreferences.edit().putLong(KEY_USER_ID, userId).apply();
    }
}
