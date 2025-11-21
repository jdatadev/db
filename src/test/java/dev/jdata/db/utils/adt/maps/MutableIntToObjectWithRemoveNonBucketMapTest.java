package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.lists.IMutableList;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableIntToObjectWithRemoveNonBucketMapTest

        extends BaseMutableIntToObjectNonContainsKeyNonBucketMapTest<Integer, MutableIntToObjectWithRemoveNonBucketMap<Integer, ?>> {

    @Override
    int objectToInt(Integer object) {

        return object.intValue();
    }

    @Override
    Integer intToObject(int integer) {

        return Integer.valueOf(integer);
    }

    @Override
    MutableIntToObjectWithRemoveNonBucketMap<Integer, ?> createMap(int initialCapacity) {

        return HeapMutableIntToObjectWithRemoveNonBucketMap.create(AllocationType.HEAP, initialCapacity, Integer[]::new);
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
    boolean remove(MutableIntToObjectWithRemoveNonBucketMap<Integer, ?> map, int key) {

        map.remove(key);

        return true;
    }
}
