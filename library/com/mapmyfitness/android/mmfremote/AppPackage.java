package com.mapmyfitness.android.mmfremote;

/**
 * This enum describes the MMF apps that this SDK supports. For this version they include:
 * <ul>
 *     <li>MapMyRun</li>
 *     <li>MapMyRun+</li>
 *     <li>MapMyRide</li>
 *     <li>MapMyRide+</li>
 *     <li>MapMyWalk</li>
 *     <li>MapMyWalk+</li>
 *     <li>MapMyHike</li>
 *     <li>MapMyHike+</li>
 *     <li>MapMyFitness</li>
 *     <li>MapMyFitness+</li>
 * </ul>
 */
public enum AppPackage {
    MAPMYRUN("com.mapmyrun.android2", "com.mapmyrun.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE"),
    MAPMYRUNPLUS("com.mapmyrunplus.android2", "com.mapmyrunplus.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE"),
    MAPMYRIDE("com.mapmyride.android2", "com.mapmyride.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE"),
    MAPMYRIDEPLUS("com.mapmyrideplus.android2", "com.mapmyrideplus.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE"),
    MAPMYWALK("com.mapmywalk.android2", "com.mapmywalk.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE"),
    MAPMYWALKPLUS("com.mapmywalkplus.android2", "com.mapmywalkplus.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE"),
    MAPMYFITNESS("com.mapmyfitness.android2", "com.mapmyfitness.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE"),
    MAPMYFITNESSPLUS("com.mapmyfitnessplus.android2", "com.mapmyfitnessplus.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE"),
    MAPMYHIKE("com.mapmyhike.android2", "com.mapmyhike.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE"),
    MAPMYHIKEPLUS("com.mapmyhikeplus.android2", "com.mapmyhikeplus.android.remote.MAP_MY_FITNESS_REMOTE_SERVICE");

    private final String mPackageName;
    private final String mIntentActionFilter;

    private AppPackage(String packageName, String intentActionFilter) {
        mPackageName = packageName;
        mIntentActionFilter = intentActionFilter;
    }

    /**
     * Checks whether this app equals another
     *
     * @param otherPackage the other {@link AppPackage} being compared
     * @return true if they are the same app, false otherwise
     */
    public boolean equalsPackage(String otherPackage){
        return (otherPackage == null) ? false : mPackageName.equals(otherPackage);
    }

    /**
     * Returns the package name of the app
     *
     * @return package name of the app
     */
    public String toString(){
        return mPackageName;
    }

    protected String getIntentActionFilter() {
        return mIntentActionFilter;
    }
}