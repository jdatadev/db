package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;

abstract class BaseLongToIntNonContainsKeyNonBucketMap extends BaseLongToIntNonBucketMap<BaseLongToIntNonContainsKeyNonBucketMap> implements ILongToIntBaseStaticMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_INT_NON_CONTAINS_NON_BUCKET_MAP;

    BaseLongToIntNonContainsKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent) {
        super(allocationType, initialCapacityExponent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongToIntNonContainsKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongToIntNonContainsKeyNonBucketMap(AllocationType allocationType, BaseLongToIntNonContainsKeyNonBucketMap toCopy) {
        super(allocationType, toCopy);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final int get(long key) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int result;

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
            IMakeFromElementsFunction<long[], BaseLongToIntNonContainsKeyNonBucketMap, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(allocationType, long[]::new, this, getMakeFromElementsNumElements(), parameter);
    }

    @Override
    protected final int scanHashArrayForIndex(long key, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int result = HashArray.getIndexScanEntireHashArray(getHashed(), key, keyMask);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
    }

    final int putValue(long key, int value, int defaultPreviousValue) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final long putResult = put(key);

        final int index = IntCapacityPutResult.getPutIndex(putResult);
        final int[] values = getValues();

        final int result = IntCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values[index];

        values[index] = value;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equals(P1 thisParameter, ILongToIntBaseStaticMapView other, P2 otherParameter,
            IIntValueMapEqualityTester<P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseLongToIntNonBucketMap) {

            final BaseLongToIntNonBucketMap<?> otherMap = (BaseLongToIntNonBucketMap<?>)other;

            result = equalsLongToIntNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = ILongToIntBaseStaticMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equalsParameters(IntValueMapScratchEqualsParameter<ILongToIntBaseStaticMapView, P1, P2, E> scratchEqualsParameter)
            throws E {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final ILongToIntBaseStaticMapView other = scratchEqualsParameter.getOther();

        if (other instanceof BaseLongToIntNonBucketMap) {

            final BaseLongToIntNonBucketMap<?> otherMap = (BaseLongToIntNonBucketMap<?>)other;

            result = equalsLongToIntNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = ILongToIntBaseStaticMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
