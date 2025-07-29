package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceIntMapOperations;

abstract class BaseIntToObjectMaxDistanceNonBucketMap<T> extends BaseIntToObjectNonBucketMap<T, IIntToObjectDynamicMapCommon<T>> implements IIntToObjectDynamicMapCommon<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_OBJECT_MAX_DISTANCE_NON_BUCKET_MAP;

    private byte[] maxDistances;

    BaseIntToObjectMaxDistanceNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("createValuesArray", createValuesArray));
        }

        initialize();

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToObjectMaxDistanceNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createValuesArray", createValuesArray));
        }

        initialize();

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToObjectMaxDistanceNonBucketMap(BaseIntToObjectMaxDistanceNonBucketMap<T> toCopy) {
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
    public final T get(int key, T defaultValue) {

        IntNonBucket.checkIsHashArrayElement(key);

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

    private final MaxDistanceIntMapOperations<BaseIntToObjectMaxDistanceNonBucketMap<T>, T[]> maxDistanceOperations
            = new MaxDistanceIntMapOperations<BaseIntToObjectMaxDistanceNonBucketMap<T>, T[]>() {

        @Override
        public long put(BaseIntToObjectMaxDistanceNonBucketMap<T> hashed, int[] hashArray, int key, int hashArrayIndex) {

            return hashed.put(hashArray, key, hashArrayIndex);
        }

        @Override
        public void incrementNumElements(BaseIntToObjectMaxDistanceNonBucketMap<T> hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseIntToObjectMaxDistanceNonBucketMap<T> hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public T[] getValues(BaseIntToObjectMaxDistanceNonBucketMap<T> hashed) {

            return hashed.getValues();
        }

        @Override
        public byte[] getMaxDistances(BaseIntToObjectMaxDistanceNonBucketMap<T> hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseIntToObjectMaxDistanceNonBucketMap<T> hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public int[] getHashArray(BaseIntToObjectMaxDistanceNonBucketMap<T> hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseIntToObjectMaxDistanceNonBucketMap<T> hashed) {

            return hashed.getCapacity();
        }
    };

    final T putMaxDistance(int key, T value, T defaultPreviousValue) {

        IntNonBucket.checkIsHashArrayElement(key);

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
    public final <P1, P2> boolean equals(P1 thisParameter, IIntToObjectDynamicMapCommon<T> other, P2 otherParameter, IObjectValueMapEqualityTester<T, P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseIntToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseIntToObjectNonBucketMap<T, IIntToObjectDynamicMapCommon<T>> otherMap = (BaseIntToObjectNonBucketMap<T, IIntToObjectDynamicMapCommon<T>>)other;

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
    public final <P1, P2> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<T, IIntToObjectDynamicMapCommon<T>, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final IIntToObjectDynamicMapCommon<T> other = scratchEqualsParameter.getOther();

        if (other instanceof BaseIntToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseIntToObjectNonBucketMap<T, IIntToObjectDynamicMapCommon<T>> otherMap = (BaseIntToObjectNonBucketMap<T, IIntToObjectDynamicMapCommon<T>>)other;

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
            final BaseIntToObjectMaxDistanceNonBucketMap<T> other = (BaseIntToObjectMaxDistanceNonBucketMap<T>)object;

            result = equalsIntKeyNonBucketMapWithIndex(other, null, null, null, (v1, i1, p1, v2, i2, p2, predicate) -> Objects.equals(v1[i1], v2[i2]));
        }

        return result;
    }
}
