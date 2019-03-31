package com.suidadabian.lixiaofeng.pilipili.util;

import org.apache.commons.lang3.time.FastDateFormat;

import java.util.Date;

public final class DateUtil {
    public static FastDateFormat YYYY_MM_DD_HH_MM_SS = FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss");
    public static FastDateFormat YYYY_MM_DD = FastDateFormat.getInstance("yyyy-MM-dd");

    private DateUtil() {
    }

    public static String getDateString(Date date) {
        return YYYY_MM_DD_HH_MM_SS.format(date);
    }

    public static String getDateString(long millis) {
        return YYYY_MM_DD_HH_MM_SS.format(millis);
    }

    public static String getCurrentDateString() {
        return YYYY_MM_DD_HH_MM_SS.format(System.currentTimeMillis());
    }

    public static String getDateString(Date date, FastDateFormat fastDateFormat) {
        return fastDateFormat.format(date);
    }

    public static String getDateString(long millis, FastDateFormat fastDateFormat) {
        return fastDateFormat.format(millis);
    }

    public static String getCurrentDateString(FastDateFormat fastDateFormat) {
        return fastDateFormat.format(System.currentTimeMillis());
    }
}
