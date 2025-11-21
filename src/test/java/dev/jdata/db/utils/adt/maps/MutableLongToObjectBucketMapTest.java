package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.lists.IMutableList;
import dev.jdata.db.utils.adt.lists.IMutableLongList;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.scalars.Integers;

public final class MutableLongToObjectBucketMapTest

        extends BaseMutableLongToIntegerOrObjectBucketMapTest<Integer[], IMutableList<Integer>, MutableLongToObjectBucketMap<Integer>> {

    @Override
    MutableLongToObjectBucketMap<Integer> createMap(int initialCapacity) {

        return HeapMutableLongToObjectBucketMap.create(AllocationType.HEAP, initialCapacity, Integer[][]::new, Integer[]::new);
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
    void keysAndValues(MutableLongToObjectBucketMap<Integer> map, IMutableLongList keysAddable, IMutableList<Integer> valuesAddable) {

        map.keysAndValues(keysAddable, valuesAddable);
    }
}
