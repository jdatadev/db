package dev.jdata.db.utils.adt.sets;

public final class LargeLongBucketSet extends BaseLargeLongBucketSet implements ILongSet {

    public static LargeLongBucketSet of(int initialOuterCapacity, int innerCapacityExponent, long[] values) {

        return new LargeLongBucketSet(initialOuterCapacity, innerCapacityExponent, values);
    }

    private LargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent, long[] values) {
        super(initialOuterCapacity, innerCapacityExponent, values);
    }
}
