package dev.jdata.db.utils.adt.sets;

final class LongLargeBucketSet extends BaseLongLargeBucketSet implements IBaseLongSet {

    static LongLargeBucketSet of(int initialOuterCapacity, int innerCapacityExponent, long[] values) {

        return new LongLargeBucketSet(initialOuterCapacity, innerCapacityExponent, values);
    }

    private LongLargeBucketSet(int initialOuterCapacity, int innerCapacityExponent, long[] values) {
        super(initialOuterCapacity, innerCapacityExponent, values);
    }
}
