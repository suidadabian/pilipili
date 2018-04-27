package com.suidadabian.lixiaofeng.pilipili.login;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toolbar;

import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.util.CheckUtil;

public class RegisteredActivity extends Activity {
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_CONFIRM_PASSWORD = "confirm_password";
    private Toolbar mToolbar;
    private EditText mAccountEt;
    private EditText mUsernameEt;
    private EditText mPasswordEt;
    private EditText mConfirmPasswordEt;
    private Button mRegisteredBtn;
    private String mAccount;
    private String mUsername;
    private String mPassword;
    private String mConfirmPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);
        initData(savedInstanceState);
        initView();
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        mAccount = savedInstanceState.getString(KEY_ACCOUNT);
        mUsername = savedInstanceState.getString(KEY_USERNAME);
        mPassword = savedInstanceState.getString(KEY_PASSWORD);
        mConfirmPassword = savedInstanceState.getString(KEY_CONFIRM_PASSWORD);
    }

    private void initView() {
        mToolbar = findViewById(R.id.registered_toolbar);
        mAccountEt = findViewById(R.id.registered_account_et);
        mUsernameEt = findViewById(R.id.registered_username_et);
        mPasswordEt = findViewById(R.id.registered_password_et);
        mConfirmPasswordEt = findViewById(R.id.registered_confirm_password_et);
        mRegisteredBtn = findViewById(R.id.registered_registered_btn);

        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        mAccountEt.setText(mAccount);
        mAccountEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mAccount = s.toString();
                mRegisteredBtn.setEnabled(CheckUtil.checkRegisteredData(mAccount, mUsername, mPassword, mConfirmPassword));
            }
        });
        mUsernameEt.setText(mUsername);
        mUsernameEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mUsername = s.toString();
                mRegisteredBtn.setEnabled(CheckUtil.checkRegisteredData(mAccount, mUsername, mPassword, mConfirmPassword));
            }
        });
        mPasswordEt.setText(mPassword);
        mPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPassword = s.toString();
                mRegisteredBtn.setEnabled(CheckUtil.checkRegisteredData(mAccount, mUsername, mPassword, mConfirmPassword));
            }
        });
        mConfirmPasswordEt.setText(mConfirmPassword);
        mConfirmPasswordEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mConfirmPassword = s.toString();
                mRegisteredBtn.setEnabled(CheckUtil.checkRegisteredData(mAccount, mUsername, mPassword, mConfirmPassword));
            }
        });
        mRegisteredBtn.setOnClickListener(v -> {
            // TODO: 2018/4/23 注册逻辑
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_ACCOUNT, mAccount);
        outState.putString(KEY_USERNAME, mUsername);
        outState.putString(KEY_PASSWORD, mPassword);
        outState.putString(KEY_CONFIRM_PASSWORD, mConfirmPassword);
    }

    private void onRegisteredSuccess() {
        // TODO: 2018/4/23 注册成功逻辑
    }

    private void onRegisteredFail() {
        // TODO: 2018/4/23 注册失败逻辑
    }

    private void onRegisteredError() {
        // TODO: 2018/4/23 注册错误逻辑
    }
}
