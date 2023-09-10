package com.programmerartist.artist.util.date;

import com.programmerartist.artist.util.common.ParamUtil;

/**
 * @author 程序员Artist
 * @date 2019/8/23
 */
public enum EnumDate {

    YEAR("year"),
    MONTH("month"),
    DAY("day"),

    HOUR("hour"),
    MINUTES("minutes"),
    SECOND("second"),

    MILLISECOND("millisecond");

    private String name;

    EnumDate(String name) {
        this.name = name;
    }

    /**
     *
     * @param name
     * @return
     */
    public static EnumDate parse(String name) {
        if(ParamUtil.isBlank(name)) { return null; }

        for (EnumDate enumDate : values()) {
            if(enumDate.name.equalsIgnoreCase(name)) {
                return enumDate;
            }
        }

        return null;
    }

}
