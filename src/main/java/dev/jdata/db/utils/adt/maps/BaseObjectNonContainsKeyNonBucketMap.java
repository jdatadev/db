package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;

abstract class BaseObjectNonContainsKeyNonBucketMap<K, V>

        extends BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectStaticMapCommon<K, V>>
        implements IObjectToObjectStaticMapCommon<K, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_NON_CONTAINS_KEY_NON_BUCKET_MAP;

    private static final Object NO_VALUE = null;

    private final IntFunction<V[]> createValues;

    private V[] values;

    protected BaseObjectNonContainsKeyNonBucketMap(int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectNonContainsKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        this.createValues = Objects.requireNonNull(createValuesArray);

        this.values = createValuesArray.apply(getCapacity());

        clearValues(values);

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectNonContainsKeyNonBucketMap(BaseObjectNonContainsKeyNonBucketMap<K, V> toCopy, BiConsumer<V[], V[]> copyValuesContent) {
        super(toCopy);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyValuesContent", copyValuesContent));
        }

        final IntFunction<V[]> createValues = this.createValues = toCopy.createValues;

        final V[] values = this.values = createValues.apply(getCapacity());

        copyValuesContent.accept(toCopy.values, values);

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final V get(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = HashArray.getIndexScanEntireHashArray(getHashed(), key, getKeyMask());

        @SuppressWarnings("unchecked")
        final V noValue = (V)NO_VALUE;

        final V result = index != NO_INDEX ? getValues()[index] : noValue;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    protected final int getHashArrayIndex(K key, int keyMask) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int result = getIndexScanEntireHashArray(key, keyMask);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
    }

    final V putValue(K key, V value, V defaultPreviousValue) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        final long putResult = put(key);

        final V[] values = getValues();
        final int index = IntPutResult.getPutIndex(putResult);

        final V result = IntPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values[index];

        values[index] = value;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    final void clearBaseObjectNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P1, P2> boolean equals(P1 thisParameter, IObjectToObjectStaticMapCommon<K, V> other, P2 otherParameter, IObjectValueMapEqualityTester<V, P1, P2> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseObjectToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectStaticMapCommon<K, V>> otherMap = (BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectStaticMapCommon<K, V>>)other;

            result = equalsObjectToObjectNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = IObjectToObjectStaticMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2> boolean equalsParameters(ObjectValueMapScratchEqualsParameter<V, IObjectToObjectStaticMapCommon<K, V>, P1, P2> scratchEqualsParameter) {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final IObjectToObjectStaticMapCommon<K, V> other = scratchEqualsParameter.getOther();

        if (other instanceof BaseObjectToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectStaticMapCommon<K, V>> otherMap = (BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectStaticMapCommon<K, V>>)other;

            result = equalsObjectToObjectNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = IObjectToObjectStaticMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
