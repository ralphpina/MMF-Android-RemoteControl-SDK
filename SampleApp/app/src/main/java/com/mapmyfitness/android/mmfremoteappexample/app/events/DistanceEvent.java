package com.mapmyfitness.android.mmfremoteappexample.app.events;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class DistanceEvent {

    private Double mDistance;

    public DistanceEvent(Double distance) {
        mDistance = distance;
    }

    public Double getDistance() {
        return mDistance;
    }
}
