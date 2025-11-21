package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.elements.IIntIterableElementsView;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;

abstract class MutableIntMaxDistanceNonBucketSet extends BaseIntMaxDistanceNonBucketSet implements IMutableIntSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_MAX_DISTANCE_NON_BUCKET_SET;

    MutableIntMaxDistanceNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    MutableIntMaxDistanceNonBucketSet(AllocationType allocationType, int capacityExponentIncrease, float loadFactor, IIntIterableElementsView mutableFrom) {
        super(allocationType, capacityExponentIncrease, loadFactor, mutableFrom);
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

        clearBaseIntNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean addToSet(int value) {

        IntNonBucket.checkIsHashArrayElement(value);

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
    public final boolean removeAtMostOne(int value) {

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
