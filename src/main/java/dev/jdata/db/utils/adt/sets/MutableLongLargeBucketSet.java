package dev.jdata.db.utils.adt.sets;

import java.util.Objects;

import dev.jdata.db.DebugConstants;

final class MutableLongLargeBucketSet extends BaseLongLargeBucketSet implements IMutableLongLargeSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LARGE_LONG_BUCKET_SET;

    static MutableLongLargeBucketSet of(int initialOuterCapacity, int innerCapacityExponent, long[] values) {

        return new MutableLongLargeBucketSet(initialOuterCapacity, innerCapacityExponent, values);
    }

    MutableLongLargeBucketSet(int initialOuterCapacityExponent, int innerCapacityExponent) {
        this(initialOuterCapacityExponent, innerCapacityExponent, DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("innerCapacityExponent", innerCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongLargeBucketSet(int initialOuterCapacityExponent, int innerCapacityExponent, float loadFactor) {
        super(initialOuterCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableLongLargeBucketSet(int initialOuterCapacityExponent, int innerCapacityExponent, long[] values) {
        super(initialOuterCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, innerCapacityExponent, DEFAULT_LOAD_FACTOR);
    }

    @Override
    public long getCapacity() {

        return getHashedCapacity();
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
    public <T extends IBaseLongSet> T toImmutable(IBaseLongSetAllocator<T> longSetAllocator) {

        Objects.requireNonNull(longSetAllocator);

        return longSetAllocator.copyToImmutable(this);
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
