package com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class MmfStatsCache {

    /**
     * Latest heart rate in bpm.
     */
    private Integer mHeartRate;
    /**
     * Avg. heart rate in bpm.
     */
    private Integer mAverageHeartRate;
    /**
     * Max heart rate in bpm.
     */
    private Integer mMaxHeartRate;
    /**
     * Current heart rate zone.
     */
    private Integer mHeartRateZone;

    /**
     * Latest cadence in rpm.
     */
    private Integer mCadence;
    /**
     * Avg. cadence in rpm.
     */
    private Integer mAverageCadence;
    /**
     * Max cadence in rpm.
     */
    private Integer mMaxCadence;

    /**
     * Total calories burned so far in kcals.
     */
    private Integer mCalories;

    /**
     * Total distanced traveled so far in meters.
     */
    private Double mDistance;

    /**
     * Total duration of workout so far in milliseconds.
     */
    private Long mDuration;

    /**
     * Latest speed in meters per second.
     */
    private Double mSpeed;
    /**
     * Average speed in meters per second.
     */
    private Double mAverageSpeed;
    /**
     * Max speed in meters per second.
     */
    private Double mMaxSpeed;

    /**
     * Latest power in watts.
     */
    private Integer mPower;
    /**
     * Average power in watts.
     */
    private Integer mAveragePower;
    /**
     * Max power in watts.
     */
    private Integer mMaxPower;

    public MmfStatsCache() {
        zeroOutValues();
    }

    /**
     * At the start and after every workout zero out the values for the next
     */
    public void zeroOutValues() {
        mHeartRate = 0;
        mAverageHeartRate = 0;
        mMaxHeartRate = 0;
        mHeartRateZone = 0;

        mCadence = 0;
        mAverageCadence = 0;
        mMaxCadence = 0;

        mCalories = 0;

        mDistance = 0.0;

        mDuration = 0l;

        mSpeed = 0.0;
        mAverageSpeed = 0.0;
        mMaxSpeed = 0.0;

        mPower = 0;
        mAveragePower = 0;
        mMaxPower = 0;
    }

    public Integer getHeartRate() {
        return mHeartRate;
    }

    public void setHeartRate(Integer heartRate) {
        mHeartRate = heartRate;
    }

    public Integer getAverageHeartRate() {
        return mAverageHeartRate;
    }

    public void setAverageHeartRate(Integer averageHeartRate) {
        mAverageHeartRate = averageHeartRate;
    }

    public Integer getMaxHeartRate() {
        return mMaxHeartRate;
    }

    public void setMaxHeartRate(Integer maxHeartRate) {
        mMaxHeartRate = maxHeartRate;
    }

    public Integer getHeartRateZone() {
        return mHeartRateZone;
    }

    public void setHeartRateZone(Integer heartRateZone) {
        mHeartRateZone = heartRateZone;
    }

    public Integer getCadence() {
        return mCadence;
    }

    public void setCadence(Integer cadence) {
        mCadence = cadence;
    }

    public Integer getAverageCadence() {
        return mAverageCadence;
    }

    public void setAverageCadence(Integer averageCadence) {
        mAverageCadence = averageCadence;
    }

    public Integer getMaxCadence() {
        return mMaxCadence;
    }

    public void setMaxCadence(Integer maxCadence) {
        mMaxCadence = maxCadence;
    }

    public Integer getCalories() {
        return mCalories;
    }

    public void setCalories(Integer calories) {
        mCalories = calories;
    }

    public Double getDistance() {
        return mDistance;
    }

    public void setDistance(Double distance) {
        mDistance = distance;
    }

    public Long getDuration() {
        return mDuration;
    }

    public void setDuration(Long duration) {
        mDuration = duration;
    }

    public Double getSpeed() {
        return mSpeed;
    }

    public void setSpeed(Double speed) {
        mSpeed = speed;
    }

    public Double getAverageSpeed() {
        return mAverageSpeed;
    }

    public void setAverageSpeed(Double averageSpeed) {
        mAverageSpeed = averageSpeed;
    }

    public Double getMaxSpeed() {
        return mMaxSpeed;
    }

    public void setMaxSpeed(Double maxSpeed) {
        mMaxSpeed = maxSpeed;
    }

    public Integer getPower() {
        return mPower;
    }

    public void setPower(Integer power) {
        mPower = power;
    }

    public Integer getAveragePower() {
        return mAveragePower;
    }

    public void setAveragePower(Integer averagePower) {
        mAveragePower = averagePower;
    }

    public Integer getMaxPower() {
        return mMaxPower;
    }

    public void setMaxPower(Integer maxPower) {
        mMaxPower = maxPower;
    }
}
