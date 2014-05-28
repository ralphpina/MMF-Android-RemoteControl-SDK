package com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote;

/**
 * Enum describing the state of the MMF app. Possible values are:
 * <ul>
 *     <li>{@link #APP_NOT_INSTALLED}</li>
 *     <li>{@link #NOT_LOGGED_IN}</li>
 *     <li>{@link #NOT_RECORDING}</li>
 *     <li>{@link #RECORDING}</li>
 *     <li>{@link #RECORDING_PAUSED}</li>
 *     <li>{@link #POST_RECORDING}</li>
 * </ul>
 *
 */
public enum MmfAppState {
    /**
     * A companion MMF app has not been installed on the device. At this time, we
     * support:
     * <ul>
     *     <li>MapMyRun</li>
     *     <li>MapMyRun+</li>
     *     <li>MapMyRide</li>
     *     <li>MapMyRide+</li>
     *     <li>MapMyWalk</li>
     *     <li>MapMyWalk+</li>
     *     <li>MapMyHike</li>
     *     <li>MapMyHike+</li>
     *     <li>MapMyFitness</li>
     *     <li>MapMyFitness+</li>
     * </ul>
     */
    APP_NOT_INSTALLED,
    /**
     * The user is not logged into the app. Before any other action can be
     * taken he must be prompted to login.
     */
    NOT_LOGGED_IN,
    /**
     * The user is logged into the app, but the app is not in a recording state.
     * At this state the app is ready to begin a recording.
     */
    NOT_RECORDING,
    /**
     * The app is currently recording a workout.
     */
    RECORDING,
    /**
     * The app is paused, but in a recording state. The workout can not be stopped
     * or resummed.
     */
    RECORDING_PAUSED,
    /**
     * The workout was stopped, but it has yet to be saved or discarded.
     * Once it is saved or discarded it will go back to the {@link #NOT_RECORDING} state.
     */
    POST_RECORDING,
}
