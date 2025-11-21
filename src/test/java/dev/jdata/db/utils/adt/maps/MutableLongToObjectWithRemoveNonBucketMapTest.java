package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.utils.adt.lists.IMutableList;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class MutableLongToObjectWithRemoveNonBucketMapTest

        extends BaseMutableLongToObjectNonContainsKeyNonBucketMapTest<Integer, MutableLongToObjectWithRemoveNonBucketMap<Integer>> {

    @Override
    boolean supportsContainsKey() {

        return false;
    }

    @Override
    MutableLongToObjectWithRemoveNonBucketMap<Integer> createMap(int initialCapacity) {

        return HeapMutableLongToObjectWithRemoveNonBucketMap.create(AllocationType.HEAP, initialCapacity, Integer[]::new);
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
