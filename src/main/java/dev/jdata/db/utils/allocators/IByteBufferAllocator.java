package dev.jdata.db.utils.allocators;

import java.nio.ByteBuffer;

public interface IByteBufferAllocator {

    void freeByteBuffer(ByteBuffer byteBuffer);
}
