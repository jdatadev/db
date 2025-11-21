package dev.jdata.db.utils.adt.maps;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;

abstract class MutableLongToIntNonRemoveNonBucketMap<M extends MutableLongToIntNonRemoveNonBucketMap<M>>

        extends BaseLongToIntNonRemoveNonBucketMap<M>
        implements IMutableLongToIntNonRemoveStaticMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_LONG_TO_INT_NON_REMOVE_NON_BUCKET_MAP;

    MutableLongToIntNonRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableLongToIntNonRemoveNonBucketMap(AllocationType allocationType, MutableLongToIntNonRemoveNonBucketMap<M> toCopy) {
        super(allocationType, toCopy);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
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

        clearBaseNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final int put(long key, int value, int defaultPreviousValue) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final int result = putValue(key, value, defaultPreviousValue);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }
}
