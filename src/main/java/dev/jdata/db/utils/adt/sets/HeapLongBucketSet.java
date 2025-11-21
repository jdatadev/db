package dev.jdata.db.utils.adt.sets;

final class HeapLongBucketSet extends LongBucketSet implements IHeapLongSet {

    static HeapLongBucketSet of(AllocationType allocationType, long ... values) {

        checkOfValuesParameters(allocationType, AllocationMechanism.HEAP, values, values.length);

        return new HeapLongBucketSet(AllocationType.HEAP, values);
    }

    private HeapLongBucketSet(AllocationType allocationType, long[] values) {
        super(allocationType, values);
    }

    @Override
    public IHeapLongSet toHeapAllocated() {

        return this;
    }
}
