package com.mapmyfitness.android.mmfremoteappexample.app.events;

import com.mapmyfitness.android.mmfremote.AppState;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class UpdateUiEvent {

    private AppState mAppState;

    public UpdateUiEvent(AppState appState) {
        mAppState = appState;
    }

    public AppState getAppState() {
        return mAppState;
    }
}
