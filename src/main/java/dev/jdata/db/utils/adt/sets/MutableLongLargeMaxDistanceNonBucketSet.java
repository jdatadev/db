package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;

abstract class MutableLongLargeMaxDistanceNonBucketSet extends BaseLongLargeMaxDistanceNonBucketSet implements IMutableLongLargeSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_LARGE_MAX_DISTANCE_NON_BUCKET_SET;

    MutableLongLargeMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        this(allocationType, initialCapacityExponent, DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableLongLargeMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, float loadFactor) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor);
    }

    private MutableLongLargeMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        this(allocationType, initialCapacityExponent, capacityExponentIncrease, DEFAULT_INNER_CAPACITY_EXPONENT, loadFactor);
    }

    private MutableLongLargeMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent,
            float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor);
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

        clearBaseIntToIntNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final void addUnordered(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addMaxDistance(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    @Override
    public final boolean addToSet(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final boolean result = addMaxDistance(value);

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

        final long index = removeMaxDistance(value);

        final boolean result = index != LargeHashArray.NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }
}
