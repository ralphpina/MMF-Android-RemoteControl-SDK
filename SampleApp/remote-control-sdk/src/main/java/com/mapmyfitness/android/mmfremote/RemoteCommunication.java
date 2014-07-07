package com.mapmyfitness.android.mmfremote;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

/**
 * This class takes care of all the communication between the MMF apps and this SDK.
 * It uses string and int constants to bind and pass information to the RemoteControllerService
 * in the MMF apps.
 */
public class RemoteCommunication {

    // string to pass state to Gear app.
    // these strings are mirrored in the Gear apk
    private static final String CURRENT_STATE = "current_state";
    private static final String NOT_LOGGED_IN = "not_logged_in";
    private static final String NOT_RECORDING = "not_recording";
    private static final String RECORDING = "recording";
    private static final String RECORDING_PAUSED = "recording_paused";
    private static final String POST_RECORDING = "post_recording";

    // strings to pass values to Gear
    // these strings are mirrored in the Gear apk
    private static final String DURATION = "duration";
    private static final String DISTANCE = "distance";

    private static final String SPEED = "speed";
    private static final String SPEED_AVG = "average_speed";
    private static final String SPEED_MAX = "max_speed";

    private static final String HEART_RATE = "heart_rate";
    private static final String HEART_RATE_AVG = "average_heart_rate";
    private static final String HEART_RATE_MAX = "max_heart_rate";
    private static final String HEART_RATE_ZONE = "heart_rate_zone";

    private static final String CALORIES = "calories";

    private static final String CADENCE = "cadence";
    private static final String CADENCE_AVG = "cadence_avg";
    private static final String CADENCE_MAX = "cadence_max";

    private static final String POWER = "power";
    private static final String POWER_AVG = "power_avg";
    private static final String POWER_MAX = "power_max";

    // button presses to send and receieve from Gear
    // these strings are mirrored in the Gear apk
    // in the Gear they are located in watch/src/main/java/*our package*/controller/IMmfController.java
    private static final String COMMAND_START = "start";
    private static final String COMMAND_CONFIGURE = "configure";
    private static final String COMMAND_PAUSE = "pause";
    private static final String COMMAND_RESUME = "resume";
    private static final String COMMAND_STOP = "stop";
    private static final String COMMAND_DISCARD = "discard";
    private static final String COMMAND_SAVE = "save";
    private static final String COMMAND_FORCE_UPGRADE = "force_upgrade";
    private static final String PHONE_REQUIRED_MINIMUM_VERSION = "phone_required_minimum_version";
    private static final String COMMAND_CHECK_PHONE_VERSION = "check_phone_version";

    // configuration options to initialize Gear ui
    // these strings are mirrored in the Gear apk
    private static final String HAS_HEART_RATE_MONITOR = "has_heart_rate_monitor";
    private static final String HAS_CALORIES = "has_calories";
    private static final String IS_METRIC = "is_metric";
    private static final String IS_SPEED = "is_speed";

    private static final String GPS_AVAILABLE = "gps_available";
    private static final String GPS_NOT_AVAILABLE = "gps_not_available";
    private static final String GPS_LOC_SERVICES = "gps_loc_services";
    private static final String GPS_NON_TRACKABLE = "gps_non_trackable";

    private static final String COMMAND_FROM_MMF_APP = "command_from_mmf_app";

    private static final String PHONE_VERSION_NUMBER = "phone_version_number";
    private static final String REMOTE_COMMAND_VERSION_NUMBER = "remote_command_version_number";
    private static final String REMOTE_CONTROL_REQUIRED_MINIMUM_VERSION = "remote_control_required_minimum_version";

    // used to send command and data from Gear Provider apps to our Android apps
    // this is also being used in the Android app
    private static final int REGISTER_REMOTE_CONTROLLER_CLIENT = 1;
    private static final int UNREGISTER_REMOTE_CONTROLLER_CLIENT = 2;
    private static final int REMOTE_CONTROLLER_CLIENT_COMMAND_START = 3;
    private static final int REMOTE_CONTROLLER_CLIENT_COMMAND_PAUSE = 4;
    private static final int REMOTE_CONTROLLER_CLIENT_COMMAND_RESUME = 5;
    private static final int REMOTE_CONTROLLER_CLIENT_COMMAND_STOP = 6;
    private static final int REMOTE_CONTROLLER_CLIENT_COMMAND_DISCARD = 7;
    private static final int REMOTE_CONTROLLER_CLIENT_COMMAND_SAVE = 8;
    private static final int REMOTE_CONTROLLER_CLIENT_CURRENT_STATE = 9;
    private static final int REMOTE_CONTROLLER_CLIENT_COMMAND_START_WITHOUT_GPS = 10;
    private static final int REMOTE_CONTROLLER_CLIENT_COMMAND_CANCEL_WORKOUT_START = 11;
    private static final int REMOTE_CONTROLLER_CLIENT_COMMAND_CHECK_VERSION = 12;
    private static final int REMOTE_CONTROLLER_CLIENT_FORCE_UPGRADE = 13;

    protected final static int REMOTE_CONTROL_SDK_VERSION_NUMBER = 1000000; //xx.xxx.xxx <- rest are major , next 2 xx are minor, last 3 xxx are patch
    private final static int MINIMUM_PHONE_VERSION_NUMBER = 3000000;

    /** Messenger for communicating with service. */
    private Messenger mService = null;

    /** Flag indicating whether we have called bind on the service. */
    private boolean mIsBound = false;

    /* Send data to remote app that registered the listener */
    private RemoteDataListener mRemoteDataListener;
    /* Send commands to remote app that registered the listener */
    private RemoteCommandListener mRemoteCommandListener;

    /* Keep a cache of the data for easy access */
    private StatsCache mStatsCache;

    protected RemoteCommunication() {
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    private final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * Check if the service is bound
     */
    protected boolean isBound() {
        return mIsBound;
    }

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            if (RemoteManager.DETAIL_LOG) {
                Log.e(RemoteManager.TAG, "++ onServiceConnected ++");
            }
            // This is called when the connection with the service has been
            // established, giving us the service object we can use to
            // interact with the service.  We are communicating with our
            // service through an IDL interface, so get a client-side
            // representation of that from the raw service object.
            mService = new Messenger(service);

            // We want to monitor the service for as long as we are
            // connected to it.
            try {
                Message msg = Message.obtain(null,
                        REGISTER_REMOTE_CONTROLLER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);

            } catch (RemoteException e) {
                Log.e(RemoteManager.TAG, e.getMessage(), e);
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.e(RemoteManager.TAG, "++ onServiceDisconnected ++");
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
        }
    };

    protected boolean onConnectionTried(Context context, String intentFilterAction) {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onConnectionTried ++");
        }
        boolean success = false;
        if (!mIsBound) {
            success = context.bindService(new Intent(intentFilterAction), mConnection, context.BIND_AUTO_CREATE);
            mIsBound = true;
        }
        return success;
    }

    protected void onConnectionClosed(Context context) {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onConnectionClosed ++");
        }
        if (mIsBound) {
            // If we have received the service, and hence registered with
            // it, then now is the time to unregister.
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            UNREGISTER_REMOTE_CONTROLLER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                    // There is nothing special we need to do if the service
                    // has crashed.
                }
            }

            // Detach our existing connection.
            context.unbindService(mConnection);
            mIsBound = false;
        }
    }

    /**
     * Callback method to send stats back to the SDK
     * @param remoteDataListener
     */
    protected void setDataListener(RemoteDataListener remoteDataListener) {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ setDataListener ++");
        }
        mRemoteDataListener = remoteDataListener;
    }

    /**
     * Callback method to send commands back to the SDK
     * @param remoteCommandListener
     */
    protected void setCommandListener(RemoteCommandListener remoteCommandListener) {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ setCommandListener ++");
        }
        mRemoteCommandListener = remoteCommandListener;
    }

    protected RemoteDataListener getDataListener() {
        return mRemoteDataListener;
    }

    protected RemoteCommandListener getCommandListener() {
        return mRemoteCommandListener;
    }

    protected void setStatsCache(StatsCache statsCache) {
        mStatsCache = statsCache;
    }

    protected StatsCache getStatsCache() {
        return mStatsCache;
    }

    /**
     * Send start command to RemoteControllerService in MMF apps
     */
    protected void startCommand() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ startCommand() ++");
        }
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_START));
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);
        }
    }

    /**
     * Send pause command to RemoteControllerService in MMF apps
     */
    protected void pauseCommand() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ pauseCommand() ++");
        }
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_PAUSE));
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);
        }
    }

    /**
     * Send resume command to RemoteControllerService in MMF apps
     */
    protected void resumeCommand() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ resumeCommand ++");
        }
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_RESUME));
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);;
        }
    }

    /**
     * Send stop command to RemoteControllerService in MMF apps
     */
    protected void stopCommand() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ stopCommand ++");
        }
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_STOP));
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);
        }
    }

    /**
     * Send discard command to RemoteControllerService in MMF apps
     */
    protected void discardCommand() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ discardCommand ++");
        }
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_DISCARD));
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);
        }
    }

    /**
     * Send save command to RemoteControllerService in MMF apps
     */
    protected void saveCommand() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ saveCommand ++");
        }
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_SAVE));
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);
        }
    }

    /**
     * Ask the RemoteControllerService in MMF apps what the state of the app is.
     * These are listed in the {@link com.mapmyfitness.android.mmfremote.AppState} enum.
     */
    protected void getCurrentStateCommand() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ getCurrentStateCommand ++");
        }
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_CURRENT_STATE));
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);
        }
    }

    /**
     * Send start command whether or not we have GPS to RemoteControllerService in MMF apps
     */
    protected void startWithoutGpsCommand() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ startWithoutGpsCommand ++");
        }
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_START_WITHOUT_GPS));
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);
        }
    }

    /**
     * Tell the RemoteControllerService in MMF apps to cancel a start that it had
     * on hold because of the lack of GPS.
     */
    protected void cancelWorkoutStartCommand() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ cancelWorkoutStartCommand ++");
        }
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_CANCEL_WORKOUT_START));
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);
        }
    }

    /**
     * Tell the MMF app that it needs a higher minimum version to work properly with
     * this SDK.
     */
    protected void sendForceUpgradeCommand() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ sendForceUpgradeCommand ++");
        }
        try {
            Message msg = new Message();
            msg.what = REMOTE_CONTROLLER_CLIENT_FORCE_UPGRADE;
            Bundle data = new Bundle();
            data.putInt(PHONE_REQUIRED_MINIMUM_VERSION, MINIMUM_PHONE_VERSION_NUMBER);
            msg.setData(data);
            mService.send(msg);
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);
        }
    }

    /**
     * Send the version number of this SDK to the MMF app to make sure it meets
     * the minimum requirement.
     */
    protected void sendVersionNumber() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ sendVersionNumber ++");
        }
        try {
            Message msg = new Message();
            msg.what = REMOTE_CONTROLLER_CLIENT_COMMAND_CHECK_VERSION;
            Bundle data = new Bundle();
            data.putInt(REMOTE_COMMAND_VERSION_NUMBER, REMOTE_CONTROL_SDK_VERSION_NUMBER);
            msg.setData(data);
            mService.send(msg);
        } catch (RemoteException e) {
            Log.e(RemoteManager.TAG, e.getMessage(), e);
        }
    }

    /**
     * Checks the version of the MMF phone app to make sure it meets the minimum.
     * At this point it sends our version for the phone to check.
     *
     * @param version of the phone app
     */
    private void checkVersionAndTriggerWarning(int version) {
        if (version < MINIMUM_PHONE_VERSION_NUMBER) {
            sendForceUpgradeCommand();
        }
        sendVersionNumber();
    }

    /**
     * Handler of incoming messages from service.
     */
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Bundle data = msg.getData();

            if (data.containsKey(DURATION)) {
                onPhoneTimeEvent(data.getLong(DURATION));
            }

            if (data.containsKey(CALORIES)) {
                onPhoneSendCaloriesEvent(data.getInt(CALORIES));
            }

            if (data.containsKey(DISTANCE)) {
                onPhoneSendDistanceEvent(data.getDouble(DISTANCE));
            }

            if (data.containsKey(SPEED) && data.containsKey(SPEED_AVG) && data.containsKey(SPEED_MAX)) {
                onPhoneSendSpeedEvent(data.getDouble(SPEED), data.getDouble(SPEED_AVG), data.getDouble(SPEED_MAX));
            }

            if (data.containsKey(CADENCE) && data.containsKey(CADENCE_AVG) && data.containsKey(CADENCE_MAX)) {
                onPhoneSendCadenceEvent(data.getInt(CADENCE), data.getInt(CADENCE_AVG), data.getInt(CADENCE_MAX));
            }

            if (data.containsKey(POWER) && data.containsKey(POWER_AVG) && data.containsKey(POWER_MAX)) {
                onPhoneSendPowerEvent(data.getInt(POWER), data.getInt(POWER_AVG), data.getInt(POWER_MAX));
            }

            if (data.containsKey(HEART_RATE) && data.containsKey(HEART_RATE_AVG)
                    && data.containsKey(HEART_RATE_MAX)&& data.containsKey(HEART_RATE_ZONE)) {
                onPhoneSendHeartRateEvent(data.getInt(HEART_RATE), data.getInt(HEART_RATE_AVG), data.getInt(HEART_RATE_MAX), data.getInt(HEART_RATE_ZONE));
            }

            if (data.containsKey(COMMAND_FROM_MMF_APP)) {

                if (data.containsKey(COMMAND_FORCE_UPGRADE)) {
                    onPhoneForceUpgradeEvent(data.getInt(REMOTE_CONTROL_REQUIRED_MINIMUM_VERSION));
                }

                if (data.containsKey(COMMAND_CHECK_PHONE_VERSION)) {
                    checkVersionAndTriggerWarning(data.getInt(PHONE_VERSION_NUMBER));
                }

                if (data.getString(COMMAND_FROM_MMF_APP).equals(COMMAND_START)) {

                    boolean isMetric = data.getBoolean(IS_METRIC);
                    boolean hasCalories = data.getBoolean(HAS_CALORIES);
                    boolean hasHeartRate = data.getBoolean(HAS_HEART_RATE_MONITOR);
                    boolean isSpeed = data.getBoolean(IS_SPEED);
                    onSendToRemoteStartEvent(isMetric, hasCalories, hasHeartRate, isSpeed);

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(COMMAND_CONFIGURE)) {

                    boolean isMetric = data.getBoolean(IS_METRIC);
                    boolean hasCalories = data.getBoolean(HAS_CALORIES);
                    boolean hasHeartRate = data.getBoolean(HAS_HEART_RATE_MONITOR);
                    boolean isSpeed = data.getBoolean(IS_SPEED);
                    onPhoneSendConfigurationEvent(isMetric, hasCalories, hasHeartRate, isSpeed);

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(COMMAND_PAUSE)) {

                    onSendToRemotePauseEvent();

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(COMMAND_RESUME)) {

                    onSendToRemoteResumeEvent();

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(COMMAND_STOP)) {

                    onSendToRemoteStopEvent();

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(COMMAND_DISCARD)) {

                    onSendToRemoteDiscardEvent();

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(COMMAND_SAVE)) {

                    onSendToRemoteSaveEvent();

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(GPS_AVAILABLE)) {

                    if (mRemoteCommandListener != null) {
                        mRemoteCommandListener.onGpsStatusWarning(GpsStatus.GPS_AVAILABLE);
                    }

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(GPS_NOT_AVAILABLE)) {

                    if (mRemoteCommandListener != null) {
                        mRemoteCommandListener.onGpsStatusWarning(GpsStatus.GPS_NOT_AVAILABLE);
                    }

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(GPS_LOC_SERVICES)) {

                    // when the phone app is open, if user tries to start a trackable workout
                    // with the location services turned on, a dialog will show
                    if (mRemoteCommandListener != null) {
                        mRemoteCommandListener.onLocationServicesStatusEvent();
                    }

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(GPS_NON_TRACKABLE)) {

                    if (mRemoteCommandListener != null) {
                        mRemoteCommandListener.onGpsStatusWarning(GpsStatus.GPS_NON_TRACKABLE);
                    }

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(NOT_LOGGED_IN)) {

                    onSendToRemoteAppStateEvent(NOT_LOGGED_IN);

                }
            }

            if (data.containsKey(CURRENT_STATE)) {
                onSendToRemoteAppStateEvent(data.getString(CURRENT_STATE));
            }
        }
    }

    private void onPhoneForceUpgradeEvent(Integer minSdkVersion) {
//        if (RemoteManager.DETAIL_LOG) {
//            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onPhoneForceUpgradeEvent ++");
//        }
        if (mRemoteCommandListener != null) {
            if (REMOTE_CONTROL_SDK_VERSION_NUMBER < minSdkVersion) {
                mRemoteCommandListener.onForceUpgrade(minSdkVersion);
            }
        }
    }

    private void onPhoneTimeEvent(Long duration) {
//        if (RemoteManager.DETAIL_LOG) {
//            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onPhoneTimeEvent ++");
//        }
        if (mStatsCache != null) {
            mStatsCache.setDuration(duration);
        }
        if (mRemoteDataListener != null) {
            mRemoteDataListener.onDurationEvent(duration);
        }
    }

    private void onPhoneSendCaloriesEvent(Integer calories) {
//        if (RemoteManager.DETAIL_LOG) {
//            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onPhoneSendCaloriesEvent ++");
//        }
        if (mStatsCache != null) {
            mStatsCache.setCalories(calories);
        }
        if (mRemoteDataListener != null) {
            mRemoteDataListener.onCalorieEvent(calories);
        }
    }

    private void onPhoneSendDistanceEvent(Double distance) {
//        if (RemoteManager.DETAIL_LOG) {
//            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onPhoneSendDistanceEvent ++");
//        }
        if (mStatsCache != null) {
            mStatsCache.setDistance(distance);
        }
        if (mRemoteDataListener != null) {
            mRemoteDataListener.onDistanceEvent(distance);
        }
    }

    private void onPhoneSendSpeedEvent(Double speed, Double avgSpeed, Double maxSpeed) {
//        if (RemoteManager.DETAIL_LOG) {
//            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onPhoneSendSpeedEvent ++");
//        }
        if (mStatsCache != null) {
            mStatsCache.setSpeed(speed);
            mStatsCache.setAverageSpeed(avgSpeed);
            mStatsCache.setMaxSpeed(maxSpeed);
        }
        if (mRemoteDataListener != null) {
            mRemoteDataListener.onSpeedEvent(speed, avgSpeed, maxSpeed);
        }
    }

    private void onPhoneSendCadenceEvent(Integer cadence, Integer averageCadence, Integer maxCadence) {
//        if (RemoteManager.DETAIL_LOG) {
//            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onPhoneSendCadenceEvent ++");
//        }
        if (mStatsCache != null) {
            mStatsCache.setCadence(cadence);
            mStatsCache.setAverageCadence(averageCadence);
            mStatsCache.setMaxCadence(maxCadence);
        }
        if (mRemoteDataListener != null) {
            mRemoteDataListener.onCadenceEvent(cadence, averageCadence, maxCadence);
        }
    }

    private void onPhoneSendPowerEvent(Integer power, Integer averagePower, Integer maxPower) {
//        if (RemoteManager.DETAIL_LOG) {
//            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onPhoneSendPowerEvent ++");
//        }
        if (mStatsCache != null) {
            mStatsCache.setPower(power);
            mStatsCache.setAveragePower(averagePower);
            mStatsCache.setMaxPower(maxPower);
        }
        if (mRemoteDataListener != null) {
            mRemoteDataListener.onPowerEvent(power, averagePower, maxPower);
        }
    }

    private void onPhoneSendHeartRateEvent(Integer heartRate, Integer heartRateAvg, Integer heartRateMax, Integer heartRateZone) {
//        if (RemoteManager.DETAIL_LOG) {
//            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onPhoneSendHeartRateEvent ++");
//        }
        if (mStatsCache != null) {
            mStatsCache.setHeartRate(heartRate);
            mStatsCache.setAverageHeartRate(heartRateAvg);
            mStatsCache.setMaxHeartRate(heartRateMax);
            mStatsCache.setHeartRateZone(heartRateZone);
        }
        if (mRemoteDataListener != null) {
            mRemoteDataListener.onHeartRateEvent(heartRate, heartRateAvg, heartRateMax, heartRateZone);
        }
    }

    private void onSendToRemoteStartEvent(Boolean isMetric,
                                         Boolean hasCalories,
                                         Boolean hasHeartRate,
                                         Boolean isSpeed) {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onSendToRemoteStartEvent ++");
        }
        if (mRemoteCommandListener != null) {
            mRemoteCommandListener.onStartWorkoutEvent(isMetric, hasHeartRate, hasCalories, isSpeed);
        }
    }

    private void onPhoneSendConfigurationEvent(Boolean isMetric,
                                              Boolean hasCalories,
                                              Boolean hasHeartRate,
                                              Boolean isSpeed) {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onPhoneSendConfigurationEvent ++");
        }
        if (mRemoteCommandListener != null) {
            mRemoteCommandListener.onAppConfiguredEvent(isMetric, hasHeartRate, hasCalories, isSpeed);
        }
    }

    private void onSendToRemotePauseEvent() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onSendToRemotePauseEvent ++");
        }
        if (mRemoteCommandListener != null) {
            mRemoteCommandListener.onPauseWorkoutEvent();
        }
    }

    private void onSendToRemoteResumeEvent() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onSendToRemoteResumeEvent ++");
        }
        if (mRemoteCommandListener != null) {
            mRemoteCommandListener.onResumeWorkoutEvent();
        }
    }

    private void onSendToRemoteStopEvent() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onSendToRemoteStopEvent ++");
        }
        if (mRemoteCommandListener != null) {
            mRemoteCommandListener.onStopWorkoutEvent();
        }
    }

    private void onSendToRemoteDiscardEvent() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onSendToRemoteDiscardEvent ++");
        }
        if (mRemoteCommandListener != null) {
            mRemoteCommandListener.onDiscardWorkoutEvent();
        }
    }

    private void onSendToRemoteSaveEvent() {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onSendToRemoteSaveEvent ++");
        }
        if (mRemoteCommandListener != null) {
            mRemoteCommandListener.onSaveWorkoutEvent();
        }
    }

    private void onSendToRemoteAppStateEvent(String currentState) {
        if (RemoteManager.DETAIL_LOG) {
            Log.e(RemoteManager.TAG, "RemoteCommunication ++ onSendToRemoteAppStateEvent ++ currentState = " + currentState);
        }
        AppState appState = AppState.NOT_RECORDING;
        if (currentState.equals(NOT_LOGGED_IN)) {
            appState = AppState.NOT_LOGGED_IN;
        }
        if (currentState.equals(NOT_RECORDING)) {
            appState = AppState.NOT_RECORDING;
        }
        if (currentState.equals(RECORDING)) {
            appState = AppState.RECORDING;
        }
        if (currentState.equals(RECORDING_PAUSED)) {
            appState = AppState.RECORDING_PAUSED;
        }
        if (currentState.equals(POST_RECORDING)) {
            appState = AppState.POST_RECORDING;
        }

        if (mRemoteCommandListener != null) {
            mRemoteCommandListener.onAppStateEvent(appState);
        }
    }
}
