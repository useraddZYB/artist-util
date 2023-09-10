package com.programmerartist.artist.util.log;

import org.slf4j.Logger;

/**
 * 用于调试阶段打印日志
 *
 * @author 程序员Artist
 * @date 2021-11-08
 */

public class PrintLogger {

    private Logger log;
    private boolean logger;
    private boolean print;

    /**
     *
     * @param log    log
     * @param logger true ? 打印 : 不打印
     * @param print  true ? 打印 : 不打印
     */
    public PrintLogger(Logger log, boolean logger, boolean print) {
        this.log = log;
        this.logger = logger;
        this.print = print;
    }


    /**
     *
     * @param logContent
     */
    public void info(String logContent) {
       if(logger) { log.info(logContent); }
       if(print) { System.out.println(logContent); }
    }


}
