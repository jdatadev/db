package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;

public final class ByteArrayByteBufferAllocatorTest extends ByteBufferAllocatorTest<CachedByteArrayByteBufferAllocator> {

    @Override
    protected CachedByteArrayByteBufferAllocator createAllocator() {

        return new CachedByteArrayByteBufferAllocator();
    }

    @Override
    protected ByteBuffer allocate(CachedByteArrayByteBufferAllocator allocator, int minimumCapacity) {

        return allocator.allocateByteArrayByteBuffer(minimumCapacity);
    }

    @Override
    protected void free(CachedByteArrayByteBufferAllocator allocator, ByteBuffer instance) {

        allocator.freeByteBuffer(instance);
    }
}
