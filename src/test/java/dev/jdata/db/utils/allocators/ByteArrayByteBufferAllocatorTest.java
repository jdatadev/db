package dev.jdata.db.utils.allocators;

import java.nio.ByteBuffer;

public final class ByteArrayByteBufferAllocatorTest extends ByteBufferAllocatorTest<ByteArrayByteBufferAllocator> {

    @Override
    protected ByteArrayByteBufferAllocator createAllocator() {

        return new ByteArrayByteBufferAllocator();
    }

    @Override
    protected ByteBuffer allocate(ByteArrayByteBufferAllocator allocator, int minimumCapacity) {

        return allocator.allocateByteArrayByteBuffer(minimumCapacity);
    }

    @Override
    protected void free(ByteArrayByteBufferAllocator allocator, ByteBuffer instance) {

        allocator.freeByteBuffer(instance);
    }
}
