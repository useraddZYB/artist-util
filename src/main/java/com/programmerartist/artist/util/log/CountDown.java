package com.programmerartist.artist.util.log;

import java.util.concurrent.atomic.AtomicLong;

/**
 * 计数器
 *
 * @author 程序员Artist
 * @date 2023-08-10
 */
public class CountDown {

    private AtomicLong count;

    private int down;

    /**
     *
     * @param down
     */
    public CountDown(int down) {
        this.count = new AtomicLong();
        this.down = down;
    }


    /**
     *
     * @return
     */
    public boolean hasDown() {
        return count.get() >= down;
    }

    /**
     *
     */
    public long incrementAndGet() {
        return this.count.incrementAndGet();
    }

    /**
     *
     * @return
     */
    public long get() {
        return this.count.get();
    }

    /**
     *
     */
    public void reset() {
        this.count = new AtomicLong();
    }

}
