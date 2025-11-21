package dev.jdata.db.utils.adt.sets;

final class HeapIntBucketSet extends IntBucketSet implements IHeapIntSet {

    static HeapIntBucketSet of(AllocationType allocationType, int ... values) {

        checkOfValuesParameters(allocationType, AllocationMechanism.HEAP, values, values.length);

        return new HeapIntBucketSet(allocationType, values);
    }

    private HeapIntBucketSet(AllocationType allocationType, int[] values) {
        super(allocationType, values);
    }

    @Override
    public IHeapIntSet toHeapAllocated() {

        return this;
    }
}
