package dev.jdata.db.utils.adt.sets;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ObjIntConsumer;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.IClearable;

abstract class BaseMutableIntegerSetTest<T extends IMutableSetType & IClearable> extends BaseIntegerSetTest<T> {

    abstract T createSet(int initialCapacity);

    abstract void addInAnyOrder(T set, int value);
    abstract void addUnordered(T set, int value);
    abstract boolean addToSet(T set, int value);

    abstract boolean remove(T set, int value);

    @Test
    @Category(UnitTest.class)
    public final void testForEach() {

        checkInitialCapacities(c -> {

            checkForEach(c, this::addUnordered);
            checkForEach(c, (s, e) -> assertThat(addToSet(s, e)).isTrue());
        });
    }

    private void checkForEach(int initialCapacity, ObjIntConsumer<T> add) {

        final T argumentsSet = createSet(initialCapacity);

        assertThatThrownBy(() -> forEach(argumentsSet, null, null)).isInstanceOf(NullPointerException.class);

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final T set = createSet(initialCapacity);

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

        checkInitialCapacities(c -> {

            checkAddToSetAndContainsWithOverwrite(c, this::addUnordered, this::addUnordered);
            checkAddToSetAndContainsWithOverwrite(c, (s, e) -> assertThat(addToSet(s, e)).isTrue(), this::addUnordered);
            checkAddToSetAndContainsWithOverwrite(c, this::addUnordered, (s, e) -> assertThat(addToSet(s, e)).isFalse());
            checkAddToSetAndContainsWithOverwrite(c, (s, e) -> assertThat(addToSet(s, e)).isTrue(), (s, e) -> assertThat(addToSet(s, e)).isFalse());
        });
    }

    private void checkAddToSetAndContainsWithOverwrite(int initialCapacity, ObjIntConsumer<T> add1, ObjIntConsumer<T> add2) {

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final T set = createSet(initialCapacity);

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

        checkInitialCapacities(c -> {

            checkAddContainsAndRemoveNewSet(c, this::addUnordered);
            checkAddContainsAndRemoveNewSet(c, this::addToSet);
        });
    }

    private void checkAddContainsAndRemoveNewSet(int initialCapacity, ObjIntConsumer<T> add) {

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final T set = createSet(initialCapacity);

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

        checkInitialCapacities(c -> {

            checkAddAndContainsWithClear(c, this::addUnordered);
            checkAddAndContainsWithClear(c, this::addToSet);
        });
    }

    private void checkAddAndContainsWithClear(int initialCapacity, ObjIntConsumer<T> add) {

        final T set = createSet(initialCapacity);

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

        checkInitialCapacities(c -> {

            checkIsEmpty(c, this::addUnordered);
            checkIsEmpty(c, this::addToSet);
        });
    }

    private void checkIsEmpty(int initialCapacity, ObjIntConsumer<T> add) {

        final T set = createSet(initialCapacity);

        assertThat(set).isEmpty();

        add.accept(set, 1);

        assertThat(set).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        checkInitialCapacities(c -> {

            checkGetNumElements(c, this::addUnordered);
            checkGetNumElements(c, this::addToSet);
        });
    }

    private void checkGetNumElements(int initialCapacity, ObjIntConsumer<T> add) {

        final T set = createSet(initialCapacity);

        assertThat(set).hasNumElements(0L);

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            add.accept(set, i);

            assertThat(set).hasNumElements(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testClear() {

        checkInitialCapacities(c -> {

            checkClear(c, this::addUnordered);
            checkClear(c, this::addToSet);
        });
    }

    private void checkClear(int initialCapacity, ObjIntConsumer<T> add) {

        final T set = createSet(initialCapacity);

        assertThat(set).isEmpty();

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            add.accept(set, i);
        }

        assertThat(set).isNotEmpty();

        set.clear();

        assertThat(set).isEmpty();
    }
}
