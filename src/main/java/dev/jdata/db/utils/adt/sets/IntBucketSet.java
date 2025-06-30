package dev.jdata.db.utils.adt.sets;

public final class IntBucketSet extends BaseIntBucketSet {

    public static IntBucketSet of(int ... values) {

        return new IntBucketSet(values);
    }

    IntBucketSet(BaseIntBucketSet toCopy) {
        super(toCopy);
    }

    private IntBucketSet(int[] values) {
        super(values);
    }
}
