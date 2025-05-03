package dev.jdata.db.utils.adt.maps;

import java.util.List;

public final class MutableIntToObjectMaxDistanceNonBucketMapTest

        extends BaseMutableIntToIntegerOrObjectMaxDistanceTest<Integer[], MutableIntToObjectMaxDistanceNonBucketMap<Integer>> {

    @Override
    MutableIntToObjectMaxDistanceNonBucketMap<Integer> createMap(int initialCapacityExponent) {

        return new MutableIntToObjectMaxDistanceNonBucketMap<>(initialCapacityExponent, Integer[]::new);
    }

    @Override
    int[] createKeysArray(int length) {

        return new int[length];
    }

    @Override
    Integer[] createValuesArray(int length) {

        return new Integer[length];
    }

    @Override
    int getValue(Integer[] values, int index) {

        return values[index];
    }

    @Override
    int get(MutableIntToObjectMaxDistanceNonBucketMap<Integer> map, int key) {

        return map.get(key, -1);
    }

    @Override
    int getWithDefaultValue(MutableIntToObjectMaxDistanceNonBucketMap<Integer> map, int key, int defaultValue) {

        return map.get(key, defaultValue);
    }

    @Override
    <P> void forEachKeysAndValues(MutableIntToObjectMaxDistanceNonBucketMap<Integer> map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeysAndValues(MutableIntToObjectMaxDistanceNonBucketMap<Integer> map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(k);
            valuesDst.add(v);
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(MutableIntToObjectMaxDistanceNonBucketMap<Integer> map, int[] keysDst, Integer[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    int put(MutableIntToObjectMaxDistanceNonBucketMap<Integer> map, int key, int value, int defaultPreviousValue) {

        return map.put(key, value, defaultPreviousValue);
    }

    @Override
    int removeWithDefaultValue(MutableIntToObjectMaxDistanceNonBucketMap<Integer> map, int key, int defaultValue) {

        return map.removeAndReturnPrevious(key, defaultValue);
    }
}
