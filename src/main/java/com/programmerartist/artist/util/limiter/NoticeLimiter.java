package com.programmerartist.artist.util.limiter;


/**
 * 对外AP：通知告警频控工具
 *
 * @author 程序员Artist
 * @date 2022/12/1
 */
public interface NoticeLimiter {

    /**
     * 频控判断api：是否可通行：true表示可通行未被限流，false表示不可通行被限流了
     *
     * @param notice
     * @return
     */
    NoticeLimitResult limit(Notice notice);

}
