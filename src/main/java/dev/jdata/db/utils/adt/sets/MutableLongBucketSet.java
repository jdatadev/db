package dev.jdata.db.utils.adt.sets;

import dev.jdata.db.DebugConstants;

abstract class MutableLongBucketSet extends BaseLongBucketSet implements IMutableLongSet {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_BUCKET_SET;

    MutableLongBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, int bucketsInnerCapacityExponent) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, bucketsInnerCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("bucketsInnerCapacityExponent", bucketsInnerCapacityExponent));
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
    public final void clear() {

        if (DEBUG) {

            enter();
        }

        clearBaseBucketSet();

        if (DEBUG) {

            exit();
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
