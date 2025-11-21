package dev.jdata.db.utils.adt.lists;

import java.util.Objects;
import java.util.function.Function;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.allocators.IFreeing;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseFreeListTest extends BaseTest {

    static <T, U extends IFreeing<T> & IOnlyElementsView> void checkAllocateAndFree(U freeList, T object1, T object2, T object3, Function<U, T> allocator) {

        assertThatThrownBy(() -> freeList.free(null)).isInstanceOf(NullPointerException.class);

        checkIsEmpty(freeList, allocator);

        freeList.free(object1);
        checkHasElements(freeList, object1);

        assertThat(allocator.apply(freeList)).isSameAs(object1);
        checkIsEmpty(freeList, allocator);

        freeList.free(object1);
        freeList.free(object2);
        checkHasElements(freeList, object1, object2);

        assertThat(allocator.apply(freeList)).isSameAs(object2);
        checkHasElements(freeList, object1);

        freeList.free(object2);
        freeList.free(object3);
        checkHasElements(freeList, object1, object2, object3);

        assertThat(allocator.apply(freeList)).isSameAs(object3);
        checkHasElements(freeList, object1, object2);

        assertThat(allocator.apply(freeList)).isSameAs(object2);
        checkHasElements(freeList, object1);

        assertThat(allocator.apply(freeList)).isSameAs(object1);
        checkIsEmpty(freeList, allocator);
    }

    static <T, U extends IFreeing<T> & IOnlyElementsView> void checkIsEmpty(U freeList, Function<U, T> allocator) {

        Objects.requireNonNull(freeList);
        Objects.requireNonNull(allocator);

        assertThatThrownBy(() -> allocator.apply(freeList)).isInstanceOf(IllegalStateException.class);

        checkIsEmpty(freeList);
    }

    @SafeVarargs
    static <T, U extends IFreeing<T> & IOnlyElementsView> void checkHasElements(U freeList, T ... expectedObjects) {

        Objects.requireNonNull(freeList);
        Checks.isNotEmpty(expectedObjects);

        assertThat(freeList).isNotEmpty();
        assertThat(freeList).hasNumElements(expectedObjects.length);

        for (T expectedObject : expectedObjects) {

            assertThatThrownBy(() -> freeList.free(expectedObject)).isInstanceOf(IllegalArgumentException.class);
        }
    }
}
