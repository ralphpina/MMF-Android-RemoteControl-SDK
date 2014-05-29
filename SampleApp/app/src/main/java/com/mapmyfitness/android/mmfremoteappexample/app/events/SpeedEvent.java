package com.mapmyfitness.android.mmfremoteappexample.app.events;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class SpeedEvent {

    private Double mSpeed;
    private Double mSpeedAverage;
    private Double mSpeedMax;

    public SpeedEvent(Double speed, Double speedAverage, Double speedMax) {
        mSpeed = speed;
        mSpeedAverage = speedAverage;
        mSpeedMax = speedMax;
    }

    public Double getSpeed() {
        return mSpeed;
    }

    public Double getSpeedAverage() {
        return mSpeedAverage;
    }

    public Double getSpeedMax() {
        return mSpeedMax;
    }
}
