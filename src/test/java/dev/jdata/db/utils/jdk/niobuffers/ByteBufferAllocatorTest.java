package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;

abstract class ByteBufferAllocatorTest<A extends ByteBufferAllocator> extends BaseBufferAllocatorTest<ByteBuffer, A> {

    @Override
    protected final ByteBuffer[] allocateArray(int length) {

        return new ByteBuffer[length];
    }
}
