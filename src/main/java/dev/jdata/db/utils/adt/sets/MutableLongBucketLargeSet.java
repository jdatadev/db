package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;

abstract class MutableLongBucketLargeSet extends BaseLongBucketLargeSet implements IMutableLongLargeSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_BUCKET_LARGE_SET;

    MutableLongBucketLargeSet(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("capacityExponentIncrease", capacityExponentIncrease).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableLongBucketLargeSet(AllocationType allocationType, int initialOuterCapacityExponent, int innerCapacityExponent, long[] values) {
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

        clearBaseBucketLargeSet();

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
