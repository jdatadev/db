package dev.jdata.db.utils.jdk.niobuffers;

import java.nio.ByteBuffer;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class CopyByteBufferAllocatorTest extends ByteBufferAllocatorTest<CopyByteBufferAllocator> {

    @Test
    @Category(UnitTest.class)
    public void testCopyByteBuffer() {

        final byte[] bytes = new byte[] { 12, 23, 34 };

        assertThatThrownBy(() -> new CopyByteBufferAllocator().allocate(ByteBuffer.wrap(bytes), 1, 0)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> new CopyByteBufferAllocator().allocate(ByteBuffer.wrap(bytes), 0, 4)).isInstanceOf(IndexOutOfBoundsException.class);
        assertThatThrownBy(() -> new CopyByteBufferAllocator().allocate(ByteBuffer.wrap(bytes), -1, 3)).isInstanceOf(IndexOutOfBoundsException.class);

        checkCopyByteBuffer(bytes, 0, 1, (byte)12);
        checkCopyByteBuffer(bytes, 0, 2, (byte)12, (byte)23);
        checkCopyByteBuffer(bytes, 0, 3, (byte)12, (byte)23, (byte)34);
        checkCopyByteBuffer(bytes, 1, 1, (byte)23);
        checkCopyByteBuffer(bytes, 1, 2, (byte)23, (byte)34);
        checkCopyByteBuffer(bytes, 2, 1, (byte)34);
    }

    @Test
    @Category(UnitTest.class)
    public void testCopyByteBufferResetsPosition() {

        final byte[] bytes = new byte[] { 12, 23, 34 };

        final CopyByteBufferAllocator allocator = new CopyByteBufferAllocator();

        final int length = bytes.length;

        final ByteBuffer copy = allocator.allocate(ByteBuffer.wrap(bytes), 0, length);

        assertThat(copy.position()).isEqualTo(0);
        assertThat(copy.limit()).isEqualTo(length);

        copy.get();
        copy.limit(length - 1);

        assertThat(copy.position()).isEqualTo(1);
        assertThat(copy.limit()).isEqualTo(length - 1);

        allocator.freeByteBuffer(copy);

        final ByteBuffer anotherCopy = allocator.allocate(ByteBuffer.wrap(bytes), 0, length);

        assertThat(copy).isSameAs(anotherCopy);

        assertThat(anotherCopy.position()).isEqualTo(0);
        assertThat(anotherCopy.limit()).isEqualTo(length);
    }

    private static void checkCopyByteBuffer(byte[] byteArray, int offset, int length, byte ... expectedBytes) {

        final CopyByteBufferAllocator allocator = new CopyByteBufferAllocator();

        final ByteBuffer copy = allocator.allocate(ByteBuffer.wrap(byteArray), offset, length);

        assertThat(copy).isNotNull();
        assertThat(copy.position()).isEqualTo(0);
        assertThat(copy.limit()).isEqualTo(length);
    }

    @Override
    protected CopyByteBufferAllocator createAllocator() {

        return new CopyByteBufferAllocator();
    }

    @Override
    protected ByteBuffer allocate(CopyByteBufferAllocator allocator, int minimumCapacity) {

        return allocator.allocate(ByteBuffer.wrap(new byte[minimumCapacity]), 0, minimumCapacity);
    }

    @Override
    protected void free(CopyByteBufferAllocator allocator, ByteBuffer instance) {

        allocator.freeByteBuffer(instance);
    }
}
