package com.suidadabian.lixiaofeng.pilipili.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserSpManager;
import com.suidadabian.lixiaofeng.pilipili.main.MainActivity;
import com.suidadabian.lixiaofeng.pilipili.model.User;
import com.suidadabian.lixiaofeng.pilipili.net.FakeServer;
import com.suidadabian.lixiaofeng.pilipili.util.CheckUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private static final String KEY_ACCOUNT = "account";
    private static final String KEY_PASSWORD = "password";
    private Toolbar mToolbar;
    private EditText mAccountEt;
    private EditText mPasswordEt;
    private Button mLoginBtn;
    private Button mRegisteredBtn;
    private String mAccount;
    private String mPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initData(savedInstanceState);
        initView();
    }

    private void initData(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            return;
        }

        mAccount = savedInstanceState.getString(KEY_ACCOUNT);
        mPassword = savedInstanceState.getString(KEY_PASSWORD);
    }

    private void initView() {
        mToolbar = findViewById(R.id.login_toolbar);
        mAccountEt = findViewById(R.id.login_account_et);
        mPasswordEt = findViewById(R.id.login_password_et);
        mLoginBtn = findViewById(R.id.login_login_btn);
        mRegisteredBtn = findViewById(R.id.login_registered_btn);

        mToolbar.setTitle(R.string.login_toolbar_title);
        mToolbar.setNavigationOnClickListener(view -> {
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
                mLoginBtn.setEnabled(CheckUtil.checkLoginData(mAccount, mPassword));
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
                mLoginBtn.setEnabled(CheckUtil.checkLoginData(mAccount, mPassword));
            }
        });
        mLoginBtn.setEnabled(CheckUtil.checkLoginData(mAccount, mPassword));
        mLoginBtn.setOnClickListener(v -> {
            // TODO: 2018/4/23 登录逻辑
            FakeServer.getInstance().login(mAccount, mPassword)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(user -> {
                        if (user == null) {
                            onLoginFail();
                        }

                        onLoginSuccess(user);
                    });
        });
        mRegisteredBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisteredActivity.class));
        });
    }

    private void onLoginSuccess(User user) {
        // TODO: 2018/4/26 完善登录成功逻辑
        UserSpManager userSpManager = UserSpManager.getInstance();
        userSpManager.setLogin(true);
        userSpManager.setUserId(user.getId());
        UserSpManager.getInstance().setLogin(true);
        startActivity(new Intent(this, MainActivity.class));
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
    }

    private void onLoginFail() {
        // TODO: 2018/4/26 完善登录失败逻辑
        Toast.makeText(this, "登录失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_ACCOUNT, mAccount);
        outState.putString(KEY_PASSWORD, mPassword);
    }
}
