package dev.jdata.db.utils.allocators;

import dev.jdata.db.utils.adt.sets.MutableIntBucketSet;

public final class IntBucketSetAllocator extends BaseSetAllocator<MutableIntBucketSet> implements IIntSetAllocator {

    public IntBucketSetAllocator() {
        super(MutableIntBucketSet::new, MutableIntBucketSet::getCapacityExponent);
    }

    @Override
    public MutableIntBucketSet allocateIntSet(int minimumCapacityExponent) {

        return allocateArrayInstance(minimumCapacityExponent);
    }

    @Override
    public void freeIntSet(MutableIntBucketSet intSet) {

        freeSet(intSet);
    }
}
