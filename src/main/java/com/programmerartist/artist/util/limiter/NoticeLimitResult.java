package com.programmerartist.artist.util.limiter;


import java.util.List;

/**
 * 对外AP：通知告警频控工具，频控结果返回值（内含被限流的历史通知记录）
 *
 * 1、当返回pass=false时，另外两个历史被限流通知集合都为null，注意判断null
 * 2、当返回pass=true时，如果是首次命中 ? 另外两个历史被限流通知集合都为null : 另外两个历史被限流通知集合都有值（建议追加到当前通知后面）
 *
 * @author 程序员Artist
 * @date 2022-12-05
 */
public class NoticeLimitResult {

    /**
     * 是否可通行：true表示可通行未被限流，false表示不可通行被限流了
     */
    private boolean pass;


    /**
     * 被限流的历史通知记录
     */
    private List<Notice> limitTitleQueue;
    private List<Notice> limitContentQueue;

    /**
     *
     * @param pass
     */
    public NoticeLimitResult(boolean pass) {
        this.pass = pass;
    }

    /**
     *
     * @param pass
     * @param limitTitleQueue
     * @param limitContentQueue
     */
    public NoticeLimitResult(boolean pass, List<Notice> limitTitleQueue, List<Notice> limitContentQueue) {
        this.pass = pass;
        this.limitTitleQueue = limitTitleQueue;
        this.limitContentQueue = limitContentQueue;
    }

    /**
     *
     * @return
     */
    public String toStringWithDetailWhenTrue() {
        return pass ? this.toString() : "false";
    }

    public boolean isPass() {
        return pass;
    }

    public List<Notice> getLimitTitleQueue() {
        return limitTitleQueue;
    }

    public List<Notice> getLimitContentQueue() {
        return limitContentQueue;
    }

    @Override
    public String toString() {
        return "NoticeLimitResult{" +
                "pass=" + pass +
                ", limitTitleQueue=" + limitTitleQueue +
                ", limitContentQueue=" + limitContentQueue +
                '}';
    }
}
