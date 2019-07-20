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

    private static DataView view;

    @BeforeAll
    public static void init() {
        float[] times = { 0.0f, 0.5f, 1.0f, 1.5f };
        float[] data = { 0.0f, 1.0f, 2.0f, 3.0f, 4.0f, 5.0f, 6.0f, 7.0f, 8.0f, 9.0f, 10.0f, 11.0f };
        keyframes = new KeyframeList(times, data);
        view = new DataView();
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
    @DisplayName("Keyframe from beginning")
    public void testFirstKeyframe() {
        keyframes.getData(0.0f, view);
        assertEquals(0.0f, view.fraction(), 0.0001, "keyframe fraction");
        assertEquals(0.0f, view.getValue(0, 0), 0.001, "data[0,0]");
        assertEquals(0.0f, view.getValue(-1, 0), 0.001, "data[-1,0]");
        assertEquals(3.0f, view.getValue(4, 0), 0.001, "data[4,0]");
        assertEquals(4.0f, view.getValue(0, 1), 0.001, "data[0,1]");
        assertEquals(1.0f, view.getValue(1, 0), 0.001, "data[1,0]");
    }

    @Test
    @DisplayName("Keyframe in middle")
    public void testMiddleKeyframe() {
        keyframes.getData(0.6f, view);
        assertEquals(0.2f, view.fraction(), 0.001, "keyframe fraction");
        assertEquals(1.0f, view.getValue(0, 0), 0.001, "data[0,0]");
        assertEquals(0.0f, view.getValue(-1, 0), 0.001, "data[-1,0]");
        assertEquals(3.0f, view.getValue(4, 0), 0.001, "data[4,0]");
        assertEquals(5.0f, view.getValue(0, 1), 0.001, "data[0,1]");
        assertEquals(2.0f, view.getValue(1, 0), 0.001, "data[1,0]");
        assertThrows(IllegalArgumentException.class, () -> view.getValue(0, -1));
        assertThrows(IllegalArgumentException.class, () -> view.getValue(0, 4));
    }

    @Test
    @DisplayName("Exact keyframe in middle")
    public void testMiddleKeyframeExact() {
        keyframes.getData(0.5f, view);
        assertEquals(0.0f, view.fraction(), 0.001, "keyframe fraction");
        assertEquals(1.0f, view.getValue(0, 0), 0.001, "data[0,0]");
        assertEquals(0.0f, view.getValue(-1, 0), 0.001, "data[-1,0]");
        assertEquals(3.0f, view.getValue(4, 0), 0.001, "data[4,0]");
        assertEquals(5.0f, view.getValue(0, 1), 0.001, "data[0,1]");
        assertEquals(2.0f, view.getValue(1, 0), 0.001, "data[1,0]");
    }

    @Test
    @DisplayName("Keyframe at end")
    public void testEndKeyframe() {
        keyframes.getData(1.5f, view);
        assertEquals(1.0f, view.fraction(), 0.001, "keyframe fraction");
        assertEquals(2.0f, view.getValue(0, 0), 0.001, "data[0,0]");
        assertEquals(1.0f, view.getValue(-1, 0), 0.001, "data[-1,0]");
        assertEquals(3.0f, view.getValue(4, 0), 0.001, "data[4,0]");
        assertEquals(6.0f, view.getValue(0, 1), 0.001, "data[0,1]");
        assertEquals(3.0f, view.getValue(1, 0), 0.001, "data[1,0]");
    }

    @Test
    @DisplayName("Keyframe past end")
    public void testPastEndKeyframe() {
        keyframes.getData(2.0f, view);
        assertEquals(1.0f, view.fraction(), 0.001, "keyframe fraction");
        assertEquals(2.0f, view.getValue(0, 0), 0.001, "data[0,0]");
        assertEquals(1.0f, view.getValue(-1, 0), 0.001, "data[-1,0]");
        assertEquals(3.0f, view.getValue(4, 0), 0.001, "data[4,0]");
        assertEquals(6.0f, view.getValue(0, 1), 0.001, "data[0,1]");
        assertEquals(3.0f, view.getValue(1, 0), 0.001, "data[1,0]");
    }
}
