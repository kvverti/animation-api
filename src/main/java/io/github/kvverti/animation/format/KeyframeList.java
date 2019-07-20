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
     * An array of keyframe values. The keyframe data are stored in parallel
     * form, i.e. all of the x values followed by all of the y values, etc.
     */
    private final float[] data;

    KeyframeList(float[] times, float[] data) {
        checkNotNull(times, "times must be nonnull");
        checkNotNull(data, "data must be nonnull");
        checkArgument(times.length != 0, "time must be nonempty");
        checkArgument(data.length % times.length == 0, "times and data must be parallel");
        checkArgument(times[0] == 0.0f, "Keyframes must start at 0.0f");
        this.times = times;
        this.data  = data;
    }

    /**
     * Calculates the adjacent keyframes to the given time, and returns the
     * fractional keyframe index. It is expected that this method will
     * be called many times on each render tick.
     *
     * @param time a nonnegative float representing the animation time
     * @param dst a view into which a neighborhood of keyframe data will
     *     be put.
     */
    public void getData(float time, DataView dst) {
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
        dst.assign(times.length, data, idx, (time - times[flr]) / (times[cil] - times[flr]));
    }
}
