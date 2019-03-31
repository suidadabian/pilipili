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
import android.widget.Toast;
import android.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.suidadabian.lixiaofeng.pilipili.GlideApp;
import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserManager;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;
import com.suidadabian.lixiaofeng.pilipili.net.ImgurServer;
import com.suidadabian.lixiaofeng.pilipili.net.NetExceptionHandler;
import com.suidadabian.lixiaofeng.pilipili.net.PiliPiliServer;
import com.suidadabian.lixiaofeng.pilipili.util.CheckUtil;
import com.suidadabian.lixiaofeng.pilipili.util.PathUtil;
import com.suidadabian.lixiaofeng.pilipili.util.ScaleUtil;
import com.suidadabian.lixiaofeng.pilipili.watch.PictureWatchActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UploadLightPictureActivity extends AppCompatActivity {
    private static final int CODE_REQUEST_PERMISSION = 2;
    private static final int CODE_PICk_PICTURE = 1;
    private Toolbar mToolbar;
    private ImageView mPictureIv;
    private ImageButton mEditPictureIbtn;
    private CheckBox mShareCb;
    private LightPicture mLightPicture;
    private MaterialDialog mUploadDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_upload_light_picture);
        initData();
        initView();
    }

    private void initData() {
        mLightPicture = new LightPicture();
        mLightPicture.setUserId(UserManager.getInstance().getUser().getId());
    }

    private void initView() {
        mToolbar = findViewById(R.id.upload_light_picture_toolbar);
        mPictureIv = findViewById(R.id.upload_picture_iv);
        mEditPictureIbtn = findViewById(R.id.upload_picture_edit_ibtn);
        mShareCb = findViewById(R.id.upload_light_picture_share_cb);

        mToolbar.setTitle("上传轻图");
        mToolbar.inflateMenu(R.menu.menu_upload_light_picture);
        mToolbar.setNavigationOnClickListener(v -> {
            finish();
        });
        mToolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_upload_light_picture_complete: {
                    if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        uploadLightPicture();
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

        if (mLightPicture.getUrl() != null) {
            GlideApp.with(this)
                    .load(mLightPicture.getUrl())
                    .placeholder(R.drawable.icon_placeholder)
                    .error(R.drawable.icon_error)
                    .into(mPictureIv);
        }
        mShareCb.setChecked(mLightPicture.isShare());

        mEditPictureIbtn.setOnClickListener(v -> pickPicture());
        findViewById(R.id.upload_light_picture_share_ll).setOnClickListener(v -> {
            boolean share = mLightPicture.isShare();
            mLightPicture.setShare(!share);
            mShareCb.setChecked(!share);
        });
    }

    private void pickPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, CODE_PICk_PICTURE);
    }

    private void uploadLightPicture() {
        int reason = CheckUtil.checkLightPicture(mLightPicture);
        if (reason != CheckUtil.PASS) {
            uploadLightPictureInvalid(reason);
            return;
        }

        if (mUploadDialog == null) {
            mUploadDialog = new MaterialDialog.Builder(this)
                    .content("正在上传中，请稍后")
                    .progress(true, 0)
                    .show();
        }

        Observable<LightPicture> observable;

        if (URLUtil.isNetworkUrl(mLightPicture.getUrl())) {
            observable = PiliPiliServer.getInstance().uploadLightPicture(mLightPicture);
        } else {
            observable = ImgurServer.getInstance().postImage(mLightPicture.getUrl())
                    .subscribeOn(Schedulers.io())
                    .observeOn(Schedulers.io())
                    .flatMap(uploadPicture -> {
                        mLightPicture.setUrl(uploadPicture.getLink());
                        mLightPicture.setScale(ScaleUtil.getScale(uploadPicture.getWidht(), uploadPicture.getHeight()));
                        mLightPicture.setDeletehash(uploadPicture.getDeletehash());
                        return PiliPiliServer.getInstance().uploadLightPicture(mLightPicture);
                    });
        }

        observable.observeOn(AndroidSchedulers.mainThread())
                .subscribe(rxLightPicture -> uploadLightPictureSuccess(rxLightPicture), throwable -> uploadLightPictureFail(throwable));
    }

    private void uploadLightPictureSuccess(LightPicture lightPicture) {
        mUploadDialog.dismiss();

        Intent intent = new Intent(this, PictureWatchActivity.class);
        intent.putExtra(PictureWatchActivity.KEY_PICTURE_URL, lightPicture.getUrl());
        intent.putExtra(PictureWatchActivity.KEY_FLAG_DOWNLOAD_ENABLE, true);
        startActivity(intent);
        finish();
    }

    private void uploadLightPictureFail(Throwable throwable) {
        mUploadDialog.dismiss();
        new NetExceptionHandler().handleException(this, throwable);
    }

    private void uploadLightPictureInvalid(int reason) {
        switch (reason) {
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
                case CODE_PICk_PICTURE: {
                    mLightPicture.setUrl(PathUtil.getRealPathFromUri(data.getData()));
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
                    uploadLightPicture();
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
