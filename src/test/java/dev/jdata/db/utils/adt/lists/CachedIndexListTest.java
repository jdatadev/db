package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

public final class CachedIndexListTest extends BaseTest {

    @Test
    @Category(UnitTest.class)
    public void testBuildHeapAllocated() {

        final CacheIndexListAllocator<String> cacheIndexListAllocator = new CacheIndexListAllocator<>(String[]::new);
        final CachedIndexListBuilder<String> indexListBuilder = CachedIndexList.createBuilder(cacheIndexListAllocator);

        final String string = "abc";

        indexListBuilder.addTail(string);

        final HeapIndexList<String> heapIndexList = indexListBuilder.buildHeapAllocated();

        assertThat(heapIndexList).containsExactly(string);
    }
}
