package dev.jdata.db.utils.adt.arrays;

final class MutableCharLargeArray extends BaseMutableCharLargeArray {

    MutableCharLargeArray(int initialOuterCapacity, int innerCapacityExponent, char clearValue) {
        super(initialOuterCapacity, innerCapacityExponent, clearValue);
    }

    MutableCharLargeArray(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent);
    }

    MutableCharLargeArray(BaseMutableCharLargeArray toCopy) {
        super(toCopy);
    }
}
