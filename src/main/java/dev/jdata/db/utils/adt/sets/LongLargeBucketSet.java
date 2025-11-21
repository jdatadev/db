package dev.jdata.db.utils.adt.sets;

abstract class LongLargeBucketSet extends BaseLongLargeBucketSet implements IBaseLongSet {

    LongLargeBucketSet(AllocationType allocationType, int initialOuterCapacity, int innerCapacityExponent, long[] values) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, values);
    }
}
