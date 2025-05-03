package dev.jdata.db.utils.adt.maps;

import java.util.List;

public final class MutableIntToIntMaxDistanceNonBucketMapTest extends BaseMutableIntToIntegerOrObjectMaxDistanceTest<int[], MutableIntToIntMaxDistanceNonBucketMap> {

    @Override
    MutableIntToIntMaxDistanceNonBucketMap createMap(int initialCapacityExponent) {

        return new MutableIntToIntMaxDistanceNonBucketMap(initialCapacityExponent);
    }

    @Override
    int[] createKeysArray(int length) {

        return new int[length];
    }

    @Override
    int[] createValuesArray(int length) {

        return new int[length];
    }

    @Override
    int getValue(int[] values, int index) {

        return values[index];
    }

    @Override
    int get(MutableIntToIntMaxDistanceNonBucketMap map, int key) {

        return map.get(key, -1);
    }

    @Override
    int getWithDefaultValue(MutableIntToIntMaxDistanceNonBucketMap map, int key, int defaultValue) {

        return map.get(key, defaultValue);
    }

    @Override
    <P> void forEachKeysAndValues(MutableIntToIntMaxDistanceNonBucketMap map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeysAndValues(MutableIntToIntMaxDistanceNonBucketMap map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(k);
            valuesDst.add(v);
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(MutableIntToIntMaxDistanceNonBucketMap map, int[] keysDst, int[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    int put(MutableIntToIntMaxDistanceNonBucketMap map, int key, int value, int defaultPreviousValue) {

        return map.put(key, value, defaultPreviousValue);
    }

    @Override
    int removeWithDefaultValue(MutableIntToIntMaxDistanceNonBucketMap map, int key, int defaultValue) {

        return map.removeAndReturnPrevious(key, defaultValue);
    }
}
