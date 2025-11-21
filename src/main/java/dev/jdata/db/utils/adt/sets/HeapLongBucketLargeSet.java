package dev.jdata.db.utils.adt.sets;

final class HeapLongBucketLargeSet extends LongBucketLargeSet implements IHeapLongLargeSet {

    static HeapLongBucketLargeSet of(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent, long ... values) {

        return instantiateWithOuterExponentInnerExponentAndValues(allocationType, AllocationMechanism.HEAP, initialOuterCapacityExponent, innerCapacityExponent, values,
                values.length, HeapLongBucketLargeSet::new);
    }

    private HeapLongBucketLargeSet(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent, long[] values) {
        super(allocationType, initialOuterCapacityExponent, innerCapacityExponent, values);
    }

    @Override
    public IHeapLongLargeSet toHeapAllocated() {

        return this;
    }
}
