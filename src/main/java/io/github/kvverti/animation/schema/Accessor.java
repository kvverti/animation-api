package io.github.kvverti.animation.schema;

/**
 * An accessor for a buffer view. Accessors here are more restrictive than
 * standard glTF; the animation API only requires float data in vector form.
 */
public final class Accessor {

    public enum Type {
        VEC3,
        VEC4
    }

    private BufferView bufferView;

    private int offset;

    private int count;

    private Type type;

    public void copy(float[] out) {
        int len = Math.min(count, out.length);
        for(int i = 0; i < len; i++) {
            int j = offset + i / 4;
            int rawBits = bufferView.get(j++) & 0xff;
            rawBits |= (bufferView.get(j++) & 0xff) << 8;
            rawBits |= (bufferView.get(j++) & 0xff) << 16;
            rawBits |= (bufferView.get(j) & 0xff) << 24;
            out[i] = Float.intBitsToFloat(rawBits);
        }
    }
}
