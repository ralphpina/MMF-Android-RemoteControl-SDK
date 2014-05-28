package com.mapmyfitness.android.mmfremoteappexample.app.events;

/**
 * Created by ralph.pina on 5/28/14.
 */
public class CalorieEvent {

    private Integer mCalories;

    public CalorieEvent(Integer calories) {
        mCalories = calories;
    }

    public Integer getCalories() {
        return mCalories;
    }
}
