package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.maps.MutableLongToIntWithRemoveNonBucketMap;

public interface ILongToIntMapAllocator {

    MutableLongToIntWithRemoveNonBucketMap allocateLongToIntMap(int initialCapacityExponent);

    void freeLongToIntMap(MutableLongToIntWithRemoveNonBucketMap longToIntMap);
}

