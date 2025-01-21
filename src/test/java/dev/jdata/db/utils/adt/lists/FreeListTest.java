package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class FreeListTest extends BaseFreeListTest {

    @Test
    @Category(UnitTest.class)
    public void testAllocateAndFree() {

        final FreeList<Object> freeList = new FreeList<>(Object[]::new);

        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();

        checkAllocateAndFree(freeList, object1, object2, object3, FreeList::allocate);
    }
}
