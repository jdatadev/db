package dev.jdata.db.utils.adt.arrays;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.IClearable;

abstract class BaseLargeArrayTest<O, I, T extends ExponentLargeArray<O, I> & IOneDimensionalArrayCommon & IClearable> extends BaseArrayTest<T> {

    private static final int INITIAL_OUTER_CAPACITY = 0;
    private static final int INNER_CAPACITY_EXPONENT = 3;

    abstract T createArray(int initialOuterCapacity, int innerCapacityExponent, int clearValue);

    @Test
    @Category(UnitTest.class)
    public final void testAllocateOuter() {

        final T array = createArray(0, 0);

        assertThat(array.getInnerElementCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(0);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(0);

        addValue(array, 12);

        assertThat(array.getInnerElementCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(1);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(1);

        checkElements(array, 12);

        addValue(array, 23);

        assertThat(array.getInnerElementCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(2);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(2);

        checkElements(array, 12, 23);

        addValue(array, 34);

        assertThat(array.getInnerElementCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(3);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(3);

        checkElements(array, 12, 23, 34);

        array.clear();

        assertThat(array.getInnerElementCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(0);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(3);

        addValue(array, 12);

        assertThat(array.getInnerElementCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(1);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(3);

        checkElements(array, 12);

        addValue(array, 23);

        assertThat(array.getInnerElementCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(2);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(3);

        checkElements(array, 12, 23);

        addValue(array, 34);

        assertThat(array.getInnerElementCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(3);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(3);

        checkElements(array, 12, 23, 34);
    }

    @Test
    @Category(UnitTest.class)
    public final void testSetLargeIndices() {

        // TODO: sparse arrays
        throw new UnsupportedOperationException();
    }

    @Override
    final T createArray() {

        return createArray(INITIAL_OUTER_CAPACITY, INNER_CAPACITY_EXPONENT);
    }

    @Override
    final T createClearArray(int clearValue) {

        return createArray(INITIAL_OUTER_CAPACITY, INNER_CAPACITY_EXPONENT, clearValue);
    }

    @Override
    final T createClearArray(int initialOuterCapacity, int initialInnerCapacityExponent, int clearValue) {

        return createArray(initialOuterCapacity, initialInnerCapacityExponent, clearValue);
    }
}
