package com.mapmyfitness.android.mmfremoteappexample.app.events;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class HeartRateEvent {

    private Integer mHeartRate;
    private Integer mHeartRateAverage;
    private Integer mHeartRateMax;
    private Integer mHeartRateZone;

    public HeartRateEvent(Integer heartRate, Integer heartRateAverage, Integer heartRateMax, Integer heartRateZone) {
        this.mHeartRate = heartRate;
        this.mHeartRateAverage = heartRateAverage;
        this.mHeartRateMax = heartRateMax;
        this.mHeartRateZone = heartRateZone;
    }

    public Integer getHeartRate() {
        return mHeartRate;
    }

    public Integer getHeartRateAverage() {
        return mHeartRateAverage;
    }

    public Integer getHeartRateMax() {
        return mHeartRateMax;
    }

    public Integer getHeartRateZone() {
        return mHeartRateZone;
    }
}
