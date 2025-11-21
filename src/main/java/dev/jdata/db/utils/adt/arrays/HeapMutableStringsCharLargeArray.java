package dev.jdata.db.utils.adt.arrays;

final class HeapMutableStringsCharLargeArray extends MutableStringsCharLargeArray implements IHeapMutableStringsCharLargeArray {

    HeapMutableStringsCharLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }
}
