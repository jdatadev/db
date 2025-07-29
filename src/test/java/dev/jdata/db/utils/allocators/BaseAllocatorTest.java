package dev.jdata.db.utils.allocators;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

abstract class BaseAllocatorTest<T, A> extends BaseTest {

    protected abstract A createAllocator();

    abstract T allocate(A allocator);
    protected abstract T[] allocateArray(int length);

    protected abstract void free(A allocator, T instance);

    abstract boolean freesInSameOrder();

    @Test
    @Category(UnitTest.class)
    public final void testFreeNull() {

        final A allocator = createAllocator();

        assertThatThrownBy(() -> free(allocator, null)).isInstanceOf(NullPointerException.class);
    }

    @Test
    @Category(UnitTest.class)
    public final void testAllocateAndFreeTwice() {

        final A allocator = createAllocator();

        final T instance = allocate(allocator);

        assertThat(instance).isNotNull();

        free(allocator, instance);

        assertThatThrownBy(() -> free(allocator, instance)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Category(UnitTest.class)
    public final void testAllocateFreeReallocate() {

        final A allocator = createAllocator();

        final T instance = allocate(allocator);

        assertThat(instance).isNotNull();

        free(allocator, instance);

        assertThat(allocate(allocator)).isSameAs(instance);
    }

    @Test
    @Category(UnitTest.class)
    public final void testAllocateMany() {

        final int numElements = 10 * 1000;

        final T[] allocated = allocateArray(numElements);

        final A allocator = createAllocator();

        for (int i = 0; i < numElements; ++ i) {

            allocated[i] = allocate(allocator);
        }

        for (int i = 0; i < numElements; ++ i) {

            free(allocator, allocated[i]);
        }

        final boolean freesInSameOrder = freesInSameOrder();

        for (int i = 0; i < numElements; ++ i) {

            final int index = freesInSameOrder ? i : numElements - i - 1;

            assertThat(allocate(allocator)).isSameAs(allocated[index]);
        }
    }
}
