package dev.jdata.db.utils.allocators;

import java.nio.ByteBuffer;

abstract class ByteBufferAllocatorTest<A extends ByteBufferAllocator> extends BaseBufferAllocatorTest<ByteBuffer, A> {

    @Override
    protected final ByteBuffer[] allocateArray(int length) {

        return new ByteBuffer[length];
    }
}
