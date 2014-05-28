package com.mapmyfitness.android.mmfremoteappexample.app.events;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class PowerEvent {

    private Integer mPower;
    private Integer mPowerAverage;
    private Integer mPowerMax;

    public PowerEvent(Integer power, Integer powerAverage, Integer powerMax) {
        mPower = power;
        mPowerAverage = powerAverage;
        mPowerMax = powerMax;
    }

    public Integer getPower() {
        return mPower;
    }

    public Integer getPowerAverage() {
        return mPowerAverage;
    }

    public Integer getPowerMax() {
        return mPowerMax;
    }
}
