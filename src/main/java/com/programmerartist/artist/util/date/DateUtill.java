package com.programmerartist.artist.util.date;

import com.programmerartist.artist.util.common.ParamUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author 程序员Artist
 * @date 2019/8/23
 */
public class DateUtill {

    private static final ThreadLocal<Map<DateFormat, SimpleDateFormat>> SDF = ThreadLocal.withInitial(() -> {
        Map<DateFormat, SimpleDateFormat> df2Sdf = new HashMap<>();
        for(DateFormat df : DateFormat.values()) {
            df2Sdf.put(df, new SimpleDateFormat(df.getValue()));
        }
        return df2Sdf;
    });

    /**
     * 格式化日期
     *
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     */
    public static String format(Date date) {

        return format(date, DateFormat.YMD_HMS_MIDDLE);
    }


    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     * @author yideng
     */
    public static String format(Date date, DateFormat format) {
        if (null==date || null==format) return null;

        return SDF.get().get(format).format(date);
    }

    /**
     * 格式化日期
     *
     * yyyy-MM-dd HH:mm:ss
     *
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parse(String date) throws ParseException {

        return parse(date, DateFormat.YMD_HMS_MIDDLE);
    }

    /**
     * 格式化日期
     *
     * @param date
     * @param format
     * @return
     * @author yideng
     */
    public static Date parse(String date, DateFormat format) throws ParseException {

        if (ParamUtil.isBlank(date) || null==format) return null;

        return SDF.get().get(format).parse(date);
    }


    /**
     * 设置（修改）传入日期date的时分秒为指定值，不想修改的传-1
     *
     * @param date
     * @param hour
     * @param minute
     * @param second
     * @return
     */
    public static Date set(Date date, int hour, int minute, int second) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        if(hour >= 0)   calendar.set(Calendar.HOUR_OF_DAY, hour);
        if(minute >= 0) calendar.set(Calendar.MINUTE, minute);
        if(second >= 0) calendar.set(Calendar.SECOND, second);

        return calendar.getTime();
    }


    /**
     * 当前时间
     *
     * yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String now() {

        return DateUtill.format(new Date(), DateFormat.YMD_HMS_MIDDLE);
    }

    /**
     * 当前时间
     *
     * @param format
     * @return
     */
    public static String now(DateFormat format) {

        return DateUtill.format(new Date(), format);
    }

    /**
     * 当前时间戳
     *
     * @return
     */
    public static long current() {

        return System.currentTimeMillis();
    }

    /**
     * 当前时间戳：秒级
     *
     *
     * @return
     */
    public static int currentSec() {

        return (int)(System.currentTimeMillis() / 1000);
    }


    /**
     * 一段时间后 或者 一段时间前
     *
     * @param source
     * @param field
     * @param amount
     * @return
     */
    public static Date add(Date source, EnumDate field, int amount) {
        Date target;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(source);

        switch (field) {
            case YEAR:
                calendar.add(Calendar.YEAR, amount);
                break;
            case MONTH:
                calendar.add(Calendar.MONTH, amount);
                break;
            case DAY:
                calendar.add(Calendar.DATE, amount);
                break;

            case HOUR:
                calendar.add(Calendar.HOUR, amount);
                break;
            case MINUTES:
                calendar.add(Calendar.MINUTE, amount);
                break;
            case SECOND:
                calendar.add(Calendar.SECOND, amount);
                break;

            case MILLISECOND:
                calendar.add(Calendar.MILLISECOND, amount);
                break;

            default:
                break;
        }

        target = calendar.getTime();

        return target;
    }


    /**
     * 获取时间戳
     *
     * @return
     */
    public static long getTimestamp() {
        return System.currentTimeMillis();
    }



}
