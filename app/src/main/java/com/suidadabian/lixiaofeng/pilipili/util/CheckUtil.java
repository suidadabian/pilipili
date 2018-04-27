package com.suidadabian.lixiaofeng.pilipili.util;

public final class CheckUtil {
    private CheckUtil() {

    }

    public static boolean checkLoginData(String account, String password) {
        return account != null && !account.isEmpty() && password != null && !password.isEmpty();
    }

    public static boolean checkRegisteredData(String account, String userName, String password, String confirmPassword) {
        return account != null && !account.isEmpty()
                && userName != null && !userName.isEmpty()
                && password != null && !confirmPassword.isEmpty()
                && confirmPassword != null && !confirmPassword.isEmpty();
    }
}
