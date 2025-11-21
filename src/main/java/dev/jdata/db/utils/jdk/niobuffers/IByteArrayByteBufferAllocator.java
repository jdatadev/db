package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;

public interface IByteArrayByteBufferAllocator extends IByteBufferAllocator {

    ByteBuffer allocateByteArrayByteBuffer(int minimumCapacity);
}
