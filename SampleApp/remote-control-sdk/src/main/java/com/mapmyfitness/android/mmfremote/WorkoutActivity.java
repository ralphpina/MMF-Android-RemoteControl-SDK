package com.mapmyfitness.android.mmfremote;

/**
 * This enum describes basic Workout Activities that the user can select. A remote device can use these
 * to see what the user selected and make a change to the activity that will be recorded.
 *
 * Please note this is not an exhaustive list of the Workout Activities that the MapMyFitness Platform offers.
 * It is only meant to be a subset. If the user has selected a Workout Activity in their phone that does
 * not correspond to one of these, the one selected by the user will be used in the recording.
 *
 * <ul>
 *     <li>Run</li>
 *     <li>Walk</li>
 *     <li>Bike</li>
 *     <li>Gym</li>
 *     <li>Other</li>
 * </ul>
 */
public enum WorkoutActivity {
    RUN(16, "Run"),
    WALK(9, "Walk"),
    RIDE(36, "Bike"),
    GYM(12, "Gym"),
    OTHER(-1, "Other");

    private final int mWorkoutActivityId;
    private final String mWorkoutActivityName;

    private WorkoutActivity(int workoutActivityId, String workoutActivityName) {
        mWorkoutActivityId = workoutActivityId;
        mWorkoutActivityName = workoutActivityName;
    }

    /**
     * Get the Workout Activity ID. These correspond to the IDs enumerated in the MapMyApi docs.
     *
     * @return the Workout Activity ID currently set.
     */
    public int getWorkoutActivityId() {
        return mWorkoutActivityId;
    }

    /**
     * Get the name of the Workout Activity currently selected.
     *
     * @return name of Workout Activity selected.
     */
    public String getWorkoutActivityName() {
        return mWorkoutActivityName;
    }
}
