package com.mapmyfitness.android.mmfremoteappexample.app.events;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class DurationEvent {

    private Long mDuration;

    public DurationEvent(Long duration) {
        mDuration = duration;
    }

    public Long getDuration() {
        return mDuration;
    }
}
