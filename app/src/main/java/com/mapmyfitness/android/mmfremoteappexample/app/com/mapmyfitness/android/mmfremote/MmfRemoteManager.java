package com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;

/**
 * Created by ralph.pina on 5/27/14.
 */
public class MmfRemoteManager {

    private static final String TAG = "MmfRemComLogger";

    private Context mContext;
    private MmfRemoteCommunication mMmfRemoteCommunication;
    private MmfAppPackage mAppPackage;

    private MmfRemoteManager(Builder init) {
        mMmfRemoteCommunication = new MmfRemoteCommunication();

        this.mContext = init.context;
        if (init.context != null) {
            mContext = init.context;
        }
        if (init.appPackage != null) {
            mAppPackage = init.appPackage;
        }
        mMmfRemoteCommunication.setDataListener(init.dataListener);
        mMmfRemoteCommunication.setCommandListener(init.commandListener);
        if (init.statsCache != null) {
            mMmfRemoteCommunication.setStatsCache(init.statsCache);
        }
    }

        // register a receiver to learn when our companion app
        // is installed or uninstalled
//        mPackageChangeReceiver = new PackageChangeReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
//        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
//        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
//        filter.addDataScheme("package");
//        mContext.registerReceiver(mPackageChangeReceiver, filter);

    /**
     * get the version number of this SDK. It will be versioned accordingly:
     * - First 3 digits are patches
     * - Fourth and fifth digits are minor
     * - Sixth and seventh digits are major releases.
     * Thus, a version might look like 1010300. That would be equivalent to 1.1.3.
     *
     * @return version number of the SDK
     */
    public int getVersionNumber() {
        return mMmfRemoteCommunication.REMOTE_CONTROL_SDK_VERSION_NUMBER;
    }


    public boolean tryToConnectToMmfApp() {
        if (!isAppConnected()) {
            return mMmfRemoteCommunication.onConnectionTried(mContext, mAppPackage.getIntentActionFilter());
        } else {
            return true;
        }
    }

    public MmfAppPackage getAppPackage() {
        return mAppPackage;
    }

    public void setAppPackage(MmfAppPackage appPackage) {
        mAppPackage = appPackage;
    }

    public void setStatsCache(MmfStatsCache statsCache) {
        mMmfRemoteCommunication.setStatsCache(statsCache);
    }

    public MmfStatsCache getStatsCache() {
        return mMmfRemoteCommunication.getStatsCache();
    }

    public void setDataListener(MmfRemoteDataListener mmfRemoteDataListener) {
        mMmfRemoteCommunication.setDataListener(mmfRemoteDataListener);
    }

    public void setCommandListener(MmfRemoteCommandListener mmfRemoteCommandListener) {
        mMmfRemoteCommunication.setCommandListener(mmfRemoteCommandListener);
    }

    public void disconnectFromMmfApp() {
        if (isAppConnected()) {
            mMmfRemoteCommunication.onConnectionClosed(mContext);
        }
    }

    public void startWorkoutCommand() throws MmfRemoteException {
        checkIsBound();
        mMmfRemoteCommunication.startCommand();
    }

    public void pauseWorkoutCommand() throws MmfRemoteException {
        checkIsBound();
        mMmfRemoteCommunication.pauseCommand();
    }

    public void resumeWorkoutCommand() throws MmfRemoteException {
        checkIsBound();
        mMmfRemoteCommunication.resumeCommand();
    }

    public void stopWorkoutCommand() throws MmfRemoteException {
        checkIsBound();
        mMmfRemoteCommunication.stopCommand();
    }

    public void discardWorkoutCommand() throws MmfRemoteException {
        checkIsBound();
        mMmfRemoteCommunication.discardCommand();
    }

    public void saveWorkoutCommand() throws MmfRemoteException {
        checkIsBound();
        mMmfRemoteCommunication.saveCommand();
    }

    public void startWithoutGpsCommand() throws MmfRemoteException {
        checkIsBound();
        mMmfRemoteCommunication.startWithoutGpsCommand();
    }

    public void cancelWorkoutStart() throws MmfRemoteException {
        checkIsBound();
        mMmfRemoteCommunication.cancelWorkoutStartCommand();
    }

    public void requestAppState() {
        if (!isAppConnected()) {
            mMmfRemoteCommunication.getCurrentStateCommand();
        } else if (mMmfRemoteCommunication.getCommandListener() != null) {
            mMmfRemoteCommunication.getCommandListener().onAppStateEvent(MmfAppState.APP_NOT_INSTALLED);
        }
    }

    private void checkIsBound() throws MmfRemoteException {
        if (!isAppConnected()) {
            throw new MmfRemoteException(MmfRemoteException.Code.APP_NOT_CONNECTED, "App is not connected to an MMF app");
        }
    }

    public boolean isAppConnected() {
        return mMmfRemoteCommunication.isBound();
    }

    public ArrayList<MmfAppInfo> findInstalledApps() {
        MmfAppInfo appInfo;
        ArrayList<MmfAppInfo> appInfoList = new ArrayList<MmfAppInfo>();

        // try each of the 10 apps
        appInfo = getAppInfo(MmfAppPackage.MAPMYFITNESS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(MmfAppPackage.MAPMYFITNESSPLUS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(MmfAppPackage.MAPMYRUN);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(MmfAppPackage.MAPMYRUNPLUS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(MmfAppPackage.MAPMYRIDE);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(MmfAppPackage.MAPMYRIDEPLUS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(MmfAppPackage.MAPMYHIKE);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(MmfAppPackage.MAPMYHIKEPLUS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(MmfAppPackage.MAPMYWALK);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(MmfAppPackage.MAPMYWALKPLUS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        return appInfoList;
    }

    public MmfAppInfo getAppInfo(MmfAppPackage mmfAppPackage) {
        PackageManager pm = mContext.getPackageManager();
        MmfAppInfo appInfo;

        try {
            PackageInfo pi = pm.getPackageInfo(mmfAppPackage.toString(), PackageManager.GET_ACTIVITIES);
            appInfo = new MmfAppInfo(mmfAppPackage, true, pi.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            // app is not install
            appInfo = new MmfAppInfo(mmfAppPackage, false, 0);
        }

        return appInfo;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public static final class Builder {
        Context context;
        MmfAppPackage appPackage;
        MmfRemoteDataListener dataListener;
        MmfRemoteCommandListener commandListener;
        MmfStatsCache statsCache;

        private Builder() {
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setAppPackage(MmfAppPackage appPackage) {
            this.appPackage = appPackage;
            return this;
        }

        public Builder setDataListener(MmfRemoteDataListener dataListener) {
            this.dataListener = dataListener;
            return this;
        }

        public Builder setCommandListener(MmfRemoteCommandListener commandListener) {
            this.commandListener = commandListener;
            return this;
        }

        public Builder setStatsCache(MmfStatsCache statsCache) {
            this.statsCache = statsCache;
            return this;
        }

        public MmfRemoteManager build() throws MmfRemoteException {
            synchronized (MmfRemoteManager.class) {
                if (context == null) {
                    throw new MmfRemoteException(MmfRemoteException.Code.CONTEXT_NEEDED, "A context needs to be set to build the object");
                }

                MmfRemoteManager newInstance = new MmfRemoteManager(this);

                return newInstance;
            }
        }
    }


    // TODO do we want to check for each package and send an event when a package is installed? R.Pina 20140528
//    private class PackageChangeReceiver extends BroadcastReceiver {
//        @Override
//        public void onReceive(Context ctx, Intent intent) {
//            Log.d(TAG, "PackageChangeReceiver ++ onReceive ++ action = " + intent.getAction());
//
//            mPackageInfo = checkMmfAppIsInstalled();
//
//            if (mAppInstalled) {
//                Log.d(TAG, "PackageChangeReceiver ++ onReceive ++ app installed");
//                if (mPackageInfo != null && mPackageInfo.versionCode < 3000000) {
//                    Log.d(TAG, "PackageChangeReceiver ++ onReceive ++ old app version " + mPackageInfo.versionCode);
//                    stopSelf();
//                } else {
//                    Log.d(TAG, "PackageChangeReceiver ++ onReceive ++ new app version " + mPackageInfo.versionCode);
//                    mSamsungGearManager.sendCommandToWatch(APP_INSTALLED);
//                    mSamsungGearManager.onConnectionTried();
//                }
//            } else {
//                Log.d(TAG, "PackageChangeReceiver ++ onReceive ++ app NOT installed");
//                mSamsungGearManager.sendCommandToWatch(APP_NOT_INSTALLED);
//                mSamsungGearManager.onConnectionClosed();
//            }
//        }
//    }
}
