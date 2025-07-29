package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;

public final class MutableLongBucketSet extends BaseLongBucketSet implements IMutableLongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_BUCKET_SET;

    public static MutableLongBucketSet of(long ... values) {

        return new MutableLongBucketSet(values);
    }

    public MutableLongBucketSet() {
        this(DEFAULT_INITIAL_CAPACITY_EXPONENT);
    }

    public MutableLongBucketSet(int initialCapacityExponent) {
        this(initialCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    public MutableLongBucketSet(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);
    }

    private MutableLongBucketSet(long[] values) {
        super(values);
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseLongBucketSet();

        if (DEBUG) {

            exit();
        }
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

        final boolean result = removeElement(value);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
