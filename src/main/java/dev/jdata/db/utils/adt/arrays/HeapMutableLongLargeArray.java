package dev.jdata.db.utils.adt.arrays;

final class HeapMutableLongLargeArray extends MutableLongLargeArray implements IHeapMutableLongLargeArray {

    HeapMutableLongLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    HeapMutableLongLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, long clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    HeapMutableLongLargeArray(AllocationType allocationType, MutableLongLargeArray toCopy) {
        super(allocationType, toCopy);
    }
}
