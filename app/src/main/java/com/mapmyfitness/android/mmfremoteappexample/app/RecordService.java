package com.mapmyfitness.android.mmfremoteappexample.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfAppState;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfGpsStatus;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfRemoteCommandListener;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfRemoteDataListener;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfRemoteManager;
import com.mapmyfitness.android.mmfremoteappexample.app.events.CadenceEvent;
import com.mapmyfitness.android.mmfremoteappexample.app.events.CalorieEvent;
import com.mapmyfitness.android.mmfremoteappexample.app.events.CommandEvent;
import com.mapmyfitness.android.mmfremoteappexample.app.events.DistanceEvent;
import com.mapmyfitness.android.mmfremoteappexample.app.events.DurationEvent;
import com.mapmyfitness.android.mmfremoteappexample.app.events.EventBus;
import com.mapmyfitness.android.mmfremoteappexample.app.events.HeartRateEvent;
import com.mapmyfitness.android.mmfremoteappexample.app.events.PowerEvent;
import com.mapmyfitness.android.mmfremoteappexample.app.events.SpeedEvent;
import com.mapmyfitness.android.mmfremoteappexample.app.events.UpdateUiEvent;
import com.squareup.otto.Subscribe;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class RecordService extends Service implements MmfRemoteDataListener, MmfRemoteCommandListener {

    private static final String TAG = "RecordService";

    private MmfRemoteManager mMmfRemoteManager;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e(TAG, "++ onCreate() ++");
        EventBus.getInstance().register(this);
        //mMmfRemoteManager = mMmfRemoteManager.getInstance(this);
        mMmfRemoteManager.setCommandListener(this);
        mMmfRemoteManager.setDataListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "++ onDestroy() ++");
        EventBus.getInstance().unregister(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // nobody should bind, this runs in the background
        Log.e(TAG, "++ onBind() ++");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "++ onStartCommand() ++");
        return START_STICKY;
    }

    @Subscribe
    public void onCommandEvent(CommandEvent event) {
//        Log.e(TAG, "++ onCommandEvent() ++");
//        if (event.getCommand().equals("START")) {
//            mMmfRemoteManager.startWorkoutCommand();
//        } else if (event.getCommand().equals("PAUSE")) {
//            mMmfRemoteManager.pauseWorkoutCommand();
//        } else if (event.getCommand().equals("RESUME")) {
//            mMmfRemoteManager.resumeWorkoutCommand();
//        } else if (event.getCommand().equals("STOP")) {
//            mMmfRemoteManager.stopWorkoutCommand();
//        } else if (event.getCommand().equals("DISCARD")) {
//            mMmfRemoteManager.discardWorkoutCommand();
//        } else if (event.getCommand().equals("SAVE")) {
//            mMmfRemoteManager.saveWorkoutCommand();
//        } else if (event.getCommand().equals("CONNECT")) {
//            mMmfRemoteManager.connectToMmfApp();
//        } else if (event.getCommand().equals("DISCONNECT")) {
//            mMmfRemoteManager.disconnectFromMmfApp();
//        }
    }

    @Override
    public void onAppStateEvent(MmfAppState appState) {
//        Log.e(TAG, "++ onAppStateEvent ++ App state = " + appState);
//        Toast.makeText(this, "App state = " + appState, Toast.LENGTH_LONG);
        EventBus.getInstance().post(new UpdateUiEvent(appState));
    }

    @Override
    public void onAppConfiguredEvent(Boolean metric, Boolean hasHeartRate, Boolean calculatesCalories, Boolean isSpeed) {
        //Toast.makeText(this, "App configured", Toast.LENGTH_LONG);
    }

    @Override
    public void onStartWorkoutEvent(Boolean metric, Boolean hasHeartRate, Boolean calculatesCalories, Boolean isSpeed) {
        Toast.makeText(this, "Workout started", Toast.LENGTH_LONG);
        //EventBus.getInstance().post(new UpdateUiEvent(MmfAppState.RECORDING));
    }

    @Override
    public void onPauseWorkoutEvent() {
        //Toast.makeText(this, "Workout paused", Toast.LENGTH_LONG);
        EventBus.getInstance().post(new UpdateUiEvent(MmfAppState.RECORDING_PAUSED));
    }

    @Override
    public void onResumeWorkoutEvent() {
        //Toast.makeText(this, "Workout Resumed", Toast.LENGTH_LONG);
        EventBus.getInstance().post(new UpdateUiEvent(MmfAppState.RECORDING));
    }

    @Override
    public void onStopWorkoutEvent() {
//        Toast.makeText(this, "Workout stopped", Toast.LENGTH_LONG);
        EventBus.getInstance().post(new UpdateUiEvent(MmfAppState.POST_RECORDING));
    }

    @Override
    public void onSaveWorkoutEvent() {
//        Toast.makeText(this, "Workout saved", Toast.LENGTH_LONG);
        EventBus.getInstance().post(new UpdateUiEvent(MmfAppState.NOT_RECORDING));
    }

    @Override
    public void onDiscardWorkoutEvent() {
//        Toast.makeText(this, "Workout discarded", Toast.LENGTH_LONG);
        EventBus.getInstance().post(new UpdateUiEvent(MmfAppState.NOT_RECORDING));
    }

    @Override
    public void onGpsStatusWarning(MmfGpsStatus gpsStatus) {

    }

    @Override
    public void onLocationServicesStatusEvent() {

    }

    @Override
    public void onForceUpgrade(Integer minSdkVersion) {

    }

    @Override
    public void onHeartRateEvent(Integer heartRate, Integer heartRateAvg, Integer heartRateMax, Integer heartRateZone) {
        EventBus.getInstance().post(new HeartRateEvent(heartRate, heartRateAvg, heartRateMax, heartRateZone));
    }

    @Override
    public void onCadenceEvent(Integer cadence, Integer cadenceAvg, Integer cadenceMax) {
        EventBus.getInstance().post(new CadenceEvent(cadence, cadenceAvg, cadenceMax));
    }

    @Override
    public void onCalorieEvent(Integer calories) {
        EventBus.getInstance().post(new CalorieEvent(calories));
    }

    @Override
    public void onDistanceEvent(Double distance) {
        EventBus.getInstance().post(new DistanceEvent(distance));
    }

    @Override
    public void onDurationEvent(Long duration) {
        EventBus.getInstance().post(new DurationEvent(duration));
    }

    @Override
    public void onSpeedEvent(Double speed, Double speedAvg, Double speedMax) {
        EventBus.getInstance().post(new SpeedEvent(speed, speedAvg, speedMax));
    }

    @Override
    public void onPowerEvent(Integer power, Integer powerAvg, Integer powerMax) {
        EventBus.getInstance().post(new PowerEvent(power, powerAvg, powerMax));
    }

}