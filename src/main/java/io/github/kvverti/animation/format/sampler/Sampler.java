package io.github.kvverti.animation.format.sampler;

import io.github.kvverti.animation.format.KeyframeList;

/**
 * Samplers control interpolation over a list of keyframes
 */
public abstract class Sampler {

    /**
     * Keyframes.
     */
    private final KeyframeList keyframes;

    /**
     * Number of elements in the data array used to interpolate between
     * adjacent keyframes.
     */
    private final int dataCount;

    /**
     * Keyframe interpolation data. The structure of the data is described
     * in {@link #initData}. The contents of the data are not defined in this
     * class, but rather by subclasses.
     */
    protected final float[] data;

    /**
     * Construct a Sampler.
     *
     * @param keyframes the keyframes to sample
     * @param sampleRadius the neighboorhood of keyframes to reference
     *     when sampling
     * @param dCount the number of elements required for each keyframe
     *     in the implemented interpolation algorithm
     */
    protected Sampler(KeyframeList keyframes, int sampleRadius, int dCount) {
        this.keyframes = keyframes;
        this.dataCount = dCount;
        this.data = initData(sampleRadius);
    }

    private final float[] initData(int sampleRadius) {
        int ts = keyframes.size();
        int ds = keyframes.dataSize();
        // set up array as follows:
        // { x0l, x0r, y0l, y0r, z0l, z0r, x1l, x1r, y1l, y1r, z1l, z1r, ... }
        // where data is logically (x, y, z).
        // Index is calulcated via the formula
        //     idx = dc * (ds * timeIdx + valIdx) + dataIdx
        // In the example above, ds = 3 and dc = 2, so the right value of y1 would be
        //     2 * (3 * 1 + 1) + 1 = 9
        float[] data = new float[ts * ds * dataCount];
        float[] times = new float[sampleRadius * 2];
        float[] vals = new float[sampleRadius * 2];
        float[] dst = new float[dataCount];
        int offset = sampleRadius - 1;
        for(int i = 0; i < ts; i++) {
            for(int j = 0; j < ds; j++) {
                for(int m = 0; m < 2 * sampleRadius; m++) {
                    int n = i + m - offset;
                    if(n < 0) {
                        n = 0;
                    } if(n >= ts) {
                        n = ts - 1;
                    }
                    times[m] = keyframes.time(n);
                    vals[m] = keyframes.datum(n, j);
                }
                transform(times, vals, dst);
                int k = dataCount * (ds * i + j);
                System.arraycopy(dst, 0, data, k, dataCount);
            }
        }
        return data;
    }

    /**
     * Called in the constructor in order to transform neighborhood keyframe
     * data into a form suitable for the subclass algorithm. The default
     * implementation copies as many elements as possible from `vals` into `dst`.
     *
     * API Note: Implementations of this method must not rely on any accessible
     * fields being initialized. In particular, subclass fields will not be
     * initialized when this method is called.
     */
    protected void transform(float[] times, float[] vals, float[] dst) {
        System.arraycopy(vals, 0, dst, 0, dst.length);
    }

    /**
     * Interpolates between keyframes using the given fractional distance.
     *
     * @param partial the fraction distance between the keyframes
     * @param idx an index into the data array at the start of the
     *     relevant data
     * @return the interpolated value
     */
    protected abstract float interpolate(float partial, int idx);

    /**
     * Samples the keyframes at the given time and places the interpolated
     * data into `out`. out.length` must equal the keyframe data size.
     *
     * @param time a nonnegative float value.
     * @param out storage for interpolated keyframe data
     */
    public void sample(float time, float[] out) {
        float fractionalIndex = keyframes.getTimeIndex(time);
        int whole = (int)fractionalIndex;
        float part = fractionalIndex % 1;
        for(int i = 0; i < out.length; i++) {
            int idx = dataCount * (keyframes.dataSize() * whole + i);
            out[i] = interpolate(part, idx);
        }
    }
}
