package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;

abstract class MutableLongMaxDistanceNonBucketLargeSet extends BaseLongMaxDistanceNonBucketLargeSet implements IMutableLongLargeSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_LARGE_MAX_DISTANCE_NON_BUCKET_SET;

    MutableLongMaxDistanceNonBucketLargeSet(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent,
            float loadFactor) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor);
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

        clearNonBucketLargeSet();

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
