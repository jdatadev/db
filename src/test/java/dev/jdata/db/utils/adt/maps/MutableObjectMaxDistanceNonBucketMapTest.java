package dev.jdata.db.utils.adt.maps;

import java.util.List;

public final class MutableObjectMaxDistanceNonBucketMapTest

        extends BaseMutableObjectToIntegerOrObjectMaxDistanceMapTest<Integer, String, MutableObjectMaxDistanceNonBucketMap<Integer, String>> {

    @Override
    MutableObjectMaxDistanceNonBucketMap<Integer, String> createMap(int initialCapacityExponent) {

        return new MutableObjectMaxDistanceNonBucketMap<>(initialCapacityExponent, Integer[]::new, String[]::new);
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
    int getValue(String[] values, int index) {

        return valueToInteger(values[index]);
    }

    @Override
    int get(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, int key) {

        final String value = map.get(integerToKey(key), integerToValue(-1));

        return valueToInteger(value);
    }

    @Override
    int getWithDefaultValue(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, int key, int defaultValue) {

        final String value = map.get(integerToKey(key), integerToValue(defaultValue));

        return valueToInteger(value);
    }

    @Override
    <P> void forEachKeysAndValues(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeysAndValues(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(keyToInteger(k));
            valuesDst.add(valueToInteger(v));
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(MutableObjectMaxDistanceNonBucketMap<Integer, String> map, Integer[] keysDst, String[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
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
