package io.github.kvverti.animation.schema;

/**
 * A view into a raw buffer.
 */
public final class BufferView {

    private byte[] buffer;

    private int offset;

    private int length;

    public BufferView(byte[] buffer, int offset, int length) {
        this.buffer = buffer;
        this.offset = offset;
        this.length = length;
    }

    public byte get(int idx) {
        if(idx >= length) {
            throw new ArrayIndexOutOfBoundsException(idx);
        }
        return buffer[offset + idx];
    }
}
