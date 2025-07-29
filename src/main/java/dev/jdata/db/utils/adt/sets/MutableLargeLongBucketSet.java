package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;

public final class MutableLargeLongBucketSet extends BaseLargeLongBucketSet implements IMutableLongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LARGE_LONG_BUCKET_SET;

    public static MutableLargeLongBucketSet of(int initialOuterCapacity, int innerCapacityExponent, long[] values) {

        return new MutableLargeLongBucketSet(initialOuterCapacity, innerCapacityExponent, values);
    }

    public MutableLargeLongBucketSet(int initialOuterCapacityExponent, int innerCapacityExponent) {
        this(initialOuterCapacityExponent, innerCapacityExponent, DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    public MutableLargeLongBucketSet(int initialOuterCapacityExponent, int innerCapacityExponent, float loadFactor) {
        super(initialOuterCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableLargeLongBucketSet(int initialOuterCapacityExponent, int innerCapacityExponent, long[] values) {
        super(initialOuterCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseLargeLongBucketSet();

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

            exit(result, b -> b.add("value", value));
        }

        return result;
    }
}
