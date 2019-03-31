package com.suidadabian.lixiaofeng.pilipili.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserManager;
import com.suidadabian.lixiaofeng.pilipili.model.User;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.util.CheckUtil;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.suidadabian.lixiaofeng.pilipili.util.CheckUtil.CURRENT_PASSWORD_INCORRECT;
import static com.suidadabian.lixiaofeng.pilipili.util.CheckUtil.EMPTY_CONFIRM_NEW_PASSWORD;
import static com.suidadabian.lixiaofeng.pilipili.util.CheckUtil.EMPTY_CURRENT_PASSWORD;
import static com.suidadabian.lixiaofeng.pilipili.util.CheckUtil.EMPTY_NEW_PASSWORD;
import static com.suidadabian.lixiaofeng.pilipili.util.CheckUtil.NEW_CONFIRM_PASSWORD_INCORRECT;
import static com.suidadabian.lixiaofeng.pilipili.util.CheckUtil.UNKNOWN;

public class PasswordActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private EditText mCurrentPasswordEt;
    private EditText mNewPasswordEt;
    private EditText mConfirmNewPasswordEt;
    private MaterialDialog mModifyDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_password);
        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.password_toolbar);
        mCurrentPasswordEt = findViewById(R.id.password_current_et);
        mNewPasswordEt = findViewById(R.id.password_new_et);
        mConfirmNewPasswordEt = findViewById(R.id.password_confirm_et);

        mToolbar.setTitle("修改密码");
        mToolbar.inflateMenu(R.menu.menu_password);
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_password_complete: {
                    modifyPassword();
                    break;
                }
                default: {
                    break;
                }
            }
            return true;
        });
        mToolbar.setNavigationOnClickListener(v -> finish());
    }

    private void modifyPassword() {
        String inputCurrentPassword = mCurrentPasswordEt.getText().toString();
        String newPassword = mNewPasswordEt.getText().toString();
        String confirmNewPassword = mConfirmNewPasswordEt.getText().toString();

        int reason = CheckUtil.checkModifyPassword(UserManager.getInstance().getUser().getPassword(), inputCurrentPassword, newPassword, confirmNewPassword);
        if (reason != CheckUtil.PASS) {
            modifyPasswordInvalid(reason);
            return;
        }

        User user = UserManager.getInstance().getUser();
        user.setPassword(newPassword);

        if (mModifyDialog == null) {
            mModifyDialog = new MaterialDialog.Builder(this)
                    .content("正在修改中，请稍候")
                    .progress(true, 0)
                    .show();
        } else {
            mModifyDialog.show();
        }

        PiliPiliServer.getInstance().modifyUserInfo(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxUser -> modifyPasswordSuccess(rxUser), throwable -> modifyPasswordFail(throwable));
    }

    private void modifyPasswordSuccess(User user) {
        mModifyDialog.dismiss();

        UserManager.getInstance().setUser(user);
        finish();
    }

    private void modifyPasswordFail(Throwable throwable) {
        mModifyDialog.dismiss();
        new NetExceptionHandler().handleException(this, throwable);
    }

    private void modifyPasswordInvalid(int reason) {
        switch (reason) {
            case EMPTY_CURRENT_PASSWORD: {
                Toast.makeText(this, "当前密码不能为空", Toast.LENGTH_SHORT).show();
                break;
            }
            case EMPTY_NEW_PASSWORD: {
                Toast.makeText(this, "新密码不能为空", Toast.LENGTH_SHORT).show();
                break;
            }
            case EMPTY_CONFIRM_NEW_PASSWORD: {
                Toast.makeText(this, "确认新密码不能为空", Toast.LENGTH_SHORT).show();
                break;
            }
            case CURRENT_PASSWORD_INCORRECT: {
                Toast.makeText(this, "当前密码错误", Toast.LENGTH_SHORT).show();
                break;
            }
            case NEW_CONFIRM_PASSWORD_INCORRECT: {
                Toast.makeText(this, "两次新密码不相同", Toast.LENGTH_SHORT).show();
                break;
            }
            case UNKNOWN: {
                Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
                break;
            }
            default: {
                break;
            }
        }
    }
}
