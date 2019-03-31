package com.suidadabian.lixiaofeng.pilipili.common.sp;

import android.content.SharedPreferences;

public abstract class ModelSharedPreferencesConverter<T> {

    protected void modelToSharedPreferences(T model, SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        setupSharedPreferencesEditor(editor);
        editor.apply();
    }

    public abstract void setupSharedPreferencesEditor(SharedPreferences.Editor editor);

    public abstract void sharedPreferencesToModel(T model, SharedPreferences sharedPreferences);
}
