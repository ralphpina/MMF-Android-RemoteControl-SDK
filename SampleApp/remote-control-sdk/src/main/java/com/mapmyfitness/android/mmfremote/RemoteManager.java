package com.mapmyfitness.android.mmfremote;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;

/**
 * This is the main entry point for the SDK. An instance of this class will be built using {@link com.mapmyfitness.android.mmfremote.RemoteManager.Builder}.
 * You can use this SDK to maintain multiple connections to multiple apps, or multiple connections to one app. You can use one
 * instance of this class for each of those connections.
 *
 * This SDK uses your app's {@link android.content.Context} to bind to an exposed {@link android.app.Service}
 * in the MMF apps. Each of our apps has a unique IntentFilter action name to trigger the connection. The filters are
 * held in the {@link com.mapmyfitness.android.mmfremote.AppPackage} enum.
 *
 * Distributed with these docs should be a sample client app that shows how to use this SDK to connect to our apps.
 *
 * To build it:
 * <pre>
 *     {@code
 *
 *          RemoteManager.Builder remoteControlBuilder = RemoteManager.getBuilder();
 *          remoteControlBuilder.setContext(this);
 *          remoteControlBuilder.setStatsCache(mMmfStatsCache);
 *          remoteControlBuilder.setDataListener(this);
 *          remoteControlBuilder.setCommandListener(this);
 *          remoteControlBuilder.setAppPackage(AppPackage.MAPMYRUN);
 *
 *          try {
 *              mMmfRemoteManager = remoteControlBuilder.build();
 *          } catch (RemoteException e) {
 *              Log.e(TAG, e.getMessage(), e);
 *          }
 *      }
 * </pre>
 *
 * After building the object you can use it connect it to an MMF app, or get the state if the app
 * is already connected:
 *
 * <pre>
 *     {@code
 *
 *          if (!mMmfRemoteManager.isAppConnected()) {
 *              updateUi(AppState.APP_NOT_CONNECTED);
 *              connectApp();
 *          } else {
 *              mMmfRemoteManager.requestAppState();
 *          }
 *     }
 * </pre>
 *
 * You can also send commands like this:
 *
 * <pre>
 *     {@code
 *
 *          try {
 *              mMmfRemoteManager.startWorkoutCommand();
 *          } catch (RemoteException e) {
 *              if (e.getCode() == RemoteException.Code.APP_NOT_CONNECTED) {
 *                  // Do something on the UI
 *                  connectApp();
 *              }
 *          }
 *     }
 * </pre>
 *
 *
 */
public class RemoteManager {

    public static final String TAG = "MmfRemComLogger";
    public static final boolean DETAIL_LOG = true; // default is false

    private Context mContext;
    private RemoteCommunication mRemoteCommunication;
    private AppPackage mAppPackage;

    private RemoteManager(Builder init) {
        mRemoteCommunication = new RemoteCommunication();

        this.mContext = init.context;
        if (init.context != null) {
            mContext = init.context;
        }
        if (init.appPackage != null) {
            mAppPackage = init.appPackage;
        }
        mRemoteCommunication.setDataListener(init.dataListener);
        mRemoteCommunication.setCommandListener(init.commandListener);
        if (init.statsCache != null) {
            mRemoteCommunication.setStatsCache(init.statsCache);
        }
    }

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
        return mRemoteCommunication.REMOTE_CONTROL_SDK_VERSION_NUMBER;
    }

    /**
     * Try to connect to the MMF app by passing it an {@link com.mapmyfitness.android.mmfremote.AppPackage}
     * This method will set the package and call {@link #tryToConnectToMmfApp()}
     *
     * @return true if it is already connected or the connection was successful, false if connection failed
     */
    public boolean tryToConnectToMmfApp(AppPackage appPackage) {
        mAppPackage = appPackage;
        return tryToConnectToMmfApp();
    }

    /**
     * Try to connect to the MMF app in the currently assigned {@link com.mapmyfitness.android.mmfremote.AppPackage}. If this enum
     * is null, this method will simply return false. You can use {@link #tryToConnectToMmfApp(com.mapmyfitness.android.mmfremote.AppPackage)}
     * if you would like to pass it one at the time you try to connect, or use {@link #getAppPackage()} to check
     * if one is present.
     * @return
     */
    public boolean tryToConnectToMmfApp() {
        if (!isAppConnected() && mAppPackage != null) {
            return mRemoteCommunication.onConnectionTried(mContext, mAppPackage.getIntentActionFilter());
        } else if (mAppPackage == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Get the {@link com.mapmyfitness.android.mmfremote.AppPackage} for the currently connected MMF app.
     *
     * @return app package
     */
    public AppPackage getAppPackage() {
        return mAppPackage;
    }

    /**
     * Set the {@link com.mapmyfitness.android.mmfremote.AppPackage} you would like to use to connect to an MMF app.
     *
     * @param appPackage app package to connect to
     */
    public void setAppPackage(AppPackage appPackage) {
        mAppPackage = appPackage;
    }

    /**
     * Set a {@link StatsCache} to use during the session. This can be null and no data will be cached
     * automatically. You can also set it during the building of this class.
     *
     * @param statsCache set a cache
     */
    public void setStatsCache(StatsCache statsCache) {
        mRemoteCommunication.setStatsCache(statsCache);
    }

    /**
     * Get the @{link StatsCache}, this can return null if none is set.
     *
     * @return return the cache
     */
    public StatsCache getStatsCache() {
        return mRemoteCommunication.getStatsCache();
    }

    /**
     * Set the {@link com.mapmyfitness.android.mmfremote.RemoteDataListener} to receive callbacks for data changes
     * during the lifetime of a workout. This can be set to null, in which case you will
     * not be notified when the changes happen.
     *
     * @param remoteDataListener the listener to receive the data callbacks
     */
    public void setDataListener(RemoteDataListener remoteDataListener) {
        mRemoteCommunication.setDataListener(remoteDataListener);
    }

    /**
     * Set the {@link com.mapmyfitness.android.mmfremote.RemoteCommandListener} to receive callbacks for commands from
     * the phone. Whenever you send a command to the MMF app, it will take the appropriate action
     * and send the command back. That is how you can be sure that the MMF app responded property.
     *
     * For example, if you use the {@link #saveWorkoutCommand()} it will send it to th phone. The
     * MMF app on the phone will try to start, it it is successful it will send a callback to
     * {@link com.mapmyfitness.android.mmfremote.RemoteCommandListener#onStartWorkoutEvent(Boolean, Boolean, Boolean, Boolean) onStartWorkoutEvent(Boolean metric, Boolean hasHeartRate, Boolean calculatesCalories, Boolean isSpeed);}
     *
     * This can be null, but then you won't be notified of events from the phone.
     *
     * @param remoteCommandListener the listener to receive the data callbacks
     */
    public void setCommandListener(RemoteCommandListener remoteCommandListener) {
        mRemoteCommunication.setCommandListener(remoteCommandListener);
    }

    /**
     * Disconnect from the current MMF app. If the app is not currently connected
     * this method does nothing. You can check if the app is connected by calling
     * {@link #isAppConnected()};
     */
    public void disconnectFromMmfApp() {
        if (isAppConnected()) {
            mRemoteCommunication.onConnectionClosed(mContext);
        }
        requestAppState();
    }

    /**
     * Tell the MMF app to start a workout. This method will check if
     * the app is connected. If it is not it will throw a {@link com.mapmyfitness.android.mmfremote.RemoteException} with
     * {@link com.mapmyfitness.android.mmfremote.RemoteException.Code#APP_NOT_CONNECTED}
     *
     * @throws com.mapmyfitness.android.mmfremote.RemoteException if app is not connected.
     */
    public void startWorkoutCommand() throws RemoteException {
        checkIsBound();
        mRemoteCommunication.startCommand();
    }

    /**
     * Tell the MMF app to pause a workout. This method will check if
     * the app is connected. If it is not it will throw a {@link com.mapmyfitness.android.mmfremote.RemoteException} with
     * {@link com.mapmyfitness.android.mmfremote.RemoteException.Code#APP_NOT_CONNECTED}
     *
     * @throws com.mapmyfitness.android.mmfremote.RemoteException if app is not connected.
     */
    public void pauseWorkoutCommand() throws RemoteException {
        checkIsBound();
        mRemoteCommunication.pauseCommand();
    }

    /**
     * Tell the MMF app to resume a workout. This method will check if
     * the app is connected. If it is not it will throw a {@link com.mapmyfitness.android.mmfremote.RemoteException} with
     * {@link com.mapmyfitness.android.mmfremote.RemoteException.Code#APP_NOT_CONNECTED}
     *
     * @throws com.mapmyfitness.android.mmfremote.RemoteException if app is not connected.
     */
    public void resumeWorkoutCommand() throws RemoteException {
        checkIsBound();
        mRemoteCommunication.resumeCommand();
    }

    /**
     * Tell the MMF app to stop a workout. This method will check if
     * the app is connected. If it is not it will throw a {@link com.mapmyfitness.android.mmfremote.RemoteException} with
     * {@link com.mapmyfitness.android.mmfremote.RemoteException.Code#APP_NOT_CONNECTED}
     *
     * @throws com.mapmyfitness.android.mmfremote.RemoteException if app is not connected.
     */
    public void stopWorkoutCommand() throws RemoteException {
        checkIsBound();
        mRemoteCommunication.stopCommand();
    }

    /**
     * Tell the MMF app to discard a workout. This will erase all data from the workout
     * that was recorded permanently. If user triggers this method you may
     * want to have him confirm his action. This method will check if
     * the app is connected. If it is not it will throw a {@link com.mapmyfitness.android.mmfremote.RemoteException} with
     * {@link com.mapmyfitness.android.mmfremote.RemoteException.Code#APP_NOT_CONNECTED}
     *
     * @throws com.mapmyfitness.android.mmfremote.RemoteException if app is not connected.
     */
    public void discardWorkoutCommand() throws RemoteException {
        checkIsBound();
        mRemoteCommunication.discardCommand();
    }

    /**
     * Tell the MMF app to save a workout. Saving means this workout will be saved to our servers
     * under the account the user is logged in the MMF app. If the phone
     * currently does not have a data connection, the workout will be saved locally
     * to the phone and the MMF app will try to upload it later. This method will check if
     * the app is connected. If it is not it will throw a {@link com.mapmyfitness.android.mmfremote.RemoteException} with
     * {@link com.mapmyfitness.android.mmfremote.RemoteException.Code#APP_NOT_CONNECTED}
     *
     * @throws com.mapmyfitness.android.mmfremote.RemoteException if app is not connected.
     */
    public void saveWorkoutCommand() throws RemoteException {
        checkIsBound();
        mRemoteCommunication.saveCommand();
    }

    /**
     * Tell the MMF app to start a workout, whether or not a GPS fix is available.
     * This method will check if
     * the app is connected. If it is not it will throw a {@link com.mapmyfitness.android.mmfremote.RemoteException} with
     * {@link com.mapmyfitness.android.mmfremote.RemoteException.Code#APP_NOT_CONNECTED}
     *
     * @throws com.mapmyfitness.android.mmfremote.RemoteException if app is not connected.
     */
    public void startWithoutGpsCommand() throws RemoteException {
        checkIsBound();
        mRemoteCommunication.startWithoutGpsCommand();
    }

    /**
     * Tell the MMF app to cancel a workout start. This may happen if there was no GPS and the
     * app is waiting for the user to confirm that he wants to start the workout withou it
     * . This method will check if the app is connected. If it is not it will throw a {@link com.mapmyfitness.android.mmfremote.RemoteException} with
     * {@link com.mapmyfitness.android.mmfremote.RemoteException.Code#APP_NOT_CONNECTED}
     *
     * @throws com.mapmyfitness.android.mmfremote.RemoteException if app is not connected.
     */
    public void cancelWorkoutStart() throws RemoteException {
        checkIsBound();
        mRemoteCommunication.cancelWorkoutStartCommand();
    }

    /**
     * This method is a bit dangerous and it makes some assumptions. Ideally, if we are
     * connected to an MMF app we would just ask it for the app state and get that state
     * in the {@link com.mapmyfitness.android.mmfremote.RemoteCommandListener#onAppStateEvent(com.mapmyfitness.android.mmfremote.AppState)} callback. However,
     * if we are not connected to an MMF app we can't be 100% sure which MMF app you intend to check for
     * if the {@link com.mapmyfitness.android.mmfremote.AppPackage} is not set in this class. Lastly, if no {@link com.mapmyfitness.android.mmfremote.RemoteCommandListener} is set,
     * you will not receieve a callback. In that case the method will appear to fail silently.
     * These are steps we take:
     *
     * <ol>
     *    <li>If MMF app is connected to SDK: ask it the current state and listen for it in the {@link com.mapmyfitness.android.mmfremote.RemoteCommandListener#onAppStateEvent(com.mapmyfitness.android.mmfremote.AppState)} callback.
     *    Remember if no callback is set, you will not get any feedback about the state of the app.</li>
     *    <li>If we have an {@link com.mapmyfitness.android.mmfremote.AppPackage} set, then check it to see if app is installed.
     *    If the app is installed we will send a callback with {@link com.mapmyfitness.android.mmfremote.AppState#APP_NOT_CONNECTED}.
     *    If the app is not installed we will send a callback with {@link com.mapmyfitness.android.mmfremote.AppState#APP_NOT_INSTALLED}.</li>
     *    <li>If the {@link com.mapmyfitness.android.mmfremote.AppPackage} is not set, we are not sure which one of the 10 apps you are
     *    requesting. In that case, we will use {@link #findInstalledApps()} to see if any of our supported
     *    apps are installed. If at least one of them is installed we return {@link com.mapmyfitness.android.mmfremote.AppState#APP_NOT_CONNECTED}.
     *    If none of our supported are installed, we return {@link com.mapmyfitness.android.mmfremote.AppState#APP_NOT_INSTALLED}.</li>
     * </ol>
     *
     * A better way to get an app's state is to use {@link #requestAppState(com.mapmyfitness.android.mmfremote.AppPackage)} passing it the
     * {@link com.mapmyfitness.android.mmfremote.AppPackage} you are interested in. That will be more precise by narrowing down the options to one app.
     */
    public void requestAppState() {
        if (isAppConnected()) {
            mRemoteCommunication.getCurrentStateCommand();
        } else if (mAppPackage != null) {
            AppInfo appInfo = getAppInfo(mAppPackage);
            if (appInfo.isInstalled()) {
                if (mRemoteCommunication.getCommandListener() != null) {
                    mRemoteCommunication.getCommandListener().onAppStateEvent(AppState.APP_NOT_CONNECTED);
                }
            } else {
                if (mRemoteCommunication.getCommandListener() != null) {
                    mRemoteCommunication.getCommandListener().onAppStateEvent(AppState.APP_NOT_INSTALLED);
                }
            }
        } else {
            if (findInstalledApps().size() > 0) {
                if (mRemoteCommunication.getCommandListener() != null) {
                    mRemoteCommunication.getCommandListener().onAppStateEvent(AppState.APP_NOT_CONNECTED);
                }
            } else {
                if (mRemoteCommunication.getCommandListener() != null) {
                    mRemoteCommunication.getCommandListener().onAppStateEvent(AppState.APP_NOT_INSTALLED);
                }
            }
        }
    }

    /**
     * This method tries to check the state of an app represented by an {@link com.mapmyfitness.android.mmfremote.AppPackage}. Remember, like
     * {@link #requestAppState()}, it no {@link com.mapmyfitness.android.mmfremote.RemoteCommandListener} is set, you will not receive any callbacks
     * on the state of the app. These are the steps taken in this method:
     *
     * <ol>
     *     <li>If the app is connected, we check if the {@link com.mapmyfitness.android.mmfremote.AppPackage} in the parameter is equal to the one set
     *     in this class. In that case, we ask the MMF app to send us the state. If the package in the parameter is different
     *     than the one held by this page, we check to see if it is installed or just not connected.</li>
     *     <li>If the app is not connected, we check if the package is installed, if so, we return {@link com.mapmyfitness.android.mmfremote.AppState#APP_NOT_CONNECTED}</li>
     *     <li>If none of the above qualify, we return {@link com.mapmyfitness.android.mmfremote.AppState#APP_NOT_INSTALLED}</li>
     * </ol>
     *
     * You can also use the {@link #getAppInfo(com.mapmyfitness.android.mmfremote.AppPackage)} method and the returned {@link com.mapmyfitness.android.mmfremote.AppInfo} object
     * to see if the app is installed or not.
     *
     * @param appPackage
     */
    public void requestAppState(AppPackage appPackage) {
        if (isAppConnected()) {
            if (mAppPackage != null && mAppPackage.equals(appPackage)) {
                mRemoteCommunication.getCurrentStateCommand();
            } else if (mAppPackage != null && getAppInfo(appPackage).isInstalled()) {
                if (mRemoteCommunication.getCommandListener() != null) {
                    mRemoteCommunication.getCommandListener().onAppStateEvent(AppState.APP_NOT_CONNECTED);
                }
            } else {
                if (mRemoteCommunication.getCommandListener() != null) {
                    mRemoteCommunication.getCommandListener().onAppStateEvent(AppState.APP_NOT_INSTALLED);
                }
            }
        } else if (getAppInfo(appPackage).isInstalled()) {
            if (mRemoteCommunication.getCommandListener() != null) {
                mRemoteCommunication.getCommandListener().onAppStateEvent(AppState.APP_NOT_CONNECTED);
            }
        } else {
            if (mRemoteCommunication.getCommandListener() != null) {
                mRemoteCommunication.getCommandListener().onAppStateEvent(AppState.APP_NOT_INSTALLED);
            }
        }
    }

    /**
     * private: it checks if we are connected to the MMF app. If not, we throw a
     * {@link com.mapmyfitness.android.mmfremote.RemoteException} with {@link com.mapmyfitness.android.mmfremote.RemoteException.Code#APP_NOT_CONNECTED}
     *
     * @throws com.mapmyfitness.android.mmfremote.RemoteException exception if app is not connected
     */
    private void checkIsBound() throws RemoteException {
        if (!isAppConnected()) {
            throw new RemoteException(RemoteException.Code.APP_NOT_CONNECTED, "App is not connected to an MMF app");
        }
    }

    /**
     * Check if app is currently connected to one of the MMF apps.
     * You can then use {@link #getAppPackage()} to check which app it is.
     *
     * @return whether this is bound to an MMF's app service
     */
    public boolean isAppConnected() {
        return mRemoteCommunication.isBound();
    }

    /**
     * Get a list of all MMF apps installed. This will return an empty {@link java.util.ArrayList<  com.mapmyfitness.android.mmfremote.AppInfo >} object.
     * Then you would know non of our supported apps are installed in the phone.
     *
     * @return array of type {@link com.mapmyfitness.android.mmfremote.AppInfo} with info on apps installed
     */
    public ArrayList<AppInfo> findInstalledApps() {
        AppInfo appInfo;
        ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();

        // try each of the 10 apps
        appInfo = getAppInfo(AppPackage.MAPMYFITNESS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(AppPackage.MAPMYFITNESSPLUS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(AppPackage.MAPMYRUN);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(AppPackage.MAPMYRUNPLUS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(AppPackage.MAPMYRIDE);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(AppPackage.MAPMYRIDEPLUS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(AppPackage.MAPMYHIKE);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(AppPackage.MAPMYHIKEPLUS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(AppPackage.MAPMYWALK);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        appInfo = getAppInfo(AppPackage.MAPMYWALKPLUS);
        if (appInfo.isInstalled()) {
            appInfoList.add(appInfo);
        }

        return appInfoList;
    }

    /**
     * Get the {@link com.mapmyfitness.android.mmfremote.AppInfo} for the app package passed.
     *
     * @param appPackage the app package which you want to check
     *
     * @return and {@link com.mapmyfitness.android.mmfremote.AppInfo} with info on the app
     */
    public AppInfo getAppInfo(AppPackage appPackage) {
        PackageManager pm = mContext.getPackageManager();
        AppInfo appInfo;

        try {
            PackageInfo pi = pm.getPackageInfo(appPackage.toString(), PackageManager.GET_ACTIVITIES);
            appInfo = new AppInfo(appPackage, true, pi.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            // app is not install
            appInfo = new AppInfo(appPackage, false, 0);
        }

        return appInfo;
    }

    public void requestWorkoutActivity() {
        mRemoteCommunication.getCurrentWorkoutActivityCommand();
    }

    public void setWorkoutActivity(WorkoutActivity workoutActivity) {
        mRemoteCommunication.setWorkoutActivityCommand(workoutActivity);
    }

    public void sendHeartRateDataBPM(int heartRateBPM) {
        mRemoteCommunication.sendHeartrateData(heartRateBPM);
    }

    /**
     * Get the {@link com.mapmyfitness.android.mmfremote.RemoteManager.Builder} to create this class.
     *
     * @return a builder object
     */
    public static Builder getBuilder() {
        return new Builder();
    }

    /**
     * This builder is used to create this class with the desired parameters. The only that is
     * required is a {@link android.content.Context} so we can try to bind to the app and retrieve
     * information about the packages installed in the phone. Below is an example of how it can be used.
     *
     * <pre>
     *     {@code
     *
     *          RemoteManager.Builder remoteControlBuilder = RemoteManager.getBuilder();
     *          remoteControlBuilder.setContext(this);
     *          remoteControlBuilder.setStatsCache(mMmfStatsCache);
     *          remoteControlBuilder.setDataListener(this);
     *          remoteControlBuilder.setCommandListener(this);
     *          remoteControlBuilder.setAppPackage(AppPackage.MAPMYRUN);
     *
     *          try {
     *              mMmfRemoteManager = remoteControlBuilder.build();
     *          } catch (RemoteException e) {
     *              Log.e(TAG, e.getMessage(), e);
     *          }
     *      }
     * </pre>
     */
    public static final class Builder {
        Context context;
        AppPackage appPackage;
        RemoteDataListener dataListener;
        RemoteCommandListener commandListener;
        StatsCache statsCache;

        private Builder() {
        }

        /**
         * Set the {@link android.content.Context} for the Activity, Service, or Application you
         * wish to bing to the MMF apps. This is REQUIRED, otherwise the {@link #build()} method will throw
         * a {@link com.mapmyfitness.android.mmfremote.RemoteException}.
         *
         * @param context
         * @return A Builder object
         */
        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        /**
         * Set the app package you are interested in connecting to.
         *
         * @param appPackage
         * @return the Builder
         */
        public Builder setAppPackage(AppPackage appPackage) {
            this.appPackage = appPackage;
            return this;
        }

        /**
         * Set the {@link com.mapmyfitness.android.mmfremote.RemoteDataListener} in which you want to receive callbacks about data.
         *
         * @param dataListener
         * @return the Builder
         */
        public Builder setDataListener(RemoteDataListener dataListener) {
            this.dataListener = dataListener;
            return this;
        }

        /**
         * Set the {@link com.mapmyfitness.android.mmfremote.RemoteCommandListener} in which you want to receive callbacks about app commands and state.
         *
         * @param commandListener
         * @return the Builder
         */
        public Builder setCommandListener(RemoteCommandListener commandListener) {
            this.commandListener = commandListener;
            return this;
        }

        /**
         * Set the app cache you would like to use for the data.
         *
         * @param statsCache
         * @return the Builder
         */
        public Builder setStatsCache(StatsCache statsCache) {
            this.statsCache = statsCache;
            return this;
        }

        /**
         * Build this object.
         *
         * @return a {@link com.mapmyfitness.android.mmfremote.RemoteManager} object.
         * @throws com.mapmyfitness.android.mmfremote.RemoteException if {@link android.content.Context} was not set.
         */
        public RemoteManager build() throws RemoteException {
            synchronized (RemoteManager.class) {
                if (context == null) {
                    throw new RemoteException(RemoteException.Code.CONTEXT_NEEDED, "A context needs to be set to build the object");
                }

                RemoteManager newInstance = new RemoteManager(this);

                return newInstance;
            }
        }
    }
}
