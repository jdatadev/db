package dev.jdata.db.utils.allocators;

import java.nio.ByteBuffer;

public final class ByteArrayByteBufferAllocator extends ByteBufferAllocator implements IByteArrayByteBufferAllocator {

    public ByteArrayByteBufferAllocator() {
        super(ByteBuffer::allocate);
    }

    @Override
    public ByteBuffer allocateByteArrayByteBuffer(int minimumCapacity) {

        return allocateArrayInstance(minimumCapacity);
    }
}
