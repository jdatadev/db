package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.sets.IMutableSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableObjectMaxDistanceNonBucketMapTest

        extends BaseMutableObjectToIntegerOrObjectMaxDistanceMapTest<Integer, String[], IMutableSet<String>, MutableObjectMaxDistanceNonBucketMap<Integer, String>> {

    @Override
    MutableObjectMaxDistanceNonBucketMap<Integer, String> createMap(int initialCapacityExponent) {

        return new HeapMutableObjectMaxDistanceNonBucketMap<>(AllocationType.HEAP, initialCapacityExponent, Integer[]::new, String[]::new);
    }

    @Override
    Integer[] createKeysArray(int length) {

        return new Integer[length];
    }

    @Override
    String[] createValuesArray(int length) {

        return new String[length];
    }

    @Override
    IMutableSet<Integer> createKeysAddable(int initialCapacity) {

        return createObjectAddable(initialCapacity, Integer[]::new);
    }

    @Override
    IMutableSet<String> createValuesAddable(int initialCapacity) {

        return createObjectAddable(initialCapacity, String[]::new);
    }

    @Override
    Integer[] keysToArray(IMutableSet<Integer> keysAddable) {

        return toArray(keysAddable, Integer[]::new);
    }

    @Override
    String[] valuesToArray(IMutableSet<String> valuesAddable) {

        return toArray(valuesAddable, String[]::new);
    }

    @Override
    int getValue(String[] values, int index) {

        return valueToInteger(values[index]);
    }

    @Override
    int get(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, int key) {

        throw new UnsupportedOperationException();
    }

    @Override
    int getWithDefaultValue(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, int key, int defaultValue) {

        final String value = map.get(integerToKey(key), integerToValue(defaultValue));

        return valueToInteger(value);
    }

    @Override
    <P> void forEachKeyAndValueWithNullFunction(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeyAndValue(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(keyToInteger(k));
            valuesDst.add(valueToInteger(v));
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, IMutableSet<Integer> keysAddable, IMutableSet<String> valuesAddable) {

        map.keysAndValues(keysAddable, valuesAddable);
    }

    @Override
    int put(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, int key, int value, int defaultPreviousValue) {

        final String previousValue = map.put(integerToKey(key), integerToValue(value), integerToValue(defaultPreviousValue));

        return valueToInteger(previousValue);
    }

    @Override
    int removeWithDefaultValue(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, int key, int defaultValue) {

        final String previousValue = map.removeAndReturnPrevious(integerToKey(key), integerToValue(defaultValue));

        return valueToInteger(previousValue);
    }

    @Override
    Integer integerToKey(int key) {

        return Integer.valueOf(key);
    }

    @Override
    int keyToInteger(Integer key) {

        return key.intValue();
    }

    private static String integerToValue(int integer) {

        return String.valueOf(integer);
    }

    private static int valueToInteger(String value) {

        return Integer.parseInt(value);
    }
}
