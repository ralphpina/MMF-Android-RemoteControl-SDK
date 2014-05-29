package com.mapmyfitness.android.mmfremoteappexample.app;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfAppInfo;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfAppPackage;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfAppState;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfGpsStatus;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfRemoteCommandListener;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfRemoteDataListener;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfRemoteException;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfRemoteManager;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfStatsCache;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity implements MmfRemoteDataListener, MmfRemoteCommandListener, AdapterView.OnItemSelectedListener {

    private static final String TAG = "MainActivity";

    @InjectView(R.id.appStateValue)
    TextView mAppStateValue;

    @InjectView(R.id.distanceText)
    TextView mDistanceText;
    @InjectView(R.id.durationText)
    TextView mDurationText;
    @InjectView(R.id.caloriesText)
    TextView mCaloriesText;

    @InjectView(R.id.speedText)
    TextView mSpeedText;
    @InjectView(R.id.speedAvgText)
    TextView mSpeedAvgText;
    @InjectView(R.id.speedMaxText)
    TextView mSpeedMaxText;

    @InjectView(R.id.heartRateText)
    TextView mHeartRateText;
    @InjectView(R.id.heartRateAvgText)
    TextView mHeartRateAvgText;
    @InjectView(R.id.heartRateMaxText)
    TextView mHeartRateMaxText;
    @InjectView(R.id.heartRateZoneText)
    TextView mHeartRateZoneText;

    @InjectView(R.id.cadenceText)
    TextView mCadenceText;
    @InjectView(R.id.cadenceAvgText)
    TextView mCadenceAvgText;
    @InjectView(R.id.speedMaxText)
    TextView mCadenceMaxText;

    @InjectView(R.id.powerText)
    TextView mPowerText;
    @InjectView(R.id.powerAvgText)
    TextView mPowerAvgText;
    @InjectView(R.id.powerMaxText)
    TextView mPowerMaxText;

    @InjectView(R.id.distanceValue)
    TextView mDistanceValue;
    @InjectView(R.id.durationValue)
    TextView mDurationValue;
    @InjectView(R.id.caloriesValue)
    TextView mCaloriesValue;

    @InjectView(R.id.speedValue)
    TextView mSpeedValue;
    @InjectView(R.id.speedAvgValue)
    TextView mSpeedAvgValue;
    @InjectView(R.id.speedMaxValue)
    TextView mSpeedMaxValue;

    @InjectView(R.id.heartRateValue)
    TextView mHeartRateValue;
    @InjectView(R.id.heartRateAvgValue)
    TextView mHeartRateAvgValue;
    @InjectView(R.id.heartRateMaxValue)
    TextView mHeartRateMaxValue;
    @InjectView(R.id.heartRateZoneValue)
    TextView mHeartRateZoneValue;

    @InjectView(R.id.cadenceValue)
    TextView mCadenceValue;
    @InjectView(R.id.cadenceAvgValue)
    TextView mCadenceAvgValue;
    @InjectView(R.id.cadenceMaxValue)
    TextView mCadenceMaxValue;

    @InjectView(R.id.powerValue)
    TextView mPowerValue;
    @InjectView(R.id.powerAvgValue)
    TextView mPowerAvgValue;
    @InjectView(R.id.powerMaxValue)
    TextView mPowerMaxValue;

    @InjectView(R.id.startButton)
    Button mStartButton;
    @InjectView(R.id.pauseButton)
    Button mPauseButton;
    @InjectView(R.id.resumeButton)
    Button mResumeButton;
    @InjectView(R.id.stopButton)
    Button mStopButton;
    @InjectView(R.id.discardButton)
    Button mDiscardButton;
    @InjectView(R.id.saveButton)
    Button mSaveButton;
    @InjectView(R.id.connectButton)
    Button mConnectButton;
    @InjectView(R.id.disconnectButton)
    Button mDisconnectButton;

    @InjectView(R.id.spinner)
    Spinner mAppsSpinner;

    private MmfRemoteManager mMmfRemoteManager;
    private MmfStatsCache mMmfStatsCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "++ onCreate()++");
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        mMmfStatsCache = new MmfStatsCache();

        MmfRemoteManager.Builder remoteControlBuilder = MmfRemoteManager.getBuilder();
        remoteControlBuilder.setContext(this);
        remoteControlBuilder.setStatsCache(mMmfStatsCache);
        remoteControlBuilder.setDataListener(this);
        remoteControlBuilder.setCommandListener(this);
        remoteControlBuilder.setAppPackage(MmfAppPackage.MAPMYRUN);

        try {
            mMmfRemoteManager = remoteControlBuilder.build();
        } catch (MmfRemoteException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        ArrayList<MmfAppInfo> appPackages = mMmfRemoteManager.findInstalledApps();

        ArrayAdapter<MmfAppInfo> adapter = new ArrayAdapter<MmfAppInfo>(this, android.R.layout.simple_spinner_item, appPackages);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mAppsSpinner.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "++ onResume()++");
        if (!mMmfRemoteManager.isAppConnected()) {
            updateUi(MmfAppState.APP_NOT_CONNECTED);
            connectApp();
        } else {
            mMmfRemoteManager.requestAppState();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "++ onPause()++");
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    // for spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
    }

    // for spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }

    @OnClick(R.id.startButton) void start() {
//        Log.e(TAG, "++ start()++");
        try {
            mMmfRemoteManager.startWorkoutCommand();
        } catch (MmfRemoteException e) {
            if (e.getCode() == MmfRemoteException.Code.APP_NOT_CONNECTED) {
                // Do something on the UI
                connectApp();
            }
        }
    }

    @OnClick(R.id.pauseButton) void pause() {
//        Log.e(TAG, "++ pause()++");
        try {
            mMmfRemoteManager.pauseWorkoutCommand();
        } catch (MmfRemoteException e) {
            if (e.getCode() == MmfRemoteException.Code.APP_NOT_CONNECTED) {
                // Do something on the UI
                connectApp();
            }
        }
    }

    @OnClick(R.id.resumeButton) void resume() {
//        Log.e(TAG, "++ resume()++");
        try {
            mMmfRemoteManager.resumeWorkoutCommand();
        } catch (MmfRemoteException e) {
            if (e.getCode() == MmfRemoteException.Code.APP_NOT_CONNECTED) {
                // Do something on the UI
                connectApp();
            }
        }
    }

    @OnClick(R.id.stopButton) void stop() {
//        Log.e(TAG, "++ stop()++");
        try {
            mMmfRemoteManager.stopWorkoutCommand();
        } catch (MmfRemoteException e) {
            if (e.getCode() == MmfRemoteException.Code.APP_NOT_CONNECTED) {
                // Do something on the UI
                connectApp();
            }
        }
    }

    @OnClick(R.id.discardButton) void discard() {
//        Log.e(TAG, "++ discard()++");
        try {
            mMmfRemoteManager.discardWorkoutCommand();
        } catch (MmfRemoteException e) {
            if (e.getCode() == MmfRemoteException.Code.APP_NOT_CONNECTED) {
                // Do something on the UI
                connectApp();
            }
        }
    }

    @OnClick(R.id.saveButton) void save() {
//        Log.e(TAG, "++ save()++");
        try {
            mMmfRemoteManager.saveWorkoutCommand();
        } catch (MmfRemoteException e) {
            if (e.getCode() == MmfRemoteException.Code.APP_NOT_CONNECTED) {
                // Do something on the UI
                connectApp();
            }
        }
    }

    @OnClick(R.id.connectButton) void connect() {
//        Log.e(TAG, "++ connect()++");
        connectApp();
    }

    @OnClick(R.id.disconnectButton) void disconnect() {
//        Log.e(TAG, "++ disconnect()++");
        mMmfRemoteManager.disconnectFromMmfApp();
    }

    private void connectApp() {
        boolean connected = mMmfRemoteManager.tryToConnectToMmfApp();
        Toast.makeText(this, "Connection was successful = " + connected, Toast.LENGTH_LONG);
    }

    private void updateUi(MmfAppState state) {
        Log.e(TAG, "++ onUpdateUiEvent() ++ state = " + state);
        mAppStateValue.setText(state.toString());

        if (state == MmfAppState.APP_NOT_CONNECTED) {
            clearValues();
            mStartButton.setVisibility(View.VISIBLE);
            mStartButton.setEnabled(false);
            mPauseButton.setVisibility(View.GONE);
            mResumeButton.setVisibility(View.GONE);
            mStopButton.setVisibility(View.GONE);
            mDiscardButton.setVisibility(View.GONE);
            mSaveButton.setVisibility(View.GONE);
            mConnectButton.setVisibility(View.VISIBLE);
            mDisconnectButton.setVisibility(View.GONE);
        } else if (state == MmfAppState.APP_NOT_INSTALLED || state == MmfAppState.NOT_RECORDING) {
            Log.e(TAG, "++ onUpdateUiEvent() ++ state first");
            clearValues();
            mStartButton.setVisibility(View.VISIBLE);
            mStartButton.setEnabled(true);
            mPauseButton.setVisibility(View.GONE);
            mResumeButton.setVisibility(View.GONE);
            mStopButton.setVisibility(View.GONE);
            mDiscardButton.setVisibility(View.GONE);
            mSaveButton.setVisibility(View.GONE);
            mConnectButton.setVisibility(View.GONE);
            mDisconnectButton.setVisibility(View.VISIBLE);
        } else if (state == MmfAppState.RECORDING) {
            Log.e(TAG, "++ onUpdateUiEvent() ++ state second");
            mStartButton.setVisibility(View.GONE);
            mPauseButton.setVisibility(View.VISIBLE);
            mResumeButton.setVisibility(View.GONE);
            mStopButton.setVisibility(View.GONE);
            mDiscardButton.setVisibility(View.GONE);
            mSaveButton.setVisibility(View.GONE);
            mConnectButton.setVisibility(View.GONE);
            mDisconnectButton.setVisibility(View.VISIBLE);
        } else if (state == MmfAppState.RECORDING_PAUSED) {
            Log.e(TAG, "++ onUpdateUiEvent() ++ state third");
            mStartButton.setVisibility(View.GONE);
            mPauseButton.setVisibility(View.GONE);
            mResumeButton.setVisibility(View.VISIBLE);
            mStopButton.setVisibility(View.VISIBLE);
            mDiscardButton.setVisibility(View.GONE);
            mSaveButton.setVisibility(View.GONE);
            mConnectButton.setVisibility(View.GONE);
            mDisconnectButton.setVisibility(View.VISIBLE);
        } else if (state == MmfAppState.POST_RECORDING) {
            Log.e(TAG, "++ onUpdateUiEvent() ++ state fourth");
            mStartButton.setVisibility(View.GONE);
            mPauseButton.setVisibility(View.GONE);
            mResumeButton.setVisibility(View.GONE);
            mStopButton.setVisibility(View.GONE);
            mDiscardButton.setVisibility(View.VISIBLE);
            mSaveButton.setVisibility(View.VISIBLE);
            mConnectButton.setVisibility(View.GONE);
            mDisconnectButton.setVisibility(View.VISIBLE);
        }
    }

    private void clearValues() {
        mMmfStatsCache.zeroOutValues();

        mCaloriesValue.setText(getString(R.string.dash));
        mDistanceValue.setText(getString(R.string.dash));
        mDurationValue.setText(getString(R.string.dash));

        mSpeedValue.setText(getString(R.string.dash));
        mSpeedAvgValue.setText(getString(R.string.dash));
        mSpeedMaxValue.setText(getString(R.string.dash));

        mHeartRateValue.setText(getString(R.string.dash));
        mHeartRateAvgValue.setText(getString(R.string.dash));
        mHeartRateMaxValue.setText(getString(R.string.dash));
        mHeartRateZoneValue.setText(getString(R.string.dash));

        mCadenceValue.setText(getString(R.string.dash));
        mCadenceAvgValue.setText(getString(R.string.dash));
        mCadenceMaxValue.setText(getString(R.string.dash));

        mPowerValue.setText(getString(R.string.dash));
        mPowerAvgValue.setText(getString(R.string.dash));
        mPowerMaxValue.setText(getString(R.string.dash));
    }

    @Override
    public void onAppStateEvent(MmfAppState appState) {
//        Log.e(TAG, "++ onAppStateEvent ++ App state = " + appState);
//        Toast.makeText(this, "App state = " + appState, Toast.LENGTH_LONG);
        updateUi(appState);
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
        updateUi(MmfAppState.RECORDING_PAUSED);
    }

    @Override
    public void onResumeWorkoutEvent() {
        //Toast.makeText(this, "Workout Resumed", Toast.LENGTH_LONG);
        updateUi(MmfAppState.RECORDING);
    }

    @Override
    public void onStopWorkoutEvent() {
//        Toast.makeText(this, "Workout stopped", Toast.LENGTH_LONG);
        updateUi(MmfAppState.POST_RECORDING);
    }

    @Override
    public void onSaveWorkoutEvent() {
//        Toast.makeText(this, "Workout saved", Toast.LENGTH_LONG);
        updateUi(MmfAppState.NOT_RECORDING);
    }

    @Override
    public void onDiscardWorkoutEvent() {
//        Toast.makeText(this, "Workout discarded", Toast.LENGTH_LONG);
        updateUi(MmfAppState.NOT_RECORDING);
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
        mHeartRateValue.setText(heartRate.toString());
        mHeartRateAvgValue.setText(heartRateAvg.toString());
        mHeartRateMaxValue.setText(heartRateMax.toString());
        mHeartRateZoneValue.setText(heartRateZone.toString());
    }

    @Override
    public void onCadenceEvent(Integer cadence, Integer cadenceAvg, Integer cadenceMax) {
        mCadenceValue.setText(cadence.toString());
        mCadenceAvgValue.setText(cadenceAvg.toString());
        mCadenceMaxValue.setText(cadenceMax.toString());
    }

    @Override
    public void onCalorieEvent(Integer calories) {
        mCaloriesValue.setText(calories.toString());
    }

    @Override
    public void onDistanceEvent(Double distance) {
        mDistanceValue.setText(distance.toString());
    }

    @Override
    public void onDurationEvent(Long duration) {
        mDurationValue.setText(duration.toString());
    }

    @Override
    public void onSpeedEvent(Double speed, Double speedAvg, Double speedMax) {
        mSpeedValue.setText(speed.toString());
        mSpeedAvgValue.setText(speedAvg.toString());
        mSpeedMaxValue.setText(speedMax.toString());
    }

    @Override
    public void onPowerEvent(Integer power, Integer powerAvg, Integer powerMax) {
        mPowerValue.setText(power.toString());
        mPowerAvgValue.setText(powerAvg.toString());
        mPowerMaxValue.setText(powerMax.toString());
    }

}
