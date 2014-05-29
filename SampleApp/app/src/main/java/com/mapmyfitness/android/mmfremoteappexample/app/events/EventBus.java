package com.mapmyfitness.android.mmfremoteappexample.app.events;

/**
 * Created by ralph.pina on 5/28/14.
 */
import com.squareup.otto.Bus;

public final class EventBus {

    private static Bus mEventBus;

    public static Bus getInstance() {
        if (mEventBus == null) {
            mEventBus = new Bus();
        }
        return mEventBus;
    }

}
