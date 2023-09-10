package com.programmerartist.artist.util.date;

/**
 * @author 程序员Artist
 * @date 2019/8/23
 */
public enum DateFormat {

    YMD_HMS_NONE("yyyyMMdd HH:mm:ss"),
    YMD_HMS_MIDDLE("yyyy-MM-dd HH:mm:ss"),
    YMD_HMS_DOWN("yyyy/MM/dd HH:mm:ss"),

    YMD_NONE("yyyyMMdd"),
    YMD_MIDDLE("yyyy-MM-dd"),
    YMD_DOWN("yyyy/MM/dd"),

    Y_NONE("yyyy"),
    YM_NONE("yyyyMM"),
    YMDH_NONE("yyyyMMddHH"),
    YMDHM_NONE("yyyyMMddHHmm"),
    YMDHMS_NONE("yyyyMMddHHmmss"),
    YMD_HM_MIDDLE("yyyy-MM-dd HH:mm"),

    HMS_MIDDLE("HH:mm:ss");

    private String value;

    DateFormat(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
