package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.sets.IMutableLongSet;
import dev.jdata.db.utils.adt.sets.IMutableSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongToObjectBucketMapTest

        extends BaseMutableLongToIntegerOrObjectBucketMapTest<Integer[], IMutableSet<Integer>, MutableLongToObjectBucketMap<Integer>> {

    @Override
    MutableLongToObjectBucketMap<Integer> createMap(int initialCapacityExponent) {

        return new HeapMutableLongToObjectBucketMap<>(AllocationType.HEAP, initialCapacityExponent, Integer[][]::new, Integer[]::new);
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
    int put(MutableLongToObjectBucketMap<Integer> map, int key, int value, int defaultPreviousValue) {

        return map.put(key, value, defaultPreviousValue);
    }

    @Override
    int removeWithDefaultValue(MutableLongToObjectBucketMap<Integer> map, int key, int defaultValue) {

        return map.removeAndReturnPrevious(key, defaultValue);
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
    int get(MutableLongToObjectBucketMap<Integer> map, int key) {

        throw new UnsupportedOperationException();
    }

    @Override
    int getWithDefaultValue(MutableLongToObjectBucketMap<Integer> map, int key, int defaultValue) {

        return map.get(key, defaultValue);
    }

    @Override
    <P> void forEachKeyAndValueWithNullFunction(MutableLongToObjectBucketMap<Integer> map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeyAndValue(MutableLongToObjectBucketMap<Integer> map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(Integers.checkLongToInt(k));
            valuesDst.add(v);
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(MutableLongToObjectBucketMap<Integer> map, IMutableLongSet keysAddable, IMutableSet<Integer> valuesAddable) {

        map.keysAndValues(keysAddable, valuesAddable);
    }
}
