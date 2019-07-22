package io.github.kvverti.animation.format.sampler;

import io.github.kvverti.animation.format.KeyframeList;

/**
 * A Catmull-Rom cubic spline with tension 0.5.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Centripetal_Catmull%E2%80%93Rom_spline">Catmull-Rom Spline on Wikipedia</a>
 */
public final class CubicSampler extends Sampler {

    public CubicSampler(KeyframeList keyframes) {
        super(keyframes, 2, 4);
    }

    @Override
    protected void transform(float[] times, float[] vals, float[] dst) {
        float dx, dx1, dx2, s1, s2;
        dx = times[2] - times[1];
        dx1 = times[1] - times[0];
        dx2 = times[3] - times[2];
        s1 = 2 * (dx / (dx1 + dx));
        s2 = 2 * (dx / (dx + dx2));
        final float c = 0.5f;
        float m1, m2;
        m1 = s1 * c * (vals[2] - vals[0]);
        m2 = s2 * c * (vals[3] - vals[1]);
        dst[0] = vals[1];
        dst[1] = vals[2];
        dst[2] = m1;
        dst[3] = m2;
    }

    private static float h1(float t) {
        return 1 + t * t * (-3 + t * 2);
    }

    private static float h2(float t) {
        return t * t * (3 - t * 2);
    }

    private static float h3(float t) {
        return t * (1 + t * (-2 + t));
    }

    private static float h4(float t) {
        return t * t * (-1 + t);
    }

    @Override
    protected float interpolate(float partial, int idx) {
        return this.data[idx] * h1(partial)
            + this.data[idx + 1] * h2(partial)
            + this.data[idx + 2] * h3(partial)
            + this.data[idx + 3] * h4(partial);
    }
}
