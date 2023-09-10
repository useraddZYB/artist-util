package com.programmerartist.artist.util.concurrent;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 拒绝策略
 *
 * @author 程序员Artist
 * @date 2019/4/3
 */
public enum RejectedType {

    ABORT("abort", ThreadPoolFactory.DEFAULT_REJECTED_HANDLER),
    DISCARD("discard", new ThreadPoolExecutor.DiscardPolicy()),
    DISCARD_OLDEST("discardOldest", new ThreadPoolExecutor.DiscardOldestPolicy()),
    CALLER_RUNS("callerRuns", new ThreadPoolExecutor.CallerRunsPolicy()),
    LOG("log", null),
    METRICS("metrics", null),
    LOG_AND_METRICS("logAndMetrics", null);

    private String name;
    private RejectedExecutionHandler handler;

    /**
     *
     * @param namePrefix
     * @return
     */
    public RejectedExecutionHandler getHandler(String namePrefix) {
        switch (this) {
            case LOG:
                return new RejectedPolicy.LogPolicy(namePrefix);
            case METRICS:
                return new RejectedPolicy.MetricsPolicy(namePrefix);
            case LOG_AND_METRICS:
                return new RejectedPolicy.LogAndMetricsPolicy(namePrefix);
            default:
                return this.handler;
        }
    }

    RejectedType(String name, RejectedExecutionHandler handler) {
        this.name = name;
        this.handler = handler;
    }

    public String getName() {
        return name;
    }
}
