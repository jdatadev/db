package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;

abstract class MutableLongBucketSet extends BaseLongBucketSet implements IMutableLongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_BUCKET_SET;

    MutableLongBucketSet(AllocationType allocationType) {
        this(allocationType, DEFAULT_INITIAL_CAPACITY_EXPONENT);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongBucketSet(AllocationType allocationType, int initialCapacityExponent) {
        this(allocationType, initialCapacityExponent, DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongBucketSet(AllocationType allocationType, int initialCapacityExponent, float loadFactor) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, DEFAULT_BUCKETS_INNER_CAPACITY_EXPONENT);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    private MutableLongBucketSet(AllocationType allocationType, long[] values) {
        super(allocationType, values);
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

        clearBaseLongBucketSet();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public void addUnordered(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        addValue(value);

        if (DEBUG) {

            exit(b -> b.add("value", value));
        }
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

            exit(result);
        }

        return result;
    }
}
