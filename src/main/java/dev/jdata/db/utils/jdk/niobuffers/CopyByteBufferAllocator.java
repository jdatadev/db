package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;

import dev.jdata.db.utils.checks.Checks;

public final class CopyByteBufferAllocator extends ByteBufferAllocator implements ICopyByteBufferAllocator {

    public CopyByteBufferAllocator() {
        super(ByteBuffer::allocate);
    }

    @Override
    public ByteBuffer allocate(ByteBuffer byteBuffer, int offset, int length) {

        Checks.checkBuffer(byteBuffer, offset, length);
        Checks.isIntLengthAboveZero(length);

        final ByteBuffer result = allocateFromFreeListOrCreateCapacityInstance(length);

        result.position(0);
        result.limit(length);

        result.put(byteBuffer.array(), offset, length);

        return result;
    }
}
