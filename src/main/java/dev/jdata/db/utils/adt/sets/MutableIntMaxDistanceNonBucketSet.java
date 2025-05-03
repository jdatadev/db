package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;

public final class MutableIntMaxDistanceNonBucketSet extends BaseIntMaxDistanceNonBucketSet implements IMutableIntSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_MAX_DISTANCE_NON_BUCKET_SET;

    public MutableIntMaxDistanceNonBucketSet(int initialCapacityExponent) {
        super(initialCapacityExponent, HashedConstants.DEFAULT_LOAD_FACTOR);
    }

    public MutableIntMaxDistanceNonBucketSet(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, loadFactor);
    }

    public MutableIntMaxDistanceNonBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    @Override
    public void add(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addMaxDistance(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    @Override
    public boolean addToSet(int value) {

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
    public boolean remove(int value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        final int index = removeAndReturnIndex(value);

        final boolean result = index != HashArray.NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("value", value));
        }

        return result;
    }

    @Override
    public void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseIntToIntNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }
}
