package com.programmerartist.artist.util.date;

import java.util.concurrent.TimeUnit;

/**
 * 函数分阶段代码 和 函数整体 耗时
 *
 * @Author 程序员Artist
 * @Date 2018/7/19 下午7:51
 **/
public class Watch {

    // 内部实现：用于记录"函数内分阶段程序"耗时
    private long lastTime;
    // 内部实现：用于记录"函数整体"耗时
    private long totalLastTime;

    /**
     * private
     *
     * @param lastTime      代码片段计时器
     * @param totalLastTime 函数计时器
     */
    private Watch(long lastTime, long totalLastTime) {
        this.lastTime = lastTime;
        this.totalLastTime = totalLastTime;
    }

    /**
     * 生成计时器，并开始计时
     *
     * @return this
     */
    public static Watch start() {
        long now = System.currentTimeMillis();
        return new Watch(now, now);
    }

    /**
     * 耗时：毫秒
     *
     * @return 毫秒
     */
    public long cost() {
        return System.currentTimeMillis() - lastTime;
    }

    /**
     * 耗时
     *
     * @param timeUnit 单位
     * @return 耗时
     */
    public long cost(TimeUnit timeUnit) {
        return TimeUnit.MICROSECONDS.convert(System.currentTimeMillis() - lastTime, timeUnit);
    }

    /**
     * 耗时，然后重置计时器：毫秒
     *
     * @return 耗时
     */
    public long costAndReStart() {
        long cost = this.cost();
        this.reStart();
        return cost;
    }

    /**
     * 耗时
     *
     * @param timeUnit 单位
     * @return 耗时
     */
    public long costAndReStart(TimeUnit timeUnit) {
        long cost = this.cost(timeUnit);
        this.reStart();
        return cost;
    }

    /**
     * 重新开始计时
     */
    public void reStart() {
        lastTime = System.currentTimeMillis();
    }

    /**
     * 函数整体耗时：毫秒
     *
     * @return 耗时
     */
    public long totalCost() {
        return System.currentTimeMillis() - totalLastTime;
    }

    /**
     * 函数整体耗时
     *
     * @param timeUnit 单位
     * @return 耗时
     */
    public long totalCost(TimeUnit timeUnit) {
        return TimeUnit.MICROSECONDS.convert(System.currentTimeMillis() - totalLastTime, timeUnit);
    }





    /**
     *  DEMO
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {

        //创建一个stopwatch并且开始计时
        Watch watch = Watch.start();

        Thread.sleep(3000);
        System.out.println(watch.cost());
        Thread.sleep(1000);
        System.out.println(watch.cost());
        System.out.println("-------------------");

        //再次开始计时
        watch.reStart();
        Thread.sleep(2000);
        System.out.println(watch.cost());

        //再次开始计时
//        watch.reStart();
        Thread.sleep(3000);

        System.out.println(watch.cost());
        System.out.println("-------------------");

        // 整体耗时
        System.out.println(watch.totalCost());

    }
}
