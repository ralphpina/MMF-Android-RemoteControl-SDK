package com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote;

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
 * Created by ralph.pina on 5/27/14.
 */
public class MmfRemoteCommunication {

    private static final String TAG = "MmfRemComLogger";

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
    private static final String COMMAND_CANCEL_WORKOUT_START = "cancel_workout_start";
    private static final String COMMAND_START_WITHOUT_GPS = "start_without_gps";
    private static final String COMMAND_FORCE_UPGRADE = "force_upgrade";

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

    private static final String COMMAND_FROM_REMOTE = "command_from_remote";
    private static final String COMMAND_FROM_MMF_APP = "command_from_mmf_app";

    // TODO add to phone side R.Pina 20140527
    private static final String REMOTE_COMMAND_VERSION_NUMBER = "remote_command_version_number";

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

    /** Messenger for communicating with service. */
    private Messenger mService = null;

    /** Flag indicating whether we have called bind on the service. */
    private boolean mIsBound = false;

    /* Send data to remote app that registered the listener */
    private MmfRemoteDataListener mMmfRemoteDataListener;
    /* Send commands to remote app that registered the listener */
    private MmfRemoteCommandListener mMmfRemoteCommandListener;

    /* Keep a cache of the data for easy access */
    private MmfStatsCache mMmfStatsCache;

    protected MmfRemoteCommunication() {
        mMmfStatsCache = mMmfStatsCache.getInstance();
    }

    /**
     * Target we publish for clients to send messages to IncomingHandler.
     */
    final Messenger mMessenger = new Messenger(new IncomingHandler());

    /**
     * Check if the service is bound
     */
    public boolean isBound() {
        return mIsBound;
    }

    /**
     * Class for interacting with the main interface of the service.
     */
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            Log.e(TAG, "++ onServiceConnected ++");
            //if (service  != null) {
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
                Log.e(TAG, e.getMessage(), e);
            }
            //}
        }

        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "++ onServiceDisconnected ++");
            // This is called when the connection with the service has been
            // unexpectedly disconnected -- that is, its process crashed.
            mService = null;
        }
    };

    public boolean onConnectionEstablished(Context context, String intentFilterAction) {
        Log.e(TAG, "++ onConnectionEstablished ++");
        boolean success = false;
        if (!mIsBound) {
            success = context.bindService(new Intent(intentFilterAction), mConnection, context.BIND_AUTO_CREATE);
            mIsBound = true;
        }
        return success;
    }

    public void onConnectionClosed(Context context) {
        Log.e(TAG, "++ onConnectionClosed ++");
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

    public void setDataListener(MmfRemoteDataListener mmfRemoteDataListener) {
        Log.e(TAG, "++ setDataListener ++");
        mMmfRemoteDataListener = mmfRemoteDataListener;
    }

    public void setCommandListener(MmfRemoteCommandListener mmfRemoteCommandListener) {
        Log.e(TAG, "++ setCommandListener ++");
        mMmfRemoteCommandListener = mmfRemoteCommandListener;
    }

    public void startCommand() {
        Log.d(TAG, "MmfRemoteCommunication ++ startCommand() ++");
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_START));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void pauseCommand() {
        Log.d(TAG, "MmfRemoteCommunication ++ pauseCommand() ++");
        try {
            // TODO add appdisconnect exception
//            if (mService == null) {
//                throw new AppDisconnectedException("Service to app closed");
//            }
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_PAUSE));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void resumeCommand() {
        Log.d(TAG, "MmfRemoteCommunication ++ resumeCommand() ++");
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_RESUME));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void stopCommand() {
        Log.d(TAG, "MmfRemoteCommunication ++ stopCommand() ++");
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_STOP));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void discardCommand() {
        Log.d(TAG, "MmfRemoteCommunication ++ discardCommand() ++");
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_DISCARD));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void saveCommand() {
        Log.d(TAG, "MmfRemoteCommunication ++ saveCommand() ++");
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_SAVE));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void getCurrentStateCommand() {
        Log.d(TAG, "MmfRemoteCommunication ++ getCurrentState() ++");
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_CURRENT_STATE));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void startWithoutGpsCommand() {
        Log.d(TAG, "MmfRemoteCommunication ++ startWithoutGpsCommand() ++");
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_START_WITHOUT_GPS));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void cancelWorkoutStartCommand() {
        Log.d(TAG, "MmfRemoteCommunication ++ cancelWorkoutStartCommand() ++");
        try {
            mService.send(Message.obtain(null,
                    REMOTE_CONTROLLER_CLIENT_COMMAND_CANCEL_WORKOUT_START));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendVersionNumber(String version) {
        Log.d(TAG, "MmfRemoteCommunication ++ sendVersionNumber() ++");
        try {
            Message msg = new Message();
            msg.what = REMOTE_CONTROLLER_CLIENT_COMMAND_CHECK_VERSION;
            Bundle data = new Bundle();
            data.putString(REMOTE_COMMAND_VERSION_NUMBER, version);
            msg.setData(data);
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handler of incoming messages from service.
     */
    private class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            Log.e(TAG, "++ handleMessage ++");
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

                    if (mMmfRemoteCommandListener != null) {
                        mMmfRemoteCommandListener.onGpsStatusWarning(MmfGpsStatus.GPS_AVAILABLE);
                    }

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(GPS_NOT_AVAILABLE)) {

                    if (mMmfRemoteCommandListener != null) {
                        mMmfRemoteCommandListener.onGpsStatusWarning(MmfGpsStatus.GPS_NOT_AVAILABLE);
                    }

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(GPS_LOC_SERVICES)) {

                    // when the phone app is open, if user tries to start a trackable workout
                    // with the location services turned on, a dialog will show
                    if (mMmfRemoteCommandListener != null) {
                        mMmfRemoteCommandListener.onLocationServicesStatusEvent();
                    }

                } else if (data.getString(COMMAND_FROM_MMF_APP).equals(GPS_NON_TRACKABLE)) {

                    if (mMmfRemoteCommandListener != null) {
                        mMmfRemoteCommandListener.onGpsStatusWarning(MmfGpsStatus.GPS_NON_TRACKABLE);
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

    public void onPhoneTimeEvent(Long duration) {
        Log.e(TAG, "++ onPhoneTimeEvent ++");
        if (mMmfRemoteDataListener != null) {
            mMmfStatsCache.setDuration(duration);
            mMmfRemoteDataListener.onDurationEvent(duration);
        }
    }

    public void onPhoneSendCaloriesEvent(Integer calories) {
        Log.e(TAG, "++ onPhoneSendCaloriesEvent ++");
        if (mMmfRemoteDataListener != null) {
            mMmfStatsCache.setCalories(calories);
            mMmfRemoteDataListener.onCalorieEvent(calories);
        }
    }

    public void onPhoneSendDistanceEvent(Double distance) {
        Log.e(TAG, "++ onPhoneSendDistanceEvent ++");
        if (mMmfRemoteDataListener != null) {
            mMmfStatsCache.setDistance(distance);
            mMmfRemoteDataListener.onDistanceEvent(distance);
        }
    }

    public void onPhoneSendSpeedEvent(Double speed, Double avgSpeed, Double maxSpeed) {
        Log.e(TAG, "++ onPhoneSendSpeedEvent ++");
        if (mMmfRemoteDataListener != null) {
            mMmfStatsCache.setSpeed(speed);
            mMmfStatsCache.setAverageSpeed(avgSpeed);
            mMmfStatsCache.setMaxSpeed(maxSpeed);
            mMmfRemoteDataListener.onSpeedEvent(speed, avgSpeed, maxSpeed);
        }
    }

    public void onPhoneSendCadenceEvent(Integer cadence, Integer averageCadence, Integer maxCadence) {
        Log.e(TAG, "++ onPhoneSendCadenceEvent ++");
        if (mMmfRemoteDataListener != null) {
            mMmfStatsCache.setCadence(cadence);
            mMmfStatsCache.setAverageCadence(averageCadence);
            mMmfStatsCache.setMaxCadence(maxCadence);
            mMmfRemoteDataListener.onCadenceEvent(cadence, averageCadence, maxCadence);
        }
    }

    public void onPhoneSendPowerEvent(Integer power, Integer averagePower, Integer maxPower) {
        Log.e(TAG, "++ onPhoneSendPowerEvent ++");
        if (mMmfRemoteDataListener != null) {
            mMmfStatsCache.setPower(power);
            mMmfStatsCache.setAveragePower(averagePower);
            mMmfStatsCache.setMaxPower(maxPower);
            mMmfRemoteDataListener.onPowerEvent(power, averagePower, maxPower);
        }
    }

    public void onPhoneSendHeartRateEvent(Integer heartRate, Integer heartRateAvg, Integer heartRateMax, Integer heartRateZone) {
        Log.e(TAG, "++ onPhoneSendHeartRateEvent ++");
        if (mMmfRemoteDataListener != null) {
            mMmfStatsCache.setHeartRate(heartRate);
            mMmfStatsCache.setAverageHeartRate(heartRateAvg);
            mMmfStatsCache.setMaxHeartRate(heartRateMax);
            mMmfStatsCache.setHeartRateZone(heartRateZone);
            mMmfRemoteDataListener.onHeartRateEvent(heartRate, heartRateAvg, heartRateMax, heartRateZone);
        }
    }

    public void onSendToRemoteStartEvent(Boolean isMetric,
                                         Boolean hasCalories,
                                         Boolean hasHeartRate,
                                         Boolean isSpeed) {
        Log.e(TAG, "++ onSendToRemoteStartEvent ++");
        if (mMmfRemoteCommandListener != null) {
            mMmfRemoteCommandListener.onStartWorkoutEvent(isMetric, hasHeartRate, hasCalories, isSpeed);
        }
    }

    public void onPhoneSendConfigurationEvent(Boolean isMetric,
                                              Boolean hasCalories,
                                              Boolean hasHeartRate,
                                              Boolean isSpeed) {
        Log.e(TAG, "++ onPhoneSendConfigurationEvent ++");
        if (mMmfRemoteCommandListener != null) {
            mMmfRemoteCommandListener.onAppConfiguredEvent(isMetric, hasHeartRate, hasCalories, isSpeed);
        }
    }

    public void onSendToRemotePauseEvent() {
        Log.e(TAG, "++ onSendToRemotePauseEvent ++");
        if (mMmfRemoteCommandListener != null) {
            mMmfRemoteCommandListener.onPauseWorkoutEvent();
        }
    }

    public void onSendToRemoteResumeEvent() {
        Log.e(TAG, "++ onSendToRemoteResumeEvent ++");
        if (mMmfRemoteCommandListener != null) {
            mMmfRemoteCommandListener.onResumeWorkoutEvent();
        }
    }

    public void onSendToRemoteStopEvent() {
        Log.e(TAG, "++ onSendToRemoteStopEvent ++");
        if (mMmfRemoteCommandListener != null) {
            mMmfRemoteCommandListener.onStopWorkoutEvent();
        }
    }

    public void onSendToRemoteDiscardEvent() {
        Log.e(TAG, "++ onSendToRemoteDiscardEvent ++");
        if (mMmfRemoteCommandListener != null) {
            mMmfRemoteCommandListener.onDiscardWorkoutEvent();
        }
    }

    public void onSendToRemoteSaveEvent() {
        Log.e(TAG, "++ onSendToRemoteSaveEvent ++");
        if (mMmfRemoteCommandListener != null) {
            mMmfRemoteCommandListener.onSaveWorkoutEvent();
        }
    }

    // TODO need to add APP_NOT_INSTALLED R.Pina 20140528
    public void onSendToRemoteAppStateEvent(String currentState) {
        Log.e(TAG, "++ onSendToRemoteAppStateEvent ++ currentState = " + currentState);
        MmfAppState appState = MmfAppState.NOT_RECORDING;
        if (currentState.equals(NOT_LOGGED_IN)) {
            appState = MmfAppState.NOT_LOGGED_IN;
        }
        if (currentState.equals(NOT_RECORDING)) {
            appState = MmfAppState.NOT_RECORDING;
        }
        if (currentState.equals(RECORDING)) {
            appState = MmfAppState.RECORDING;
        }
        if (currentState.equals(RECORDING_PAUSED)) {
            appState = MmfAppState.RECORDING_PAUSED;
        }
        if (currentState.equals(POST_RECORDING)) {
            appState = MmfAppState.POST_RECORDING;
        }
        MmfStatsCache.getInstance().setMmfAppState(appState);
        if (mMmfRemoteCommandListener != null) {
            mMmfRemoteCommandListener.onAppStateEvent(appState);
        }
    }
}
