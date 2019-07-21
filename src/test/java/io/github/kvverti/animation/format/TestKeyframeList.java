package io.github.kvverti.animation.format;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests KeyframeList and DataView classes.
 */
public class TestKeyframeList {

    private static KeyframeList keyframes;

    @BeforeAll
    public static void init() {
        float[] times = { 0.0f, 0.5f, 1.0f, 1.5f };
        float[] data = { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f };
        keyframes = new KeyframeList(times, data);
    }

    @Test
    @DisplayName("Successful construction")
    public void testConstruct() {
        float[] times = { 0.0f, 0.5f, 1.0f };
        float[] data = { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f };
        KeyframeList k = new KeyframeList(times, data);
        assertEquals(3, k.size(), "size");
        assertEquals(2, k.dataSize(), "dataSize");
        assertEquals(1.0f, k.time(2), "time");
        assertEquals(5.0f, k.datum(2, 1), "datum");
    }

    @Test
    @DisplayName("Construct with wrong data size")
    public void testConstructWrongDataSize() {
        float[] times = { 0.0f, 0.5f, 1.0f };
        float[] data = { 0.0f, 1.0f, 2.0f, 3.0f };
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
    @DisplayName("Keyframe at beginning")
    public void testFirstKeyframe() {
        float timeIdx = keyframes.getTimeIndex(0.0f);
        assertEquals(0.0f, timeIdx, 0.001, "keyframe fraction");
    }

    @Test
    @DisplayName("Keyframe in middle")
    public void testMiddleKeyframe() {
        float timeIdx = keyframes.getTimeIndex(0.6f);
        assertEquals(1.2f, timeIdx, 0.001, "keyframe fraction");
    }

    @Test
    @DisplayName("Exact keyframe in middle")
    public void testMiddleKeyframeExact() {
        float timeIdx = keyframes.getTimeIndex(0.5f);
        assertEquals(1.0f, timeIdx, 0.001, "keyframe fraction");
    }

    @Test
    @DisplayName("Keyframe at end")
    public void testEndKeyframe() {
        float timeIdx = keyframes.getTimeIndex(1.5f);
        assertEquals(3.0f, timeIdx, 0.001, "keyframe fraction");
    }

    @Test
    @DisplayName("Keyframe past end")
    public void testPastEndKeyframe() {
        float timeIdx = keyframes.getTimeIndex(2.0f);
        assertEquals(3.0f, timeIdx, 0.001, "keyframe fraction");
    }
}
