package com.suidadabian.lixiaofeng.pilipili.net;

import android.content.Context;
import android.widget.Toast;

import static com.suidadabian.lixiaofeng.pilipili.net.Code.ACCOUNT_EXIST;
import static com.suidadabian.lixiaofeng.pilipili.net.Code.NO_ACCOUNT;
import static com.suidadabian.lixiaofeng.pilipili.net.Code.NO_AUTHORITY;
import static com.suidadabian.lixiaofeng.pilipili.net.Code.NO_COMMENT;
import static com.suidadabian.lixiaofeng.pilipili.net.Code.NO_INFO_PICTURE;
import static com.suidadabian.lixiaofeng.pilipili.net.Code.NO_LIGHT_PICTURE;
import static com.suidadabian.lixiaofeng.pilipili.net.Code.PASSWORD_ERROR;
import static com.suidadabian.lixiaofeng.pilipili.net.Code.USERNAME_EXIST;

public class NetExceptionHandler {

    public void handleException(Context context, Throwable throwable) {
        if (throwable == null) {
            Toast.makeText(context, "发生未知错误", Toast.LENGTH_SHORT).show();
            return;
        }

        if (throwable instanceof NoConnectException) {
            Toast.makeText(context, "无网络连接", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!(throwable instanceof ServerException)) {
            Toast.makeText(context, "发生未知错误", Toast.LENGTH_SHORT).show();
            return;
        }

        ServerException serverException = (ServerException) throwable;
        if (handlerServerException(context, serverException)) {
            return;
        }

        switch (serverException.errorCode) {
            case NO_ACCOUNT: {
                Toast.makeText(context, "账号不存在", Toast.LENGTH_SHORT).show();
                break;
            }
            case PASSWORD_ERROR: {
                Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
                break;
            }
            case ACCOUNT_EXIST: {
                Toast.makeText(context, "账号已存在", Toast.LENGTH_SHORT).show();
                break;
            }
            case USERNAME_EXIST: {
                Toast.makeText(context, "用户名已存在", Toast.LENGTH_SHORT).show();
                break;
            }
            case NO_INFO_PICTURE: {
                Toast.makeText(context, "没有相应重图", Toast.LENGTH_SHORT).show();
                break;
            }
            case NO_LIGHT_PICTURE: {
                Toast.makeText(context, "没有相应轻图", Toast.LENGTH_SHORT).show();
                break;
            }
            case NO_COMMENT: {
                Toast.makeText(context, "没有相应评论", Toast.LENGTH_SHORT).show();
                break;
            }
            case NO_AUTHORITY: {
                Toast.makeText(context, "没有足够权限", Toast.LENGTH_SHORT).show();
                break;
            }
            default: {
                Toast.makeText(context, "发生未知错误", Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }


    protected boolean handlerServerException(Context context, ServerException serverException) {
        return false;
    }
}
