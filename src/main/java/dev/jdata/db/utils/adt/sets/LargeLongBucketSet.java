package dev.jdata.db.utils.adt.sets;

public final class LargeLongBucketSet extends BaseLargeLongBucketSet implements ILongSet {

    public static LargeLongBucketSet of(int initialOuterCapacity, int innerCapacityExponent, long[] values) {

        return new LargeLongBucketSet(initialOuterCapacity, innerCapacityExponent, values);
    }

    public LargeLongBucketSet() {

    }

    public LargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent);
    }

    public LargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent, float loadFactor) {
        super(initialOuterCapacity, innerCapacityExponent, loadFactor);
    }

    public LargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent, long[] values) {
        super(initialOuterCapacity, innerCapacityExponent, values);
    }
}
