package dev.jdata.db.utils.allocators;

import java.nio.ByteBuffer;

public interface IByteArrayByteBufferAllocator extends IByteBufferAllocator {

    ByteBuffer allocateByteArrayByteBuffer(int minimumCapacity);
}
