package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;

public final class MutableLargeLongBucketSet extends BaseLargeLongBucketSet implements IMutableLongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LARGE_LONG_BUCKET_SET;

    public static MutableLargeLongBucketSet of(int initialOuterCapacity, int innerCapacityExponent, long[] values) {

        return new MutableLargeLongBucketSet(initialOuterCapacity, innerCapacityExponent, values);
    }

    public MutableLargeLongBucketSet() {

    }

    public MutableLargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent) {
        super(initialOuterCapacity, innerCapacityExponent);
    }

    public MutableLargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent, float loadFactor) {
        super(initialOuterCapacity, innerCapacityExponent, loadFactor);
    }

    private MutableLargeLongBucketSet(int initialOuterCapacity, int innerCapacityExponent, long[] values) {
        super(initialOuterCapacity, innerCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void clear() {

        clearBaseLargeLongBucketSet();
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

        final boolean result = removeAtMostOneValue(value);

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }
}
