package dev.jdata.db.utils.allocators;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.maps.MutableLongToObjectMaxDistanceNonBucketMap;

public interface ILongToObjectMaxDistanceMapAllocator<T> {

    MutableLongToObjectMaxDistanceNonBucketMap<T> allocateLongToObjectMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray);

    void freeLongToObjectMap(MutableLongToObjectMaxDistanceNonBucketMap<T> longToObjectMap);

    default MutableLongToObjectMaxDistanceNonBucketMap<T> copyLongToObjectMap(MutableLongToObjectMaxDistanceNonBucketMap<T> toCopy) {

        final MutableLongToObjectMaxDistanceNonBucketMap<T> copy = allocateLongToObjectMap(toCopy.getCapacityExponent(), toCopy.getCreateValues());

        toCopy.forEachKeyAndValue(copy, (k, v, c) -> copy.put(k, v));

        return copy;
    }
}
