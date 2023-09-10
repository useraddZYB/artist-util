package com.programmerartist.artist.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 只打印特定次数的异常堆栈，或者特定次数的日志
 *
 * @author 程序员Artist
 * @date 2023-08-10
 */
public class LogControl {

    /**
     * 最多打印指定次数的堆栈信息，之后只打印 e.getMessage() 不打印异常堆栈
     *
     * @param log
     * @param countDown
     * @param message
     * @param e
     */
    public static void warnException(Logger log, CountDown countDown, String message, Throwable e) {
        if(countDown.hasDown()) {
            log.warn(message, e.getMessage());
        }else {
            countDown.incrementAndGet();
            log.warn(message, e);
        }
    }


    /**
     * 最多打印指定次数的堆栈信息，之后只打印 e.getMessage() 不打印异常堆栈
     *
     * @param log
     * @param countDown
     * @param message
     * @param e
     */
    public static void errorException(Logger log, CountDown countDown, String message, Throwable e) {
        if(countDown.hasDown()) {
            log.error(message, e.getMessage());
        }else {
            countDown.incrementAndGet();
            log.error(message, e);
        }
    }

    /**
     * 最多打印指定次数的日志，之后不打印日志
     *
     * @param log
     * @param countDown
     * @param format
     * @param args
     */
    public static void error(Logger log, CountDown countDown, String format, Object... args) {
        doLog(SmartLogger.LogLevel.ERROR, log, countDown, format, args);
    }

    /**
     * 最多打印指定次数的日志，之后不打印日志
     *
     * @param log
     * @param countDown
     * @param format
     * @param args
     */
    public static void info(Logger log, CountDown countDown, String format, Object... args) {
        doLog(SmartLogger.LogLevel.INFO, log, countDown, format, args);
    }

    /**
     * 最多打印指定次数的日志，之后不打印日志
     *
     * @param log
     * @param countDown
     * @param format
     * @param args
     */
    public static void warn(Logger log, CountDown countDown, String format, Object... args) {
        doLog(SmartLogger.LogLevel.WARN, log, countDown, format, args);
    }

    /**
     * 最多打印指定次数的日志，之后不打印日志
     *
     * @param log
     * @param countDown
     * @param format
     * @param args
     */
    public static void debug(Logger log, CountDown countDown, String format, Object... args) {
        doLog(SmartLogger.LogLevel.DEBUG, log, countDown, format, args);
    }

    /**
     *
     * @param log
     * @param countDown
     * @param format
     * @param args
     */
    private static void doLog(SmartLogger.LogLevel level, Logger log, CountDown countDown, String format, Object... args) {
        if(!countDown.hasDown()) {
            countDown.incrementAndGet();

            switch (level) {
                case ERROR:
                    log.error(format, args);
                    break;
                case INFO:
                    log.info(format, args);
                    break;
                case WARN:
                    log.warn(format, args);
                    break;
                case DEBUG:
                    log.debug(format, args);
                    break;
            }
        }
    }


    public static void main(String[] args) {
        CountDown countDown = new CountDown(2);
        Logger log = LoggerFactory.getLogger(LogControl.class);

        for(int i=0; i<10; i++) {
            LogControl.error(log, countDown, "test error {}", i);
        }

        System.out.println();
        System.out.println();
        System.out.println();

        countDown.reset();
        for(int i=0; i<10; i++) {
            try {
                int e = i / 0;
            } catch (Exception ex) {
                LogControl.errorException(log, countDown, "test Error " + i, ex);
            }
        }
    }



}
