package dev.jdata.db.utils.adt.sets;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ObjIntConsumer;

import org.junit.Test;
import org.junit.experimental.categories.Category;

abstract class BaseMutableIntegerSetTest<T extends IMutableSet> extends BaseIntegerSetTest<T> {

    abstract T createSet(int initialCapacityExponent);

    abstract void add(T set, int value);
    abstract boolean addToSet(T set, int value);

    abstract boolean remove(T set, int value);

    @Test
    @Category(UnitTest.class)
    public final void testForEach() {

        checkForEach(this::add);
        checkForEach((s, e) -> assertThat(addToSet(s, e)).isTrue());
    }

    private void checkForEach(ObjIntConsumer<T> add) {

        final T argumentsSet = createSet(0);

        assertThatThrownBy(() -> forEach(argumentsSet, null, null)).isInstanceOf(NullPointerException.class);

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final T set = createSet(0);

            assertThat(set).isEmpty();
            assertThat(set).hasNumElements(0L);

            for (int i = 0; i < numElements; ++ i) {

                add.accept(set, i);
            }

            assertThat(set).isNotEmpty();
            assertThat(set).hasNumElements(numElements);

            final Object parameter = new Object();

            final Set<Integer> values = new HashSet<>(numElements);

            forEach(set, parameter, (e, p) -> {

                assertThat(p).isSameAs(parameter);

                final Integer integer = Integer.valueOf(e);

                assertThat(values.contains(integer)).isFalse();

                values.add(integer);
            });

            assertThat(set).isNotEmpty();
            assertThat(set).hasNumElements(numElements);

            assertThat(values).hasSize(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(values.contains(i)).isTrue();
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddAndContainsWithOverwrite() {

        checkAddToSetAndContainsWithOverwrite(this::add, this::add);
        checkAddToSetAndContainsWithOverwrite((s, e) -> assertThat(addToSet(s, e)).isTrue(), this::add);
        checkAddToSetAndContainsWithOverwrite(this::add, (s, e) -> assertThat(addToSet(s, e)).isFalse());
        checkAddToSetAndContainsWithOverwrite((s, e) -> assertThat(addToSet(s, e)).isTrue(), (s, e) -> assertThat(addToSet(s, e)).isFalse());
    }

    private void checkAddToSetAndContainsWithOverwrite(ObjIntConsumer<T> add1, ObjIntConsumer<T> add2) {

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final T set = createSet(0);

            assertThat(set).isEmpty();
            assertThat(set).hasNumElements(0L);

            for (int i = 0; i < numElements; ++ i) {

                add1.accept(set, i);
            }

            assertThat(set).isNotEmpty();
            assertThat(set).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(contains(set, i)).isTrue();
            }

            assertThat(set).isNotEmpty();
            assertThat(set).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                add2.accept(set, i);
            }

            assertThat(set).isNotEmpty();
            assertThat(set).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(contains(set, i)).isTrue();
            }

            assertThat(set).isNotEmpty();
            assertThat(set).hasNumElements(numElements);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddContainsAndRemoveNewSet() {

        checkAddContainsAndRemoveNewSet(this::add);
        checkAddContainsAndRemoveNewSet(this::addToSet);
    }

    private void checkAddContainsAndRemoveNewSet(ObjIntConsumer<T> add) {

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final T set = createSet(0);

            assertThat(set).isEmpty();

            for (int i = 0; i < numElements; ++ i) {

                add.accept(set, i);

                assertThat(set).isNotEmpty();
                assertThat(set).hasNumElements(i + 1);
            }

            assertThat(set).isNotEmpty();
            assertThat(set).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(contains(set, i)).isTrue();
            }

            assertThat(set).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(remove(set, i)).isTrue();

                assertThat(set).hasNumElements(numElements - i - 1);
            }

            assertThat(set).isEmpty();
            assertThat(set).hasNumElements(0L);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(remove(set, i)).isFalse();

                assertThat(set).isEmpty();
                assertThat(set).hasNumElements(0L);
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testAddAndContainsWithClear() {

        checkAddAndContainsWithClear(this::add);
        checkAddAndContainsWithClear(this::addToSet);
    }

    private void checkAddAndContainsWithClear(ObjIntConsumer<T> add) {

        final T set = createSet(0);

        assertThat(set).isEmpty();
        assertThat(set).hasNumElements(0L);

        for (int numElements = 1; numElements < MAX_ELEMENTS; numElements *= 10) {

            for (int i = 0; i < numElements; ++ i) {

                add.accept(set, i);

                assertThat(set).isNotEmpty();
                assertThat(set).hasNumElements(i + 1);
            }

            for (int i = 0; i < numElements; ++ i) {

                assertThat(contains(set, i)).isTrue();

                assertThat(set).isNotEmpty();
                assertThat(set).hasNumElements(numElements);
            }

            set.clear();

            assertThat(set).isEmpty();
            assertThat(set).hasNumElements(0L);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        checkIsEmpty(this::add);
        checkIsEmpty(this::addToSet);
    }

    private void checkIsEmpty(ObjIntConsumer<T> add) {

        final T set = createSet(0);

        assertThat(set).isEmpty();

        add.accept(set, 1);

        assertThat(set).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        checkGetNumElements(this::add);
        checkGetNumElements(this::addToSet);
    }

    private void checkGetNumElements(ObjIntConsumer<T> add) {

        final T set = createSet(0);

        assertThat(set).hasNumElements(0L);

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            add.accept(set, i);

            assertThat(set).hasNumElements(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testClear() {

        checkClear(this::add);
        checkClear(this::addToSet);
    }

    private void checkClear(ObjIntConsumer<T> add) {

        final T set = createSet(0);

        assertThat(set).isEmpty();

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            add.accept(set, i);
        }

        assertThat(set).isNotEmpty();

        set.clear();

        assertThat(set).isEmpty();
    }
}
