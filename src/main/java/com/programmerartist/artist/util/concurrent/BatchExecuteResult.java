package com.programmerartist.artist.util.concurrent;

import java.util.LinkedHashMap;

/**
 * 批量执行结果
 *
 * @author 程序员Artist
 * @date 2023-07-18
 */
public class BatchExecuteResult<T> {

    /**
     * 任务标识 to 结果
     */
    private LinkedHashMap<String, T> taskFlag2Result;

    /**
     * 任务标识 to 异常，执行遇到异常的任务才会put到此map中
     */
    private LinkedHashMap<String, Throwable> taskFlag2Error;

    /**
     * 批量任务全部执行完成的总耗时
     */
    private long totalCost;

    /**
     *
     */
    public BatchExecuteResult() {
    }

    /**
     *
     * @return
     */
    public static BatchExecuteResult newResult() {
        BatchExecuteResult result = new BatchExecuteResult();
        result.taskFlag2Result    = new LinkedHashMap();
        result.taskFlag2Error     = new LinkedHashMap();

        return result;
    }

    public LinkedHashMap<String, T> getTaskFlag2Result() {
        return taskFlag2Result;
    }

    public LinkedHashMap<String, Throwable> getTaskFlag2Error() {
        return taskFlag2Error;
    }

    public long getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(long totalCost) {
        this.totalCost = totalCost;
    }

}
