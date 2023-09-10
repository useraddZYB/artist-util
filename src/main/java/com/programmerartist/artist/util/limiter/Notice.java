package com.programmerartist.artist.util.limiter;

import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通知告警限流工具。通知实体bean类
 *
 * @author 程序员Artist
 * @date 2022-12-01
 */
public class Notice {

    private static final AtomicInteger uniqCoder = new AtomicInteger();

    /**
     * 通知标题：严格匹配；用标题这种粗粒度频控来限制
     */
    private String title;

    /**
     * 通知内容：模糊匹配；用内容这种细粒度频控来限制
     */
    private String content;

    /**
     *
     * @param title
     * @param content
     */
    public Notice(String title, String content) {
        this.title    = title;
        this.content  = content;
        this.uniqCode = uniqCoder.incrementAndGet();
        this.date     = new Date();
    }
    /**
     *
     * @param title
     * @param content
     * @param remark
     */
    public Notice(String title, String content, String remark) {
        this.title    = title;
        this.content  = content;
        this.uniqCode = uniqCoder.incrementAndGet();
        this.date     = new Date();
        this.remark   = remark;
    }

    public static AtomicInteger getUniqCoder() {
        return uniqCoder;
    }



// ===================================== 以下都是附属信息 ===========================================

    /**
     * 为每一封邮件给一个唯一的编号，用以后续返回值中聚合消息用
     */
    protected int uniqCode;
    /**
     * 当时这封通知的发送日期
     */
    protected Date date;
    /**
     * 告警附属摘要：用以最终展示被限流的告警主体
     */
    private String remark = "";

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getUniqCode() {
        return uniqCode;
    }

    public Date getDate() {
        return date;
    }

    public String getRemark() {
        return remark;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Notice{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", uniqCode=" + uniqCode +
                ", date=" + date +
                ", remark='" + remark + '\'' +
                '}';
    }
}
