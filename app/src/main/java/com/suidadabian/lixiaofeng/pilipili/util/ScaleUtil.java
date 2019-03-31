package com.suidadabian.lixiaofeng.pilipili.util;

public final class ScaleUtil {

    private ScaleUtil() {

    }

    public static float getScale(int width, int height) {
        return (float) height / width;
    }

    public static int getWidth(int height, float scale) {
        return (int) (height / scale);
    }

    public static int getHeight(int width, float scale) {
        return (int) (width * scale);
    }
}
