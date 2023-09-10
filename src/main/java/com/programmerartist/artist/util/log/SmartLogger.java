package com.programmerartist.artist.util.log;

import com.programmerartist.artist.util.common.ParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.*;


/**
 * 智能明细日志。（支持配置中心在线改日志明细级别和白名单uid打明细日志，以实现避免刷大量日志的情况下也能看到明细日志的目标）
 *
 * @author 程序员Artist
 * @date 2019-08-23
 **/
public class SmartLogger {
    private static final Logger selfLog = LoggerFactory.getLogger(SmartLogger.class);

    private Logger log;

    private static Set<String> detailUserIdsConfig;
    private static LogType logTypeConfig;
    private static boolean printErrorConfig;

    /**
     * 业务初始化，只使用此构造参数
     *
     * @param log
     */
    public SmartLogger(Logger log) {
        this.log = log;
    }

    /**
     * spring装载配置中心配置，使支持在线调整开关
     *
     * @param detailUserIdsConfig
     * @param logTypeConfig
     * @param printErrorConfig
     */
    public SmartLogger(Set<String> detailUserIdsConfig, LogType logTypeConfig, boolean printErrorConfig) {
        selfLog.info("SmartLogger detailUserIdsConfig=" + ParamUtil.print(detailUserIdsConfig));
        selfLog.info("SmartLogger logTypeConfig=" + ParamUtil.print(logTypeConfig));
        selfLog.info("SmartLogger printErrorConfig=" + printErrorConfig);

        SmartLogger.detailUserIdsConfig = detailUserIdsConfig;
        SmartLogger.logTypeConfig = logTypeConfig;
        SmartLogger.printErrorConfig = printErrorConfig;
    }

    /**
     *
     * @param detailUserIdsConfig
     * @param logTypeConfig
     * @param printErrorConfig
     */
    public static void changeConfig(Set<String> detailUserIdsConfig, LogType logTypeConfig, boolean printErrorConfig) {
        selfLog.info("changeConfig detailUserIdsConfig=" + ParamUtil.print(detailUserIdsConfig));
        selfLog.info("changeConfig logTypeConfig=" + ParamUtil.print(logTypeConfig));
        selfLog.info("changeConfig printErrorConfig=" + printErrorConfig);

        SmartLogger.detailUserIdsConfig = detailUserIdsConfig;
        SmartLogger.logTypeConfig = logTypeConfig;
        SmartLogger.printErrorConfig = printErrorConfig;
    }


    /**
     * info
     */
    public void info(String format, Object... arguments) { this.infoUid(null, format, arguments); }

    public void infoUid(String userId, String format, Object... arguments) {
        String userIdLog = ParamUtil.isNotBlank(userId) ? "uid=" + userId + ", " : "";
        LogType logType  = this.getLogType(userId);

        switch (logType) {
            case NO:
                break;
            case SIMPLE:
                if(null!=arguments && arguments.length>0) {
                    Object[] newArgs = new Object[arguments.length];
                    int i = 0;
                    for(Object arg : arguments) {
                        if(arg instanceof Collection) {
                            newArgs[i] = ((Collection)arg).size();
                        }else if(arg instanceof Map) {
                            newArgs[i] = ((Map)arg).size();
                        }else {
                            newArgs[i] = arg;
                        }
                        i++;
                    }
                    log.info(userIdLog + format, newArgs);
                }else {
                    log.info(userIdLog + format, arguments);
                }
                break;
            case DETAIL:
                log.info(userIdLog + format, arguments);
                break;
        }
    }

    /**
     * error
     */
    public void error(String msg) { this.errorUid(null, msg, null); }
    public void errorUid(String userId, String msg) { this.errorUid(userId, msg, null); }
    public void error(String msg, Throwable t) { this.errorUid(null, msg, t); }
    public void errorUid(String userId, String msg, Throwable t) { this.doLog(LogLevel.ERROR, userId, msg, t); }

    /**
     * warn
     */
    public void warn(String msg) { this.warnUid(null, msg, null); }
    public void warnUid(String userId, String msg) { this.warnUid(userId, msg, null); }
    public void warn(String msg, Throwable t) { this.warnUid(null, msg, t); }
    public void warnUid(String userId, String msg, Throwable t) { this.doLog(LogLevel.WARN, userId, msg, t); }

    /**
     * debug
     */
    public void debug(String msg) { this.debugUid(null, msg, null); }
    public void debugUid(String userId, String msg) { this.debugUid(userId, msg, null); }
    public void debug(String msg, Throwable t) { this.debugUid(null, msg, t); }
    public void debugUid(String userId, String msg, Throwable t) { this.doLog(LogLevel.DEBUG, userId, msg, t); }


    /**
     *
     * @return
     */
    public LogType getLogType() { return this.getLogType(null); }
    public LogType getLogType(String userId) { return this.doGetType(ParamUtil.isNotBlank(userId), userId, false); }


    /**
     * 打印日志类型
     */
    public enum LogType {
        NO, SIMPLE, DETAIL
    }

    public Set<String> getDetailUserIdsConfig() {
        return detailUserIdsConfig;
    }

    public LogType getLogTypeConfig() {
        return logTypeConfig;
    }

    public boolean isPrintErrorConfig() {
        return printErrorConfig;
    }

    // ======================================================================================

    /**
     *
     * @param logLevel
     * @param userId
     * @param msg
     * @param e
     */
    private void doLog(LogLevel logLevel, String userId, String msg, Throwable e) {
        boolean hasUid;
        String userIdLog = (hasUid=ParamUtil.isNotBlank(userId)) ? "uid=" + userId + ", " : "";
        LogType type     = this.doGetType(hasUid, userId, LogLevel.ERROR==logLevel);

        switch (type) {
            case NO:
                break;
            case SIMPLE:
                switch (logLevel) {
                    case ERROR:
                        log.error(userIdLog + msg + (null!=e ? ", eMsg=" + e.getMessage() : ""));
                        break;
                    case WARN:
                        log.warn(userIdLog + msg + (null!=e ? ", eMsg=" + e.getMessage() : ""));
                        break;
                    case DEBUG:
                        log.debug(userIdLog + msg + (null!=e ? ", eMsg=" + e.getMessage() : ""));
                }
                break;
            case DETAIL:
                switch (logLevel) {
                    case ERROR:
                        log.error(userIdLog + msg, e);
                        break;
                    case WARN:
                        if(null == e) {
                            log.warn(userIdLog + msg);
                        }else {
                            log.warn(userIdLog + msg, e);
                        }
                        break;
                    case DEBUG:
                        if(null == e) {
                            log.debug(userIdLog + msg);
                        }else {
                            log.debug(userIdLog + msg, e);
                        }
                }
                break;
        }
    }


    /**
     *
     * @param hasUid
     * @param userId
     * @param levelIsError
     * @return
     */
    private LogType doGetType(boolean hasUid, String userId, boolean levelIsError) {
        // 判断日志类型
        LogType logType = logTypeConfig;

        if(hasUid && detailUserIdsConfig.contains(userId)) {
            logType = LogType.DETAIL;
        }else if(levelIsError && !printErrorConfig)  {
            logType = LogType.NO;
        }
        return null!=logType ? logType : LogType.SIMPLE;
    }

    /**
     *
     */
    public enum LogLevel {
        ERROR, INFO, WARN, DEBUG
    }


    /**
     * 此demo方法仅供API示范，不可运行，需要在业务spring环境中执行
     *
     * @param args
     */
    public static void main(String[] args) {
        Logger log = LoggerFactory.getLogger(SmartLogger.class);
        SmartLogger smartLog = new SmartLogger(log);

        List<String> list = new ArrayList<String>(){{
            add("china");
            add("america");
        }};

        smartLog.info("test smart info: hello {}, list={}", "world", list);
        smartLog.infoUid("12356", "test smart info: hello {}, list={}", "world", list);
        smartLog.warn("test smart warn: hello " + "world, " + "list=" + list, new RuntimeException("ex"));
        smartLog.warnUid("123561", "test smart warn: hello " + "world, " + "list=" + list, new RuntimeException("ex"));
        smartLog.debug("test smart debug: hello " + "world, " + "list=" + list, new RuntimeException("ex111"));
        smartLog.error("test smart error: hello " + "world, " + "list=" + list, new RuntimeException("ex"));
        smartLog.errorUid("123561", "test smart error: hello " + "world, " + "list=" + list, new RuntimeException("ex"));
    }


}
