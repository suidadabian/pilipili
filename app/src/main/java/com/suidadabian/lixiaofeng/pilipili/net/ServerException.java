package com.suidadabian.lixiaofeng.pilipili.net;

public class ServerException extends RuntimeException {
    public int errorCode;

    public ServerException(int errorCode) {
        this.errorCode = errorCode;
    }
}
