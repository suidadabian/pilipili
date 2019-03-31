package com.suidadabian.lixiaofeng.pilipili.upload;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.webkit.URLUtil;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserManager;
import com.suidadabian.lixiaofeng.pilipili.detail.InfoPictureActivity;
import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.net.ImgurServer;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.util.CheckUtil;
import com.suidadabian.lixiaofeng.pilipili.util.PathUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UploadInfoPictureActivity extends AppCompatActivity {
    private static final int CODE_REQUEST_PERMISSION = 2;
    private static final int CODE_PICK_PICTURE = 1;
    private Toolbar mToolbar;
    private ImageView mPictureIv;
    private ImageButton mPictureEditIbtn;
    private TextView mTitleTv;
    private TextView mIntroTv;
    private CheckBox mShareCb;
    private InfoPicture mInfoPicture;
    private MaterialDialog mUploadDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_info_picture);
        initData();
        initView();
    }

    private void initData() {
        mInfoPicture = new InfoPicture();
        mInfoPicture.setTitle("");
        mInfoPicture.setIntro("");
        mInfoPicture.setUserId(UserManager.getInstance().getUser().getId());
    }

    private void initView() {
        mToolbar = findViewById(R.id.upload_info_picture_toolbar);
        mPictureIv = findViewById(R.id.upload_picture_iv);
        mPictureEditIbtn = findViewById(R.id.upload_picture_edit_ibtn);
        mTitleTv = findViewById(R.id.upload_info_picture_title_tv);
        mIntroTv = findViewById(R.id.upload_info_picture_intro_tv);
        mShareCb = findViewById(R.id.upload_info_picture_share_cb);

        mToolbar.setTitle("上传重图");
        mToolbar.inflateMenu(R.menu.menu_upload_info_picture);
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_upload_info_picture_complete: {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        uploadInfoPicture();
                    } else {
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_REQUEST_PERMISSION);
                    }
                    break;
                }
            }
            return true;
        });
        mToolbar.setNavigationOnClickListener(v -> finish());

        if (mInfoPicture.getUrl() != null) {
            GlideApp.with(this)
                    .load(mInfoPicture.getUrl())
                    .placeholder(R.drawable.icon_placeholder)
                    .error(R.drawable.icon_error)
                    .into(mPictureIv);
        }
        mTitleTv.setText(mInfoPicture.getTitle());
        mIntroTv.setText(mInfoPicture.getIntro());
        mShareCb.setChecked(mInfoPicture.isShare());

        mPictureEditIbtn.setOnClickListener(v -> pickPicture());
        findViewById(R.id.upload_info_picture_title_ll).setOnClickListener(v -> {
            new MaterialDialog.Builder(UploadInfoPictureActivity.this)
                    .title("编辑标题")
                    .input("编辑你的标题", mInfoPicture.getTitle(), (dialog, input) -> {
                        mInfoPicture.setTitle(input.toString());
                        mTitleTv.setText(input);
                    })
                    .show();
        });
        findViewById(R.id.upload_info_picture_intro_ll).setOnClickListener(v -> {
            new MaterialDialog.Builder(UploadInfoPictureActivity.this)
                    .title("编辑简介")
                    .input("编辑你的简介", mInfoPicture.getIntro(), (dialog, input) -> {
                        mInfoPicture.setIntro(input.toString());
                        mIntroTv.setText(input);
                    })
                    .show();
        });
        findViewById(R.id.upload_info_picture_share_ll).setOnClickListener(v -> {
            boolean share = mInfoPicture.isShare();
            mInfoPicture.setShare(!share);
            mShareCb.setChecked(!share);
        });
    }

    private void pickPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CODE_PICK_PICTURE);
    }

    private void uploadInfoPicture() {
        int reason = CheckUtil.checkUploadInfoPicture(mInfoPicture);
        if (reason != CheckUtil.PASS) {
            uploadInfoPictureInvalid(reason);
            return;
        }

        if (mUploadDialog == null) {
            mUploadDialog = new MaterialDialog.Builder(this)
                    .content("正在上传中，请稍后")
                    .progress(true, 0)
                    .show();
        } else {
            mUploadDialog.show();
        }

        Observable<InfoPicture> observable;

        if (URLUtil.isNetworkUrl(mInfoPicture.getUrl())) {
            observable = PiliPiliServer.getInstance().uploadInfoPicture(mInfoPicture);
        } else {
            observable = ImgurServer.getInstance().postImage(mInfoPicture.getUrl())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .flatMap(uploadPicture -> {
                        mInfoPicture.setUrl(uploadPicture.getLink());
                        mInfoPicture.setDeletehash(uploadPicture.getDeletehash());

                        return PiliPiliServer.getInstance().uploadInfoPicture(mInfoPicture);
                    });
        }

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxInfoPicture -> uploadInfoPictureSuccess(rxInfoPicture), throwable -> uploadInfoPictureFail(throwable));
    }

    private void uploadInfoPictureSuccess(InfoPicture infoPicture) {
        mUploadDialog.dismiss();

        Intent intent = new Intent(this, InfoPictureActivity.class);
        intent.putExtra(InfoPictureActivity.kEY_INFO_PICTURE_ID, infoPicture.getId());
        startActivity(intent);
        finish();
    }

    private void uploadInfoPictureFail(Throwable throwable) {
        mUploadDialog.dismiss();
        new NetExceptionHandler().handleException(this, throwable);
    }

    private void uploadInfoPictureInvalid(int reason) {
        switch (reason) {
            case CheckUtil.EMPTY_TITLE: {
                Toast.makeText(this, "标题不能为空", Toast.LENGTH_SHORT).show();
                break;
            }
            case CheckUtil.EMPTY_PICTURE_URL: {
                Toast.makeText(this, "图片不能为空", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_PICK_PICTURE: {
                    mInfoPicture.setUrl(PathUtil.getRealPathFromUri(data.getData()));
                    GlideApp.with(this)
                            .load(data.getData())
                            .placeholder(R.drawable.icon_placeholder)
                            .error(R.drawable.icon_error)
                            .into(mPictureIv);
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
                    uploadInfoPicture();
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
