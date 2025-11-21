package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;

abstract class MutableObjectWithRemoveNonBucketMap<K, V> extends BaseObjectWithRemoveNonBucketMap<K, V> implements IMutableWithRemoveStaticMap<K, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_OBJECT_TO_OBJECT_WITH_REMOVE_NON_BUCKET_MAP;

    MutableObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createKeysArray", createKeysArray)
                    .add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    MutableObjectWithRemoveNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
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

        clearBaseObjectToObjectNonBucketMap();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final V put(K key, V value, V defaultPreviousValue) {

        Objects.requireNonNull(key);
        Objects.requireNonNull(value);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final V result = putValue(key, value, defaultPreviousValue);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public final V removeAndReturnPrevious(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final V result;

        final int indexToRemove = HashArray.removeAndReturnIndexScanEntire(getHashed(), key, getKeyMask(), getNoKey());

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

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final void remove(K key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int indexToRemove = HashArray.removeAndReturnIndexScanEntire(getHashed(), key, getKeyMask(), getNoKey());

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
