package dev.jdata.db.utils.allocators;

import java.nio.CharBuffer;

public final class CharBufferAllocatorTest extends BaseBufferAllocatorTest<CharBuffer, CharBufferAllocator> {

    @Override
    CharBufferAllocator createAllocator() {

        return new CharBufferAllocator();
    }

    @Override
    CharBuffer allocate(CharBufferAllocator allocator, int minimumCapacity) {

        return allocator.allocateArrayInstance(minimumCapacity);
    }

    @Override
    CharBuffer[] allocateArray(int length) {

        return new CharBuffer[length];
    }

    @Override
    void free(CharBufferAllocator allocator, CharBuffer instance) {

        allocator.freeCharBuffer(instance);
    }
}
