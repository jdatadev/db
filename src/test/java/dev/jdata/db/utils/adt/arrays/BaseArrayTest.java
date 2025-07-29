package dev.jdata.db.utils.adt.arrays;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.IClearable;

abstract class BaseArrayTest<T extends IOneDimensionalArrayCommon & IClearable> extends BaseTest {

    abstract T createArray();
    abstract T createArray(int initialOuterCapacity, int initialInnerCapacityExponent);

    abstract T createClearArray(int clearValue);
    abstract T createClearArray(int initialOuterCapacity, int initialInnerCapacityExponent, int clearValue);

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
    public final void testAddIncreaseCapacity() {

        final T array = createArray(0, 0);

        final int numIterations = getNumIterations();
        final int offset = getOffset();

        for (int i = 0; i < numIterations; ++ i) {

            addValue(array, offset + i);

            for (int j = 0; j <= i; ++ j) {

                assertThat(getValue(array, j)).isEqualTo(offset + j);
            }
        }
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
    public final void testSetIncreaseCapacity() {

        final T array = createArray(0, 0);

        final int numIterations = getNumIterations();
        final int offset = getOffset();

        for (int i = 0; i < numIterations; ++ i) {

            setValue(array, i, offset + i);

            for (int j = 0; j <= i; ++ j) {

                assertThat(getValue(array, j)).isEqualTo(offset + j);
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testSetWithClear() {

        final int clearValue = 123;

        final T array = createClearArray(clearValue);

        setValue(array, 1L, 12);
        setValue(array, 3L, 23);
        setValue(array, 5L, 34);

        checkElements(array, clearValue, 12, clearValue, 23, clearValue, 34);
    }

    @Test
    @Category(UnitTest.class)
    public final void testSetWithClearAndIncreaseCapacity() {

        final int clearValue = 123;
        final int numIterations = getNumIterations();
        final int offset = getOffset();

        final int initialOuterCapacity = 1;
        final int innerCapacityExponent = 1;

        int i;

        final T setIndex0Array = createClearArray(initialOuterCapacity, innerCapacityExponent, clearValue);

        for (i = 0; i < numIterations; i += 2) {

            setValue(setIndex0Array, i, offset + i);

            for (int j = 0; j <= i; ++ j) {

                assertThat(getValue(setIndex0Array, j)).isEqualTo((j & 0x1) == 0 ? offset + j : clearValue);
            }
        }

        assertThat(setIndex0Array.getLimit()).isEqualTo(i - 1);

        final T setIndex1Array = createClearArray(initialOuterCapacity, innerCapacityExponent, clearValue);

        for (i = 0; i < numIterations; i += 2) {

            final int index = i + 1;

            setValue(setIndex1Array, index, offset + index);

            for (int j = 0; j <= index; ++ j) {

                assertThat(getValue(setIndex1Array, j)).isEqualTo((j & 0x1) == 1 ? offset + j : clearValue);
           }
        }

        assertThat(setIndex1Array.getLimit()).isEqualTo(i);
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

    int getNumIterations() {

        return 1000;
    }

    int getOffset() {

        return 1000;
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
