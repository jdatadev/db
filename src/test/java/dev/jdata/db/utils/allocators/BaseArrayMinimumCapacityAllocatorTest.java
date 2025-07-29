package dev.jdata.db.utils.allocators;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public abstract class BaseArrayMinimumCapacityAllocatorTest<T, A> extends BaseArrayAllocatorTest<T, A> {

    @Test
    @Category(UnitTest.class)
    public void testAllocate() {

        final A allocator = createAllocator();

        final T buffer10 = allocate(allocator, 10);
        final T buffer100 = allocate(allocator, 100);
        final T buffer1000 = allocate(allocator, 1000);

        free(allocator, buffer1000);
        free(allocator, buffer10);
        free(allocator, buffer100);

        assertThat(allocate(allocator, 1)).isSameAs(buffer10);
        assertThat(allocate(allocator, 1)).isSameAs(buffer100);
        assertThat(allocate(allocator, 1)).isSameAs(buffer1000);
    }
}
