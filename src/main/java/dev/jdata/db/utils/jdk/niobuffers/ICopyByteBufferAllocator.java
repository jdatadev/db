package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;

public interface ICopyByteBufferAllocator extends IByteBufferAllocator {

    ByteBuffer allocate(ByteBuffer byteBuffer, int offset, int length);
}
