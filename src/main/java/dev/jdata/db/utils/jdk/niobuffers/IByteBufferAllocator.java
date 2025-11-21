package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;

public interface IByteBufferAllocator {

    void freeByteBuffer(ByteBuffer byteBuffer);
}
