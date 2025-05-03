package dev.jdata.db.utils.allocators;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.maps.MutableIntToObjectWithRemoveNonBucketMap;

public final class IntToObjectMapAllocator<T> extends BaseObjectMapAllocator<MutableIntToObjectWithRemoveNonBucketMap<T>> implements IIntToObjectMapAllocator<T> {

    public IntToObjectMapAllocator(IntFunction<T[]> createArray) {
        super(c -> new MutableIntToObjectWithRemoveNonBucketMap<>(c, createArray), MutableIntToObjectWithRemoveNonBucketMap::getCapacityExponent);
    }

    @Override
    public MutableIntToObjectWithRemoveNonBucketMap<T> allocateIntToObjectMap(int minimumCapacityExponent) {

        return allocateArrayInstance(minimumCapacityExponent);
    }

    @Override
    public void freeIntToObjectMap(MutableIntToObjectWithRemoveNonBucketMap<T> intToObjectMap) {

        freeArrayInstance(intToObjectMap);
    }
}
