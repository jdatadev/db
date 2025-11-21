package dev.jdata.db.utils.adt.maps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.hashed.BaseHashedTest;
import dev.jdata.db.utils.adt.marker.IAnyOrderAddable;
import dev.jdata.db.utils.adt.sets.IHeapMutableIntSet;
import dev.jdata.db.utils.adt.sets.IHeapMutableLongSet;
import dev.jdata.db.utils.adt.sets.IHeapMutableSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableLongSet;
import dev.jdata.db.utils.adt.sets.IMutableSet;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseIntegerToIntegerOrObjectMapTest<

                KEYS_ARRAY,
                VALUES_ARRAY,
                KEYS_ADDABLE extends IAnyOrderAddable,
                VALUES_ADDABLE extends IAnyOrderAddable,
                MAP extends IMutableBaseMap<?>>

        extends BaseHashedTest {

    static int value(int key) {

        return key + 1000;
    }

    abstract MAP createMap(int[] keys, int[] values);

    abstract boolean supportsContainsKey();

    abstract KEYS_ARRAY createKeysArray(int length);
    abstract VALUES_ARRAY createValuesArray(int length);

    abstract KEYS_ADDABLE createKeysAddable(int initialCapacity);
    abstract VALUES_ADDABLE createValuesAddable(int initialCapacity);

    abstract KEYS_ARRAY keysToArray(KEYS_ADDABLE keysAddable);
    abstract VALUES_ARRAY valuesToArray(VALUES_ADDABLE valuesAddable);

    abstract int getKey(KEYS_ARRAY keys, int index);
    abstract int getValue(VALUES_ARRAY values, int index);

    abstract boolean containsKey(MAP map, int key);
    abstract int get(MAP map, int key);
    abstract int getWithDefaultValue(MAP map, int key, int defaultValue);
    abstract void getKeys(MAP map, KEYS_ADDABLE keysAddable);
    abstract <P> void forEachKeyAndValueWithNullFunction(MAP map, P parameter);
    abstract <P> void forEachKeyAndValue(MAP map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters);
    abstract void keysAndValues(MAP map, KEYS_ADDABLE keysAddable, VALUES_ADDABLE valuesAddable);

    @Test
    @Category(UnitTest.class)
    public final void testContainsKey() {

        if (supportsContainsKey()) {

            final int key = 123;
            final int value = 234;

            final MAP map = createMap(new int[] { key }, new int[] { value });

            assertThat(map).hasNumElements(1L);

            assertThat(key).isNotEqualTo(value);

            assertThat(containsKey(map, key)).isTrue();
            assertThat(containsKey(map, value)).isFalse();
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testGet() {

        final int key = 123;
        final int value = 234;

        final MAP map = createMap(new int[] { key }, new int[] { value });

        assertThat(map).hasNumElements(1L);

        assertThat(key).isNotEqualTo(value);

        final int getResult = supportsGetWithDefaultValue() ? getWithDefaultValue(map, key, -1) : get(map, key);
        assertThat(getResult).isEqualTo(234);
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetWithDefaultValue() {

        if (supportsGetWithDefaultValue()) {

            final int key = 123;
            final int value = 234;

            final MAP map = createMap(new int[] { key }, new int[] { value });

            assertThat(map).hasNumElements(1L);

            assertThat(key).isNotEqualTo(value);

            assertThat(getWithDefaultValue(map, 345, 456)).isEqualTo(456);
            assertThat(getWithDefaultValue(map, 123, 456)).isEqualTo(234);
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testKeys() {

        final int[] keys = new int[] { 123, 234, 345 };
        final int[] values = Array.closureOrConstantMapInt(keys, e -> value(e));

        final MAP map = createMap(keys, values);

        assertThat(getKeys(map)).containsExactlyInAnyOrder(123, 234, 345);
    }

    @Test
    @Category(UnitTest.class)
    public final void testForEachKeysAndValues() {

        checkForEachKeysAndValues(false);
        checkForEachKeysAndValues(true);
    }

    private void checkForEachKeysAndValues(boolean passParameter) {

        final MAP map = createMap(new int[] { 123, 345, 567 }, new int[] { 234, 456, 678 });

        final int numElements = 3;

        final BigDecimal parameter = passParameter ? new BigDecimal("789.01") : null;

        assertThatThrownBy(() -> forEachKeyAndValueWithNullFunction(map, parameter)).isInstanceOf(NullPointerException.class);

        final List<Integer> keysDst = new ArrayList<>(numElements);
        final List<Integer> valuesDst = new ArrayList<>(numElements);
        final List<BigDecimal> parametersDst = new ArrayList<>(numElements);

        forEachKeyAndValue(map, parameter, keysDst, valuesDst, parametersDst);

        for (int i = 0; i < numElements; ++ i) {

            final long expectedValue;

            switch (keysDst.get(i)) {

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

            assertThat(valuesDst.get(i)).isEqualTo(expectedValue);

            final BigDecimal listAddedParameter = parametersDst.get(i);

            if (passParameter) {

                assertThat(listAddedParameter).isSameAs(parameter);
            }
            else {
                assertThat(listAddedParameter).isNull();
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public final void testKeysAndValues() {

        final MAP map = createMap(new int[] { 123, 345, 567 }, new int[] { 234, 456, 678 });

        final int numElements = 3;

        assertThatThrownBy(() -> {

            final KEYS_ARRAY keysDst = createKeysArray(numElements + 1);
            final VALUES_ARRAY valuesDst = createValuesArray(numElements);

            keysAndValues(map, keysDst, valuesDst);
        })
        .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> {

            final KEYS_ARRAY keysDst = createKeysArray(numElements);
            final VALUES_ARRAY valuesDst = createValuesArray(numElements + 1);

            keysAndValues(map, keysDst, valuesDst);
        })
        .isInstanceOf(IllegalArgumentException.class);

        final KEYS_ARRAY keysDst = createKeysArray(numElements);
        final VALUES_ARRAY valuesDst = createValuesArray(numElements);

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

        final MAP emptyMap = createMapFromArrays(0);
        assertThat(emptyMap).isEmpty();

        final MAP nonEmptyMap = createMapFromArrays(1);
        assertThat(nonEmptyMap).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final MAP zeroLengthMap = createMapFromArrays(0);

        assertThat(zeroLengthMap).hasNumElements(0L);

        for (int numElements = 1; numElements < MAX_ELEMENTS; numElements *= 10) {

            final MAP map = createMapFromArrays(numElements);

            assertThat(map).hasNumElements(numElements);
        }
    }

    final boolean supportsGetWithDefaultValue() {

        return supportsContainsKey();
    }

    final IMutableIntSet createIntAddable(int initialCapacity) {

        return IHeapMutableIntSet.create(initialCapacity);
    }

    final IMutableLongSet createLongAddable(int initialCapacity) {

        Checks.isIntInitialCapacity(initialCapacity);

        return IHeapMutableLongSet.create(initialCapacity);
    }

    final <T> IMutableSet<T> createObjectAddable(int initialCapacity, IntFunction<T[]> createHashed) {

        Checks.isIntInitialCapacity(initialCapacity);
        Objects.requireNonNull(createHashed);

        return IHeapMutableSet.create(initialCapacity, createHashed);
    }

    final int[] toArray(IMutableIntSet intAddable) {

        Objects.requireNonNull(intAddable);

        return intAddable.toArray();
    }

    final long[] toArray(IMutableLongSet longAddable) {

        Objects.requireNonNull(longAddable);

        return longAddable.toArray();
    }

    final <T> T[] toArray(IMutableSet<T> objectAddable, IntFunction<T[]> createArray) {

        Objects.requireNonNull(objectAddable);
        Objects.requireNonNull(createArray);

        return objectAddable.toArray(createArray);
    }

    final int[] getKeys(MAP map) {

        final int numElements = IOnlyElementsView.intNumElements(map);

        final KEYS_ADDABLE keysAddable = createKeysAddable(numElements);

        getKeys(map, keysAddable);

        final KEYS_ARRAY keysArray = keysToArray(keysAddable);

        return keysToIntArray(keysArray, numElements);
    }

    private int[] keysToIntArray(KEYS_ARRAY keysArray, int numElements) {

        return Array.mapToInt(keysArray, numElements, this, (a, i, t) -> t.getKey(a, i));
    }

    private void keysAndValues(MAP map, KEYS_ARRAY keysArrayDst, VALUES_ARRAY valuesArrayDst) {

        final int numElements = IOnlyElementsView.intNumElements(map);

        final KEYS_ADDABLE keysAddable = createKeysAddable(numElements);
        final VALUES_ADDABLE valuesAddable = createValuesAddable(numElements);

        keysAndValues(map, keysAddable, valuesAddable);

        final KEYS_ARRAY keysArray = keysToArray(keysAddable);
        final VALUES_ARRAY valuesArray = valuesToArray(valuesAddable);

        System.arraycopy(keysArray, 0, keysArrayDst, 0, numElements);
        System.arraycopy(valuesArray, 0, valuesArrayDst, 0, numElements);
    }

    private MAP createMapFromArrays(int numElements) {

        final int[] keys = new int[numElements];
        final int[] values = new int[numElements];

        for (int i = 0; i < numElements; ++ i) {

            final int key = keys[i] = 123 + i;

            values[i] = value(key);
        }

        return createMap(keys, values);
    }
}
