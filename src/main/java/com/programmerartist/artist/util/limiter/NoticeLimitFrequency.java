package com.programmerartist.artist.util.limiter;



/**
 * 对外AP：通知告警频控，具体时间频率初始化注册类
 *
 * @author 程序员Artist
 * @date 2022-12-05
 */
public class NoticeLimitFrequency {

    private Integer titleSecondsPerPass;
    private Integer contentSecondsPerPass;

    /**
     * 设置具体时间频率：同时设置，表示标题频控和正文频控需要同时满足，才能放行
     *
     * @param titleSecondsPerPass   标题间隔多少秒才能发一封通知。不设置可输入null，表示不需要标题频控
     * @param contentSecondsPerPass 正文间隔多少秒才能发一封通知。不设置可输入null
     */
    public NoticeLimitFrequency(Integer titleSecondsPerPass, Integer contentSecondsPerPass) {
        this.titleSecondsPerPass = titleSecondsPerPass;
        this.contentSecondsPerPass = contentSecondsPerPass;
    }

    public Integer getTitleSecondsPerPass() {
        return titleSecondsPerPass;
    }

    public Integer getContentSecondsPerPass() {
        return contentSecondsPerPass;
    }
}
