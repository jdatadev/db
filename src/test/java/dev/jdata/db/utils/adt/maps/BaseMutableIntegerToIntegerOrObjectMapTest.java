package dev.jdata.db.utils.adt.maps;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.elements.INonDistinct;
import dev.jdata.db.utils.adt.elements.IOrderedAddable;
import dev.jdata.db.utils.adt.sets.IHeapIntSet;
import dev.jdata.db.utils.adt.sets.IIntSet;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseMutableIntegerToIntegerOrObjectMapTest<

                KEYS_ARRAY,
                VALUES_ARRAY,
                KEYS_ORDERED_ADDABLE extends IOrderedAddable<?> & INonDistinct,
                VALUES_ORDERED_ADDABLE extends IOrderedAddable<?> & INonDistinct,
                MAP extends IMutableBaseMap<?>>

        extends BaseIntegerToIntegerOrObjectMapTest<KEYS_ARRAY, VALUES_ARRAY, KEYS_ORDERED_ADDABLE, VALUES_ORDERED_ADDABLE, MAP> {

    abstract MAP createMap(int initialCapacity);

    abstract boolean supportsRemoveNonAdded();

    abstract int put(MAP map, int key, int value, int defaultPreviousValue);

    abstract boolean remove(MAP map, int key);
    abstract int removeWithDefaultValue(MAP map, int key, int defaultValue);

    @Test
    @Category(UnitTest.class)
    public final void testPutAndGetWithOverwrite() {

        final boolean supportsGetWithDefaultValue = supportsGetWithDefaultValue();

        checkInitialCapacities(c -> {

            final MAP map = createMap(c);

            assertThat(map).isEmpty();
            assertThat(map).hasNumElements(0L);

            checkNumElements(n -> {

                for (int i = 0; i < n; ++ i) {

                    put(map, i);

                    assertThat(map).isNotEmpty();
                    assertThat(map).hasNumElements(i + 1);
                }

                for (int i = 0; i < n; ++ i) {

                    put(map, i, true);

                    assertThat(map).isNotEmpty();
                    assertThat(map).hasNumElements(n);
                }

                assertThat(map).hasNumElements(n);

                for (int i = 0; i < n; ++ i) {

                    final int getResult = supportsGetWithDefaultValue ? getWithDefaultValue(map, i, -1) : get(map, i);

                    assertThat(getResult).isEqualTo(value(i));

                    assertThat(map).isNotEmpty();
                    assertThat(map).hasNumElements(n);
                }

                assertThat(map).hasNumElements(n);

                checkGetKeys(map, n);

                map.clear();
            });
        });
    }

    @Test
    @Category(UnitTest.class)
    public final void testPutGetAndRemoveNewMap() {

        final boolean supportsGetWithDefaultValue = supportsGetWithDefaultValue();
        final boolean supportsRemoveNonAdded = supportsRemoveNonAdded();

        checkInitialCapacities(c -> {

            checkNumElements(n -> {

                final MAP map = createMap(c);

                assertThat(map).isEmpty();
                assertThat(map).hasNumElements(0);

                for (int i = 0; i < n; ++ i) {

                    put(map, i);

                    assertThat(map).isNotEmpty();
                    assertThat(map).hasNumElements(i + 1);
                }

                assertThat(map).hasNumElements(n);

                for (int i = 0; i < n; ++ i) {

                    final int getResult = supportsGetWithDefaultValue ? getWithDefaultValue(map, i, -1) : get(map, i);

                    assertThat(getResult).isEqualTo(value(i));

                    assertThat(map).isNotEmpty();
                    assertThat(map).hasNumElements(n);
                }

                checkGetKeys(map, n);

                for (int i = 0; i < n; ++ i) {

                    assertThat(remove(map, i)).isTrue();

                    assertThat(map.isEmpty()).isEqualTo(i == n - 1);
                    assertThat(map).hasNumElements(n - i - 1);
                }

                checkGetKeys(map, 0);

                for (int i = 0; i < n; ++ i) {

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
            });
        });
    }

    @Test
    @Category(UnitTest.class)
    public final void testPutAndGetWithClear() {

        final boolean supportsGetWithDefaultValue = supportsGetWithDefaultValue();

        checkInitialCapacities(c -> {

            final MAP map = createMap(c);

            assertThat(map).isEmpty();
            assertThat(map).hasNumElements(0L);

            checkNumElements(n -> {

                for (int i = 0; i < n; ++ i) {

                    put(map, i);

                    assertThat(map).isNotEmpty();
                    assertThat(map).hasNumElements(i + 1);
                }

                for (int i = 0; i < n; ++ i) {

                    final int getResult = supportsGetWithDefaultValue ? getWithDefaultValue(map, i, -1) : get(map, i);

                    assertThat(getResult).isEqualTo(value(i));

                    assertThat(map).isNotEmpty();
                    assertThat(map).hasNumElements(n);
                }

                checkGetKeys(map, n);

                map.clear();

                assertThat(map).isEmpty();
                assertThat(map).hasNumElements(0L);
            });
        });
    }

    @Test
    @Category(UnitTest.class)
    public final void testMutableGetNumElements() {

        checkInitialCapacities(c -> {

            final MAP map = createMap(c);

            assertThat(map).hasNumElements(0L);

            for (int i = 0; i < MAX_ELEMENTS; ++ i) {

                put(map, i);

                assertThat(map).hasNumElements(i + 1);
            }
        });
    }

    @Test
    @Category(UnitTest.class)
    public final void testPutPutRemovePut() {

        checkInitialCapacities(c -> {

            checkPutPutRemovePut(c, 0, 1, 0);
            checkPutPutRemovePut(c, 0, 1, 1);
        });
    }

    private void checkPutPutRemovePut(int initialCapacity, int key1, int key2, int removeKey) {

        final MAP map = createMap(initialCapacity);

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

            checkInitialCapacities(c -> {

                final MAP map = createMap(c);

                assertThat(map).isEmpty();

                assertThat(removeWithDefaultValue(map, 123, 345)).isEqualTo(345);

                put(map, 123, 234, -1);

                assertThat(map).isNotEmpty();
                assertThat(map).hasNumElements(1L);

                assertThat(removeWithDefaultValue(map, 123, 345)).isEqualTo(234);

                assertThat(map).isEmpty();
                assertThat(map).hasNumElements(0L);
            });
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testClear() {

        checkInitialCapacities(c -> {

            final MAP map = createMap(c);

            assertThat(map).isEmpty();

            for (int i = 0; i < MAX_ELEMENTS; ++ i) {

                put(map, i);
            }

            assertThat(map).isNotEmpty();

            map.clear();

            assertThat(map).isEmpty();
        });
    }

    @Override
    final MAP createMap(int[] keys, int[] values) {

        Checks.areSameLength(keys, values);

        final int keysLength = keys.length;

        final MAP map = createMap(keysLength);

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

    private void put(MAP map, int integer) {

        put(map, integer, false);
    }

    private void put(MAP map, int integer, boolean alreadyContainsValue) {

        final int defaultValue = -1;

        final int value = value(integer);

        assertThat(value).isNotEqualTo(defaultValue);

        final int previousValue = put(map, integer, value, defaultValue);

        assertThat(previousValue).isEqualTo(alreadyContainsValue ? value : defaultValue);
    }

    private void checkGetKeys(MAP map, int expectedNumElements) {

        final int[] keys = getKeys(map);

        assertThat(keys.length).isEqualTo(expectedNumElements);

        final IIntSet values = IHeapIntSet.of(keys);

        for (int i = 0; i < expectedNumElements; ++ i) {

            assertThat(values).contains(i);
        }
    }
}
