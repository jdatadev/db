package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;

final class MutableLongLargeMaxDistanceNonBucketSet extends BaseLongLargeMaxDistanceNonBucketSet implements IMutableLongLargeSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_LARGE_MAX_DISTANCE_NON_BUCKET_SET;

    MutableLongLargeMaxDistanceNonBucketSet(int initialCapacityExponent) {
        this(initialCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    private MutableLongLargeMaxDistanceNonBucketSet(int initialCapacityExponent, float loadFactor) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor);
    }

    private MutableLongLargeMaxDistanceNonBucketSet(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        this(initialCapacityExponent, capacityExponentIncrease, DEFAULT_INNER_CAPACITY_EXPONENT, loadFactor);
    }

    private MutableLongLargeMaxDistanceNonBucketSet(int initialCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor);
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

        final long index = removeMaxDistance(value);

        final boolean result = index != LargeHashArray.NO_INDEX;

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
