package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;

final class MutableLongMaxDistanceNonBucketSet extends BaseLongMaxDistanceNonBucketSet implements IMutableLongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_MAX_DISTANCE_NON_BUCKET_SET;

    MutableLongMaxDistanceNonBucketSet(int initialCapacityExponent) {
        super(initialCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    MutableLongMaxDistanceNonBucketSet(int initialCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, loadFactor);
    }

    MutableLongMaxDistanceNonBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    @Override
    public long getCapacity() {

        return getHashedCapacity();
    }

    @Override
    public void addUnordered(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addMaxDistance(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
    }

    @Override
    public boolean addToSet(long value) {

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
    public boolean removeAtMostOne(long value) {

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

    @Override
    public <T extends IBaseLongSet> T toImmutable(IBaseLongSetAllocator<T> longSetAllocator) {

        Objects.requireNonNull(longSetAllocator);

        return longSetAllocator.copyToImmutable(this);
    }
}
