package io.github.kvverti.animation.format.sampler;

import io.github.kvverti.animation.format.KeyframeList;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestDefaultSamplers {

    private static KeyframeList keyframes;

    @BeforeAll
    public static void init() {
        float[] times = { 0.0f, 0.5f, 1.0f };
        float[] vals = { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f };
        keyframes = new KeyframeList(times, vals);
    }

    private float[] out;

    @BeforeEach
    public void initEach() {
        out = new float[2];
    }

    @Test
    @DisplayName("Step sampler")
    public void testStepSampler() {
        StepSampler sampler = new StepSampler(keyframes);
        sampler.sample(0.2f, out);
        float[] expected = { 0.0f, 3.0f };
        assertArrayEquals(expected, out, 0.001f, "floor step");
        sampler.sample(0.3f, out);
        expected = new float[] { 1.0f, 4.0f };
        assertArrayEquals(expected, out, 0.001f, "ceil step");
    }

    @Test
    @DisplayName("Linear sampler")
    public void testLinearSampler() {
        LinearSampler sampler = new LinearSampler(keyframes);
        sampler.sample(0.2f, out);
        float[] expected = { 0.4f, 3.4f };
        assertArrayEquals(expected, out, 0.001f, "lower half");
        sampler.sample(0.3f, out);
        expected = new float[] { 0.6f, 3.6f };
        assertArrayEquals(expected, out, 0.001f, "upper half");
        sampler.sample(0.5f, out);
        expected = new float[] { 1.0f, 4.0f };
        assertArrayEquals(expected, out, 0.001f, "frac = 0.0");
        sampler.sample(1.0f, out);
        expected = new float[] { 2.0f, 5.0f };
        assertArrayEquals(expected, out, 0.001f, "frac = 1.0");
        sampler.sample(2.0f, out);
        expected = new float[] { 2.0f, 5.0f };
        assertArrayEquals(expected, out, 0.001f, "past end");
    }

    @Test
    @DisplayName("Sampler curves")
    public void testSamplerCurves() {
        // use special keyframes
        float[] times = { 0.0f, 0.4f, 0.7f, 1.5f, 2.0f };
        float[] vals = { 4.0f, 2.0f, 5.0f, 1.0f, 4.0f };
        KeyframeList keyframes = new KeyframeList(times, vals);
        printCurve(new CubicSampler(keyframes));
        printCurve(new LinearSampler(keyframes));
        printCurve(new StepSampler(keyframes));
    }

    private void printCurve(Sampler sampler) {
        float[] out = new float[1];
        char[] chars = new char[80];
        System.out.format("Printing visual interpolated curve for %s...%n", sampler.getClass().getSimpleName());
        for(int i = 0; i < 50; i++) {
            float time = i / 50.0f * 2.0f;
            sampler.sample(time, out);
            Arrays.fill(chars, ' ');
            chars[(int)(out[0] * 10)] = '#';
            System.out.format("> %s <%n", new String(chars));
        }
    }
}
