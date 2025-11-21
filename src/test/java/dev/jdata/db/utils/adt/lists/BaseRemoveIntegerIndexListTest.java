package dev.jdata.db.utils.adt.lists;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.elements.IOnlyElementsView;

@Deprecated // currently not in use
abstract class BaseRemoveIntegerIndexListTest<T extends IOnlyElementsView & IListTypeMutable> extends BaseIntegerIndexListTest<T> {

    abstract boolean removeAtMostOne(T list, int value);

    @Test
    @Category(UnitTest.class)
    public final void testRemove() {

        final int value = 123;

        final T list = createArray(value, value);

        assertThatThrownBy(() -> removeAtMostOne(list, value)).isInstanceOf(IllegalStateException.class);

        assertThat(toArray(list)).isEqualTo(new int[] { value, value });

        checkRemove(new int[0], 123, new int[0], false);
        checkRemove(new int[] { 123 }, 123, new int[0], true);
        checkRemove(new int[] { 123 }, 234, new int[] { 123 }, false);
        checkRemove(new int[] { 123, 234 }, 345, new int[] { 123, 234 }, false);
        checkRemove(new int[] { 123, 234 }, 123, new int[] { 234 }, true);
        checkRemove(new int[] { 123, 234 }, 234, new int[] { 123 }, true);
        checkRemove(new int[] { 123, 234, 345 }, 123, new int[] { 234, 345 }, true);
        checkRemove(new int[] { 123, 234, 345 }, 234, new int[] { 123, 345 }, true);
        checkRemove(new int[] { 123, 234, 345 }, 345, new int[] { 123, 234 }, true);
    }

    private void checkRemove(int[] elements, int toRemove, int[] expectedElements, boolean expecteRemoved) {

        final T list = createArray();

        for (int element : elements) {

            addTail(list, element);
        }

        assertThat(removeAtMostOne(list, toRemove)).isEqualTo(expecteRemoved);

        assertThat(toArray(list)).isEqualTo(expectedElements);
    }
}
