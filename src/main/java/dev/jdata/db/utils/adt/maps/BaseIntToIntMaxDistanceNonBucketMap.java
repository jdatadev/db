package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance;
import dev.jdata.db.utils.adt.hashed.helpers.MaxDistance.MaxDistanceIntMapOperations;

abstract class BaseIntToIntMaxDistanceNonBucketMap extends BaseIntToIntNonBucketMap<BaseIntToIntMaxDistanceNonBucketMap> implements IIntToIntDynamicMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_INT_MAX_DISTANCE_NON_BUCKET_MAP;

    private byte[] maxDistances;

    BaseIntToIntMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent));
        }

        initialize();

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToIntMaxDistanceNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        initialize();

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToIntMaxDistanceNonBucketMap(AllocationType allocationType, BaseIntToIntMaxDistanceNonBucketMap toCopy) {
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
    public final int get(int key, int defaultValue) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        final int index = getHashArrayIndex(key, getKeyMask());

        final int result = index != NO_INDEX ? getValues()[index] : defaultValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("defaultValue", defaultValue));
        }

        return result;
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter,
            IMakeFromElementsFunction<int[], BaseIntToIntMaxDistanceNonBucketMap, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(allocationType, int[]::new, this, getMakeFromElementsNumElements(), parameter);
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

    private static final MaxDistanceIntMapOperations<BaseIntToIntMaxDistanceNonBucketMap, int[]> maxDistanceOperations
            = new MaxDistanceIntMapOperations<BaseIntToIntMaxDistanceNonBucketMap, int[]>() {

        @Override
        public long put(BaseIntToIntMaxDistanceNonBucketMap hashed, int[] hashArray, int key, int hashArrayIndex) {

            return hashed.put(hashArray, key, hashArrayIndex);
        }

        @Override
        public void incrementNumElements(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            hashed.incrementNumElements();
        }

        @Override
        public void increaseCapacityAndRehash(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            hashed.increaseCapacityAndRehash();
        }

        @Override
        public int[] getValues(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            return hashed.getValues();
        }

        @Override
        public byte[] getMaxDistances(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            return hashed.maxDistances;
        }

        @Override
        public int getKeyMask(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            return hashed.getKeyMask();
        }

        @Override
        public int[] getHashArray(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            return hashed.getHashed();
        }

        @Override
        public long getCapacity(BaseIntToIntMaxDistanceNonBucketMap hashed) {

            return hashed.getHashedCapacity();
        }
    };

    final int putMaxDistance(int key, int value, int defaultPreviousValue) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        checkCapacity(1);

        final int result = MaxDistance.putMaxDistance(this, key, value, defaultPreviousValue, maxDistanceOperations);

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
    public final <P1, P2, E extends Exception> boolean equals(P1 thisParameter, IIntToIntDynamicMapView other, P2 otherParameter,
            IIntValueMapEqualityTester<P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseIntToIntNonBucketMap) {

            final BaseIntToIntNonBucketMap<?> otherMap = (BaseIntToIntNonBucketMap<?>)other;

            result = equalsIntToIntNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = IIntToIntDynamicMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equalsParameters(IntValueMapScratchEqualsParameter<IIntToIntDynamicMapView, P1, P2, E> scratchEqualsParameter) throws E {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final IIntToIntDynamicMapView other = scratchEqualsParameter.getOther();

        if (other instanceof BaseIntToIntNonBucketMap) {

            final BaseIntToIntNonBucketMap<?> otherMap = (BaseIntToIntNonBucketMap<?>)other;

            result = equalsIntToIntNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = IIntToIntDynamicMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
