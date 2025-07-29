package dev.jdata.db.utils.allocators;

import java.nio.CharBuffer;

public final class CharBufferAllocatorTest extends BaseBufferAllocatorTest<CharBuffer, CharBufferAllocator> {

    @Override
    protected CharBufferAllocator createAllocator() {

        return new CharBufferAllocator();
    }

    @Override
    protected CharBuffer allocate(CharBufferAllocator allocator, int minimumCapacity) {

        return allocator.allocateArrayInstance(minimumCapacity);
    }

    @Override
    protected CharBuffer[] allocateArray(int length) {

        return new CharBuffer[length];
    }

    @Override
    protected void free(CharBufferAllocator allocator, CharBuffer instance) {

        allocator.freeCharBuffer(instance);
    }
}
