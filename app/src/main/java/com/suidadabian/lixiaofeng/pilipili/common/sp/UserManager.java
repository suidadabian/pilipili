package com.suidadabian.lixiaofeng.pilipili.common.sp;

import android.content.Context;
import android.content.SharedPreferences;

import com.suidadabian.lixiaofeng.pilipili.PiliPiliApplication;
import com.suidadabian.lixiaofeng.pilipili.model.User;

public class UserManager {
    private static final String SP_NAME = "user_sp";
    private static final String KEY_LOGIN = "login";
    private static UserManager instance;
    private ModelSharedPreferencesConverter<User> userConverter = new ModelSharedPreferencesConverter<User>() {
        private static final String KEY_ID = "id";
        private static final String KEY_ACCOUNT = "account";
        private static final String KEY_PASSWORD = "password";
        private static final String KEY_AVATAR_URL = "avatar_url";
        private static final String KEY_NAME = "name";
        private static final String KEY_SEX = "sex";
        private static final String KEY_SIGN = "sign";

        @Override
        public void setupSharedPreferencesEditor(SharedPreferences.Editor editor) {
            editor.putLong(KEY_ID, user.getId());
            editor.putString(KEY_ACCOUNT, user.getAccount());
            editor.putString(KEY_PASSWORD, user.getPassword());
            editor.putString(KEY_AVATAR_URL, user.getAvatarUrl());
            editor.putString(KEY_NAME, user.getName());
            editor.putInt(KEY_SEX, user.getSex().getId());
            editor.putString(KEY_SIGN, user.getSign());
        }

        @Override
        public void sharedPreferencesToModel(User model, SharedPreferences sharedPreferences) {
            model.setId(sharedPreferences.getLong(KEY_ID, 0));
            model.setAccount(sharedPreferences.getString(KEY_ACCOUNT, ""));
            model.setPassword(sharedPreferences.getString(KEY_PASSWORD, ""));
            model.setAvatarUrl(sharedPreferences.getString(KEY_AVATAR_URL, ""));
            model.setName(sharedPreferences.getString(KEY_NAME, ""));
            model.setSex(User.Sex.getSex(sharedPreferences.getInt(KEY_SEX, User.Sex.SECRET.getId())));
            model.setSign(sharedPreferences.getString(KEY_SIGN, ""));
        }
    };
    private SharedPreferences sharedPreferences;
    private boolean login;
    private User user;

    private UserManager() {
        init(PiliPiliApplication.getContext());
    }

    public static UserManager getInstance() {
        if (instance == null) {
            instance = new UserManager();
        }

        return instance;
    }

    private void init(Context context) {
        sharedPreferences = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        login = sharedPreferences.getBoolean(KEY_LOGIN, false);
        user = new User();
        userConverter.sharedPreferencesToModel(user, sharedPreferences);
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
        sharedPreferences.edit().putBoolean(KEY_LOGIN, login).apply();
    }

    public User getUser() {
        User user = new User();
        user.setId(this.user.getId());
        user.setSex(this.user.getSex());
        user.setSign(this.user.getSign());
        user.setName(this.user.getName());
        user.setAvatarUrl(this.user.getAvatarUrl());
        user.setPassword(this.user.getPassword());
        user.setAccount(this.user.getAccount());
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        userConverter.modelToSharedPreferences(user, sharedPreferences);
    }
}
