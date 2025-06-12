package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceLongMapOperations;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

abstract class BaseLongToObjectMaxDistanceNonBucketMap<T>

        extends BaseLongToObjectNonBucketMap<T, ILongToObjectDynamicMapCommon<T>>
        implements ILongToObjectDynamicMapCommon<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

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

        final boolean result = getHashArrayIndex(key, getKeyMask()) != NO_INDEX;

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

        final int index = getHashArrayIndex(key, getKeyMask());

        final T result = index != NO_INDEX ? getValues()[index] : defaultValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }

    @Override
    protected final int getHashArrayIndex(long key, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final int result = getIndexScanHashArrayToMax(key, hashArrayIndex, maxDistances[hashArrayIndex]);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
    }

    @Override
    protected final long[] rehash(long[] hashArray, int newCapacity, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newKeyMask", newKeyMask));
        }

        this.maxDistances = new byte[newCapacity];

        final long[] result = super.rehash(hashArray, newCapacity, newKeyMask);

        if (DEBUG) {

            exit(result);
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

        checkCapacity(1);

        final T result = MaxDistance.putMaxDistance(this, key, value, defaultPreviousValue, maxDistanceOperations);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public final <P1, P2> boolean equals(P1 thisParameter, ILongToObjectDynamicMapCommon<T> other, P2 otherParameter, IObjectValueMapEqualityTester<T, P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseLongToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseLongToObjectNonBucketMap<T, ILongToObjectDynamicMapCommon<T>> otherMap = (BaseLongToObjectNonBucketMap<T, ILongToObjectDynamicMapCommon<T>>)other;

            result = equalsLongToObjectNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = ILongToObjectDynamicMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<T, ILongToObjectDynamicMapCommon<T>, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final ILongToObjectDynamicMapCommon<T> other = scratchEqualsParameter.getOther();

        if (other instanceof BaseLongToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseLongToObjectNonBucketMap<T, ILongToObjectDynamicMapCommon<T>> otherMap = (BaseLongToObjectNonBucketMap<T, ILongToObjectDynamicMapCommon<T>>)other;

            result = equalsLongToObjectNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = ILongToObjectDynamicMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
