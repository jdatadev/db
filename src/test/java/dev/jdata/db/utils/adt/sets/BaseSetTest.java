package dev.jdata.db.utils.adt.sets;


import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.hashed.BaseHashedTest;
abstract class BaseSetTest<T extends BaseIntegerSet<?>> extends BaseHashedTest {

    abstract T createSet(int initialCapacityExponent);
    abstract void add(T set, int value);
    abstract boolean contains(T set, int element);
    abstract boolean remove(T set, int element);

    @Test
    @Category(UnitTest.class)
    public final void testAddAndContainsWithOverwrite() {

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final T set = createSet(0);

            assertThat(set.isEmpty()).isTrue();
            assertThat(set.getNumElements()).isEqualTo(0);

            for (int i = 0; i < numElements; ++ i) {

                add(set, i);
            }

            assertThat(set.isEmpty()).isFalse();
            assertThat(set.getNumElements()).isEqualTo(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(contains(set, i)).isTrue();
            }

            assertThat(set.isEmpty()).isFalse();
            assertThat(set.getNumElements()).isEqualTo(numElements);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddContainsAndRemoveNewSet() {

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final T set = createSet(0);

            assertThat(set.isEmpty()).isTrue();

            for (int i = 0; i < numElements; ++ i) {

                add(set, i);

                assertThat(set.isEmpty()).isFalse();
                assertThat(set.getNumElements()).isEqualTo(i + 1);
            }

            assertThat(set.isEmpty()).isFalse();
            assertThat(set.getNumElements()).isEqualTo(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(contains(set, i)).isTrue();
            }

            assertThat(set.getNumElements()).isEqualTo(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(remove(set, i)).isTrue();

                assertThat(set.getNumElements()).isEqualTo(numElements - i - 1);
            }

            assertThat(set.isEmpty()).isTrue();
            assertThat(set.getNumElements()).isEqualTo(0);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(remove(set, i)).isFalse();

                assertThat(set.isEmpty()).isTrue();
                assertThat(set.getNumElements()).isEqualTo(0);
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddAndContainsWithClear() {

        final T set = createSet(0);

        assertThat(set.isEmpty()).isTrue();
        assertThat(set.getNumElements()).isEqualTo(0);

        for (int numElements = 1; numElements < MAX_ELEMENTS; numElements *= 10) {

            for (int i = 0; i < numElements; ++ i) {

                add(set, i);

                assertThat(set.isEmpty()).isFalse();
                assertThat(set.getNumElements()).isEqualTo(i + 1);
            }

            for (int i = 0; i < numElements; ++ i) {

                assertThat(contains(set, i)).isTrue();

                assertThat(set.isEmpty()).isFalse();
                assertThat(set.getNumElements()).isEqualTo(numElements);
            }

            set.clear();

            assertThat(set.isEmpty()).isTrue();
            assertThat(set.getNumElements()).isEqualTo(0);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final T set = createSet(0);

        assertThat(set.isEmpty()).isTrue();

        add(set, 1);

        assertThat(set.isEmpty()).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final T set = createSet(0);

        assertThat(set.getNumElements()).isEqualTo(0);

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            add(set, i);

            assertThat(set.getNumElements()).isEqualTo(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testClear() {

        final T set = createSet(0);

        assertThat(set.isEmpty()).isTrue();

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            add(set, i);
        }

        assertThat(set.isEmpty()).isFalse();

        set.clear();

        assertThat(set.isEmpty()).isTrue();
    }
}
