package com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote;

import android.content.Context;

/**
 * Created by ralph.pina on 5/27/14.
 */
public class MmfRemoteManager {

    private static final String TAG = "MmfRemComLogger";

    private final static String VERSION_NUMBER = "0100000"; //xx major xx minor xxx patch
    private final static String MMF_REMOTE_FILTER_ACTION = "com.mapmyfitness.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE";

    private Context mContext;

    // TODO do we want to check for each package and send an event when a package is installed? R.Pina 20140528
//    private boolean mAppInstalled = false;
    // used to check whether one of the MMF apps is installed
//    private PackageInfo mPackageInfo;
    // BroadcastReceiver to get notification when app is installed
//    private PackageChangeReceiver mPackageChangeReceiver;

    private MmfRemoteCommunication mMmfRemoteCommunication;

    // singleton
    private static MmfRemoteManager sMmfRemoteManager;
    private MmfRemoteManager(Context context) {
        mContext = context;

        mMmfRemoteCommunication = new MmfRemoteCommunication();

        // TODO do we want to check for each package and send an event when a package is installed? R.Pina 20140528
        // register a receiver to learn when our companion app
        // is installed or uninstalled
//        mPackageChangeReceiver = new PackageChangeReceiver();
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
//        filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
//        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
//        filter.addDataScheme("package");
//        mContext.registerReceiver(mPackageChangeReceiver, filter);
    }

    public static MmfRemoteManager getInstance(Context context) {
        if (sMmfRemoteManager == null) {
            sMmfRemoteManager = new MmfRemoteManager(context);
        }
        return sMmfRemoteManager;
    }

    // TODO do we want to check for each package and send an event when a package is installed? R.Pina 20140528
//    private PackageInfo checkMmfAppIsInstalled() {
//        PackageManager pm = mContext.getPackageManager();
//        PackageInfo pi = null;
//
//        try {
//            String companionApp = getString(R.string.companionAppPackage);
//            Log.d(TAG, "SamsungGearProviderImpl ++ checkAppIsInstalled ++ companionApp = " + companionApp);
//            pi = pm.getPackageInfo(companionApp, PackageManager.GET_ACTIVITIES);
//            mAppInstalled = true;
//        } catch (PackageManager.NameNotFoundException e) {
//            // app is not install
//            mAppInstalled = false;
//            Log.d(TAG, "SamsungGearProviderImpl ++ checkAppIsInstalled ++ app is not installed");
//        }
//
//        return pi;
//    }

    public void connectToMmfApp() {
//        if (mmfRemoteCommandListener == null) {
//            throw new IllegalArgumentException("Required MmfRemoteCommandListener missing");
//        }
//        if (mmfRemoteDataListener == null) {
//            throw new IllegalArgumentException("Required MmfRemoteDataListener missing");
//        }
//        Log.e(TAG, "mMmfRemoteCommunication.isBound() = " + mMmfRemoteCommunication.isBound());
        if (mMmfRemoteCommunication.isBound()) {
            throw new IllegalStateException("App is already connected to an MMF app, call {@link #disconnectFromMmfApp} before trying to connect");
        }
        // try to connect
        boolean connected = mMmfRemoteCommunication.onConnectionEstablished(mContext, MMF_REMOTE_FILTER_ACTION);
        // connection not successful, assume app is not installed
        if (!connected) {
            //mmfRemoteCommandListener.onAppStateEvent(MmfAppState.APP_NOT_INSTALLED);
        } else {
            //mMmfRemoteCommunication.sendVersionNumber(VERSION_NUMBER);
        }
    }

    private boolean tryToConnectToMmfApp() {
        return mMmfRemoteCommunication.onConnectionEstablished(mContext, MMF_REMOTE_FILTER_ACTION);
    }

    public void setDataListener(MmfRemoteDataListener mmfRemoteDataListener) {
        mMmfRemoteCommunication.setDataListener(mmfRemoteDataListener);
    }

    public void setCommandListener(MmfRemoteCommandListener mmfRemoteCommandListener) {
        mMmfRemoteCommunication.setCommandListener(mmfRemoteCommandListener);
    }

    public void disconnectFromMmfApp() {
        if (!mMmfRemoteCommunication.isBound()) {
            throw new IllegalStateException("App is not connected to an MMF app, call {@link #connectToMmfApp} before trying to disconnect");
        }
        mMmfRemoteCommunication.onConnectionClosed(mContext);
    }

    public void startWorkoutCommand() {
        if (!mMmfRemoteCommunication.isBound()) {
            throw new IllegalStateException("App is not connected to an MMF app, call {@link #connectToMmfApp} before trying to start a workout");
        }
        mMmfRemoteCommunication.startCommand();
    }

    public void pauseWorkoutCommand() {
        if (!mMmfRemoteCommunication.isBound()) {
            throw new IllegalStateException("App is not connected to an MMF app, call {@link #connectToMmfApp} before trying to pause a workout");
        }
        mMmfRemoteCommunication.pauseCommand();
    }

    public void resumeWorkoutCommand() {
        if (!mMmfRemoteCommunication.isBound()) {
            throw new IllegalStateException("App is not connected to an MMF app, call {@link #connectToMmfApp} before trying to resume a workout");
        }
        mMmfRemoteCommunication.resumeCommand();
    }

    public void stopWorkoutCommand() {
        if (!mMmfRemoteCommunication.isBound()) {
            throw new IllegalStateException("App is not connected to an MMF app, call {@link #connectToMmfApp} before trying to stop a workout");
        }
        mMmfRemoteCommunication.stopCommand();
    }

    public void discardWorkoutCommand() {
        if (!mMmfRemoteCommunication.isBound()) {
            throw new IllegalStateException("App is not connected to an MMF app, call {@link #connectToMmfApp} before trying to discard a workout");
        }
        mMmfRemoteCommunication.discardCommand();
    }

    public void saveWorkoutCommand() {
        if (!mMmfRemoteCommunication.isBound()) {
            throw new IllegalStateException("App is not connected to an MMF app, call {@link #connectToMmfApp} before trying to save a workout");
        }
        mMmfRemoteCommunication.saveCommand();
    }

    public void startWithoutGpsCommand() {
        checkIsBound();
        mMmfRemoteCommunication.startWithoutGpsCommand();
    }

    public void cancelWorkoutStartCommand() {
        checkIsBound();
        mMmfRemoteCommunication.cancelWorkoutStartCommand();
    }

    private void checkIsBound() {
        if (!mMmfRemoteCommunication.isBound()) {
            throw new IllegalStateException("App is not connected to an MMF app, call {@link #connectToMmfApp} before trying to cancel a workout start");
        }
    }

    public boolean isAppConnected() {
        return mMmfRemoteCommunication.isBound();
    }

    private enum MmfAppPackages {
        MAPMYRUN("com.mapmyrun.android2"),
        MAPMYRUNPLUS("com.mapmyrunplus.android2"),
        MAPMYRIDE("com.mapmyride.android2"),
        MAPMYRIDEPLUS("com.mapmyrideplus.android2"),
        MAPMYWALK("com.mapmywalk.android2"),
        MAPMYWALKPLUS("com.mapmywalkplus.android2"),
        MAPMYFITNESS("com.mapmyfitness.android2"),
        MAPMYFITNESSPLUS("com.mapmyfitnessplus.android2"),
        MAPMYHIKE("com.mapmyhike.android2"),
        MAPMYHIKEPLUS("com.mapmyhikeplus.android2");

        private final String mPackageName;

        private MmfAppPackages(String packageName) {
            mPackageName = packageName;
        }

        public boolean equalsPackage(String otherPackage){
            return (otherPackage == null) ? false : mPackageName.equals(otherPackage);
        }

        public String toString(){
            return mPackageName;
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
//                    mSamsungGearManager.onConnectionEstablished();
//                }
//            } else {
//                Log.d(TAG, "PackageChangeReceiver ++ onReceive ++ app NOT installed");
//                mSamsungGearManager.sendCommandToWatch(APP_NOT_INSTALLED);
//                mSamsungGearManager.onConnectionClosed();
//            }
//        }
//    }
}
