package dev.jdata.db.utils.allocators;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class ArrayOfLongsAllocatorTest extends BaseArrayExactLengthAllocatorTest<long[], ArrayOfLongsAllocator> {

    @Test
    @Category(UnitTest.class)
    public void testExactMatch() {

        final ArrayOfLongsAllocator allocator = new ArrayOfLongsAllocator();

        final long[] array1000 = allocator.allocateArrayOfLongs(1000);

        allocator.freeArrayInstance(array1000);

        final long[] array100 = allocator.allocateArrayOfLongs(100);

        assertThat(array100).isNotSameAs(array1000);

        assertThat(allocator.allocateArrayOfLongs(1000)).isSameAs(array1000);
    }

    @Override
    protected long[] allocate(ArrayOfLongsAllocator allocator, int minimumCapacity) {

        return allocator.allocateArrayOfLongs(minimumCapacity);
    }

    @Override
    protected ArrayOfLongsAllocator createAllocator() {

        return new ArrayOfLongsAllocator();
    }

    @Override
    protected long[][] allocateArray(int length) {

        return new long[length][];
    }

    @Override
    protected void free(ArrayOfLongsAllocator allocator, long[] instance) {

        allocator.freeArrayOfLongs(instance);
    }
}
