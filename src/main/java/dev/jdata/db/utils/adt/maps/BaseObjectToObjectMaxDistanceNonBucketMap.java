package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceObjectMapOperations;

abstract class BaseObjectToObjectMaxDistanceNonBucketMap<K, V>

        extends BaseObjectToObjectNonBucketMap<K, V, BaseObjectToObjectMaxDistanceNonBucketMap<K, V>>
        implements IObjectToObjectDynamicMapCommon<K, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    private final IntFunction<K[]> createKeysArray;

    private byte[] maxDistances;

    BaseObjectToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createKeysArray", createKeysArray)
                    .add("createValuesArray", createValuesArray));
        }

        this.createKeysArray = Objects.requireNonNull(createKeysArray);

        initialize();

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        this.createKeysArray = Objects.requireNonNull(createKeysArray);

        initialize();

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectToObjectMaxDistanceNonBucketMap(AllocationType allocationType, BaseObjectToObjectMaxDistanceNonBucketMap<K, V> toCopy) {
        super(allocationType, toCopy);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        this.createKeysArray = toCopy.createKeysArray;

        this.maxDistances = MaxDistance.copyMaxDistances(toCopy.maxDistances);

        if (DEBUG) {

            exit();
        }
    }

    private void initialize() {

        this.maxDistances = new byte[getHashedCapacity()];
    }

    @Override
    public final boolean containsKey(K key) {

        Objects.requireNonNull(key);

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
    public final V get(K key, V defaultValue) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int index = getHashArrayIndex(key, getKeyMask());

        final V result = index != NO_INDEX ? getValues()[index] : defaultValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter,
            IMakeFromElementsFunction<K[], BaseObjectToObjectMaxDistanceNonBucketMap<K, V>, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(allocationType, createKeysArray, this, getMakeFromElementsNumElements(), parameter);
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
    protected final int getHashArrayIndex(K key, int keyMask) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, keyMask);

        final int result = getIndexScanHashArrayToMaxHashArrayIndex(key, hashArrayIndex, maxDistances[hashArrayIndex]);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
    }

    @Override
    protected final K[] rehash(K[] hashArray, int newCapacity, int newCapacityExponent, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).add("newKeyMask", newKeyMask));
        }

        this.maxDistances = new byte[newCapacity];

        final K[] result = super.rehash(hashArray, newCapacity, newCapacityExponent, newKeyMask);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).add("newKeyMask", newKeyMask));
        }

        return result;
    }

    private final MaxDistanceObjectMapOperations<BaseObjectToObjectMaxDistanceNonBucketMap<K, V>, K, V[]> maxDistanceOperations
            = new MaxDistanceObjectMapOperations<BaseObjectToObjectMaxDistanceNonBucketMap<K, V>, K, V[]>() {

        @Override
        public long put(BaseObjectToObjectMaxDistanceNonBucketMap<K, V> hashed, K[] hashArray, K key, int hashArrayIndex) {

            return hashed.put(hashArray, key, hashArrayIndex);
        }

        @Override
        public void incrementNumElements(BaseObjectToObjectMaxDistanceNonBucketMap<K, V> hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseObjectToObjectMaxDistanceNonBucketMap<K, V> hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public V[] getValues(BaseObjectToObjectMaxDistanceNonBucketMap<K, V> hashed) {

            return hashed.getValues();
        }

        @Override
        public byte[] getMaxDistances(BaseObjectToObjectMaxDistanceNonBucketMap<K, V> hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseObjectToObjectMaxDistanceNonBucketMap<K, V> hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public K[] getHashArray(BaseObjectToObjectMaxDistanceNonBucketMap<K, V> hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseObjectToObjectMaxDistanceNonBucketMap<K, V> hashed) {

            return hashed.getHashedCapacity();
        }
    };

    final V putMaxDistance(K key, V value, V defaultPreviousValue) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        checkCapacity(1);

        final V result = MaxDistance.putMaxDistance(this, key, value, defaultPreviousValue, maxDistanceOperations);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    final int removeMaxDistance(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int result = HashArray.removeAndReturnIndexScanToMax(getHashed(), key, getKeyMask(), getNoKey(), maxDistances);

        if (result != NO_INDEX) {

            decrementNumElements();
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equals(P1 thisParameter, IObjectToObjectDynamicMapView<K, V> other, P2 otherParameter,
            IObjectValueMapEqualityTester<V, P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseObjectToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseObjectToObjectNonBucketMap<K, V, ?> otherMap = (BaseObjectToObjectNonBucketMap<K, V, ?>)other;

            result = equalsObjectToObjectNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = IObjectToObjectDynamicMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equalsParameters(
            ObjectValueMapScratchEqualsParameter<V, IObjectToObjectDynamicMapView<K, V>, P1, P2, E> scratchEqualsParameter) throws E {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final IObjectToObjectDynamicMapView<K, V> other = scratchEqualsParameter.getOther();

        if (other instanceof BaseObjectKeyNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseObjectToObjectNonBucketMap<K, V, ?> otherMap = (BaseObjectToObjectNonBucketMap<K, V, ?>)other;

            result = equalsObjectToObjectNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = IObjectToObjectDynamicMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
