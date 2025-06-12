package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.arrays.Array;

public final class MutableObjectWithRemoveNonBucketObjectMapTest

        extends BaseMutableIntegerToIntegerOrObjectMapTest<String[], StringBuilder[], MutableObjectWithRemoveNonBucketMap<String, StringBuilder>> {

    @Override
    boolean supportsContainsKey() {

        return false;
    }

    @Override
    boolean supportsRemoveNonAdded() {

        return false;
    }

    @Override
    int getWithDefaultValue(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }

    @Override
    MutableObjectWithRemoveNonBucketMap<String, StringBuilder> createMap(int initialCapacityExponent) {

        return new MutableObjectWithRemoveNonBucketMap<>(initialCapacityExponent, String[]::new, StringBuilder[]::new);
    }

    @Override
    String[] createKeysArray(int length) {

        return new String[length];
    }

    @Override
    StringBuilder[] createValuesArray(int length) {

        return new StringBuilder[length];
    }

    @Override
    int getKey(String[] keys, int index) {

        return intKey(keys[index]);
    }

    @Override
    int getValue(StringBuilder[] values, int index) {

        return intValue(values[index]);
    }

    @Override
    boolean containsKey(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, int key) {

        throw new UnsupportedOperationException();
    }

    @Override
    int get(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, int key) {

        return intValue(map.get(stringKey(key)));
    }

    @Override
    int[] getKeys(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map) {

        return Array.closureOrConstantMapToInt(map.keys(), k -> intKey(k));
    }

    @Override
    <P> void forEachKeysAndValues(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeysAndValues(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, P parameter, List<Integer> keysDst, List<Integer> valuesDst,
            List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(intKey(k));
            valuesDst.add(intValue(v));
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, String[] keysDst, StringBuilder[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    int put(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, int key, int value, int defaultPreviousValue) {

        final StringBuilder previousValue = map.put(stringKey(key), stringBuilderValue(value), stringBuilderValue(defaultPreviousValue));

        return previousValue != null ? intValue(previousValue) : defaultPreviousValue;
    }

    @Override
    boolean remove(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, int key) {

        map.remove(stringKey(key));

        return true;
    }

    @Override
    int removeWithDefaultValue(MutableObjectWithRemoveNonBucketMap<String, StringBuilder> map, int key, int defaultValue) {

        throw new UnsupportedOperationException();
    }

    private static String stringKey(int key) {

        return String.valueOf(key);
    }

    private static StringBuilder stringBuilderValue(int value) {

        return new StringBuilder().append(value);
    }

    private static int intKey(String key) {

        return parseInt(key);
    }

    private static int intValue(StringBuilder value) {

        return parseInt(value);
    }

    private static int parseInt(CharSequence charSequence) {

        return Integer.parseInt(charSequence, 0, charSequence.length(), 10);
    }
}
