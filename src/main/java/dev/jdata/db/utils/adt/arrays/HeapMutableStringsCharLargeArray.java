package dev.jdata.db.utils.adt.arrays;

final class HeapMutableStringsCharLargeArray extends MutableStringsCharLargeArray implements IHeapMutableStringsCharLargeArray {

    static HeapMutableStringsCharLargeArray create(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {

        checkInstantiateParameters(allocationType, AllocationMechanism.HEAP, initialOuterCapacity, innerCapacityExponent);

        return new HeapMutableStringsCharLargeArray(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    private HeapMutableStringsCharLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }
}
