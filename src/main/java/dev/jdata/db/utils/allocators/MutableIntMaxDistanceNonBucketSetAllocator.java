package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.sets.MutableIntMaxDistanceNonBucketSet;

public final class MutableIntMaxDistanceNonBucketSetAllocator extends BaseSetAllocator<MutableIntMaxDistanceNonBucketSet> implements IMutableIntSetAllocator<MutableIntMaxDistanceNonBucketSet> {

    public MutableIntMaxDistanceNonBucketSetAllocator() {
        super(MutableIntMaxDistanceNonBucketSet::new, MutableIntMaxDistanceNonBucketSet::getCapacityExponent);
    }

    @Override
    public MutableIntMaxDistanceNonBucketSet allocateMutableIntSet(int minimumCapacityExponent) {

        return allocateArrayInstance(minimumCapacityExponent);
    }

    @Override
    public void freeMutableIntSet(MutableIntMaxDistanceNonBucketSet intSet) {

        freeSet(intSet);
    }
}
