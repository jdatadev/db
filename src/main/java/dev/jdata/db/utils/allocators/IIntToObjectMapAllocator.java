package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.maps.MutableIntToObjectWithRemoveNonBucketMap;

public interface IIntToObjectMapAllocator<T> {

    MutableIntToObjectWithRemoveNonBucketMap<T> allocateIntToObjectMap(int minimumCapacityExponent);

    void freeIntToObjectMap(MutableIntToObjectWithRemoveNonBucketMap<T> intToObjectMap);
}
