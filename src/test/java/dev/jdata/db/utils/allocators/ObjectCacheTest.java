package dev.jdata.db.utils.allocators;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class ObjectCacheTest extends BaseAllocatorTest<StringBuilder, ObjectCache<StringBuilder>> {

    @Test
    @Category(UnitTest.class)
    public void testConstructorArguments() {

        assertThatThrownBy(() -> new ObjectCache<>(null, StringBuilder[]::new)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> new ObjectCache<>(StringBuilder::new, null)).isInstanceOf(NullPointerException.class);
    }

    @Override
    protected ObjectCache<StringBuilder> createAllocator() {

        return new ObjectCache<>(StringBuilder::new, StringBuilder[]::new);
    }

    @Override
    StringBuilder allocate(ObjectCache<StringBuilder> allocator) {

        return allocator.allocate();
    }

    @Override
    protected StringBuilder[] allocateArray(int length) {

        return new StringBuilder[length];
    }

    @Override
    protected void free(ObjectCache<StringBuilder> allocator, StringBuilder instance) {

        allocator.free(instance);
    }

    @Override
    boolean freesInSameOrder() {

        return false;
    }
}
