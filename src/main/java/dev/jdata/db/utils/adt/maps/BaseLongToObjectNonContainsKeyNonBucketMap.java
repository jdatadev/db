package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

abstract class BaseLongToObjectNonContainsKeyNonBucketMap<T>

        extends BaseLongToObjectNonBucketMap<T, ILongToObjectStaticMapCommon<T>>
        implements ILongToObjectStaticMapCommon<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_TO_OBJECT_NON_CONTAINS_NON_BUCKET_MAP;

    BaseLongToObjectNonContainsKeyNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, createArray);
    }

    BaseLongToObjectNonContainsKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createArray);
    }

    @Override
    public final T get(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final T result;

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

    final T putValue(int key, T value, T defaultPreviousValue) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final T result;

        final long putResult = put(key);
        final int index = IntPutResult.getPutIndex(putResult);

        if (index != NO_INDEX) {

            final T[] values = getValues();

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
    public final <P1, P2> boolean equals(P1 thisParameter, ILongToObjectStaticMapCommon<T> other, P2 otherParameter, IObjectValueMapEqualityTester<T, P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseLongToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseLongToObjectNonBucketMap<T, ILongToObjectStaticMapCommon<T>> otherMap = (BaseLongToObjectNonBucketMap<T, ILongToObjectStaticMapCommon<T>>)other;

            result = equalsLongToObjectNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = ILongToObjectStaticMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<T, ILongToObjectStaticMapCommon<T>, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final ILongToObjectStaticMapCommon<T> other = scratchEqualsParameter.getOther();

        if (other instanceof BaseLongToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseLongToObjectNonBucketMap<T, ILongToObjectStaticMapCommon<T>> otherMap = (BaseLongToObjectNonBucketMap<T, ILongToObjectStaticMapCommon<T>>)other;

            result = equalsLongToObjectNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = ILongToObjectStaticMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
