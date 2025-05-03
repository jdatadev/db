package dev.jdata.db.utils.allocators;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.maps.MutableLongToObjectMaxDistanceNonBucketMap;

public interface ILongToObjectMaxDistanceMapAllocator<T> {

    MutableLongToObjectMaxDistanceNonBucketMap<T> allocateLongToObjectMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray);

    MutableLongToObjectMaxDistanceNonBucketMap<T> copyLongToObjectMap(MutableLongToObjectMaxDistanceNonBucketMap<T> toCopy);

    void freeLongToObjectMap(MutableLongToObjectMaxDistanceNonBucketMap<T> longToObjectMap);
}
