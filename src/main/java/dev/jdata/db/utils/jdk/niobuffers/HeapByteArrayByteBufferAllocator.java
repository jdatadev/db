package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;
import java.util.Objects;

import dev.jdata.db.utils.checks.Checks;

public final class HeapByteArrayByteBufferAllocator implements IByteArrayByteBufferAllocator {

    public static final HeapByteArrayByteBufferAllocator INSTANCE = new HeapByteArrayByteBufferAllocator();

    private HeapByteArrayByteBufferAllocator() {

    }

    @Override
    public ByteBuffer allocateByteArrayByteBuffer(int minimumCapacity) {

        Checks.isIntMinimumCapacityAtOrAboveZero(minimumCapacity);

        return ByteBuffer.allocate(minimumCapacity);
    }

    @Override
    public void freeByteBuffer(ByteBuffer byteBuffer) {

        Objects.requireNonNull(byteBuffer);
    }
}
