package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;

abstract class BaseIntToObjectNonContainsKeyNonBucketMap<T> extends BaseIntToObjectNonBucketMap<T, IIntToObjectStaticMapCommon<T>> implements IIntToObjectStaticMapCommon<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_TO_OBJECT_NON_CONTAINS_NON_BUCKET_MAP;

    BaseIntToObjectNonContainsKeyNonBucketMap(int initialCapacityExponent, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, createValuesArray);
    }

    BaseIntToObjectNonContainsKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createValuesArray);
    }

    BaseIntToObjectNonContainsKeyNonBucketMap(BaseIntToObjectNonContainsKeyNonBucketMap<T> toCopy) {
        super(toCopy);
    }

    @Override
    public final T get(int key) {

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
    protected final int getHashArrayIndex(int key, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int result = getIndexScanEntireHashArray(key, getKeyMask());

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
    public final <P1, P2> boolean equals(P1 thisParameter, IIntToObjectStaticMapCommon<T> other, P2 otherParameter, IObjectValueMapEqualityTester<T, P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseIntToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseIntToObjectNonBucketMap<T, IIntToObjectStaticMapCommon<T>> otherMap = (BaseIntToObjectNonBucketMap<T, IIntToObjectStaticMapCommon<T>>)other;

            result = equalsIntToObjectNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = IIntToObjectStaticMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<T, IIntToObjectStaticMapCommon<T>, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final IIntToObjectStaticMapCommon<T> other = scratchEqualsParameter.getOther();

        if (other instanceof BaseIntToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseIntToObjectNonBucketMap<T, IIntToObjectStaticMapCommon<T>> otherMap = (BaseIntToObjectNonBucketMap<T, IIntToObjectStaticMapCommon<T>>)other;

            result = equalsIntToObjectNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = IIntToObjectStaticMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
