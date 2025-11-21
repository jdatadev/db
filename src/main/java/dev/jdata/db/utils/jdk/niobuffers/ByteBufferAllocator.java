package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;
import java.util.function.IntFunction;

abstract class ByteBufferAllocator extends BaseBufferAllocator<ByteBuffer> implements IByteBufferAllocator {

    ByteBufferAllocator(IntFunction<ByteBuffer> createArray) {
        super(createArray);
    }

    @Override
    public void freeByteBuffer(ByteBuffer byteBuffer) {

        freeArrayInstance(byteBuffer);
    }
}
