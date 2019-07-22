package io.github.kvverti.animation.format.sampler;

import io.github.kvverti.animation.format.KeyframeList;

public final class StepSampler extends Sampler {

    public StepSampler(KeyframeList keyframes) {
        super(keyframes, 1, 2);
    }

    @Override
    protected float interpolate(float partial, int idx) {
        return partial < 0.5f ? this.data[idx] : this.data[idx + 1];
    }
}
