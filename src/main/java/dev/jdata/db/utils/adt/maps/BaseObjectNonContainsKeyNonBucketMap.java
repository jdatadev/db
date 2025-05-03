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
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseObjectNonContainsKeyNonBucketMap<K, V>

        extends BaseIntCapacityExponentMap<K[]>
        implements IObjectContainsKeyMap<K>, IToObjectMapGetters<V>, IObjectMapGetters<K, V>, IObjectNonContainsKeyNonBucketMapGetters<K, V> {

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
            IntFunction<V[]> createValues) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeyArray, BaseObjectNonContainsKeyNonBucketMap::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createValues", createValues));
        }

        this.createValues = Objects.requireNonNull(createValues);

        this.values = createValues.apply(computeCapacity());

        clearValues(values);

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectNonContainsKeyNonBucketMap(BaseObjectNonContainsKeyNonBucketMap<K, V> toCopy, BiConsumer<V[], V[]> copyValuesContent) {
        super(toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));

        final IntFunction<V[]> createValues = this.createValues = toCopy.createValues;

        final V[] values = this.values = createValues.apply(getCapacity());

        copyValuesContent.accept(toCopy.values, values);
    }

    @Override
    public final boolean containsKey(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int index = getIndex(key);

        final boolean result = index != NO_INDEX;

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    @Override
    public final K[] keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final K[] result = createHashedNonCleared(numElements);

        keys(result);

        if (DEBUG) {

            exit();
        }

        return result;
    }

    public final int keys(K[] dst) {

        return keysAndValues(dst, null, null, null);
    }

    @Override
    public V get(K key) {

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
    public final <P> void forEachValue(P parameter, ForEachValue<V, P> forEachValue) {

        Objects.requireNonNull(forEachValue);

        forEachKeyAndValue(parameter, forEachValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(values[valueIndex], p1));
    }

    @Override
    public final <P> void forEachKeyAndValue(P parameter, ForEachKeyAndValue<K, V, P> forEachKeyAndValue) {

        Objects.requireNonNull(forEachKeyAndValue);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEachKeyAndValue", forEachKeyAndValue));
        }

        forEachKeyAndValue(parameter, forEachKeyAndValue, (keys, keyIndex, values, valueIndex, p1, p2) -> p2.each(keys[keyIndex], values[valueIndex], p1));
    }

    @Override
    public final void keysAndValues(K[] keysDst, V[] valuesDst) {

        final long numElements = getNumElements();

        Checks.isGreaterThanOrEqualTo(keysDst.length, numElements);
        Checks.isGreaterThanOrEqualTo(valuesDst.length, numElements);
        Checks.areEqual(keysDst.length, valuesDst.length);

        if (DEBUG) {

            enter();
        }

        keysAndValues(keysDst, getValues(), valuesDst, (src, srcIndex, dst, dstIndex) -> dst[dstIndex] = src[srcIndex]);

        if (DEBUG) {

            exit(b -> b.add("keyDst", keysDst).add("valueDst", valuesDst));
        }
    }

    @Override
    protected final K[] rehash(K[] hashArray, int newCapacity, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("keyMask", keyMask));
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

            exit(newHashArray);
        }

        return newHashArray;
    }

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValueWithKeysAndValues<K[], V[], P1, P2> forEachKeyAndValue) {

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEachKeyAndValue", forEachKeyAndValue));
        }

        final K[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final V[] values = getValues();

        @SuppressWarnings("unchecked")
        final V[] noKey = (V[])NO_KEY;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey != noKey) {

                forEachKeyAndValue.each(hashArray, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final <S, D> int keysAndValues(K[] keysDst, S src, D dst, ValueSetter<S, D> valueSetter) {

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final K[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        @SuppressWarnings("unchecked")
        final V noKey = (V)NO_KEY;

        int dstIndex = 0;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey != noKey) {

                keysDst[dstIndex] = mapKey;

                if (dst != null) {

                    valueSetter.setValue(src, i, dst, dstIndex);
                }

                ++ dstIndex;
            }
        }

        if (DEBUG) {

            exit(dstIndex);
        }

        return dstIndex;
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

    protected final V[] getValues() {
        return values;
    }

    protected final int put(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final K[] hashArray = getHashed();

        final long putResult = put(hashArray, key);

        final int index = IntPutResult.getPutIndex(putResult);

        final boolean newAdded = IntPutResult.getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exit(index, b -> b.add("key", key).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return index;
    }

    final int removeAndReturnIndex(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%s keyMask=0x%08x", hashArrayIndex, key.toString(), getKeyMask());
        }

        final K[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        int removedIndex = NO_INDEX;

        boolean done = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey == NO_KEY) {

                done = true;

                break;
            }
            else if (mapKey.equals(key)) {

                if (DEBUG) {

                    debug("remove from map foundIndex=" + i);
                }

                @SuppressWarnings("unchecked")
                final K noKey = (K)NO_KEY;

                hashArray[i] = noKey;

                removedIndex = i;
                break;
            }
        }

        if (removedIndex == NO_INDEX && !done) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                final K mapKey = hashArray[i];

                if (mapKey == NO_KEY) {

                    if (ASSERT) {

                        done = true;
                    }

                    break;
                }
                else if (mapKey.equals(key)) {

                    if (DEBUG) {

                        debug("remove from map foundIndex=" + i);
                    }

                    @SuppressWarnings("unchecked")
                    final K noKey = (K)NO_KEY;

                    hashArray[i] = noKey;

                    removedIndex = i;
                    break;
                }
            }
        }

        final boolean removed = removedIndex != NO_INDEX;

        if (ASSERT) {

            if (!removed && !done) {

                throw new IllegalStateException();
            }
        }

        if (removed) {

            decrementNumElements();
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

    private void put(V[] values, int index, V[] newValues, int newIndex) {

        newValues[newIndex] = values[index];
    }

    private void clearValues(V[] values) {

        Arrays.fill(values, null);
    }

    private static <K> void clearHashArray(K[] hashArray) {

        Arrays.fill(hashArray, NO_KEY);
    }
}
