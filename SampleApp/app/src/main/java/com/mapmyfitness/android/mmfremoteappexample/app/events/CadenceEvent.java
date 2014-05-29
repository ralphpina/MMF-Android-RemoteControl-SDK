package com.mapmyfitness.android.mmfremoteappexample.app.events;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class CadenceEvent {

    private Integer mCadence;
    private Integer mCadenceAverage;
    private Integer mCadenceMax;

    public CadenceEvent(Integer cadence, Integer cadenceAverage, Integer cadenceMax) {
        mCadence = cadence;
        mCadenceAverage = cadenceAverage;
        mCadenceMax = cadenceMax;
    }

    public Integer getCadence() {
        return mCadence;
    }

    public Integer getCadenceAverage() {
        return mCadenceAverage;
    }

    public Integer getCadenceMax() {
        return mCadenceMax;
    }
}
