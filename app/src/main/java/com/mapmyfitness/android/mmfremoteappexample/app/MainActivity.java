package com.mapmyfitness.android.mmfremoteappexample.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfAppState;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfRemoteManager;
import com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote.MmfStatsCache;
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

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MainActivity extends ActionBarActivity {

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
    @InjectView(R.id.discardButton)
    Button mDisconnectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "++ onCreate()++");
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "++ onResume()++");
        EventBus.getInstance().register(this);

        Intent recordServiceIntent = new Intent(this, RecordService.class);
        startService(recordServiceIntent);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "++ onPause()++");
        EventBus.getInstance().unregister(this);
    }

    @OnClick(R.id.startButton) void start() {
        Log.e(TAG, "++ start()++");
        EventBus.getInstance().post(new CommandEvent("START"));
    }

    @OnClick(R.id.pauseButton) void pause() {
        Log.e(TAG, "++ pause()++");
        EventBus.getInstance().post(new CommandEvent("PAUSE"));
    }

    @OnClick(R.id.resumeButton) void resume() {
        Log.e(TAG, "++ resume()++");
        EventBus.getInstance().post(new CommandEvent("RESUME"));
    }

    @OnClick(R.id.stopButton) void stop() {
        Log.e(TAG, "++ stop()++");
        EventBus.getInstance().post(new CommandEvent("STOP"));
    }

    @OnClick(R.id.discardButton) void discard() {
        Log.e(TAG, "++ discard()++");
        EventBus.getInstance().post(new CommandEvent("DISCARD"));
    }

    @OnClick(R.id.saveButton) void save() {
        Log.e(TAG, "++ save()++");
        EventBus.getInstance().post(new CommandEvent("SAVE"));
    }

    @OnClick(R.id.connectButton) void connect() {
        Log.e(TAG, "++ connect()++");
        EventBus.getInstance().post(new CommandEvent("CONNECT"));
    }

    @OnClick(R.id.disconnectButton) void disconnect() {
        Log.e(TAG, "++ disconnect()++");
        EventBus.getInstance().post(new CommandEvent("DISCONNECT"));
    }

    @Subscribe
    public void onUpdateUiEvent(UpdateUiEvent event) {
        Log.e(TAG, "++ onUpdateUiEvent()++");
        MmfAppState appState = MmfStatsCache.getInstance().getMmfAppState();

        mAppStateValue.setText(appState.toString());

        if (!MmfRemoteManager.getInstance(this).isAppConnected()) {
            clearValues();
//            mStartButton.setVisibility(View.VISIBLE);
//            mStartButton.setEnabled(false);
//            mPauseButton.setVisibility(View.GONE);
//            mResumeButton.setVisibility(View.GONE);
//            mStopButton.setVisibility(View.GONE);
//            mDiscardButton.setVisibility(View.GONE);
//            mSaveButton.setVisibility(View.GONE);
//            mConnectButton.setVisibility(View.VISIBLE);
//            mDisconnectButton.setVisibility(View.GONE);
        } else if (appState == MmfAppState.APP_NOT_INSTALLED || appState == MmfAppState.NOT_RECORDING) {
            clearValues();
//            mStartButton.setVisibility(View.VISIBLE);
//            mStartButton.setEnabled(true);
//            mPauseButton.setVisibility(View.GONE);
//            mResumeButton.setVisibility(View.GONE);
//            mStopButton.setVisibility(View.GONE);
//            mDiscardButton.setVisibility(View.GONE);
//            mSaveButton.setVisibility(View.GONE);
//            mConnectButton.setVisibility(View.GONE);
//            mDisconnectButton.setVisibility(View.VISIBLE);
        } //else if (appState == MmfAppState.RECORDING) {
//            mStartButton.setVisibility(View.GONE);
//            mPauseButton.setVisibility(View.VISIBLE);
//            mResumeButton.setVisibility(View.GONE);
//            mStopButton.setVisibility(View.GONE);
//            mDiscardButton.setVisibility(View.GONE);
//            mSaveButton.setVisibility(View.GONE);
//            mConnectButton.setVisibility(View.GONE);
//            mDisconnectButton.setVisibility(View.VISIBLE);
//        } else if (appState == MmfAppState.RECORDING_PAUSED) {
//            mStartButton.setVisibility(View.GONE);
//            mPauseButton.setVisibility(View.GONE);
//            mResumeButton.setVisibility(View.VISIBLE);
//            mStopButton.setVisibility(View.VISIBLE);
//            mDiscardButton.setVisibility(View.GONE);
//            mSaveButton.setVisibility(View.GONE);
//            mConnectButton.setVisibility(View.GONE);
//            mDisconnectButton.setVisibility(View.VISIBLE);
//        } else if (appState == MmfAppState.POST_RECORDING) {
//            mStartButton.setVisibility(View.GONE);
//            mPauseButton.setVisibility(View.GONE);
//            mResumeButton.setVisibility(View.GONE);
//            mStopButton.setVisibility(View.GONE);
//            mDiscardButton.setVisibility(View.VISIBLE);
//            mSaveButton.setVisibility(View.VISIBLE);
//            mConnectButton.setVisibility(View.GONE);
//            mDisconnectButton.setVisibility(View.VISIBLE);
//        }
    }

    private void clearValues() {
        MmfStatsCache.getInstance().zeroOutValues();

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

    @Subscribe
    public void onHeartRateEvent(HeartRateEvent event) {
        Log.e(TAG, "++ onHeartRateEvent()++");
        mHeartRateValue.setText(event.getHeartRate().toString());
        mHeartRateAvgValue.setText(event.getHeartRateAverage().toString());
        mHeartRateMaxValue.setText(event.getHeartRateMax().toString());
        mHeartRateZoneValue.setText(event.getHeartRateZone().toString());
    }

    @Subscribe
    public void onCalorieEvent(CalorieEvent event) {
        Log.e(TAG, "++ onCalorieEvent()++");
        mCaloriesValue.setText(event.getCalories().toString());
    }

    @Subscribe
    public void onDistanceEvent(DistanceEvent event) {
        Log.e(TAG, "++ onDistanceEvent()++");
        mDistanceValue.setText(event.getDistance().toString());
    }

    @Subscribe
    public void onDurationEvent(DurationEvent event) {
        Log.e(TAG, "++ onDurationEvent()++");
        mDurationValue.setText(event.getDuration().toString());
    }

    @Subscribe
     public void onSpeedEvent(SpeedEvent event) {
//        Log.e(TAG, "++ onSpeedEvent()++");
        mSpeedValue.setText(event.getSpeed().toString());
        mSpeedAvgValue.setText(event.getSpeedAverage().toString());
        mSpeedMaxValue.setText(event.getSpeedMax().toString());
    }

    @Subscribe
    public void onCadenceEvent(CadenceEvent event) {
//        Log.e(TAG, "++ onSpeedEvent()++");
        mCadenceValue.setText(event.getCadence().toString());
        mCadenceAvgValue.setText(event.getCadenceAverage().toString());
        mCadenceMaxValue.setText(event.getCadenceMax().toString());
    }

    @Subscribe
    public void onPowerEvent(PowerEvent event) {
//        Log.e(TAG, "++ onSpeedEvent()++");
        mPowerValue.setText(event.getPower().toString());
        mPowerAvgValue.setText(event.getPowerAverage().toString());
        mPowerMaxValue.setText(event.getPowerMax().toString());
    }

}
