package io.github.kvverti.animation.format;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestKeyframeList {

    private static KeyframeList keyframes;

    @BeforeAll
    public static void init() {
        float[] times = { 0.0f, 0.5f, 1.0f };
        float[] data = { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f };
        keyframes = new KeyframeList(times, data);
    }

    private float[] floor;
    private float[] ceil;

    @BeforeEach
    public void initEach() {
        floor = new float[3];
        ceil = new float[3];
    }

    @Test
    @DisplayName("Successful construction")
    public void testConstruct() {
        float[] times = { 0.0f, 0.5f, 1.0f };
        float[] data = { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f };
        new KeyframeList(times, data);
    }

    @Test
    @DisplayName("Construct with wrong data size")
    public void testConstructWrongDataSize() {
        float[] times = { 0.0f, 0.5f, 1.0f };
        float[] data = { 0.0f, 1.0f, 2.0f };
        assertThrows(IllegalArgumentException.class, () -> new KeyframeList(times, data));
    }

    @Test
    @DisplayName("Construct not start at zero")
    public void testConstructNotAtZero() {
        float[] times = { 0.5f, 1.0f };
        float[] data = { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f };
        assertThrows(IllegalArgumentException.class, () -> new KeyframeList(times, data));
    }

    @Test
    @DisplayName("Construct empty")
    public void testConstructEmpty() {
        float[] times = {};
        float[] data = {};
        assertThrows(IllegalArgumentException.class, () -> new KeyframeList(times, data));
    }

    @Test
    @DisplayName("Keyframe from beginning")
    public void testFirstKeyframe() {
        float frac = keyframes.getData(0.0f, floor, ceil);
        assertEquals(0.0f, frac, 0.0001, "keyframe fraction");
        assertArrayEquals(new float[] { 0.0f, 1.0f, 2.0f }, floor, "floor data");
        assertArrayEquals(new float[] { 3.0f, 4.0f, 5.0f }, ceil, "ceil data");
    }

    @Test
    @DisplayName("Keyframe in middle")
    public void testMiddleKeyframe() {
        float frac = keyframes.getData(0.6f, floor, ceil);
        assertEquals(0.2f, frac, 0.001, "keyframe fraction");
        assertArrayEquals(new float[] { 3.0f, 4.0f, 5.0f }, floor, "floor data");
        assertArrayEquals(new float[] { 6.0f, 7.0f, 8.0f }, ceil, "ceil data");
    }

    @Test
    @DisplayName("Exact keyframe in middle")
    public void testMiddleKeyframeExact() {
        float frac = keyframes.getData(0.5f, floor, ceil);
        assertEquals(0.0f, frac, 0.001, "keyframe fraction");
        assertArrayEquals(new float[] { 3.0f, 4.0f, 5.0f }, floor, "floor data");
        assertArrayEquals(new float[] { 6.0f, 7.0f, 8.0f }, ceil, "ceil data");
    }

    @Test
    @DisplayName("Keyframe at end")
    public void testEndKeyframe() {
        float frac = keyframes.getData(1.0f, floor, ceil);
        assertEquals(1.0f, frac, 0.001, "keyframe fraction");
        assertArrayEquals(new float[] { 3.0f, 4.0f, 5.0f }, floor, "floor data");
        assertArrayEquals(new float[] { 6.0f, 7.0f, 8.0f }, ceil, "ceil data");
    }

    @Test
    @DisplayName("Keyframe past end")
    public void testPastEndKeyframe() {
        float frac = keyframes.getData(1.5f, floor, ceil);
        assertEquals(1.0f, frac, 0.001, "keyframe fraction");
        assertArrayEquals(new float[] { 3.0f, 4.0f, 5.0f }, floor, "floor data");
        assertArrayEquals(new float[] { 6.0f, 7.0f, 8.0f }, ceil, "ceil data");
    }
}
