package dev.jdata.db.utils.adt.arrays;

final class HeapMutableIntLargeArray extends MutableIntLargeArray implements IHeapMutableIntLargeArray {

    HeapMutableIntLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    HeapMutableIntLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, int clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    HeapMutableIntLargeArray(AllocationType allocationType, MutableIntLargeArray toCopy) {
        super(allocationType, toCopy);
    }
}
