package io.github.kvverti.animation.format.sampler;

import io.github.kvverti.animation.format.KeyframeList;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestSampler {

    private static class MockSampler extends Sampler {

        public MockSampler(KeyframeList keyframes, int radius) {
            super(keyframes, radius, radius * 2);
        }

        @Override
        public float interpolate(float partial, int idx) {
            return this.data[idx + 1];
        }

        float[] data() {
            return this.data;
        }
    }

    private static KeyframeList keyframes;

    @BeforeAll
    public static void init() {
        float[] times = { 0.0f, 0.5f, 1.0f };
        float[] data = { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f };
        keyframes = new KeyframeList(times, data);
    }

    private MockSampler sampler;

    @Test
    @DisplayName("State invariants")
    public void testState() {
        sampler = new MockSampler(keyframes, 1);
        float[] data = sampler.data();
        float[] expected = {
            0.0f, 1.0f, 3.0f, 4.0f,
            1.0f, 2.0f, 4.0f, 5.0f,
            2.0f, 2.0f, 5.0f, 5.0f
        };
        assertArrayEquals(expected, data, "data");
    }

    @Test
    @DisplayName("More state invariants")
    public void testMoreState() {
        sampler = new MockSampler(keyframes, 2);
        float[] data = sampler.data();
        float[] expected = {
            0.0f, 0.0f, 1.0f, 2.0f, 3.0f, 3.0f, 4.0f, 5.0f,
            0.0f, 1.0f, 2.0f, 2.0f, 3.0f, 4.0f, 5.0f, 5.0f,
            1.0f, 2.0f, 2.0f, 2.0f, 4.0f, 5.0f, 5.0f, 5.0f
        };
        assertArrayEquals(expected, data, "data");
        float[] out = new float[2];
        sampler.sample(0.0f, out);
        expected = new float[] { 0.0f, 3.0f };
        assertArrayEquals(expected, out, "sample at beginning exact");
        sampler.sample(0.3f, out);
        expected = new float[] { 0.0f, 3.0f };
        assertArrayEquals(expected, out, "sample at beginning");
        sampler.sample(0.7f, out);
        expected = new float[] { 1.0f, 4.0f };
        assertArrayEquals(expected, out, "sample in middle");
        sampler.sample(1.0f, out);
        expected = new float[] { 2.0f, 5.0f };
        assertArrayEquals(expected, out, "sample at end");
        sampler.sample(2.0f, out);
        expected = new float[] { 2.0f, 5.0f };
        assertArrayEquals(expected, out, "sample past end");
    }
}
