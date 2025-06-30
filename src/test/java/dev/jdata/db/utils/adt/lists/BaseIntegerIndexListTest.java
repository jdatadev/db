package dev.jdata.db.utils.adt.lists;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.elements.IIntIterableElements.IForEach;
import dev.jdata.db.utils.adt.elements.IIntIterableElements.IForEachWithResult;

abstract class BaseIntegerIndexListTest<T extends IMutableIntegerList> extends BaseTest {

    abstract T createArray(int initialCapacity);

    abstract <P> void forEach(T list, P parameter, IForEach<P, RuntimeException> forEach);
    abstract <P1, P2, R> R forEachWithResult(T list, R defaultResult, P1 parameter1, P2 parameter2, IForEachWithResult<P1, P2, R, RuntimeException> forEach);
    abstract int get(T list, int index);
    abstract void addTail(T list, int value);
    abstract boolean removeAtMostOne(T list, int value);

    abstract int[] toArray(T list);

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final T list = createArray();

        assertThat(list).isEmpty();

        addTail(list, 0);

        assertThat(list).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final T list = createArray();

        assertThat(list).hasNumElements(0L);

        addTail(list, 123);
        assertThat(list).hasNumElements(1L);

        addTail(list, 234);
        assertThat(list).hasNumElements(2L);

        addTail(list, 345);
        assertThat(list).hasNumElements(3L);
    }

    @Test
    @Category(UnitTest.class)
    public final void testForEach() {

        final T list = createArray(123, 234, 345);

        final Object parameter = new Object();

        forEach(list, parameter, (e, p) -> {

            assertThat(p).isEqualTo(parameter);
        });

        checkForEach(list, new int[] { 123, 234, 345 });
    }

    private void checkForEach(T list, int[] expectedElements) {

        final List<Integer> elementList = new ArrayList<>();

        forEach(list, null, (e, p) -> {

            elementList.add(e);
        });

        assertThat(elementList).isEqualTo(Lists.of(expectedElements));
    }

    @Test
    @Category(UnitTest.class)
    public final void testForEachWithResult() {

        final T list = createArray(123, 234, 345);

        final Object parameter1 = new Object();
        final Object parameter2 = new Object();

        final String defaultResult = "defaultResult";

        assertThat(forEachWithResult(list, defaultResult, parameter1, parameter2, (e, p1, p2) -> {

            assertThat(p1).isEqualTo(parameter1);
            assertThat(p2).isEqualTo(parameter2);

            return null;
        }))
        .isEqualTo(defaultResult);

        assertThat(forEachWithResult(list, (Integer)null, null, null, (e, p1, p2) -> null)).isNull();

        checkForEachWithResultBreakEach(list, 123, new int[] { 123 });
        checkForEachWithResultBreakEach(list, 234, new int[] { 123, 234 });
        checkForEachWithResultBreakEach(list, 345, new int[] { 123, 234, 345 });
    }

    private void checkForEachWithResultBreakEach(T list, int breakValue, int[] expectedElements) {

        final List<Integer> elementList = new ArrayList<>();

        final String breakEachResult = "breakEachResult";

        assertThat(forEachWithResult(list, (String)null, null, null, (e, p1, p2) -> {

            elementList.add(e);

            return e == breakValue ? breakEachResult : null;

        }))
        .isEqualTo(breakEachResult);

        assertThat(elementList).isEqualTo(Lists.of(expectedElements));
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddTailAndGet() {

        final T list = createArray();

        addTail(list, 123);
        addTail(list, 234);
        addTail(list, 345);

        assertThat(get(list, 0)).isEqualTo(123);
        assertThat(get(list, 1)).isEqualTo(234);
        assertThat(get(list, 2)).isEqualTo(345);
    }

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

    private T createArray(int ... elements) {

        final T list = createArray();

        for (int element : elements) {

            addTail(list, element);
        }

        return list;
    }

    private T createArray() {

        return createArray(10);
    }
}
