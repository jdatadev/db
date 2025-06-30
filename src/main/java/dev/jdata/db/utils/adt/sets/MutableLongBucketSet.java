package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;

public final class MutableLongBucketSet extends BaseLongBucketSet implements IMutableLongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_BUCKET_SET;

    public static MutableLongBucketSet of(long ... values) {

        return new MutableLongBucketSet(values);
    }

    public MutableLongBucketSet() {

    }

    public MutableLongBucketSet(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    public MutableLongBucketSet(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, loadFactor);
    }

    private MutableLongBucketSet(long[] values) {
        super(values);
    }

    @Override
    public void clear() {

        clearBaseLongBucketSet();
    }

    @Override
    public void add(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addValue(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    @Override
    public boolean addToSet(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = addValue(value);

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    @Override
    public boolean removeAtMostOne(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = removeAtMostOneElement(value);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
