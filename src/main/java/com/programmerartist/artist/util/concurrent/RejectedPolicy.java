package com.programmerartist.artist.util.concurrent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author 程序员Artist
 * @date 2019/4/3
 */
public class RejectedPolicy {

    /**
     *
     */
    public static class LogPolicy implements RejectedExecutionHandler {
        private static final Logger log = LoggerFactory.getLogger(LogPolicy.class);

        private String threadNamePre;
        public LogPolicy(String threadNamePre) { this.threadNamePre = threadNamePre; }

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            log.info("[warn] thread pool reject: threadNamePre=" + this.threadNamePre);
        }
    }

    /**
     *
     */
    public static class MetricsPolicy implements RejectedExecutionHandler {
        private String threadNamePre;
//        private static final SimpleMetrics metrics = Metrics.simple("thread_pool_reject");
        public MetricsPolicy(String threadNamePre) { this.threadNamePre = threadNamePre; }

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
//            metrics.qps(this.threadNamePre);
        }
    }

    /**
     *
     */
    public static class LogAndMetricsPolicy implements RejectedExecutionHandler {
        private static final Logger log = LoggerFactory.getLogger(LogAndMetricsPolicy.class);

        private String threadNamePre;
//        private static final SimpleMetrics metrics = Metrics.simple("thread_pool_reject");
        public LogAndMetricsPolicy(String threadNamePre) { this.threadNamePre = threadNamePre; }

        public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
            log.info("[warn] thread pool reject: threadNamePre=" + this.threadNamePre);
//            metrics.qps(this.threadNamePre);
        }
    }
}
