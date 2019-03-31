package com.suidadabian.lixiaofeng.pilipili.launcher;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.suidadabian.lixiaofeng.pilipili.R;
import com.suidadabian.lixiaofeng.pilipili.common.sp.UserManager;
import com.suidadabian.lixiaofeng.pilipili.login.LoginActivity;
import com.suidadabian.lixiaofeng.pilipili.main.MainActivity;

public class LauncherActivity extends AppCompatActivity {
    private static final long INIT_DELAY = 1000;
    private static final long EXIT_DELAY = 700;
    private static final int CODE_REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_launcher);

        new Handler().postDelayed(() -> init(), INIT_DELAY);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void init() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(this, UserManager.getInstance().isLogin() ? MainActivity.class : LoginActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, CODE_REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CODE_REQUEST_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, UserManager.getInstance().isLogin() ? MainActivity.class : LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "应用无法运行，请开放权限！", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(() -> finish(), EXIT_DELAY);
                }
                break;
            }
            default: {
                break;
            }
        }
    }
}
