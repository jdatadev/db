package dev.jdata.db.utils.adt.maps;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import dev.jdata.db.utils.adt.IClearable;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.hashed.BaseHashedTest;

abstract class BaseIntegerToIntegerOrObjectMapTest<K, V, M extends IKeyMap<K> & IClearable> extends BaseHashedTest {

    static int value(int key) {

        return key + 1000;
    }

    abstract M createMap(int[] keys, int[] values);

    abstract boolean supportsContainsKey();

    abstract K createKeysArray(int length);
    abstract V createValuesArray(int length);

    abstract int getKey(K keys, int index);
    abstract int getValue(V values, int index);

    abstract boolean containsKey(M map, int key);
    abstract int get(M map, int key);
    abstract int getWithDefaultValue(M map, int key, int defaultValue);
    abstract int[] getKeys(M map);
    abstract <P> void forEachKeyAndValueWithNullFunction(M map, P parameter);
    abstract <P> void forEachKeyAndValue(M map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters);
    abstract void keysAndValues(M map, K keysDst, V valuesDst);

    @Test
    @Category(UnitTest.class)
    public final void testContainsKey() {

        if (supportsContainsKey()) {

            final int key = 123;
            final int value = 234;

            final M map = createMap(new int[] { key }, new int[] { value });

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

        final M map = createMap(new int[] { key }, new int[] { value });

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

            final M map = createMap(new int[] { key }, new int[] { value });

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

        final M map = createMap(keys, values);

        assertThat(getKeys(map)).containsExactlyInAnyOrder(123, 234, 345);
    }

    @Test
    @Category(UnitTest.class)
    public final void testForEachKeysAndValues() {

        checkForEachKeysAndValues(false);
        checkForEachKeysAndValues(true);
    }

    private void checkForEachKeysAndValues(boolean passParameter) {

        final M map = createMap(new int[] { 123, 345, 567 }, new int[] { 234, 456, 678 });

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

        final M map = createMap(new int[] { 123, 345, 567 }, new int[] { 234, 456, 678 });

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

        final M emptyMap = createMapFromArrays(0);
        assertThat(emptyMap).isEmpty();

        final M nonEmptyMap = createMapFromArrays(1);
        assertThat(nonEmptyMap).isNotEmpty();
    }

    @Test
    @Category(UnitTest.class)
    public final void testGetNumElements() {

        final M zeroLengthMap = createMapFromArrays(0);

        assertThat(zeroLengthMap).hasNumElements(0L);

        for (int numElements = 1; numElements < MAX_ELEMENTS; numElements *= 10) {

            final M map = createMapFromArrays(numElements);

            assertThat(map).hasNumElements(numElements);
        }
    }

    final boolean supportsGetWithDefaultValue() {

        return supportsContainsKey();
    }

    private M createMapFromArrays(int numElements) {

        final int[] keys = new int[numElements];
        final int[] values = new int[numElements];

        for (int i = 0; i < numElements; ++ i) {

            final int key = keys[i] = 123 + i;

            values[i] = value(key);
        }

        return createMap(keys, values);
    }
}
