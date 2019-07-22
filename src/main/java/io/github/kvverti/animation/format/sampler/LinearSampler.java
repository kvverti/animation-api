package io.github.kvverti.animation.format.sampler;

import io.github.kvverti.animation.format.KeyframeList;

public final class LinearSampler extends Sampler {

    public LinearSampler(KeyframeList keyframes) {
        super(keyframes, 1, 2);
    }

    @Override
    protected float interpolate(float partial, int idx) {
        return partial * this.data[idx + 1] + (1 - partial) * this.data[idx];
    }
}
