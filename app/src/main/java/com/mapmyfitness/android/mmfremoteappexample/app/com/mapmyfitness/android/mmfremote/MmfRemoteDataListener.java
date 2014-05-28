package com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote;

/**
 * Created by ralph.pina on 5/27/14.
 */
public interface MmfRemoteDataListener {

    /**
     * A heart rate event with data.
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
     * rpm
     * @param cadence
     */
    public void onCadenceEvent(Integer cadence, Integer averageCadence, Integer maxCadence);


    public void onCalorieEvent(Integer calories);
    public void onDistanceEvent(Double distance);
    public void onDurationEvent(Long duration);

    /**
     * meters per second
     * @param speed
     * @param averageSpeed
     * @param maxSpeed
     */
    public void onSpeedEvent(Double speed, Double averageSpeed, Double maxSpeed);

    /**
     * watts
     * @param power
     */
    public void onPowerEvent(Integer power, Integer averagePower, Integer maxPower);

}
