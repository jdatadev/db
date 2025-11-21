package dev.jdata.db.utils.adt.arrays;

abstract class MutableCharLargeArray extends BaseMutableCharLargeArray {

    MutableCharLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    MutableCharLargeArray(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, char clearValue) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    MutableCharLargeArray(AllocationType allocationType, BaseMutableCharLargeArray toCopy) {
        super(allocationType, toCopy);
    }
}
