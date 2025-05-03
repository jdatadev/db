package dev.jdata.db.utils.adt.sets;

public final class LongBucketSet extends BaseLongBucketSet implements ILongSet {

    public static LongBucketSet of(long ... values) {

        return new LongBucketSet(values);
    }

    private LongBucketSet(long[] values) {
        super(values);
    }
}
