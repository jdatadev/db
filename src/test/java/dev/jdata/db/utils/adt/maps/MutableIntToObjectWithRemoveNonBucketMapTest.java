package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.sets.IMutableSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntToObjectWithRemoveNonBucketMapTest

        extends BaseMutableIntToObjectNonContainsKeyNonBucketMapTest<Integer, MutableIntToObjectWithRemoveNonBucketMap<Integer>> {

    @Override
    int objectToInt(Integer object) {

        return object.intValue();
    }

    @Override
    Integer intToObject(int integer) {

        return Integer.valueOf(integer);
    }

    @Override
    MutableIntToObjectWithRemoveNonBucketMap<Integer> createMap(int initialCapacityExponent) {

        return new HeapMutableIntToObjectWithRemoveNonBucketMap<>(AllocationType.HEAP, initialCapacityExponent, Integer[]::new);
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
    boolean remove(MutableIntToObjectWithRemoveNonBucketMap<Integer> map, int key) {

        map.remove(key);

        return true;
    }
}
