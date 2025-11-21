package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;

abstract class BaseObjectToObjectNonContainsKeyNonBucketMap<K, V>

        extends BaseObjectToObjectNonBucketMap<K, V, BaseObjectToObjectNonContainsKeyNonBucketMap<K, V>>
        implements IObjectToObjectBaseStaticMapCommon<K, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_TO_OBJECT_NON_CONTAINS_KEY_NON_BUCKET_MAP;

    private static final Object NO_VALUE = null;

    private final IntFunction<K[]> createKeysArray;
    private final IntFunction<V[]> createValuesArray;

    private V[] values;

    BaseObjectToObjectNonContainsKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<K[]> createKeysArray,
            IntFunction<V[]> createValuesArray) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createKeysArray", createKeysArray)
                    .add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectToObjectNonContainsKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor,
            IntFunction<K[]> createKeysArray, IntFunction<V[]> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        this.createKeysArray = Objects.requireNonNull(createKeysArray);
        this.createValuesArray = Objects.requireNonNull(createValuesArray);

        this.values = createValuesArray.apply(getHashedCapacity());

        clearValues(values);

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectToObjectNonContainsKeyNonBucketMap(AllocationType allocationType, BaseObjectToObjectNonContainsKeyNonBucketMap<K, V> toCopy,
            BiConsumer<V[], V[]> copyValuesContent) {
        super(allocationType, toCopy);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyValuesContent", copyValuesContent));
        }

        this.createKeysArray = toCopy.createKeysArray;

        final IntFunction<V[]> createValues = this.createValuesArray = toCopy.createValuesArray;

        final V[] values = this.values = createValues.apply(getHashedCapacity());

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
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter,
            IMakeFromElementsFunction<K[], BaseObjectToObjectNonContainsKeyNonBucketMap<K, V>, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        return makeFromElements.apply(allocationType, createKeysArray, this, getMakeFromElementsNumElements(), parameter);
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
        final int index = IntCapacityPutResult.getPutIndex(putResult);

        final V result = IntCapacityPutResult.getPutNewAdded(putResult) ? defaultPreviousValue : values[index];

        values[index] = value;

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("value", value).add("defaultPreviousValue", defaultPreviousValue));
        }

        return result;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equals(P1 thisParameter, IObjectToObjectBaseStaticMapView<K, V> other, P2 otherParameter,
            IObjectValueMapEqualityTester<V, P1, P2, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        final boolean result;

        if (DEBUG) {

            enter(b -> b.add("thisParameter", thisParameter).add("other", other).add("otherParameter", otherParameter).add("equalityTester", equalityTester));
        }

        if (other instanceof BaseObjectToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseObjectToObjectNonBucketMap<K, V, ?> otherMap = (BaseObjectToObjectNonBucketMap<K, V, ?>)other;

            result = equalsObjectToObjectNonBucketMap(thisParameter, otherMap, otherParameter, equalityTester);
        }
        else {
            result = IObjectToObjectBaseStaticMapCommon.super.equals(thisParameter, other, otherParameter, equalityTester);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P1, P2, E extends Exception> boolean equalsParameters(
            ObjectValueMapScratchEqualsParameter<V, IObjectToObjectBaseStaticMapView<K, V>, P1, P2, E> scratchEqualsParameter) throws E {

        Objects.requireNonNull(scratchEqualsParameter);

        if (DEBUG) {

            enter(b -> b.add("scratchEqualsParameter", scratchEqualsParameter));
        }

        final boolean result;

        final IObjectToObjectBaseStaticMapView<K, V> other = scratchEqualsParameter.getOther();

        if (other instanceof BaseObjectToObjectNonBucketMap) {

            @SuppressWarnings("unchecked")
            final BaseObjectToObjectNonBucketMap<K, V, ?> otherMap = (BaseObjectToObjectNonBucketMap<K, V, ?>)other;

            result = equalsObjectToObjectNonBucketMap(scratchEqualsParameter.getThisParameter(), otherMap, scratchEqualsParameter.getOtherParameter(),
                    scratchEqualsParameter.getEqualityTester());
        }
        else {
            result = IObjectToObjectBaseStaticMapCommon.super.equalsParameters(scratchEqualsParameter);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }
}
