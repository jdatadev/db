package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;

abstract class MutableLongMaxDistanceNonBucketSet extends BaseLongMaxDistanceNonBucketSet implements IMutableLongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_MAX_DISTANCE_NON_BUCKET_SET;

    MutableLongMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
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

        clearBaseLongNonBucketSet();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final void addUnordered(long value) {

        LongNonBucket.checkIsHashArrayElement(value);

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

        LongNonBucket.checkIsHashArrayElement(value);

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

        final int index = removeMaxDistance(value);

        final boolean result = index != HashArray.NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }
}
