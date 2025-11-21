package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;

abstract class MutableLongLargeBucketSet extends BaseLongLargeBucketSet implements IMutableLongLargeSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LARGE_LONG_BUCKET_SET;

    MutableLongLargeBucketSet(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent) {
        this(allocationType, initialOuterCapacityExponent, innerCapacityExponent, DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongLargeBucketSet(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent, float loadFactor) {
        super(allocationType, initialOuterCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableLongLargeBucketSet(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent, long[] values) {
        super(allocationType, initialOuterCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    @Override
    public final long getCapacity() {

        return getHashedCapacity();
    }

    @Override
    public final void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseLargeLongBucketSet();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean addToSet(long value) {

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
    public final boolean removeAtMostOne(long value) {

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
