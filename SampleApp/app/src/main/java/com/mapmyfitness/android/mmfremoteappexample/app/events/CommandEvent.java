package com.mapmyfitness.android.mmfremoteappexample.app.events;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class CommandEvent {

    private String mCommand;

    public CommandEvent(String command) {
        mCommand = command;
    }

    public String getCommand() {
        return mCommand;
    }
}
