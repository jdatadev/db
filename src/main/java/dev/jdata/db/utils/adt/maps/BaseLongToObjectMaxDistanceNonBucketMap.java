package dev.jdata.db.utils.adt.maps;

import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceLongMapOperations;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseLongToObjectMaxDistanceNonBucketMap<T> extends BaseLongToObjectNonBucketMap<T> implements ILongContainsKeyMap, ILongToObjectBucketMapGetters<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    private static final Class<?> debugClass = BaseIntToIntMaxDistanceNonBucketMap.class;

    private byte[] maxDistances;

    BaseLongToObjectMaxDistanceNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, createValuesArray);

        initialize();
    }

    BaseLongToObjectMaxDistanceNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);

        initialize();
    }

    BaseLongToObjectMaxDistanceNonBucketMap(BaseLongToObjectMaxDistanceNonBucketMap<T> toCopy) {
        super(toCopy);

        initialize();
    }

    private void initialize() {

        this.maxDistances = new byte[getCapacity()];
    }

    @Override
    public final boolean containsKey(long key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final boolean result = getIndexScanHashArrayToMax(key, hashArrayIndex, maxDistances[hashArrayIndex]) != NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final T get(long key, T defaultValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final int index = getIndexScanHashArrayToMax(key, hashArrayIndex, maxDistances[hashArrayIndex]);

        final T result = index != NO_INDEX ? getValues()[index] : defaultValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }

    @Override
    protected final long[] rehash(long[] hashArray, int newCapacity, int newKeyMask) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newKeyMask", newKeyMask));
        }

        this.maxDistances = new byte[newCapacity];

        final long[] result = super.rehash(hashArray, newCapacity, newKeyMask);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private final MaxDistanceLongMapOperations<BaseLongToObjectMaxDistanceNonBucketMap<T>, T[]> maxDistanceOperations
            = new MaxDistanceLongMapOperations<BaseLongToObjectMaxDistanceNonBucketMap<T>, T[]>() {

        @Override
        public long put(BaseLongToObjectMaxDistanceNonBucketMap<T> hashed, long[] hashArray, long key, int hashArrayIndex) {

            return hashed.put(hashArray, key, hashArrayIndex);
        }

        @Override
        public void incrementNumElements(BaseLongToObjectMaxDistanceNonBucketMap<T> hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseLongToObjectMaxDistanceNonBucketMap<T> hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public T[] getValues(BaseLongToObjectMaxDistanceNonBucketMap<T> hashed) {

            return hashed.getValues();
        }

        @Override
        public byte[] getMaxDistances(BaseLongToObjectMaxDistanceNonBucketMap<T> hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseLongToObjectMaxDistanceNonBucketMap<T> hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public long[] getHashArray(BaseLongToObjectMaxDistanceNonBucketMap<T> hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseLongToObjectMaxDistanceNonBucketMap<T> hashed) {

            return hashed.getCapacity();
        }
    };

    final T putMaxDistance(long key, T value, T defaultPreviousValue) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final T result = MaxDistance.putMaxDistance(this, key, value, defaultPreviousValue, maxDistanceOperations);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }
}
