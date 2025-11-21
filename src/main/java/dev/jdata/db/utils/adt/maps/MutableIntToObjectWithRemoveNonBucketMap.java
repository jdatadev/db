package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;

abstract class MutableIntToObjectWithRemoveNonBucketMap<V> extends BaseIntToObjectWithRemoveNonBucketMap<V> implements IMutableIntToObjectWithRemoveStaticMap<V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_MUTABLE_INT_TO_OBJECT_NON_BUCKET_MAP;

    MutableIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableIntToObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createValuesArray", createValuesArray));
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

        clearBaseIntToObjectNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final V put(int key, V value, V defaultPreviousValue) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final V result = putValue(key, value, defaultPreviousValue);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value));
        }

        return result;
    }

    @Override
    public final V removeAndReturnPrevious(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final V result;

        final int indexToRemove = HashArray.removeAndReturnIndexScanEntire(getHashed(), key, getKeyMask());

        if (indexToRemove != NO_INDEX) {

            final V[] values = getValues();

            result = values[indexToRemove];

            values[indexToRemove] = null;

            decrementNumElements();
        }
        else {
            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final void remove(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int indexToRemove = HashArray.removeAndReturnIndexScanEntire(getHashed(), key, getKeyMask());

        if (indexToRemove != NO_INDEX) {

            getValues()[indexToRemove] = null;

            decrementNumElements();
        }
        else {
            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(b -> b.add("key", key));
        }
    }
}
