package dev.jdata.db.utils.adt.arrays;

import org.junit.Test;
import org.junit.experimental.categories.Category;

abstract class BaseLargeArrayTest<O, I, T extends LargeExponentArray<O, I> & IArray> extends BaseArrayTest<T> {

    abstract T createArray(int initialOuterCapacity, int innerCapacityExponent);

    @Test
    @Category(UnitTest.class)
    public final void testAllocateOuter() {

        final T array = createArray(0, 0);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(0);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(0);

        addValue(array, 12);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(1);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(1);

        checkElements(array, 12);

        addValue(array, 23);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(2);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(2);

        checkElements(array, 12, 23);

        addValue(array, 34);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(3);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(3);

        checkElements(array, 12, 23, 34);

        array.clear();

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(0);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(3);

        addValue(array, 12);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(1);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(3);

        checkElements(array, 12);

        addValue(array, 23);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(2);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(3);

        checkElements(array, 12, 23);

        addValue(array, 34);

        assertThat(array.getInnerCapacity()).isEqualTo(1);
        assertThat(array.getNumOuterUtilizedEntries()).isEqualTo(3);
        assertThat(array.getNumOuterAllocatedInnerArrays()).isEqualTo(3);

        checkElements(array, 12, 23, 34);
    }

    @Override
    final T createArray() {

        return createArray(0, 3);
    }
}
