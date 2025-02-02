package dev.jdata.db.utils.adt.arrays;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.test.unit.BaseTest;

abstract class BaseLargeArrayTest<O, I, T extends LargeExponentArray> extends BaseTest {

    abstract T createArray(int initialOuterCapacity, int innerCapacityExponent);

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
        assertThatThrownBy(() -> setValue(array, 0, 12)).isInstanceOf(IndexOutOfBoundsException.class);

        addValue(array, 12);
        addValue(array, 23);
        addValue(array, 34);
        checkElements(array, 12, 23, 34);

        assertThatThrownBy(() -> setValue(array, 3, 45)).isInstanceOf(IndexOutOfBoundsException.class);

        setValue(array, 1, 45);
        checkElements(array, 12, 45, 34);

        setValue(array, 2, 56);
        checkElements(array, 12, 45, 56);

        setValue(array, 0, 67);
        checkElements(array, 67, 45, 56);
    }

    @Test
    @Category(UnitTest.class)
    public final void testAllocateOuter() {

        final T array = createArray(0, 0);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterEntries()).isEqualTo(0);
        assertThat(array.getNumOuterAllocatedEntries()).isEqualTo(0);

        addValue(array, 12);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterEntries()).isEqualTo(1);
        assertThat(array.getNumOuterAllocatedEntries()).isEqualTo(1);

        checkElements(array, 12);

        addValue(array, 23);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterEntries()).isEqualTo(2);
        assertThat(array.getNumOuterAllocatedEntries()).isEqualTo(2);

        checkElements(array, 12, 23);

        addValue(array, 34);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterEntries()).isEqualTo(3);
        assertThat(array.getNumOuterAllocatedEntries()).isEqualTo(3);

        checkElements(array, 12, 23, 34);

        array.clear();

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterEntries()).isEqualTo(0);
        assertThat(array.getNumOuterAllocatedEntries()).isEqualTo(3);

        addValue(array, 12);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterEntries()).isEqualTo(1);
        assertThat(array.getNumOuterAllocatedEntries()).isEqualTo(3);

        checkElements(array, 12);

        addValue(array, 23);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterEntries()).isEqualTo(2);
        assertThat(array.getNumOuterAllocatedEntries()).isEqualTo(3);

        checkElements(array, 12, 23);

        addValue(array, 34);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterEntries()).isEqualTo(3);
        assertThat(array.getNumOuterAllocatedEntries()).isEqualTo(3);

        checkElements(array, 12, 23, 34);
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmptyNumElementsAndClear() {

        final T array = createArray();

        assertThat(array).isEmpty();
        assertThat(array).hasNumElements(0L);

        addValue(array, 12);
        assertThat(array).isNotEmpty();
        assertThat(array).hasNumElements(1L);

        array.clear();
        assertThat(array).isEmpty();
        assertThat(array).hasNumElements(0L);
    }

    private void checkElements(T array, int ... expectedElements) {

        final int expectedNumElements = expectedElements.length;

        assertThat(array.isEmpty()).isEqualTo(expectedNumElements == 0);
        assertThat(array).hasNumElements(expectedNumElements);

        for (int i = 0; i < expectedNumElements; ++ i) {

            assertThat(getValue(array, i)).isEqualTo(expectedElements[i]);
        }
    }

    private T createArray() {

        return createArray(0, 3);
    }
}
