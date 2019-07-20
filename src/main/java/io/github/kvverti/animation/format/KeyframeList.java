package io.github.kvverti.animation.format;

import java.util.Arrays;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Keyframes for a single part's animation. Instances of this class should be
 * constructed on resource reload and shared among all parts that use the
 * keyframes.
 */
public final class KeyframeList {

    /**
     * An array of keyframe times, where each element represents ticks since
     * the start of the animation.
     */
    private final float[] times;

    /**
     * An array of keyframe values. Each triplet of values represents a
     * transformation.
     */
    private final float[] data;

    KeyframeList(float[] times, float[] data) {
        checkNotNull(times, "times must be nonnull");
        checkNotNull(data, "data must be nonnull");
        checkArgument(times.length != 0, "time must be nonempty");
        checkArgument(times.length * 3 == data.length, "times and data must be parallel");
        checkArgument(times[0] == 0.0f, "Keyframes must start at 0.0f");
        this.times = times;
        this.data  = data;
    }

    /**
     * Calculates the adjacent keyframes to the given time, and places the data
     * into the parameters `floor` and `ceil`. Returns the fraction of `time`
     * relative to the two keyframes. This method does not check arguments, as
     * it is expected to be called many times on each render tick.
     *
     * @param time a nonnegative int representing the animation time
     * @param floor a float[3] to hold the lower keyframe
     * @param ceil a float[3] to hold the upper keyframe
     */
    public float getData(float time, float[] floor, float[] ceil) {
        int idx = Arrays.binarySearch(times, time);
        if(idx < 0) {
            idx = -idx - 2; // get the lower index
        }
        if(idx >= times.length - 1) {
            idx = times.length - 2;
            time = times[idx + 1];
        }
        int flr = idx;
        int cil = idx + 1;
        System.arraycopy(data, 3 * flr, floor, 0, 3);
        System.arraycopy(data, 3 * cil, ceil, 0, 3);
        return (time - times[flr]) / (times[cil] - times[flr]);
    }
}
