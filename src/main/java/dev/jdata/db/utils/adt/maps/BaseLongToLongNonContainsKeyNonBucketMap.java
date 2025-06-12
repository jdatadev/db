package dev.jdata.db.utils.adt.maps;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

abstract class BaseLongToLongNonContainsKeyNonBucketMap extends BaseLongToLongNonBucketMap<ILongToLongStaticMapCommon> implements ILongToLongStaticMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_LONG_NON_CONTAINS_NON_BUCKET_MAP;

    BaseLongToLongNonContainsKeyNonBucketMap(int initialCapacityExponent) {
        super(initialCapacityExponent);
    }

    BaseLongToLongNonContainsKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor);
    }

    BaseLongToLongNonContainsKeyNonBucketMap(BaseLongToLongNonContainsKeyNonBucketMap toCopy) {
        super(toCopy);
    }

    @Override
    public final long get(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final long result;

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

        final int result = getIndexScanEntireHashArray(key, getKeyMask());

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
    }

    final long putValue(long key, long value, long defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final long result;

        final long putResult = put(key);
        final int index = IntPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            final long[] values = getValues();

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
    public final <P1, P2> boolean equals(P1 thisParameter, ILongToLongStaticMapCommon other, P2 otherParameter, ILongValueMapEqualityTester<P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseLongToLongNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseLongToLongNonBucketMap<ILongToLongStaticMapCommon> otherMap = (BaseLongToLongNonBucketMap<ILongToLongStaticMapCommon>)other;

            result = equalsLongToLongNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = ILongToLongStaticMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2> boolean equalsParameters(LongValueMapScratchEqualsParameter<ILongToLongStaticMapCommon, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final ILongToLongStaticMapCommon other = scratchEqualsParameter.getOther();

        if (other instanceof BaseLongToLongNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseLongToLongNonBucketMap<ILongToLongStaticMapCommon> otherMap = (BaseLongToLongNonBucketMap<ILongToLongStaticMapCommon>)other;

            result = equalsLongToLongNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = ILongToLongStaticMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
