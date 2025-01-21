package dev.jdata.db.utils.adt.maps;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.hashed.BaseHashedTest;
import dev.jdata.db.utils.adt.sets.IntSet;

abstract class BaseIntegerToIntegerOrObjectTest<K, V, M extends KeyMap<K>> extends BaseHashedTest {

    abstract M createMap(int initialCapacity);

    abstract K createKeysArray(int length);
    abstract V createValuesArray(int length);

    abstract int getKey(K keys, int index);
    abstract int getValue(V values, int index);

    abstract boolean contains(M map, int key);
    abstract int get(M map, int key);
    abstract int[] getKeys(M map);
    abstract void keysAndValues(M map, K keysDst, V valuesDst);
    abstract void put(M map, int key, int value);
    abstract boolean remove(M map, int key);

    @Test
    @Category(UnitTest.class)
    public final void testPutAndGetWithOverwrite() {

        final M map = createMap(0);

        assertThat(map).isEmpty();
        assertThat(map).hasNumElements(0L);

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(i + 1);
            }

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(numElements);
            }

            assertThat(map).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(get(map, i)).isEqualTo(value(i));

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(numElements);
            }

            assertThat(map).hasNumElements(numElements);

            checkGetKeys(map, numElements);

            map.clear();
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testPutGetAndRemoveNewMap() {

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            final M map = createMap(0);

            assertThat(map).isEmpty();
            assertThat(map).hasNumElements(0);

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(i + 1);
            }

            assertThat(map).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(get(map, i)).isEqualTo(value(i));

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(numElements);
            }

            checkGetKeys(map, numElements);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(remove(map, i)).isTrue();

                assertThat(map.isEmpty()).isEqualTo(i == numElements - 1);
                assertThat(map).hasNumElements(numElements - i - 1);
            }

            checkGetKeys(map, 0);

            for (int i = 0; i < numElements; ++ i) {

                assertThat(remove(map, i)).isFalse();

                assertThat(map).isEmpty();
                assertThat(map).hasNumElements(0L);
            }

            checkGetKeys(map, 0);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testPutAndGetWithClear() {

        final M map = createMap(0);

        assertThat(map).isEmpty();
        assertThat(map).hasNumElements(0L);

        for (int numElements = 1; numElements < MAX_ELEMENTS; numElements *= 10) {

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(i + 1);
            }

            for (int i = 0; i < numElements; ++ i) {

                assertThat(get(map, i)).isEqualTo(value(i));

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(numElements);
            }

            checkGetKeys(map, numElements);

            map.clear();

            assertThat(map).isEmpty();
            assertThat(map).hasNumElements(0L);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testContainsKey() {

        final M map = createMap(0);

        assertThat(map).isEmpty();

        put(map, 123);

        assertThat(contains(map, 123)).isTrue();
        assertThat(contains(map, 234)).isFalse();
    }

    @Test
    @Category(UnitTest.class)
    public final void testKeys() {

        final M map = createMap(0);

        assertThat(map).isEmpty();

        put(map, 123);
        put(map, 234);
        put(map, 345);

        assertThat(getKeys(map)).containsExactlyInAnyOrder(123, 234, 345);
    }

    @Test
    @Category(UnitTest.class)
    public final void testKeysAndValues() {

        final M map = createMap(0);

        assertThat(map).isEmpty();

        put(map, 123, 234);
        put(map, 345, 456);
        put(map, 567, 678);

        final int numElements = 3;

        assertThatThrownBy(() -> {

            final K keysDst = createKeysArray(numElements + 1);
            final V valuesDst = createValuesArray(numElements);

            keysAndValues(map, keysDst, valuesDst);
        })
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {

            final K keysDst = createKeysArray(numElements);
            final V valuesDst = createValuesArray(numElements + 1);

            keysAndValues(map, keysDst, valuesDst);
        })
        .isInstanceOf(IllegalArgumentException.class);

        final K keysDst = createKeysArray(numElements);
        final V valuesDst = createValuesArray(numElements);

        keysAndValues(map, keysDst, valuesDst);

        for (int i = 0; i < numElements; ++ i) {

            final long expectedValue;

            switch (getKey(keysDst, i)) {

            case 123:

                expectedValue = 234L;
                break;

            case 345:

                expectedValue = 456L;
                break;

            case 567:

                expectedValue = 678L;
                break;

            default:
                throw new UnsupportedOperationException();
            }

            assertThat(getValue(valuesDst, i)).isEqualTo(expectedValue);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testIsEmpty() {

        final M map = createMap(0);

        assertThat(map).isEmpty();

        put(map, 1);

        assertThat(map).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final M map = createMap(0);

        assertThat(map).hasNumElements(0L);

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            put(map, i);

            assertThat(map).hasNumElements(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testClear() {

        final M map = createMap(0);

        assertThat(map).isEmpty();

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            put(map, i);
        }

        assertThat(map).isNotEmpty();

        map.clear();

        assertThat(map).isEmpty();
    }

    private void checkGetKeys(M map, int numElements) {

        final int[] keys = getKeys(map);

        assertThat(keys.length).isEqualTo(numElements);

        final IntSet values = IntSet.of(keys);

        for (int i = 0; i < numElements; ++ i) {

            assertThat(values).contains(i);
        }
    }

    private void put(M map, int integer) {

        put(map, integer, value(integer));
    }

    private static int value(int key) {

        return key + 1000;
    }
}
