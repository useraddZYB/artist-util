package com.programmerartist.artist.util.sample;

import java.util.Random;

/**
 * 采样
 *
 * @author 程序员Artist
 * 2019/12/24
 */
public class LocalSample implements Sample {

    private static final int SAMPLE_MAX                     = 100;
    private static final int SAMPLE_MIN                     = 0;
    private static final ThreadLocal<Random> SAMPLE_RANDOM  = ThreadLocal.withInitial(() -> new Random());

    private volatile int sample;

    private boolean forbid = false;
    private boolean pass   = false;

    /**
     *
     * @param sample
     */
    public LocalSample(int sample) {
        sample = dealSample(sample);

        this.sample = sample;
    }

    /**
     *
     * @return
     */
    @Override
    public boolean yes() {
        if(this.forbid) {
            return false;
        }else if(this.pass) {
            return true;
        }

        return SAMPLE_RANDOM.get().nextInt(SAMPLE_MAX) < sample;
    }

    /**
     *
     * @param newSample
     */
    @Override
    public void update(int newSample) {
        this.sample = this.dealSample(newSample);
    }


    /**
     *
     * @param sample
     * @return
     */
    private int dealSample(int sample) {
        // reset
        this.forbid = false;
        this.pass = false;

        if(sample <= SAMPLE_MIN) {
            sample = SAMPLE_MIN;
            this.forbid = true;
        }else if(sample >= SAMPLE_MAX) {
            sample = SAMPLE_MAX;
            this.pass = true;
        }

        return sample;
    }


}
