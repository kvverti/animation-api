package io.github.kvverti.animation.format.sampler;

import io.github.kvverti.animation.format.KeyframeList;

@FunctionalInterface
public interface SamplerFactory<T extends Sampler> {

    /**
     * Creates a Sampler using the given keyframes.
     */
    T create(KeyframeList keyframes);
}
