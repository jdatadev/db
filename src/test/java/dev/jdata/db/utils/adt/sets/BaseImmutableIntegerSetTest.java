package dev.jdata.db.utils.adt.sets;

import org.junit.Test;
import org.junit.experimental.categories.Category;

abstract class BaseImmutableIntegerSetTest<T extends ISet> extends BaseIntegerSetTest<T> {

    abstract T createSet(int[] values);

    @Test
    @Category(UnitTest.class)
    public final void testNewAndContains() {

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final int[] array = new int[numElements];

            for (int i = 0; i < numElements; ++ i) {

                array[i] = i;
            }

            final T set = createSet(array);

            assertThat(set).isNotEmpty();
            assertThat(set).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(contains(set, i)).isTrue();
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testNewAndContainsWithOverwrite() {

        final int numIterations = 2;

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final int numToAdd = numElements * numIterations;

            final int[] array = new int[numToAdd];

            int dstIndex = 0;

            for (int i = 0; i < numIterations; ++ i) {

                for (int j = 0; j < numElements; ++ j) {

                    array[dstIndex ++] = j;
                }
            }

            final T set = createSet(array);

            assertThat(set).isNotEmpty();
            assertThat(set).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(contains(set, i)).isTrue();
            }
        }
    }
}
