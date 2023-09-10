package com.programmerartist.artist.util.limiter;

import com.programmerartist.artist.util.common.ParamUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 对外AP：通知告警频控工具实现类，基于本地单机频控
 *
 * @author 程序员Artist
 * @date 2022-12-19
 */
public class NoticeLimiterLocal implements NoticeLimiter {
    private static final Logger log = LoggerFactory.getLogger(NoticeLimiterLocal.class);

    /**
     *
     */
    private NoticeLimitFrequency frequency;

    private final boolean limitTitle;
    private final boolean limitContent;

    /**
     *
     * @param frequency
     */
    public NoticeLimiterLocal(NoticeLimitFrequency frequency) {
        this.frequency = frequency;

        this.limitTitle   = null!=frequency.getTitleSecondsPerPass();
        this.limitContent = null!=frequency.getContentSecondsPerPass();
    }

    /**
     * 所有排队的通知告警
     *
     */
    private Map<String, NoticeQueue> title2Queue   = new HashMap<>();
    private Map<String, NoticeQueue> content2Queue = new HashMap<>();


    /**
     * 频控判断api：是否可通行：true表示可通行未被限流，false表示不可通行被限流了
     *
     * 有锁，当前限流对象作为锁对象，并发安全，本方法都是本地计算，锁时间极短
     *
     * @param notice 当前通知类，通过new来初始化，每次发送业务"通知"之前需要生成一份频控通知
     * @return @see NoticeLimitResult 频控结果返回值（内含被限流的历史通知记录）
     */
    @Override
    public synchronized NoticeLimitResult limit(Notice notice) {
        if(ParamUtil.isAllBlank(notice.getTitle(), notice.getContent())) {
            return new NoticeLimitResult(true);
        }

        NoticeLimitResult limitResult = null;

        // 1、初始化
        String title      = "";
        NoticeQueue titleQueue = null;
        if(this.limitTitle) {
            title = notice.getTitle();                           // 标题采用严格匹配
            titleQueue = title2Queue.get(title);
            if(null == titleQueue) {
                titleQueue = new NoticeQueue();
                title2Queue.put(title, titleQueue);
            }
        }else {
            notice.setTitle("");
        }

        String dedupContent      = "";
        NoticeQueue contentQueue = null;
        if(this.limitContent) {
            dedupContent = this.getDedupContent(notice.getContent());  // 正文采用模糊匹配
            contentQueue = content2Queue.get(dedupContent);
            if(null == contentQueue) {
                contentQueue = new NoticeQueue();
                content2Queue.put(dedupContent, contentQueue);
            }
        }else {
            notice.setContent("");
        }

        // 2、先判断是否超过限制
        boolean pass = this.isPass(titleQueue, contentQueue, notice.getDate());

        // 3、如果超过限制，则通过，并且弹出全部排队的
        final String remark = ParamUtil.isNotBlank(notice.getRemark()) ? notice.getRemark() : "";
        if(pass) {
            List<Notice> contentQueueNotice = null;
            if(limitContent) {
                contentQueueNotice = contentQueue.offerAll();
                log.info("limitContent pass: uniqCode={}, remark={}, dedupContent={}", notice.getUniqCode(), remark, dedupContent);
                // System.out.println("limitContent pass: uniqCode="+notice.getUniqCode()+", remark="+remark+", dedupContent="+dedupContent);
            }

            List<Notice> titleQueueNotice = null;
            if(limitTitle) {
                if(limitContent) {
                    titleQueueNotice = titleQueue.offerSameUniqCode(
                            ParamUtil.isNotBlank(contentQueueNotice)
                                    ? contentQueueNotice.stream().map(Notice::getUniqCode).collect(Collectors.toSet())
                                    : null
                    );
                }else {
                    titleQueueNotice = titleQueue.offerAll();
                }
                log.info("limitTitle pass: uniqCode={}, remark={}, title={}", notice.getUniqCode(), remark, title);
                // System.out.println("limitTitle pass: uniqCode="+notice.getUniqCode()+", remark="+remark+", title="+title);
            }

            limitResult = new NoticeLimitResult(true, titleQueueNotice, contentQueueNotice);

        }
        // 4、如果没超过，则继续排队，不能发notice
        else {
            if(limitTitle) { titleQueue.add(notice); }
            if(limitContent) { contentQueue.add(notice); }

            limitResult = new NoticeLimitResult(false);

            log.info("limit forbid: uniqCode={}, remark={}, title={}, dedupContent={}", notice.getUniqCode(), remark, title, dedupContent);
            // System.out.println("limit forbit: uniqCode="+notice.getUniqCode()+", remark="+remark+", title="+title+", dedupContent="+dedupContent);
        }

        return limitResult;
    }

    /**
     *
     * @param titleQueue
     * @param contentQueue
     * @param noticeDate
     * @return
     */
    private boolean isPass(NoticeQueue titleQueue, NoticeQueue contentQueue, Date noticeDate) {
        boolean titlePass   = false;
        boolean contentPass = false;

        if(limitTitle) {
            if(null==titleQueue.getLastPass() ||
                    (noticeDate.getTime()-titleQueue.getLastPass().getTime())>=(frequency.getTitleSecondsPerPass()*1000)) {    // 第一次直接发送，之后看间隔
                titlePass = true;
            }
        }else {
            titlePass = true;
        }

        if(limitContent) {
            if(null==contentQueue.getLastPass() ||
                    (noticeDate.getTime()-contentQueue.getLastPass().getTime())>=(frequency.getContentSecondsPerPass()*1000)) {    // 第一次直接发送，之后看间隔
                contentPass = true;
            }
        }else {
            contentPass = true;
        }

        return titlePass && contentPass;
    }

    /**
     *
     * @param content
     * @return
     */
    private String getDedupContent(String content) {

        for(String key : content2Queue.keySet()) {
            if(NoticeLimiterUtil.isDedup(key, content)) {
                return key;
            }
        }
        return content;
    }


    /**
     * DEMO 通知频控器使用示例
     * DEMO 通知频控器使用示例
     * DEMO 通知频控器使用示例
     *
     *
     * @param args
     */
    public static void main(String[] args) throws Exception {

        // 1、测试内容判同
        System.out.println(NoticeLimiterUtil.isDedup("10.11.140.11:14132_operator_start_failed", "10.120.10.2:14132_operator_start_failed"));
        System.out.println(NoticeLimiterUtil.isDedup("10.11.140.11:14132_operator_start_failed", "10.11.140.11:88888_operator_start_failed"));
        System.out.println(NoticeLimiterUtil.isDedup("10.11.140.11:14132_operator_start_failed", "(build.sh)(build_index)_retry_too_many_times(20),_please_check_knn_operator_machine_again!_Stop_the_build."));


        // 2、测试频控

        // 2.1 初始化频控策略
        // 表示标题相同的要间隔10秒以上才能发送一次，内容相同的要间隔16秒才能发送一次
        NoticeLimitFrequency frequency = new NoticeLimitFrequency(10, 16);         // 表示同时限制标题、正文
//        NoticeLimitFrequency frequency = new NoticeLimitFrequency(10, null);     // 表示只限制标题
//        NoticeLimitFrequency frequency = new NoticeLimitFrequency(null, 16);     // 表示只限制正文

        // 2.2 初始化本地频控器（是并发安全的）
        NoticeLimiter limiter = new NoticeLimiterLocal(frequency);

        // 2.3 模拟频控
        Notice notice1            = new Notice("构建失败", "10.11.140.11:14132_operator_start_failed", "dm-general-microvd-strict-ob");
        NoticeLimitResult result1 = limiter.limit(notice1);
        System.out.println("result1=" + result1.toStringWithDetailWhenTrue());
        System.out.println();

        Thread.sleep(3000);
        Notice notice2            = new Notice("构建失败", "10.11.140.11:14222_operator_start_failed", "mb-loose-microvideo");
        NoticeLimitResult result2 = limiter.limit(notice2);
        System.out.println("result2=" + result2.toStringWithDetailWhenTrue());
        System.out.println();

        Thread.sleep(3000);
        Notice notice3            = new Notice("构建失败", "10.11.140.11:14333_operator_start_failed", "mb-loose-microvideo-3");
        NoticeLimitResult result3 = limiter.limit(notice3);
        System.out.println("result3=" + result3.toStringWithDetailWhenTrue());
        System.out.println();

        Thread.sleep(6000);
        Notice notice4            = new Notice("构建失败", "10.11.140.11:14444_operator_start_failed", "mb-loose-microvideo-4");
        NoticeLimitResult result4 = limiter.limit(notice4);
        System.out.println("result4=" + result4.toStringWithDetailWhenTrue());
        System.out.println();

        Thread.sleep(6000);
        Notice notice5            = new Notice("构建失败", "10.11.140.11:14555_operator_start_failed", "mb-loose-microvideo-5");
        NoticeLimitResult result5 = limiter.limit(notice5);
        System.out.println("result5=" + result5.toStringWithDetailWhenTrue());
        System.out.println();



        Thread.sleep(6000);
        Notice notice6            = new Notice("构建失败", "10.11.140.11:14666_operator_start_failed", "mb-loose-microvideo-6");
        NoticeLimitResult result6 = limiter.limit(notice6);
        System.out.println("result6=" + result6.toStringWithDetailWhenTrue());
        System.out.println();

        Thread.sleep(6000);
        Notice notice7            = new Notice("构建失败", "(build.sh)(build_index)_retry_too_many_times(20),_please_check_knn_operator_machine_again!_Stop_the_build.", "mb-loose-microvideo-7");
        NoticeLimitResult result7 = limiter.limit(notice7);
        System.out.println("result7=" + result7.toStringWithDetailWhenTrue());
        System.out.println();

        Thread.sleep(6000);
        Notice notice8            = new Notice("构建失败", "10.11.140.11:14888_operator_start_failed", "mb-loose-microvideo-8");
        NoticeLimitResult result8 = limiter.limit(notice8);
        System.out.println("result8=" + result8.toStringWithDetailWhenTrue());
        System.out.println();

        Thread.sleep(6000);
        Notice notice9            = new Notice("构建失败", "(build.sh)(build_index)_retry_too_many_times(20),_please_check_knn_operator_machine_again!_Stop_the_build.", "mb-loose-microvideo-9");
        NoticeLimitResult result9 = limiter.limit(notice9);
        System.out.println("result9=" + result9.toStringWithDetailWhenTrue());
        System.out.println();

        Thread.sleep(6000);
        Notice notice10            = new Notice("构建失败", "(build.sh)(build_index)_retry_too_many_times(20),_please_check_knn_operator_machine_again!_Stop_the_build.", "mb-loose-microvideo-10");
        NoticeLimitResult result10 = limiter.limit(notice10);
        System.out.println("result10=" + result10.toStringWithDetailWhenTrue());
        System.out.println();

        Thread.sleep(12000);
        Notice notice11            = new Notice("构建失败", "10.11.140.11:14111_operator_start_failed", "mb-loose-microvideo-11");
        NoticeLimitResult result11 = limiter.limit(notice11);
        System.out.println("result11=" + result11.toStringWithDetailWhenTrue());
        System.out.println();

    }


}
