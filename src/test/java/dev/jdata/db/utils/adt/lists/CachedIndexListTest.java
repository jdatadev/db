package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class CachedIndexListTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testBuildHeapAllocated() {

        final CachedMutableObjectIndexListAllocator<String> mutableIntIndexListAllocator = new CachedMutableObjectIndexListAllocator<>(String[]::new);
        final CachedObjectIndexListAllocator<String> cacheIndexListAllocator = new CachedObjectIndexListAllocator<>(String[]::new, mutableIntIndexListAllocator);
        final ICachedIndexListBuilder<String> indexListBuilder = cacheIndexListAllocator.createBuilder();

        final String string = "abc";

        indexListBuilder.addTail(string);

        final IHeapIndexList<String> heapIndexList = indexListBuilder.buildHeapAllocatedOrNull();

        assertThat(heapIndexList).containsExactly(string);
    }
}
