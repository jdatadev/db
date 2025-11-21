package dev.jdata.db.utils.adt.arrays;

final class HeapMutableCharLargeArray extends MutableCharLargeArray implements IHeapMutableCharLargeArray {

    HeapMutableCharLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    HeapMutableCharLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, char clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    HeapMutableCharLargeArray(AllocationType allocationType, BaseMutableCharLargeArray toCopy) {
        super(allocationType, toCopy);
    }
}
