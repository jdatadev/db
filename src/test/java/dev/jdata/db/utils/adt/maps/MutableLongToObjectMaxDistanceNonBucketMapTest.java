package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongToObjectMaxDistanceNonBucketMapTest

        extends BaseMutableLongToIntegerOrObjectMaxDistanceMapTest<Integer[], MutableLongToObjectMaxDistanceNonBucketMap<Integer>> {

    @Override
    MutableLongToObjectMaxDistanceNonBucketMap<Integer> createMap(int initialCapacityExponent) {

        return new MutableLongToObjectMaxDistanceNonBucketMap<>(initialCapacityExponent, Integer[]::new);
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
    int get(MutableLongToObjectMaxDistanceNonBucketMap<Integer> map, int key) {

        throw new UnsupportedOperationException();
    }

    @Override
    int getWithDefaultValue(MutableLongToObjectMaxDistanceNonBucketMap<Integer> map, int key, int defaultValue) {

        return map.get(key, defaultValue);
    }

    @Override
    <P> void forEachKeyAndValueWithNullFunction(MutableLongToObjectMaxDistanceNonBucketMap<Integer> map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeyAndValue(MutableLongToObjectMaxDistanceNonBucketMap<Integer> map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(Integers.checkLongToInt(k));
            valuesDst.add(v);
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(MutableLongToObjectMaxDistanceNonBucketMap<Integer> map, long[] keysDst, Integer[] valuesDst) {

        map.keysAndValues(keysDst, valuesDst);
    }

    @Override
    int put(MutableLongToObjectMaxDistanceNonBucketMap<Integer> map, int key, int value, int defaultPreviousValue) {

        return map.put(key, value, defaultPreviousValue);
    }

    @Override
    int removeWithDefaultValue(MutableLongToObjectMaxDistanceNonBucketMap<Integer> map, int key, int defaultValue) {

        return map.removeAndReturnPrevious(key, defaultValue);
    }
}
