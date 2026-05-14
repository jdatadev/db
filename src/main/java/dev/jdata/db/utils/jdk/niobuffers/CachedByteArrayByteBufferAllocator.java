package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;

public final class CachedByteArrayByteBufferAllocator extends CachedByteBufferAllocator implements IByteArrayByteBufferAllocator {

    public CachedByteArrayByteBufferAllocator() {
        super(ByteBuffer::allocate);
    }

    @Override
    public ByteBuffer allocateByteArrayByteBuffer(int minimumCapacity) {

        return allocateFromFreeListOrCreateCapacityInstance(minimumCapacity);
    }
}
