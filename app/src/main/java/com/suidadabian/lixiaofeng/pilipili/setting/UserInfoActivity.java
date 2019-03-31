package com.suidadabian.lixiaofeng.pilipili.setting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserManager;
import com.suidadabian.lixiaofeng.pilipili.login.LoginActivity;
import com.suidadabian.lixiaofeng.pilipili.model.User;
import com.suidadabian.lixiaofeng.pilipili.net.ImgurServer;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.util.CheckUtil;
import com.suidadabian.lixiaofeng.pilipili.util.PathUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UserInfoActivity extends AppCompatActivity {
    private static final int CODE_REQUEST_PERMISSION = 2;
    private static final int CODE_PICK_PICTURE = 1;
    public static final String ACTION_USER_INFO_MODIFY = "user_info_modify";
    public static final String KEY_USER = "user";
    private Toolbar mToolbar;
    private ImageView mAvatarIv;
    private TextView mNameTv;
    private TextView mSexTv;
    private TextView mSignTv;
    private User mUser;
    private Button mLogoutBtn;
    private MaterialDialog mModifyDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_info);
        initData();
        initView();
    }

    private void initView() {
        mToolbar = findViewById(R.id.user_info_toolbar);
        mAvatarIv = findViewById(R.id.user_info_avatar_civ);
        mNameTv = findViewById(R.id.user_info_name_tv);
        mSexTv = findViewById(R.id.user_info_sex_tv);
        mSignTv = findViewById(R.id.user_info_sign_tv);
        mLogoutBtn = findViewById(R.id.user_info_logout_btn);

        mToolbar.setTitle("用户信息");
        mToolbar.inflateMenu(R.menu.menu_user_info);
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_user_info_complete: {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        modifyUserInfo();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_REQUEST_PERMISSION);
                    }
                    break;
                }
                default: {
                    break;
                }
            }
            return true;
        });
        mToolbar.setNavigationOnClickListener(v -> finish());


        mNameTv.setText(mUser.getName());
        mSexTv.setText(mUser.getSex().getInfo());
        mSignTv.setText(mUser.getSign());
        GlideApp.with(this)
                .load(mUser.getAvatarUrl())
                .placeholder(R.drawable.icon_placeholder)
                .error(R.drawable.icon_error)
                .into(mAvatarIv);

        findViewById(R.id.user_info_avatar_rl).setOnClickListener(v -> pickPicture());
        findViewById(R.id.user_info_name_rl).setOnClickListener(v ->
                new MaterialDialog.Builder(UserInfoActivity.this)
                        .title("编辑用户名")
                        .input("编辑你的用户名", mUser.getName(), (dialog, input) -> {
                            mUser.setName(input.toString());
                            mNameTv.setText(input);
                        }).show()
        );
        findViewById(R.id.user_info_sex_rl).setOnClickListener(v ->
                new MaterialDialog.Builder(UserInfoActivity.this)
                        .title("选择你的性别")
                        .items(User.Sex.MALE.getInfo(), User.Sex.FEMALE.getInfo(), User.Sex.SECRET.getInfo())
                        .itemsCallback((dialog, itemView, position, text) -> {
                            User.Sex sex;
                            switch (position) {
                                case 0: {
                                    sex = User.Sex.MALE;
                                    break;
                                }
                                case 1: {
                                    sex = User.Sex.FEMALE;
                                    break;
                                }
                                default: {
                                    sex = User.Sex.SECRET;
                                    break;
                                }
                            }
                            mUser.setSex(sex);
                            mSexTv.setText(sex.getInfo());
                        })
                        .show());
        findViewById(R.id.user_info_sign_rl).setOnClickListener(v ->
                new MaterialDialog.Builder(UserInfoActivity.this)
                        .title("编辑个性签名")
                        .input("编辑你的个性签名", mUser.getSign(), (dialog, input) -> {
                            mUser.setSign(input.toString());
                            mSignTv.setText(input);
                        })
                        .show()
        );
        mLogoutBtn.setOnClickListener(v -> {
            UserManager.getInstance().setLogin(false);
            Intent intent = new Intent(UserInfoActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void initData() {
        mUser = UserManager.getInstance().getUser();
    }

    private void modifyUserInfo() {
        int reason = CheckUtil.CheckModifyUserInfo(mUser);
        if (reason != CheckUtil.PASS) {
            modifyUserInfoInvalid(reason);
            return;
        }

        if (mModifyDialog == null) {
            mModifyDialog = new MaterialDialog.Builder(this)
                    .content("正在修改中，请稍后")
                    .progress(true, 0)
                    .show();
        } else {
            mModifyDialog.show();
        }

        Observable<User> observable;

        if (URLUtil.isNetworkUrl(mUser.getAvatarUrl())) {
            observable = PiliPiliServer.getInstance().modifyUserInfo(mUser)
                    .subscribeOn(Schedulers.io());
        } else {
            observable = ImgurServer.getInstance().postImage(mUser.getAvatarUrl())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .flatMap(uploadPicture -> {
                        mUser.setAvatarUrl(uploadPicture.getLink());
                        return PiliPiliServer.getInstance().modifyUserInfo(mUser);
                    });
        }

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxUser -> modifyUserInfoSuccess(rxUser), throwable -> modifyUserInfoFail(throwable));
    }

    private void modifyUserInfoSuccess(User user) {
        mModifyDialog.dismiss();
        UserManager.getInstance().setUser(user);
        Toast.makeText(this, "用户信息修改成功", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(ACTION_USER_INFO_MODIFY);
        intent.putExtra(KEY_USER, user);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        finish();
    }

    private void modifyUserInfoFail(Throwable throwable) {
        mModifyDialog.dismiss();
        new NetExceptionHandler().handleException(this, throwable);
    }

    private void modifyUserInfoInvalid(int reason) {
        switch (reason) {
            case CheckUtil.EMPTY_AVATAR_URL: {
                Toast.makeText(this, "头像不能为空", Toast.LENGTH_SHORT).show();
                break;
            }
            case CheckUtil.EMPTY_NAME: {
                Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                break;
            }
            case CheckUtil.EMPTY_SEX: {
                Toast.makeText(this, "性别不能为空", Toast.LENGTH_SHORT).show();
                break;
            }
            case CheckUtil.UNKNOWN: {
                Toast.makeText(this, "未知错误", Toast.LENGTH_SHORT).show();
                break;
            }
            default: {
                break;
            }
        }
    }

    private void pickPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CODE_PICK_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_PICK_PICTURE: {
                    mUser.setAvatarUrl(PathUtil.getRealPathFromUri(data.getData()));
                    GlideApp.with(this)
                            .load(data.getData())
                            .placeholder(R.drawable.icon_placeholder)
                            .error(R.drawable.icon_error)
                            .into(mAvatarIv);
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    modifyUserInfo();
                } else {
                    Toast.makeText(this, "无法上传图片，请开放权限", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            default: {
                break;
            }
        }
    }
}
