package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceObjectMapOperations;

abstract class BaseObjectMaxDistanceNonBucketMap<K, V>

        extends BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectDynamicMapCommon<K, V>>
        implements IObjectContainsKeyMap<K>, IObjectToObjectDynamicMapCommon<K, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    private byte[] maxDistances;

    BaseObjectMaxDistanceNonBucketMap(int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        initialize();

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectMaxDistanceNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        initialize();

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectMaxDistanceNonBucketMap(BaseObjectMaxDistanceNonBucketMap<K, V> toCopy) {
        super(toCopy);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy));
        }

        this.maxDistances = MaxDistance.copyMaxDistances(toCopy.maxDistances);

        if (DEBUG) {

            exit();
        }
    }

    private void initialize() {

        this.maxDistances = new byte[getCapacity()];
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

    private final MaxDistanceObjectMapOperations<BaseObjectMaxDistanceNonBucketMap<K, V>, K, V[]> maxDistanceOperations
            = new MaxDistanceObjectMapOperations<BaseObjectMaxDistanceNonBucketMap<K, V>, K, V[]>() {

        @Override
        public long put(BaseObjectMaxDistanceNonBucketMap<K, V> hashed, K[] hashArray, K key, int hashArrayIndex) {

            return hashed.put(hashArray, key, hashArrayIndex);
        }

        @Override
        public void incrementNumElements(BaseObjectMaxDistanceNonBucketMap<K, V> hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseObjectMaxDistanceNonBucketMap<K, V> hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public V[] getValues(BaseObjectMaxDistanceNonBucketMap<K, V> hashed) {

            return hashed.getValues();
        }

        @Override
        public byte[] getMaxDistances(BaseObjectMaxDistanceNonBucketMap<K, V> hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseObjectMaxDistanceNonBucketMap<K, V> hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public K[] getHashArray(BaseObjectMaxDistanceNonBucketMap<K, V> hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseObjectMaxDistanceNonBucketMap<K, V> hashed) {

            return hashed.getCapacity();
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
    public final <P1, P2> boolean equals(P1 thisParameter, IObjectToObjectDynamicMapCommon<K, V> other, P2 otherParameter, IObjectValueMapEqualityTester<V, P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseObjectToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectDynamicMapCommon<K, V>> otherMap = (BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectDynamicMapCommon<K, V>>)other;

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
    public final <P1, P2> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<V, IObjectToObjectDynamicMapCommon<K, V>, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final IObjectToObjectDynamicMapCommon<K, V> other = scratchEqualsParameter.getOther();

        if (other instanceof BaseObjectKeyNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectDynamicMapCommon<K, V>> otherMap = (BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectDynamicMapCommon<K, V>>)other;

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
