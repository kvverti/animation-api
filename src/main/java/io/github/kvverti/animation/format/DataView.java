package io.github.kvverti.animation.format;

/**
 * View of a neighborhood of values about a keyframe index. Instances of this
 * class are mutable and are meant to be long-lived containers for short-lived
 * data. Clients should store a DataView instance for passing to
 * {@link KeyframeList#getData}.
 */
public final class DataView {

    private int index;

    private float partial;

    private int keyframeCount;

    private float[] keyframeData;

    /**
     * Stores the given data in this instance.
     */
    void assign(int kfCount, float[] kfData, int idx, float partial) {
        this.index = idx;
        this.partial = partial;
        this.keyframeCount = kfCount;
        this.keyframeData = kfData;
    }

    /**
     * Returns the partial index of this view.
     */
    public float frac() {
        return partial;
    }

    /**
     * Returns the keyframe `value[valIdx]` at `offset` from the view's index.
     * If the offset over- or underflows the keyframe data, then it is snapped
     * to the boundaries.
     *
     * @throws IllegalArgumentException if `valIdx` is not a valid index.
     */
    public float get(int offset, int valIdx) {
        int valOffset = keyframeCount * valIdx;
        int i = index + offset;
        if(i < 0) {
            i = 0;
        } else if(i >= keyframeCount) {
            i = keyframeCount - 1;
        }
        try {
            return keyframeData[valOffset + i];
        } catch(ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("valIdx: " + valIdx);
        }
    }
}
