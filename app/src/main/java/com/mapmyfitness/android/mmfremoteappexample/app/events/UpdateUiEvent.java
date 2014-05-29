package com.mapmyfitness.android.mmfremoteappexample.app.events;

import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfAppState;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class UpdateUiEvent {

    private MmfAppState mMmfAppState;

    public UpdateUiEvent(MmfAppState mmfAppState) {
        mMmfAppState = mmfAppState;
    }

    public MmfAppState getMmfAppState() {
        return mMmfAppState;
    }
}
