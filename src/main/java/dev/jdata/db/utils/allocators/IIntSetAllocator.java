package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;

public interface IIntSetAllocator {

    MutableIntBucketSet allocateIntSet(int minimumCapacityExponent);

    void freeIntSet(MutableIntBucketSet intSet);
}
