package com.mapmyfitness.android.mmfremoteappexample.app.com.mapmyfitness.android.mmfremote;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class MmfRemoteException extends Exception {
    public enum Code {
        APP_NOT_CONNECTED,
        APP_CONNECTED,
        CONTEXT_NEEDED,
    };

    private final Code code;

    public MmfRemoteException(Code code, String message) {
        super(message);
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}