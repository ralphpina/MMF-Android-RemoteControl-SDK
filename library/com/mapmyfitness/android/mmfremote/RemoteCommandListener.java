package com.mapmyfitness.android.mmfremote;

/**
 * Listener used to register with {@link RemoteManager}
 * to receive commands from the app. These commands will allow your UI to
 * adjust depending on the state of the MMF app.
 */
public interface RemoteCommandListener {

    /**
     * When your application connects to the MMF app, it will automatically
     * receive a call to this event. Allowing you to immediately adjust your UI
     * depending on the state of the MMF app.
     *
     * @param appState {@link com.mapmyfitness.android.mmfremote.AppState} the current state of the app
     */
    public void onAppStateEvent(AppState appState);

    /**
     * This command is sent by the MMF app whenever a configuation change
     * happens. This includes:
     * - the user changing the preferred units. They can select
     * Metric or Imperial units.
     * - the user connecting/disconnecting a heart rate device
     * - the user setting a preference for the app to display speed or pace.
     *
     * @param metric {@link Boolean} if true, the user would like to see units in metric, otherwise in imperial units.
     * @param hasHeartRate {@link Boolean} if true, the app will send heart rate information during the workout
     * @param calculatesCalories {@link Boolean} if true, the app will calculate calories and send them to the app.
     * @param isSpeed {@link Boolean} if true the user would like to see speed for all their workouts. DEFAULT is speed for biking and pace for everything else.
     */
    public void onAppConfiguredEvent(Boolean metric,
                                     Boolean hasHeartRate,
                                     Boolean calculatesCalories,
                                     Boolean isSpeed);

    /**
     * This command is sent by the MMF app whenever a workout has been
     * started. It sends the same configuration information as the {@link #onAppConfiguredEvent(Boolean, Boolean, Boolean, Boolean)}
     *
     * @param metric {@link Boolean} if true, the user would like to see units in metric, otherwise in imperial units.
     * @param hasHeartRate {@link Boolean} if true, the app will send heart rate information during the workout
     * @param calculatesCalories {@link Boolean} if true, the app will calculate calories and send them to the app.
     * @param isSpeed {@link Boolean} if true the user would like to see speed for all their workouts. DEFAULT is speed for biking and pace for everything else.
     */
    public void onStartWorkoutEvent(Boolean metric,
                                    Boolean hasHeartRate,
                                    Boolean calculatesCalories,
                                    Boolean isSpeed);

    /**
     * The user has paused the workout. The app will not send any data while the workout
     * is paused. Once a workout is paused the next event will either be {@link #onResumeWorkoutEvent()}
     * if the workout is to be resumed or {@link #onStopWorkoutEvent()}
     * if the workout is being stopped.
     */
    public void onPauseWorkoutEvent();

    /**
     * The user has resumed their workout. Data will resume from the app until the
     * user pauses the app.
     */
    public void onResumeWorkoutEvent();

    /**
     * The user has stopped the workout. At this point the workout has not been saved.
     * The user must take one of two actions:
     * - Save the workout, which will produce the {@link #onSaveWorkoutEvent()} event.
     * - Discard the workout, which will produce the {@link #onDiscardWorkoutEvent()} event.
     * The app will now be in the {@link com.mapmyfitness.android.mmfremote.AppState#POST_RECORDING} state.
     */
    public void onStopWorkoutEvent();

    /**
     * The workout has been saved and the app can now return to the {@link com.mapmyfitness.android.mmfremote.AppState#NOT_RECORDING} state.
     */
    public void onSaveWorkoutEvent();

    /**
     * The workout has been discarded and the app can now return to the {@link com.mapmyfitness.android.mmfremote.AppState#NOT_RECORDING} state.
     */
    public void onDiscardWorkoutEvent();

    /**
     * The current status of the GPS lock on the MMF app. If no GPS is available and the user tries to start
     * a workout this event will be triggered. If {@link com.mapmyfitness.android.mmfremote.GpsStatus#GPS_NOT_AVAILABLE} then the user can wait for
     * a GPS lock, at which point this event will be triggered with {@link com.mapmyfitness.android.mmfremote.GpsStatus#GPS_AVAILABLE}, or a workout
     * may be started at any point without the GPS using {@link RemoteManager#startWithoutGpsCommand()}, or the
     * workout may be cancelled using {@link RemoteManager#cancelWorkoutStart()}
     *
     * @param gpsStatus {@link com.mapmyfitness.android.mmfremote.GpsStatus} with the GPS status of the MMF app
     */
    public void onGpsStatusWarning(GpsStatus gpsStatus);

    /**
     * If the MMF app is opened on the phone, and a user tries to start a workout, and the
     * location services is turned on, a warning wil display on the app. At this point the user can
     * start without GPS using {@link RemoteManager#startWithoutGpsCommand()} or cancel the
     * start using {@link RemoteManager#cancelWorkoutStart()} ()}
     */
    public void onLocationServicesStatusEvent();

    /**
     * The phone has requested that this SDK be upgraded above the minSdkVersion. If you continue
     * to use an SDK version below this MMF will not be able to support it, nor can we guarantee the
     * behavior.
     *
     * @param minSdkVersion the minimum SDK version that the connected app supports.
     */
    public void onForceUpgrade(Integer minSdkVersion);
}
