package dev.jdata.db.utils.allocators;

import java.nio.ByteBuffer;

import dev.jdata.db.utils.checks.Checks;

public final class CopyByteBufferAllocator extends ByteBufferAllocator implements ICopyByteBufferAllocator {

    public CopyByteBufferAllocator() {
        super(ByteBuffer::allocate);
    }

    @Override
    public ByteBuffer allocate(ByteBuffer byteBuffer, int offset, int length) {

        Checks.checkBuffer(byteBuffer, offset, length);
        Checks.isLengthAboveZero(length);

        final ByteBuffer result = allocateArrayInstance(length);

        result.position(0);
        result.limit(length);

        result.put(0, byteBuffer, offset, length);

        return result;
    }
}
