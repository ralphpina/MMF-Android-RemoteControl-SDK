package com.mapmyfitness.android.mmfremote;

/**
 * Created by ralph.pina on 5/27/14.
 */
public interface RemoteDataListener {

    /**
     * A heart rate event with data. All units are in bpm. Receiving this data requires
     * that the user have a heart rate monitor connected to the MMF app in their Android phone.
     * You can check if heart rate is available using the {@link RemoteCommandListener#onStartWorkoutEvent(Boolean, Boolean, Boolean, Boolean) onStartWorkoutEvent(Boolean metric, Boolean hasHeartRate, Boolean calculatesCalories, Boolean isSpeed)}
     * listener when the app starts.
     *
     * @param heartRate the heart rate of the user in beats per minute.
     * @param averageHeartRate the heart rate of the user in beats per minute.
     * @param maxHeartRate the heart rate of the user in beats per minute.
     * @param heartRateZone the heart rate of the user. This {@link java.lang.Integer} varies
     *                      between 1 and 5. Heart rate zones are calculated using the
     *                      heart rate and the max heart rate of the user, which depends on their age.
     */
    public void onHeartRateEvent(Integer heartRate, Integer averageHeartRate, Integer maxHeartRate, Integer heartRateZone);

    /**
     * Candence data. All units are in Revolutions Per Minute. Like Heart Rate, it requires
     * a device be attached to the phone reading the data. At this time the only way to know if it
     * is available is to check for data in this method.
     *
     * @param cadence current cadence in rpm
     * @param averageCadence average cadence in rpm
     * @param maxCadence max cadence in rpm
     */
    public void onCadenceEvent(Integer cadence, Integer averageCadence, Integer maxCadence);

    /**
     * Total calories burned burned so far in the workout. Units are in kcals. To calculate calories
     * the MMF apps need certain user information, which we may or may not have. Like Heart Rate, you can check
     * if is available using the {@link RemoteCommandListener#onStartWorkoutEvent(Boolean, Boolean, Boolean, Boolean) onStartWorkoutEvent(Boolean metric, Boolean hasHeartRate, Boolean calculatesCalories, Boolean isSpeed)}
     * listener when the app starts.
     *
     * @param calories total calories burned so far in kcals.
     */
    public void onCalorieEvent(Integer calories);

    /**
     * Total distance travelled so far during the workout. Units are in meters.
     *
     * @param distance meters traveled.
     */
    public void onDistanceEvent(Double distance);

    /**
     * Total duration of the workout so far. Units are in milliseconds.
     *
     * @param duration of workout in milliseconds
     */
    public void onDurationEvent(Long duration);

    /**
     * Speed of workout. Units are in meters per second. Speed can be used to calculate Pace if the
     * user desires it. The {@link RemoteCommandListener#onStartWorkoutEvent(Boolean, Boolean, Boolean, Boolean) onStartWorkoutEvent(Boolean metric, Boolean hasHeartRate, Boolean calculatesCalories, Boolean isSpeed)}
     * command will say whether the user has chosen to see speed, or if this is an activity (like cycling) where
     * speed should be displayed, as opposed to pace which is the norm for running. Regardless
     * this callback will always receive speed, so it is up to the client UI to change it based on the user's
     * preferences.
     *
     * @param speed current speed in meters per second
     * @param averageSpeed average speed of workout in meters per second
     * @param maxSpeed max speed of workout in meters per second
     */
    public void onSpeedEvent(Double speed, Double averageSpeed, Double maxSpeed);

    /**
     * Power
     *
     * Power data. All units are in watts. Like Heart Rate, it requires
     * a device be attached to the phone reading the data. At this time the only way to know if it
     * is available is to check for data in this method.
     *
     * @param power current power in watts
     * @param averagePower average power in watts
     * @param maxPower max power in watts
     */
    public void onPowerEvent(Integer power, Integer averagePower, Integer maxPower);

}
