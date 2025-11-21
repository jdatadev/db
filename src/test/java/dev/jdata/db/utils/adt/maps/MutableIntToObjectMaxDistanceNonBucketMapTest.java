package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntToObjectMaxDistanceNonBucketMapTest

        extends BaseMutableIntToIntegerOrObjectMaxDistanceMapTest<Integer[], IMutableSet<Integer>, MutableIntToObjectMaxDistanceNonBucketMap<Integer>> {

    @Override
    MutableIntToObjectMaxDistanceNonBucketMap<Integer> createMap(int initialCapacityExponent) {

        return new HeapMutableIntToObjectMaxDistanceNonBucketMap<>(AllocationType.HEAP, initialCapacityExponent, Integer[]::new);
    }

    @Override
    Integer[] createValuesArray(int length) {

        return new Integer[length];
    }

    @Override
    IMutableSet<Integer> createValuesAddable(int initialCapacity) {

        return createObjectAddable(initialCapacity, Integer[]::new);
    }

    @Override
    Integer[] valuesToArray(IMutableSet<Integer> valuesAddable) {

        return toArray(valuesAddable, Integer[]::new);
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
    <P> void forEachKeyAndValueWithNullFunction(MutableIntToObjectMaxDistanceNonBucketMap<Integer> map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeyAndValue(MutableIntToObjectMaxDistanceNonBucketMap<Integer> map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(k);
            valuesDst.add(v);
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(MutableIntToObjectMaxDistanceNonBucketMap<Integer> map, IMutableIntSet keysAddable, IMutableSet<Integer> valuesAddable) {

        map.keysAndValues(keysAddable, valuesAddable);
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
