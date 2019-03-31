package com.suidadabian.lixiaofeng.pilipili.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserManager;
import com.suidadabian.lixiaofeng.pilipili.main.MainActivity;
import com.suidadabian.lixiaofeng.pilipili.model.User;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.OkHttpManager;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.util.CheckUtil;

import java.lang.ref.WeakReference;

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
    private MaterialDialog mLoginDialog;
    private TriggerHandler mTriggerHanlder = new TriggerHandler(this);

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
        mLoginBtn.setOnClickListener(v -> login());
        mRegisteredBtn.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisteredActivity.class));
        });

        findViewById(R.id.login_backdoor_iv).setOnClickListener(v -> mTriggerHanlder.sendEmptyMessage(TriggerHandler.MSG_BACKDOOR_CLICK));
    }

    private void login() {
        if (mLoginDialog == null) {
            mLoginDialog = new MaterialDialog.Builder(this)
                    .content("登陆中……")
                    .progress(true, 0)
                    .show();
        } else {
            mLoginDialog.show();
        }

        PiliPiliServer.getInstance().login(mAccount, mPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> onLoginSuccess(user), throwable -> onLoginFail(throwable));
    }

    private void onLoginSuccess(User user) {
        mLoginDialog.dismiss();
        UserManager userManager = UserManager.getInstance();
        userManager.setLogin(true);
        userManager.setUser(user);
        startActivity(new Intent(this, MainActivity.class));
        Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void onLoginFail(Throwable throwable) {
        mLoginDialog.dismiss();
        new NetExceptionHandler().handleException(this, throwable);
    }

    private void backdoor() {
        new MaterialDialog.Builder(LoginActivity.this)
                .title("设置IP")
                .input(null, null, (dialog, input) ->
                        PiliPiliServer.getInstance().change(OkHttpManager.getInstance().getOkHttpClient(), input.toString()))
                .show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_ACCOUNT, mAccount);
        outState.putString(KEY_PASSWORD, mPassword);
    }

    private static class TriggerHandler extends Handler {
        private static final int MSG_BACKDOOR_CLICK = 1;
        private static final int MSG_TRIGGER_RESET = 2;
        private static final int BACKDOOR_TRIGGER_COUNT = 10;
        private static final long TRIGGER_RESET_DELAY = 1000;
        private WeakReference<LoginActivity> loginActivityWeakRef;
        private int backdoorClickCount;

        public TriggerHandler(LoginActivity loginActivity) {
            loginActivityWeakRef = new WeakReference<>(loginActivity);
        }


        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_TRIGGER_RESET: {
                    backdoorClickCount = 0;
                    break;
                }
                case MSG_BACKDOOR_CLICK: {
                    backdoorClickCount++;
                    removeMessages(MSG_TRIGGER_RESET);
                    if (backdoorClickCount == BACKDOOR_TRIGGER_COUNT) {
                        LoginActivity loginActivity = loginActivityWeakRef.get();
                        backdoorClickCount = 0;
                        if (loginActivity != null) {
                            loginActivity.backdoor();
                        }
                    } else {
                        sendEmptyMessageDelayed(MSG_TRIGGER_RESET, TRIGGER_RESET_DELAY);
                    }
                    break;
                }
            }
        }
    }
}
