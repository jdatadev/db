package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;
import java.util.function.IntFunction;

abstract class CachedByteBufferAllocator extends BaseBufferAllocator<ByteBuffer> implements IByteBufferAllocator {

    CachedByteBufferAllocator(IntFunction<ByteBuffer> createArray) {
        super(createArray);
    }

    @Override
    public void freeByteBuffer(ByteBuffer byteBuffer) {

        freeArrayInstance(byteBuffer);
    }
}
