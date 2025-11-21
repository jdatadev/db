package dev.jdata.db.utils.adt.arrays;

final class HeapMutableCharLargeArray extends MutableCharLargeArray implements IHeapMutableCharLargeArray {

    static HeapMutableCharLargeArray create(AllocationType allocationType) {

        return instantiateLargeExponentArray(allocationType, AllocationMechanism.HEAP, HeapMutableCharLargeArray::new);
    }

    static HeapMutableCharLargeArray create(AllocationType allocationType, long initialCapacity) {

        return instantiateLargeExponentArray(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableCharLargeArray::new);
    }

    static HeapMutableCharLargeArray create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, char clearValue) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new HeapMutableCharLargeArray(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    static HeapMutableCharLargeArray copyOf(AllocationType allocationType, IMutableCharLargeArray toCopy) {

        checkCopyOfParameters(allocationType, AllocationMechanism.HEAP, toCopy);

        return new HeapMutableCharLargeArray(allocationType, (BaseMutableCharLargeArray)toCopy);
    }

    private HeapMutableCharLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    private HeapMutableCharLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, char clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    private HeapMutableCharLargeArray(AllocationType allocationType, BaseMutableCharLargeArray toCopy) {
        super(allocationType, toCopy);
    }
}
