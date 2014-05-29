package com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote;

/**
 * This object describes properties of MMF apps this SDK will connect with.
 * The object contains an {@link AppPackage} enum that contain the package name, a boolean
 * indicating whether the app is installed, and the version number. They can
 * be used with the Android SDK to get information like the app name
 * and the version number. This object can be retrieved for each of the supported apps
 * listed in {@link AppPackage} using {@link RemoteManager#getAppInfo(AppPackage)}
 */
public class AppInfo {

    private AppPackage mPackage;
    private boolean mIsInstalled;
    private int mVersionNumber;

    public AppInfo(AppPackage aPackage, boolean isInstalled, int versionNumber) {
        mPackage = aPackage;
        mIsInstalled = isInstalled;
        mVersionNumber = versionNumber;
    }

    /**
     * Get the {@link AppPackage} enum representing the app that
     * this info describes;
     *
     * @return the {@link AppPackage} enum.
     */
    public AppPackage getPackage() {
        return mPackage;
    }

    /**
     * Returns whether or not the app is installed on the phone.
     *
     * @return true - the app is installed, false if it is not
     */
    public boolean isInstalled() {
        return mIsInstalled;
    }

    /**
     * Returns the version number of the app installed.
     *
     * @return an integer version number
     */
    public int getVersionNumber() {
        return mVersionNumber;
    }

    /**
     * Returns the package name of the app
     *
     * @return package name
     */
    @Override
    public String toString() {
        return mPackage.toString();
    }
}
