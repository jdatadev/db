package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;

abstract class BaseIntToLongNonContainsNonBucketMap extends BaseIntToLongNonBucketMap<BaseIntToLongNonContainsNonBucketMap> implements IIntToLongBaseStaticMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_LONG_NON_CONTAINS_NON_BUCKET_MAP;

    BaseIntToLongNonContainsNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToLongNonContainsNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntToLongNonContainsNonBucketMap(AllocationType allocationType, BaseIntToLongNonContainsNonBucketMap toCopy) {
        super(allocationType, toCopy);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long get(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final long result;

        final int index = HashArray.getIndexScanEntireHashArray(getHashed(), key, getKeyMask());

        if (index != NO_INDEX) {

            result = getValues()[index];
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
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter,
            IMakeFromElementsFunction<int[], BaseIntToLongNonContainsNonBucketMap, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(allocationType, int[]::new, this, getMakeFromElementsNumElements(), parameter);
    }

    @Override
    protected final int getHashArrayIndex(int key, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int result = HashArray.getIndexScanEntireHashArray(getHashed(), key, keyMask);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
    }

    final long putValue(int key, long value, long defaultPreviousValue) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final long putResult = put(key);

        final int index = IntCapacityPutResult.getPutIndex(putResult);
        final long[] values = getValues();

        final long result = IntCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values[index];

        values[index] = value;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    final void clearBaseIntToLongNonContainsNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equals(P1 thisParameter, IIntToLongBaseStaticMapView other, P2 otherParameter,
            ILongValueMapEqualityTester<P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseIntToLongNonBucketMap) {

            final BaseIntToLongNonBucketMap<?> otherMap = (BaseIntToLongNonBucketMap<?>)other;

            result = equalsIntToLongNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = IIntToLongBaseStaticMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equalsParameters(LongValueMapScratchEqualsParameter<IIntToLongBaseStaticMapView, P1, P2, E> scratchEqualsParameter)
            throws E {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final IIntToLongBaseStaticMapView other = scratchEqualsParameter.getOther();

        if (other instanceof BaseIntToLongNonBucketMap) {

            final BaseIntToLongNonBucketMap<?> otherMap = (BaseIntToLongNonBucketMap<?>)other;

            result = equalsIntToLongNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = IIntToLongBaseStaticMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
