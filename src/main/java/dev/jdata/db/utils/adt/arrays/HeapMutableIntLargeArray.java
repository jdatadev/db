package dev.jdata.db.utils.adt.arrays;

final class HeapMutableIntLargeArray extends MutableIntLargeArray implements IHeapMutableIntLargeArray {

    static HeapMutableIntLargeArray create(AllocationType allocationType) {

        return instantiateLargeExponentArray(allocationType, AllocationMechanism.HEAP, HeapMutableIntLargeArray::new);
    }

    static HeapMutableIntLargeArray create(AllocationType allocationType, long initialCapacity) {

        return instantiateLargeExponentArray(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableIntLargeArray::new);
    }

    static HeapMutableIntLargeArray create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new HeapMutableIntLargeArray(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    static HeapMutableIntLargeArray create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, int clearValue) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new HeapMutableIntLargeArray(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    static HeapMutableIntLargeArray copyOf(AllocationType allocationType, IMutableIntLargeArray toCopy) {

        checkCopyOfParameters(allocationType, AllocationMechanism.HEAP, toCopy);

        return new HeapMutableIntLargeArray(allocationType, (MutableIntLargeArray)toCopy);
    }

    private HeapMutableIntLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    private HeapMutableIntLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, int clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    private HeapMutableIntLargeArray(AllocationType allocationType, MutableIntLargeArray toCopy) {
        super(allocationType, toCopy);
    }
}
