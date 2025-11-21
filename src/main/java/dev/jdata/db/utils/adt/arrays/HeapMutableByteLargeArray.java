package dev.jdata.db.utils.adt.arrays;

import dev.jdata.db.utils.adt.elements.IByteIterableElementsView;

final class HeapMutableByteLargeArray extends MutableByteLargeArray implements IHeapMutableByteLargeArray {

    static HeapMutableByteLargeArray create(AllocationType allocationType) {

        return instantiateLargeExponentArray(allocationType, AllocationMechanism.HEAP, HeapMutableByteLargeArray::new);
    }

    static HeapMutableByteLargeArray create(AllocationType allocationType, long initialCapacity) {

        return instantiateLargeExponentArray(allocationType, AllocationMechanism.HEAP, initialCapacity, HeapMutableByteLargeArray::new);
    }

    static HeapMutableByteLargeArray create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new HeapMutableByteLargeArray(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    static HeapMutableByteLargeArray create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, byte clearValue) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new HeapMutableByteLargeArray(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    static HeapMutableByteLargeArray copyOf(AllocationType allocationType, IByteIterableElementsView toCopy) {

        checkCopyOfParameters(allocationType, AllocationMechanism.HEAP, toCopy);

        return new HeapMutableByteLargeArray(allocationType, (MutableByteLargeArray)toCopy);
    }

    private HeapMutableByteLargeArray(AllocationType allocationType, int innerCapacityExponent) {
        super(allocationType, innerCapacityExponent);
    }

    private HeapMutableByteLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    private HeapMutableByteLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, byte clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    private HeapMutableByteLargeArray(AllocationType allocationType, MutableByteLargeArray toCopy) {
        super(allocationType, toCopy);
    }
}
