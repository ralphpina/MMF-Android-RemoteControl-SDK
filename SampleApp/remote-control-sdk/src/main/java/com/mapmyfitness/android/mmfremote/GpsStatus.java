package com.mapmyfitness.android.mmfremote;

/**
 * Enum to identify various GPS status the MMF may send. They include:
 * <ul>
 *     <li>{@link #GPS_AVAILABLE}</li>
 *     <li>{@link #GPS_NOT_AVAILABLE}</li>
 *     <li>{@link #GPS_NON_TRACKABLE}</li>
 * </ul>
 */
public enum GpsStatus {
    /**
     * GPS is available on the phone and the MMF app has locked on a location.
     */
    GPS_AVAILABLE,
    /**
     * The Android Location Services are turned on, but the MMF app has yet to lock on
     * a location.
     */
    GPS_NOT_AVAILABLE,
    /**
     * This workout selected on the MMF app is non-trackable, such as a gym workout,
     * so GPS data such as distance, speed, etc, will not be shown. The user of this SDK
     * may which to show the user a dialog. The MMF app will show a dialog if the app is opened.
     */
    GPS_NON_TRACKABLE,
}
