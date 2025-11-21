package dev.jdata.db.utils.adt.sets;

abstract class LongBucketLargeSet extends BaseLongBucketLargeSet implements ILongLargeSet {

    LongBucketLargeSet(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent, long[] values) {
        super(allocationType, initialOuterCapacityExponent, innerCapacityExponent, values);
    }
}
