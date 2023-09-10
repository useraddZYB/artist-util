package com.programmerartist.artist.util.limiter;

import com.programmerartist.artist.util.common.ParamUtil;

import java.util.*;

/**
 * 通知告警限流工具。通知排队队伍类，每个队伍包含一个上一次被放行的时间戳
 *
 * @author 程序员Artist
 * @date 2022-12-01
 */
public class NoticeQueue {

    /**
     * 通知排队的队伍：从属于一个 dedupTitle 或者 dedupContent
     */
    private List<Notice> queue;
    /**
     *  上一次被放行的时间戳
     */
    private Date lastPass;

    /**
     *
     */
    public NoticeQueue(){
        this.queue = new ArrayList<>();
    }

    /**
     *
     * @param notice
     * @return
     */
    public void add(Notice notice) {
        this.queue.add(notice);
    }

    /**
     *
     * @return
     */
    public List<Notice> offerAll() {
        List<Notice> cloneQ = new ArrayList<>(queue);

        this.lastPass = new Date();
        this.queue.clear();

        return cloneQ;
    }

    /**
     *
     * @param uniqCodes
     * @return
     */
    public List<Notice> offerSameUniqCode(Set<Integer> uniqCodes) {
        this.lastPass = new Date();

        List<Notice> cloneQ = null;
        if(ParamUtil.isNotBlank(uniqCodes)) {
            cloneQ = new ArrayList<>(uniqCodes.size());

            Iterator<Notice> it = this.queue.iterator();
            while (it.hasNext()) {
                Notice now = it.next();
                if(uniqCodes.contains(now.getUniqCode())) {
                    cloneQ.add(now);
                    it.remove();
                }
            }
        }
        return cloneQ;
    }


    public List<Notice> getQueue() {
        return queue;
    }

    public Date getLastPass() {
        return lastPass;
    }
}
