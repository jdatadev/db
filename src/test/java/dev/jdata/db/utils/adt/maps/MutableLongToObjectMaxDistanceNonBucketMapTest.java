package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.lists.IMutableList;
import dev.jdata.db.utils.adt.lists.IMutableLongList;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongToObjectMaxDistanceNonBucketMapTest

        extends BaseMutableLongToIntegerOrObjectMaxDistanceMapTest<Integer[], IMutableList<Integer>, HeapMutableLongToObjectMaxDistanceNonBucketMap<Integer>> {

    @Override
    HeapMutableLongToObjectMaxDistanceNonBucketMap<Integer> createMap(int initialCapacity) {

        return HeapMutableLongToObjectMaxDistanceNonBucketMap.create(AllocationType.HEAP, initialCapacity, Integer[]::new);
    }

    @Override
    Integer[] createValuesArray(int length) {

        return new Integer[length];
    }

    @Override
    IMutableList<Integer> createValuesOrderedAddable(int initialCapacity) {

        return createObjectOrderedAddable(initialCapacity, Integer[]::new);
    }

    @Override
    Integer[] valuesToArray(IMutableList<Integer> valuesAddable) {

        return toArray(valuesAddable, Integer[]::new);
    }

    @Override
    int getValue(Integer[] values, int index) {

        return values[index];
    }

    @Override
    int get(HeapMutableLongToObjectMaxDistanceNonBucketMap<Integer> map, int key) {

        throw new UnsupportedOperationException();
    }

    @Override
    int getWithDefaultValue(HeapMutableLongToObjectMaxDistanceNonBucketMap<Integer> map, int key, int defaultValue) {

        return map.get(key, defaultValue);
    }

    @Override
    <P> void forEachKeyAndValueWithNullFunction(HeapMutableLongToObjectMaxDistanceNonBucketMap<Integer> map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeyAndValue(HeapMutableLongToObjectMaxDistanceNonBucketMap<Integer> map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(Integers.checkLongToInt(k));
            valuesDst.add(v);
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(HeapMutableLongToObjectMaxDistanceNonBucketMap<Integer> map, IMutableLongList keysAddable, IMutableList<Integer> valuesAddable) {

        map.keysAndValues(keysAddable, valuesAddable);
    }

    @Override
    int put(HeapMutableLongToObjectMaxDistanceNonBucketMap<Integer> map, int key, int value, int defaultPreviousValue) {

        return map.put(key, value, defaultPreviousValue);
    }

    @Override
    int removeWithDefaultValue(HeapMutableLongToObjectMaxDistanceNonBucketMap<Integer> map, int key, int defaultValue) {

        return map.removeAndReturnPrevious(key, defaultValue);
    }
}
