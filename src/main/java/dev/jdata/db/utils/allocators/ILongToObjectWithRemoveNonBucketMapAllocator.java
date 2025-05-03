package dev.jdata.db.utils.allocators;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.maps.MutableLongToObjectWithRemoveNonBucketMap;

public interface ILongToObjectWithRemoveNonBucketMapAllocator<T> {

    MutableLongToObjectWithRemoveNonBucketMap<T> allocateLongToObjectMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray);

    void freeLongToObjectMap(MutableLongToObjectWithRemoveNonBucketMap<T> longToObjectMap);
}
