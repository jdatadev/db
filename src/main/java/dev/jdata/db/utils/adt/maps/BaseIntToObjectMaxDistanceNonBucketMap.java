package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceIntMapOperations;

abstract class BaseIntToObjectMaxDistanceNonBucketMap<V>

        extends BaseIntToObjectNonBucketMap<V, BaseIntToObjectMaxDistanceNonBucketMap<V>>
        implements IIntToObjectDynamicMapCommon<V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    private byte[] maxDistances;

    BaseIntToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createValuesArray", createValuesArray));
        }

        initialize();

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToObjectMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<V[]> createValuesArray) {
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

    BaseIntToObjectMaxDistanceNonBucketMap(AllocationType allocationType, BaseIntToObjectMaxDistanceNonBucketMap<V> toCopy) {
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
    public final boolean containsKey(int key) {

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
    public final V get(int key, V defaultValue) {

        IntNonBucket.checkIsHashArrayElement(key);

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
            IMakeFromElementsFunction<int[], BaseIntToObjectMaxDistanceNonBucketMap<V>, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(allocationType, int[]::new, this, getMakeFromElementsNumElements(), parameter);
    }

    @Override
    protected void recreateElements() {

        super.recreateElements();

       initialize();
    }

    @Override
    protected void resetToNull() {

        super.resetToNull();

        this.maxDistances = null;

    }

    @Override
    protected final int getHashArrayIndex(int key, int keyMask) {

        IntNonBucket.checkIsHashArrayElement(key);

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
    protected final int[] rehash(int[] hashArray, int newCapacity, int newCapacityExponent, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).add("newKeyMask", newKeyMask));
        }

        this.maxDistances = new byte[newCapacity];

        final int[] result = super.rehash(hashArray, newCapacity, newCapacityExponent, newKeyMask);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).add("newKeyMask", newKeyMask));
        }

        return result;
    }

    private final MaxDistanceIntMapOperations<BaseIntToObjectMaxDistanceNonBucketMap<V>, V[]> maxDistanceOperations
            = new MaxDistanceIntMapOperations<BaseIntToObjectMaxDistanceNonBucketMap<V>, V[]>() {

        @Override
        public long put(BaseIntToObjectMaxDistanceNonBucketMap<V> hashed, int[] hashArray, int key, int hashArrayIndex) {

            return hashed.put(hashArray, key, hashArrayIndex);
        }

        @Override
        public void incrementNumElements(BaseIntToObjectMaxDistanceNonBucketMap<V> hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseIntToObjectMaxDistanceNonBucketMap<V> hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public V[] getValues(BaseIntToObjectMaxDistanceNonBucketMap<V> hashed) {

            return hashed.getValues();
        }

        @Override
        public byte[] getMaxDistances(BaseIntToObjectMaxDistanceNonBucketMap<V> hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseIntToObjectMaxDistanceNonBucketMap<V> hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public int[] getHashArray(BaseIntToObjectMaxDistanceNonBucketMap<V> hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseIntToObjectMaxDistanceNonBucketMap<V> hashed) {

            return hashed.getHashedCapacity();
        }
    };

    final V putMaxDistance(int key, V value, V defaultPreviousValue) {

        IntNonBucket.checkIsHashArrayElement(key);

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

    final int removeMaxDistance(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

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
    public final <P1, P2, E extends Exception> boolean equals(P1 thisParameter, IIntToObjectDynamicMapView<V> other, P2 otherParameter,
            IObjectValueMapEqualityTester<V, P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseIntToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseIntToObjectNonBucketMap<V, ?> otherMap = (BaseIntToObjectNonBucketMap<V, ?>)other;

            result = equalsIntToObjectNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = IIntToObjectDynamicMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<V, IIntToObjectDynamicMapView<V>, P1, P2, E> scratchEqualsParameter)
            throws E {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final IIntToObjectDynamicMapView<V> other = scratchEqualsParameter.getOther();

        if (other instanceof BaseIntToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseIntToObjectNonBucketMap<V, ?> otherMap = (BaseIntToObjectNonBucketMap<V, ?>)other;

            result = equalsIntToObjectNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = IIntToObjectDynamicMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (!super.equals(object)) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            @SuppressWarnings("unchecked")
            final BaseIntToObjectMaxDistanceNonBucketMap<V> other = (BaseIntToObjectMaxDistanceNonBucketMap<V>)object;

            result = equalsIntKeyNonBucketMapWithIndex(other, null, null, null, (v1, i1, p1, v2, i2, p2, predicate) -> Objects.equals(v1[i1], v2[i2]));
        }

        return result;
    }
}
