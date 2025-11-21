package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceLongMapOperations;

abstract class BaseLongToObjectMaxDistanceNonBucketMap<V, M extends BaseLongToObjectMaxDistanceNonBucketMap<V, M>>

        extends BaseLongToObjectNonBucketMap<V, M>
        implements ILongToObjectDynamicMapCommon<V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    private byte[] maxDistances;

    BaseLongToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createValuesArray", createValuesArray));
        }

        initialize();

        if (DEBUG) {

            exit();
        }
    }

    BaseLongToObjectMaxDistanceNonBucketMap(AllocationType allocationType, BaseLongToObjectMaxDistanceNonBucketMap<V, M> toCopy) {
        super(allocationType, toCopy);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        this.maxDistances = MaxDistance.copyMaxDistances(toCopy.maxDistances);

        if (DEBUG) {

            exit();
        }
    }

    private void initialize() {

        this.maxDistances = new byte[getHashedCapacity()];
    }

    @Override
    public final boolean containsKey(long key) {

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final boolean result = scanHashArrayForIndex(key, getKeyMask()) != NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final V get(long key, V defaultValue) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int index = scanHashArrayForIndex(key, getKeyMask());

        final V result = index != NO_INDEX ? getValues()[index] : defaultValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<Void, M, P, R> makeFromElements) {

        checkMakeFromElementsParameters(allocationType, parameter, makeFromElements);

        @SuppressWarnings("unchecked")
        final M thisMap = (M)this;

        return makeFromElements.apply(allocationType, null, thisMap, getMakeFromElementsNumElements(), parameter);
    }

    @Override
    protected final void recreateElements() {

        super.recreateElements();

        initialize();
    }

    @Override
    protected final void resetToNull() {

        super.resetToNull();

        this.maxDistances = null;
    }

    @Override
    protected final int scanHashArrayForIndex(long key, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        final int result = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(getHashed(), key, hashArrayIndex, maxDistances[hashArrayIndex]);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
    }

    @Override
    protected final void rehashWithCapacity(long[] hashed, long[] newHashed, int newCapacity) {

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed).add("newHashed", newHashed).add("newCapacity", newCapacity));
        }

        this.maxDistances = new byte[newCapacity];

        super.rehashWithCapacity(hashed, newHashed, newCapacity);

        if (DEBUG) {

            enter(b -> b.add("hashed", hashed).add("newHashed", newHashed).add("newCapacity", newCapacity));
        }
    }

    private final MaxDistanceLongMapOperations<BaseLongToObjectMaxDistanceNonBucketMap<V, M>, V[]> maxDistanceOperations
            = new MaxDistanceLongMapOperations<BaseLongToObjectMaxDistanceNonBucketMap<V, M>, V[]>() {

        @Override
        public long put(BaseLongToObjectMaxDistanceNonBucketMap<V, M> hashed, long[] hashArray, long key, int hashArrayIndex) {

            return BaseLongToObjectMaxDistanceNonBucketMap.putKeyWithHashArrayIndex(hashArray, key, hashArrayIndex);
        }

        @Override
        public void incrementNumElements(BaseLongToObjectMaxDistanceNonBucketMap<V, M> hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseLongToObjectMaxDistanceNonBucketMap<V, M> hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public V[] getValues(BaseLongToObjectMaxDistanceNonBucketMap<V, M> hashed) {

            return hashed.getValues();
        }

        @Override
        public byte[] getMaxDistances(BaseLongToObjectMaxDistanceNonBucketMap<V, M> hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseLongToObjectMaxDistanceNonBucketMap<V, M> hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public long[] getHashArray(BaseLongToObjectMaxDistanceNonBucketMap<V, M> hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseLongToObjectMaxDistanceNonBucketMap<V, M> hashed) {

            return hashed.getHashedCapacity();
        }
    };

    final V putMaxDistance(long key, V value, V defaultPreviousValue) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        checkCapacityForOneMoreElement();

        final V result = MaxDistance.putMaxDistance(this, key, value, defaultPreviousValue, maxDistanceOperations);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    final int removeMaxDistance(long key) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int result = HashArray.removeAndReturnIndexScanToMax(getHashed(), key, getKeyMask(), maxDistances);

        if (result != NO_INDEX) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equals(P1 thisParameter, ILongToObjectDynamicMapView<V> other, P2 otherParameter,
            IObjectValueMapEqualityTester<V, P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseLongToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseLongToObjectNonBucketMap<V, ?> otherMap = (BaseLongToObjectNonBucketMap<V, ?>)other;

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
    public final <P1, P2, E extends Exception> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<V, ILongToObjectDynamicMapView<V>, P1, P2, E> scratchEqualsParameter)
            throws E {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final ILongToObjectDynamicMapView<V> other = scratchEqualsParameter.getOther();

        if (other instanceof BaseLongToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseLongToObjectNonBucketMap<V, ?> otherMap = (BaseLongToObjectNonBucketMap<V, ?>)other;

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
