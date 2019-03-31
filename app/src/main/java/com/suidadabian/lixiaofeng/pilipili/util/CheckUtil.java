package com.suidadabian.lixiaofeng.pilipili.util;

import com.suidadabian.lixiaofeng.pilipili.model.InfoPicture;
import com.suidadabian.lixiaofeng.pilipili.model.LightPicture;
import com.suidadabian.lixiaofeng.pilipili.model.User;

public final class CheckUtil {
    public static final int PASS = 0;
    public static final int UNKNOWN = -1;
    public static final int CURRENT_PASSWORD_INCORRECT = 1;
    public static final int NEW_PASSWORD_INCORRECT = 2;
    public static final int NEW_CONFIRM_PASSWORD_INCORRECT = 3;
    public static final int EMPTY_TITLE = 4;
    public static final int EMPTY_PICTURE_URL = 5;
    public static final int EMPTY_AVATAR_URL = 6;
    public static final int EMPTY_NAME = 7;
    public static final int EMPTY_SEX = 8;
    public static final int EMPTY_CURRENT_PASSWORD = 9;
    public static final int EMPTY_NEW_PASSWORD = 10;
    public static final int EMPTY_CONFIRM_NEW_PASSWORD = 11;

    private CheckUtil() {

    }

    public static boolean checkLoginData(String account, String password) {
        return account != null && !account.isEmpty() && password != null && !password.isEmpty();
    }

    public static boolean checkRegisteredData(String account, String userName, String password, String confirmPassword) {
        return account != null && !account.isEmpty()
                && userName != null && !userName.isEmpty()
                && password != null && !password.isEmpty()
                && confirmPassword != null && !confirmPassword.isEmpty();
    }

    public static boolean checkComment(String comment) {
        return comment != null && !comment.isEmpty();
    }

    public static boolean checkReply(String reply) {
        return reply != null && !reply.isEmpty();
    }

    /**
     * @param infoPicture
     * @return {@link CheckUtil#UNKNOWN}, {@link CheckUtil#EMPTY_TITLE},
     * {@link CheckUtil#EMPTY_PICTURE_URL},{@link CheckUtil#PASS}
     */
    public static int checkUploadInfoPicture(InfoPicture infoPicture) {
        if (infoPicture == null) {
            return UNKNOWN;
        }

        if (infoPicture.getTitle() == null || infoPicture.getTitle().isEmpty()) {
            return EMPTY_TITLE;
        }

        if (infoPicture.getUrl() == null || infoPicture.getUrl().isEmpty()) {
            return EMPTY_PICTURE_URL;
        }

        return PASS;
    }

    /**
     * @param lightPicture
     * @return {@link CheckUtil#UNKNOWN},  {@link CheckUtil#EMPTY_PICTURE_URL},  {@link CheckUtil#PASS}
     */
    public static int checkLightPicture(LightPicture lightPicture) {
        if (lightPicture == null) {
            return UNKNOWN;
        }

        if (lightPicture.getUrl() == null || lightPicture.getUrl().isEmpty()) {
            return EMPTY_PICTURE_URL;
        }

        return PASS;
    }

    /**
     * @param user
     * @return {@link CheckUtil#UNKNOWN}, {@link CheckUtil#EMPTY_AVATAR_URL}, {@link CheckUtil#EMPTY_NAME},
     * {@link CheckUtil#EMPTY_SEX}, {@link CheckUtil#PASS}
     */
    public static int CheckModifyUserInfo(User user) {
        if (user == null) {
            return UNKNOWN;
        }

        if (user.getAccount() == null || user.getAccount().isEmpty()) {
            return UNKNOWN;
        }

        if (user.getPassword() == null || user.getPassword().isEmpty()) {
            return UNKNOWN;
        }

        if (user.getAvatarUrl() == null || user.getAvatarUrl().isEmpty()) {
            return EMPTY_AVATAR_URL;
        }

        if (user.getName() == null || user.getName().isEmpty()) {
            return EMPTY_NAME;
        }

        if (user.getSex() == null) {
            return EMPTY_SEX;
        }

        return PASS;
    }

    /**
     * @param password
     * @param currentPassword
     * @param newPassword
     * @param confirmNewPassword
     * @return {@link CheckUtil#UNKNOWN}, {@link CheckUtil#EMPTY_CURRENT_PASSWORD}, {@link CheckUtil#EMPTY_NEW_PASSWORD},
     * {@link CheckUtil#EMPTY_CONFIRM_NEW_PASSWORD}, {@link CheckUtil#CURRENT_PASSWORD_INCORRECT}, {@link CheckUtil#NEW_CONFIRM_PASSWORD_INCORRECT},
     * {@link CheckUtil#PASS}
     */
    public static int checkModifyPassword(String password, String currentPassword, String newPassword, String confirmNewPassword) {
        if (password == null) {
            return UNKNOWN;
        }

        if (currentPassword == null || currentPassword.isEmpty()) {
            return EMPTY_CURRENT_PASSWORD;
        }

        if (newPassword == null || newPassword.isEmpty()) {
            return EMPTY_NEW_PASSWORD;
        }

        if (confirmNewPassword == null || confirmNewPassword.isEmpty()) {
            return EMPTY_CONFIRM_NEW_PASSWORD;
        }

        if (!currentPassword.equals(password)) {
            return CURRENT_PASSWORD_INCORRECT;
        }

        if (!newPassword.equals(confirmNewPassword)) {
            return NEW_CONFIRM_PASSWORD_INCORRECT;
        }

        return PASS;
    }
}
