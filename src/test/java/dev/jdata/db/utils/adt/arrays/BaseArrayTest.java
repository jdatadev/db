package dev.jdata.db.utils.adt.arrays;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.IClearable;

abstract class BaseArrayTest<T extends IArray & IClearable> extends BaseTest {

    abstract T createArray();

    abstract int getValue(T array, long index);

    abstract void addValue(T array, int value);
    abstract void setValue(T array, long index, int value);

    @Test
    @Category(UnitTest.class)
    public final void testAdd() {

        final T array = createArray();

        addValue(array, 12);
        checkElements(array, 12);

        addValue(array, 23);
        checkElements(array, 12, 23);

        addValue(array, 34);
        checkElements(array, 12, 23, 34);
    }

    @Test
    @Category(UnitTest.class)
    public final void testSet() {

        final T array = createArray();

        assertThatThrownBy(() -> setValue(null, -1, 12)).isInstanceOf(NullPointerException.class);
        assertThatThrownBy(() -> setValue(array, -1, 12)).isInstanceOf(IndexOutOfBoundsException.class);

        addValue(array, 12);
        addValue(array, 23);
        addValue(array, 34);
        checkElements(array, 12, 23, 34);

        setValue(array, 3, 45);
        checkElements(array, 12, 23, 34, 45);

        setValue(array, 1, 56);
        checkElements(array, 12, 56, 34, 45);

        setValue(array, 2, 67);
        checkElements(array, 12, 56, 67, 45);

        setValue(array, 0, 78);
        checkElements(array, 78, 56, 67, 45);
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmptyNumElementsAndClear() {

        final T array = createArray();

        assertThat(array).isEmpty();
        assertThat(array).hasLimit(0L);

        addValue(array, 12);
        assertThat(array).isNotEmpty();
        assertThat(array).hasLimit(1L);

        array.clear();
        assertThat(array).isEmpty();
        assertThat(array).hasLimit(0L);
    }

    final void checkElements(T array, int ... expectedElements) {

        final int expectedNumElements = expectedElements.length;

        assertThat(array.isEmpty()).isEqualTo(expectedNumElements == 0);
        assertThat(array).hasLimit(expectedNumElements);

        for (int i = 0; i < expectedNumElements; ++ i) {

            assertThat(getValue(array, i)).isEqualTo(expectedElements[i]);
        }
    }
}
