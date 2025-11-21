package dev.jdata.db.utils.adt.arrays;

final class HeapMutableByteLargeArray extends MutableByteLargeArray implements IHeapMutableByteLargeArray {

    HeapMutableByteLargeArray(AllocationType allocationType, int innerCapacityExponent) {
        super(allocationType, innerCapacityExponent);
    }

    HeapMutableByteLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    HeapMutableByteLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, byte clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    HeapMutableByteLargeArray(AllocationType allocationType, MutableByteLargeArray toCopy) {
        super(allocationType, toCopy);
    }
}
