package dev.jdata.db.utils.allocators;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.maps.MutableLongToObjectMaxDistanceNonBucketMap;

public final class LongToObjectMaxDistanceMapAllocator<T>

        extends BaseArrayAllocator<MutableLongToObjectMaxDistanceNonBucketMap<T>>
        implements ILongToObjectMaxDistanceMapAllocator<T> {

    public LongToObjectMaxDistanceMapAllocator(IntFunction<T[]> createValuesArray) {
        super(l -> new MutableLongToObjectMaxDistanceNonBucketMap<T>(l, createValuesArray), m -> m.getCapacityExponent());
    }

    @Override
    public MutableLongToObjectMaxDistanceNonBucketMap<T> allocateLongToObjectMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray) {

        return super.allocateArrayInstance(initialCapacityExponent);
    }

    @Override
    public void freeLongToObjectMap(MutableLongToObjectMaxDistanceNonBucketMap<T> longToObjectMap) {

        freeArrayInstance(longToObjectMap);
    }
}
