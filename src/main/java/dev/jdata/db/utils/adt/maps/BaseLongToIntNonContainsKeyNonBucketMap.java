package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

abstract class BaseLongToIntNonContainsKeyNonBucketMap extends BaseLongToIntNonBucketMap<ILongToIntStaticMapCommon> implements ILongToIntStaticMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_INT_NON_CONTAINS_NON_BUCKET_MAP;

    BaseLongToIntNonContainsKeyNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    BaseLongToIntNonContainsKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseLongToIntNonContainsKeyNonBucketMap(BaseLongToIntNonContainsKeyNonBucketMap toCopy) {
        super(toCopy);
    }

    @Override
    public final int get(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int result;

        final int index = getIndexScanEntireHashArray(key, getKeyMask());

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
    protected final int getHashArrayIndex(long key, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int result = getIndexScanEntireHashArray(key, keyMask);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
    }

    final int putValue(int key, int value, int defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final int result;

        final long putResult = put(key);
        final int index = IntPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            final int[] values = getValues();

            result = IntPutResult.getPutNewAdded(putResult) ? values[index] : defaultPreviousValue;

            values[index] = value;
        }
        else {
            throw new IllegalStateException();
        }

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public final <P1, P2> boolean equals(P1 thisParameter, ILongToIntStaticMapCommon other, P2 otherParameter, IIntValueMapEqualityTester<P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseLongToIntNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseLongToIntNonBucketMap<ILongToIntStaticMapCommon> otherMap = (BaseLongToIntNonBucketMap<ILongToIntStaticMapCommon>)other;

            result = equalsLongToIntNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = ILongToIntStaticMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2> boolean equalsParameters(IntValueMapScratchEqualsParameter<ILongToIntStaticMapCommon, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final ILongToIntStaticMapCommon other = scratchEqualsParameter.getOther();

        if (other instanceof BaseLongToIntNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseLongToIntNonBucketMap<ILongToIntStaticMapCommon> otherMap = (BaseLongToIntNonBucketMap<ILongToIntStaticMapCommon>)other;

            result = equalsLongToIntNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = ILongToIntStaticMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
