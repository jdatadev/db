package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.ILongIterableElementsView;

final class HeapMutableLongLargeArray extends MutableLongLargeArray implements IHeapMutableLongLargeArray {

    static HeapMutableLongLargeArray create(AllocationType allocationType) {

        return instantiateLargeExponentArray(allocationType, AllocationMechanism.HEAP, HeapMutableLongLargeArray::new);
    }

    static HeapMutableLongLargeArray create(AllocationType allocationType, long initialCapacity) {

        return instantiateLargeExponentArray(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableLongLargeArray::new);
    }

    static HeapMutableLongLargeArray create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new HeapMutableLongLargeArray(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    static HeapMutableLongLargeArray create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, long clearValue) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new HeapMutableLongLargeArray(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    static HeapMutableLongLargeArray copyOf(AllocationType allocationType, ILongIterableElementsView toCopy) {

        checkCopyOfParameters(allocationType, AllocationMechanism.HEAP, toCopy);

        return new HeapMutableLongLargeArray(allocationType, (MutableLongLargeArray)toCopy);
    }

    private HeapMutableLongLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    private HeapMutableLongLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, long clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    private HeapMutableLongLargeArray(AllocationType allocationType, MutableLongLargeArray toCopy) {
        super(allocationType, toCopy);
    }
}
