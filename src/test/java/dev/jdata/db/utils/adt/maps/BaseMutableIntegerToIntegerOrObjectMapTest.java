package dev.jdata.db.utils.adt.maps;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.CapacityExponents;
import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseMutableIntegerToIntegerOrObjectMapTest<K, V, M extends IKeyMap<K> & IClearable> extends BaseIntegerToIntegerOrObjectMapTest<K, V, M> {

    abstract M createMap(int initialCapacityExponent);

    abstract boolean supportsRemoveNonAdded();

    abstract int put(M map, int key, int value, int defaultPreviousValue);

    abstract boolean remove(M map, int key);
    abstract int removeWithDefaultValue(M map, int key, int defaultValue);

    @Test
    @Category(UnitTest.class)
    public final void testPutAndGetWithOverwrite() {

        final M map = createMap(0);

        assertThat(map).isEmpty();
        assertThat(map).hasNumElements(0L);

        final boolean supportsGetWithDefaultValue = supportsGetWithDefaultValue();

        for (int numElements = 1; numElements <= MAX_ELEMENTS; numElements *= 10) {

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(i + 1);
            }

            for (int i = 0; i < numElements; ++ i) {

                put(map, i, true);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(numElements);
            }

            assertThat(map).hasNumElements(numElements);

            for (int i = 0; i < numElements; ++ i) {

                final int getResult = supportsGetWithDefaultValue ? getWithDefaultValue(map, i, -1) : get(map, i);

                assertThat(getResult).isEqualTo(value(i));

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

        final boolean supportsGetWithDefaultValue = supportsGetWithDefaultValue();
        final boolean supportsRemoveNonAdded = supportsRemoveNonAdded();

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

                final int getResult = supportsGetWithDefaultValue ? getWithDefaultValue(map, i, -1) : get(map, i);

                assertThat(getResult).isEqualTo(value(i));

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

                if (supportsRemoveNonAdded) {

                    assertThat(remove(map, i)).isFalse();
                }
                else {
                    final int closureI = i;

                    assertThatThrownBy(() -> remove(map, closureI)).isInstanceOf(IllegalStateException.class);
                }

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

        final boolean supportsGetWithDefaultValue = supportsGetWithDefaultValue();

        for (int numElements = 1; numElements < MAX_ELEMENTS; numElements *= 10) {

            for (int i = 0; i < numElements; ++ i) {

                put(map, i);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(i + 1);
            }

            for (int i = 0; i < numElements; ++ i) {

                final int getResult = supportsGetWithDefaultValue ? getWithDefaultValue(map, i, -1) : get(map, i);

                assertThat(getResult).isEqualTo(value(i));

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
    public final void testMutableGetNumElements() {

        final M map = createMap(0);

        assertThat(map).hasNumElements(0L);

        for (int i = 0; i < MAX_ELEMENTS; ++ i) {

            put(map, i);

            assertThat(map).hasNumElements(i + 1);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testPutPutRemovePut() {

        checkPutPutRemovePut(0, 1, 0);
        checkPutPutRemovePut(0, 1, 1);
    }

    private void checkPutPutRemovePut(int key1, int key2, int removeKey) {

        final M map = createMap(0);

        assertThat(map).isEmpty();

        put(map, key1, 123, -1);
        put(map, key2, 234, -1);

        assertThat(map).isNotEmpty();
        assertThat(map).hasNumElements(2L);

        if (supportsRemoveNonAdded()) {

            assertThat(removeWithDefaultValue(map, removeKey, -1)).isEqualTo(removeKey == key1 ? 123 : 234);
        }
        else {
            remove(map, removeKey);
        }

        assertThat(map).isNotEmpty();
        assertThat(map).hasNumElements(1L);

        final int key = removeKey == key1 ? key2 : key1;
        final int getResult = supportsGetWithDefaultValue() ? getWithDefaultValue(map, key, -1) : get(map, key);

        assertThat(getResult).isEqualTo(removeKey == key1 ? 234 : 123);

        put(map, removeKey == key1 ? key1 : key2, removeKey == key1 ? 123 : 234, -1);

        assertThat(map).isNotEmpty();
        assertThat(map).hasNumElements(2L);

        final int key1GetResult = supportsGetWithDefaultValue() ? getWithDefaultValue(map, key1, -1) : get(map, key1);
        assertThat(key1GetResult).isEqualTo(123);

        final int key2GetResult = supportsGetWithDefaultValue() ? getWithDefaultValue(map, key2, -1) : get(map, key2);
        assertThat(key2GetResult).isEqualTo(234);
    }

    @Test
    @Category(UnitTest.class)
    public final void testRemoveAndReturnDefaultValue() {

        if (supportsRemoveNonAdded()) {

            final M map = createMap(0);

            assertThat(map).isEmpty();

            assertThat(removeWithDefaultValue(map, 123, 345)).isEqualTo(345);

            put(map, 123, 234, -1);

            assertThat(map).isNotEmpty();
            assertThat(map).hasNumElements(1L);

            assertThat(removeWithDefaultValue(map, 123, 345)).isEqualTo(234);

            assertThat(map).isEmpty();
            assertThat(map).hasNumElements(0L);
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

    @Override
    final M createMap(int[] keys, int[] values) {

        Checks.areSameLength(keys, values);

        final int keysLength = keys.length;

        final M map = createMap(CapacityExponents.computeCapacityExponent(keysLength));

        final int defaultValue = -1;
        final boolean supportsContainsKey = supportsContainsKey();

        for (int i = 0; i < keysLength; ++ i) {

            final int previousValue = put(map, keys[i], values[i], defaultValue);

            if (supportsContainsKey) {

                assertThat(previousValue).isEqualTo(defaultValue);
            }
        }

        return map;
    }

    private void put(M map, int integer) {

        put(map, integer, false);
    }

    private void put(M map, int integer, boolean alreadyContainsValue) {

        final int defaultValue = -1;

        final int value = value(integer);

        assertThat(value).isNotEqualTo(defaultValue);

        final int previousValue = put(map, integer, value, defaultValue);

        assertThat(previousValue).isEqualTo(alreadyContainsValue ? value : defaultValue);
    }

    private void checkGetKeys(M map, int numElements) {

        final int[] keys = getKeys(map);

        assertThat(keys.length).isEqualTo(numElements);

        final MutableIntBucketSet values = MutableIntBucketSet.of(keys);

        for (int i = 0; i < numElements; ++ i) {

            assertThat(values).contains(i);
        }
    }
}
