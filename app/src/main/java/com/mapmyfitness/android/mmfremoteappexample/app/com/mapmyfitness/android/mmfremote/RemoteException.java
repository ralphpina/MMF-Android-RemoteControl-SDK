package com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote;

/**
 * This exception is thrown for 3 reasons. The reason can be accessed via the {@link #getCode()}
 * method. These are:
 * <ul>
 *     <li>The app is not connected, and you called a method that requires it.</li>
 *     <li>The app is connected, and you called a method that requires it be disconnected.</li>
 *     <li>If you try to build the {@link RemoteManager} without passing it a {@link android.content.Context}</li>
 * </ul>
 *
 * Methods that throw this are documented.
 */
public class RemoteException extends Exception {
    /**
     * Enum specifing why the exception was thrown.
     */
    public enum Code {
        /**
         * App is not connected and method requires the app be connected
         */
        APP_NOT_CONNECTED,
        /**
         * App is connected and method requires that it be not
         */
        APP_CONNECTED,
        /**
         * We need a context to bind the MMF apps
         */
        CONTEXT_NEEDED,
    };

    private final Code code;

    public RemoteException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}