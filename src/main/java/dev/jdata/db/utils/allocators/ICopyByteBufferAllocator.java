package dev.jdata.db.utils.allocators;

import java.nio.ByteBuffer;

public interface ICopyByteBufferAllocator extends IByteBufferAllocator {

    ByteBuffer allocate(ByteBuffer byteBuffer, int offset, int length);
}
