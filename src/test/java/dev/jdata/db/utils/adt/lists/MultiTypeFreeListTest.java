package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

public final class MultiTypeFreeListTest extends BaseFreeListTest {

    @Test
    @Category(UnitTest.class)
    public void testAllocateUnknownType() {

        final MultiTypeFreeList<Object> freeList = new MultiTypeFreeList<>(Object[]::new, Integer.class, Long.class, String.class);

        assertThatThrownBy(() -> freeList.allocate(Short.class)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testFreeUnknownType() {

        final MultiTypeFreeList<Object> freeList = new MultiTypeFreeList<>(Object[]::new, Integer.class, Long.class, String.class);

        assertThatThrownBy(() -> freeList.free(Short.valueOf((short)1))).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testAllocateAndFree() {

        final MultiTypeFreeList<Object> freeList = new MultiTypeFreeList<>(Object[]::new, Object.class);

        final Object object1 = new Object();
        final Object object2 = new Object();
        final Object object3 = new Object();

        checkAllocateAndFree(freeList, Object.class, object1, object2, object3);
    }

    @Test
    @Category(UnitTest.class)
    public void testAllocateAndFreeMultipleTypes() {

        final Class<?> type1 = Integer.class;
        final Class<?> type2 = Long.class;
        final Class<?> type3 = String.class;

        final Integer object1 = 123;
        final Long object2 = 234L;
        final String object3 = String.valueOf(345);

        final Class<?> [] typesToAllocate = new Class<?>[] { type1, type2, type3 };

        final MultiTypeFreeList<Object> freeList = new MultiTypeFreeList<>(Object[]::new, typesToAllocate);

        checkIsEmpty(freeList, typesToAllocate);

        freeList.free(object1);
        checkHasElements(freeList, object1);

        assertThat(freeList.allocate(type1)).isSameAs(object1);
        checkIsEmpty(freeList, typesToAllocate);

        freeList.free(object1);
        freeList.free(object2);
        checkHasElements(freeList, object1, object2);

        assertThat(freeList.allocate(type2)).isSameAs(object2);
        checkHasElements(freeList, object1);

        freeList.free(object2);
        freeList.free(object3);
        checkHasElements(freeList, object1, object2, object3);

        assertThat(freeList.allocate(type3)).isSameAs(object3);
        checkHasElements(freeList, object1, object2);

        assertThat(freeList.allocate(type2)).isSameAs(object2);
        checkHasElements(freeList, object1);

        assertThat(freeList.allocate(type1)).isSameAs(object1);
        checkIsEmpty(freeList, typesToAllocate);
    }

    private static <T> void checkAllocateAndFree(MultiTypeFreeList<T> freeList, Class<T> typeToAllocate, T object1, T object2, T object3) {

        checkAllocateAndFree(freeList, object1, object2, object3, l -> l.allocate(typeToAllocate));
    }

    private static <T> void checkIsEmpty(MultiTypeFreeList<T> freeList, Class<? extends T> [] typesToAllocate) {

        for (Class<? extends T> typeToAllocate : typesToAllocate) {

            assertThatThrownBy(() -> freeList.allocate(typeToAllocate)).isInstanceOf(IllegalStateException.class);
        }

        checkIsEmpty(freeList);
    }
}
