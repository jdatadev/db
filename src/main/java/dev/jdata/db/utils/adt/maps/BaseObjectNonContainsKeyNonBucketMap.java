package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.checks.AssertionContants;

abstract class BaseObjectNonContainsKeyNonBucketMap<K, V> extends BaseObjectToObjectNonBucketMap<K, V, IObjectToObjectStaticMapCommon<K, V>> implements IObjectToObjectStaticMapCommon<K, V> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_NON_CONTAINS_KEY_NON_BUCKET_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_OBJECT_NON_CONTAINS_KEY_NON_BUCKET_MAP;

    private static final Object NO_KEY = null;
    private static final Object NO_VALUE = null;

    private final IntFunction<V[]> createValues;

    private V[] values;

    protected BaseObjectNonContainsKeyNonBucketMap(int initialCapacityExponent, IntFunction<K[]> createKeyArray, IntFunction<V[]> createValues) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createKeyArray, createValues);
    }

    BaseObjectNonContainsKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeyArray,
            IntFunction<V[]> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeyArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createValues", createValuesArray));
        }

        this.createValues = Objects.requireNonNull(createValuesArray);

        this.values = createValuesArray.apply(computeCapacity());

        clearValues(values);

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectNonContainsKeyNonBucketMap(BaseObjectNonContainsKeyNonBucketMap<K, V> toCopy, BiConsumer<V[], V[]> copyValuesContent) {
        super(toCopy);

        final IntFunction<V[]> createValues = this.createValues = toCopy.createValues;

        final V[] values = this.values = createValues.apply(getCapacity());

        copyValuesContent.accept(toCopy.values, values);
    }

    @Override
    public final V get(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = getIndex(key);

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

    @Override
    protected final K[] rehash(K[] hashArray, int newCapacity, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("newKeyMask", newKeyMask));
        }

        final K[] newHashArray = createHashedNonCleared(newCapacity);

        clearHashArray(newHashArray);

        final V[] newValuesArray = createValues.apply(newCapacity);

        final int hashArrayLength = hashArray.length;

        @SuppressWarnings("unchecked")
        final V noKey = (V)NO_KEY;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final K key = hashArray[i];

            if (key != noKey) {

                final int newIndex = IntPutResult.getPutIndex(put(newHashArray, key));

                newHashArray[newIndex] = key;

                put(values, i, newValuesArray, newIndex);
            }
        }

        this.values = newValuesArray;

        if (DEBUG) {

            exit(newHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("newKeyMask", newKeyMask));
        }

        return newHashArray;
    }

    protected final int getIndex(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, getKeyMask());

        final K[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        @SuppressWarnings("unchecked")
        final V noKey = (V)NO_KEY;

        int found = NO_INDEX;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final K hashArrayKey = hashArray[i];

            if (hashArrayKey != noKey && hashArrayKey.equals(key)) {

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                final K hashArrayKey = hashArray[i];

                if (hashArrayKey != noKey && hashArrayKey.equals(key)) {

                    found = i;
                    break;
                }
            }
        }

        if (DEBUG) {

            exit(found, b -> b.add("key", key));
        }

        return found;
    }

    final int removeAndReturnIndex(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int removedIndex = removeAndReturnIndexIfExists(key);

        if (removedIndex == NO_INDEX) {

            throw new IllegalArgumentException();
        }

        if (DEBUG) {

            exit(removedIndex);
        }

        return removedIndex;
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

    private long put(K[] hashArray, K key) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%s keyMask=0x%08x", hashArrayIndex, key.toString(), getKeyMask());
        }

        final int hashArrayLength = hashArray.length;

        @SuppressWarnings("unchecked")
        final V noKey = (V)NO_KEY;

        int found = NO_INDEX;

        boolean newAdded = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey == noKey) {

                if (DEBUG) {

                    debug("add to map new foundIndex=" + i);
                }

                hashArray[i] = key;

                found = i;

                newAdded = true;
                break;
            }
            else if (mapKey.equals(key)) {

                if (DEBUG) {

                    debug("add to map existing foundIndex=" + i);
                }

                hashArray[i] = key;

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                final K mapKey = hashArray[i];

                if (mapKey == noKey) {

                    if (DEBUG) {

                        debug("add to map foundIndex=" + i);
                    }

                    hashArray[i] = key;

                    found = i;

                    newAdded = true;
                    break;
                }
                else if (mapKey.equals(key)) {

                    if (DEBUG) {

                        debug("add to map foundIndex=" + i);
                    }

                    hashArray[i] = key;

                    found = i;
                    break;
                }
            }
        }

        if (ASSERT) {

            if (found == NO_INDEX) {

                throw new IllegalStateException();
            }
        }

        final long result = IntPutResult.makePutResult(newAdded, found);

        if (DEBUG) {

            exitWithBinary(result, b -> b.add("hashArray", hashArray).add("key", key));
        }

        return result;
    }

    private static <K> void clearHashArray(K[] hashArray) {

        Arrays.fill(hashArray, NO_KEY);
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
