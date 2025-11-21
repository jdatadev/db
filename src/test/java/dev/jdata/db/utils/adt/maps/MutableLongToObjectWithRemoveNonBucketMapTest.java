package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.sets.IMutableSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableLongToObjectWithRemoveNonBucketMapTest

        extends BaseMutableLongToObjectNonContainsKeyNonBucketMapTest<Integer, MutableLongToObjectWithRemoveNonBucketMap<Integer>> {

    @Override
    boolean supportsContainsKey() {

        return false;
    }

    @Override
    MutableLongToObjectWithRemoveNonBucketMap<Integer> createMap(int initialCapacityExponent) {

        return new HeapMutableLongToObjectWithRemoveNonBucketMap<>(AllocationType.HEAP, initialCapacityExponent, Integer[]::new);
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
    boolean remove(MutableLongToObjectWithRemoveNonBucketMap<Integer> map, int key) {

        map.remove(key);

        return true;
    }

    @Override
    int objectToInt(Integer object) {

        return object.intValue();
    }

    @Override
    Integer intToObject(int integer) {

        return Integer.valueOf(integer);
    }
}
