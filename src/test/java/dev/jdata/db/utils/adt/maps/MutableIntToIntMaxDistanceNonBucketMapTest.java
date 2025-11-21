package dev.jdata.db.utils.adt.maps;

import java.util.List;

import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntToIntMaxDistanceNonBucketMapTest

        extends BaseMutableIntToIntegerOrObjectMaxDistanceMapTest<int[], IMutableIntSet, MutableIntToIntMaxDistanceNonBucketMap> {

    @Override
    MutableIntToIntMaxDistanceNonBucketMap createMap(int initialCapacityExponent) {

        return new HeapMutableIntToIntMaxDistanceNonBucketMap(AllocationType.HEAP, initialCapacityExponent);
    }

    @Override
    int[] createValuesArray(int length) {

        return new int[length];
    }

    @Override
    IMutableIntSet createValuesAddable(int initialCapacity) {

        return createIntAddable(initialCapacity);
    }

    @Override
    int[] valuesToArray(IMutableIntSet valuesAddable) {

        return toArray(valuesAddable);
    }

    @Override
    int getValue(int[] values, int index) {

        return values[index];
    }

    @Override
    int get(MutableIntToIntMaxDistanceNonBucketMap map, int key) {

        throw new UnsupportedOperationException();
    }

    @Override
    int getWithDefaultValue(MutableIntToIntMaxDistanceNonBucketMap map, int key, int defaultValue) {

        return map.get(key, defaultValue);
    }

    @Override
    <P> void forEachKeyAndValueWithNullFunction(MutableIntToIntMaxDistanceNonBucketMap map, P parameter) {

        map.forEachKeyAndValue(parameter, null);
    }

    @Override
    <P> void forEachKeyAndValue(MutableIntToIntMaxDistanceNonBucketMap map, P parameter, List<Integer> keysDst, List<Integer> valuesDst, List<P> parameters) {

        map.forEachKeyAndValue(parameter, (k, v, p) -> {

            keysDst.add(k);
            valuesDst.add(v);
            parameters.add(p);
        });
    }

    @Override
    void keysAndValues(MutableIntToIntMaxDistanceNonBucketMap map, IMutableIntSet keysAddable, IMutableIntSet valuesAddable) {

        map.keysAndValues(keysAddable, valuesAddable);
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
