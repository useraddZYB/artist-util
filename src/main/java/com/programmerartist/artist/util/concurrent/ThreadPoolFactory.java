package com.programmerartist.artist.util.concurrent;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * factory
 * single fixed scheduled auto
 *
 * @author 程序员Artist
 * @date 2019/4/2
 */
public class ThreadPoolFactory {

    private static final int DEFAULT_ALIVE_MINUTES                        = 5;
    public static final RejectedExecutionHandler DEFAULT_REJECTED_HANDLER = new ThreadPoolExecutor.AbortPolicy();


    /**
     * factory
     *
     * @param namePrefix
     * @return
     */
    public static ThreadFactory newFactory(String namePrefix) { return new NameThreadFactory(namePrefix); }


    /**
     * single
     *
     * @param queueSize
     * @param namePrefix
     * @return
     */
    public static ThreadPoolExecutor newSingle(int queueSize, String namePrefix) {
        return new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueSize), newFactory(namePrefix));
    }


    /**
     * fixed
     *
     * @param nThreads
     * @param queueSize
     * @param namePrefix
     * @return
     */
    public static ThreadPoolExecutor newFixed(int nThreads, int queueSize, String namePrefix) {
        return ThreadPoolFactory.newFixed(nThreads, queueSize, namePrefix, DEFAULT_REJECTED_HANDLER);
    }
    public static ThreadPoolExecutor newFixed(int nThreads, int queueSize, String namePrefix, RejectedType rejectedType) {
        return newFixed(nThreads, queueSize, namePrefix, rejectedType.getHandler(namePrefix));
    }
    public static ThreadPoolExecutor newFixed(int nThreads, int queueSize, String namePrefix, RejectedExecutionHandler rejectedHandler) {
        return new ThreadPoolExecutor(nThreads, nThreads, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(queueSize),
                newFactory(namePrefix), null!=rejectedHandler ? rejectedHandler : DEFAULT_REJECTED_HANDLER);
    }


    /**
     * scheduled
     *
     * @param nThreads
     * @param namePrefix
     * @return
     */
    public static ScheduledThreadPoolExecutor newScheduled(int nThreads, String namePrefix) {
        return new ScheduledThreadPoolExecutor(nThreads, newFactory(namePrefix));
    }


    /**
     * auto
     *
     * @param core
     * @param max
     * @param queueSize
     * @param namePrefix
     * @return
     */
    public static ThreadPoolExecutor newAuto(int core, int max, int queueSize, String namePrefix) {
        return ThreadPoolFactory.newAuto(core, max, DEFAULT_ALIVE_MINUTES, queueSize, namePrefix, DEFAULT_REJECTED_HANDLER);
    }
    public static ThreadPoolExecutor newAuto(int core, int max, int aliveMinutes, int queueSize, String namePrefix) {
        return ThreadPoolFactory.newAuto(core, max, aliveMinutes, queueSize, namePrefix, DEFAULT_REJECTED_HANDLER);
    }
    public static ThreadPoolExecutor newAuto(int core, int max, int aliveMinutes, int queueSize, String namePrefix, RejectedType rejectedType) {
        return newAuto(core, max, aliveMinutes, queueSize, namePrefix, rejectedType.getHandler(namePrefix));
    }
    public static ThreadPoolExecutor newAuto(int core, int max, int aliveMinutes, int queueSize, String namePrefix, RejectedExecutionHandler rejectedHandler) {
        return new ThreadPoolExecutor(core, max, aliveMinutes, TimeUnit.MINUTES, new LinkedBlockingQueue<>(queueSize),
                newFactory(namePrefix), null!=rejectedHandler ? rejectedHandler : DEFAULT_REJECTED_HANDLER);
    }



    /**
     * Factory
     *
     */
    static class NameThreadFactory implements ThreadFactory {
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private String namePrefix;

        NameThreadFactory(String namePrefix) {

            this.namePrefix = namePrefix;
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            if (t.isDaemon()) { t.setDaemon(false); }
            if (t.getPriority() != Thread.NORM_PRIORITY) { t.setPriority(Thread.NORM_PRIORITY); }
            return t;
        }
    }

}
