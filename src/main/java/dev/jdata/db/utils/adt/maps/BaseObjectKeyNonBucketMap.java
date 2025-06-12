package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashedConstants;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseObjectKeyNonBucketMap<K, V> extends BaseIntCapacityExponentMap<K[]> implements IObjectKeyMap<K> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_NON_BUCKET_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_OBJECT_NON_BUCKET_MAP;

    private static final Object NO_KEY = null;

    private final IntFunction<K[]> createKeysArray;
    private final IntFunction<V> createValuesArray;

    private V values;

    protected abstract void put(V values, int index, V newValues, int newIndex);
    protected abstract void clearValues(V values);

    protected abstract int getHashArrayIndex(K key, int keyMask);

    BaseObjectKeyNonBucketMap(int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createKeysArray, createValuesArray);
    }

    BaseObjectKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeysArray, IntFunction<V> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, BaseObjectKeyNonBucketMap::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        this.createKeysArray = Objects.requireNonNull(createKeysArray);
        this.createValuesArray = Objects.requireNonNull(createValuesArray);

        this.values = createValuesArray.apply(computeCapacity());

        clearValues(values);

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectKeyNonBucketMap(BaseObjectKeyNonBucketMap<K, V> toCopy, BiConsumer<V, V> copyValuesContent) {
        super(toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));

        this.createKeysArray = toCopy.createKeysArray;
        final IntFunction<V> createValues = this.createValuesArray = toCopy.createValuesArray;

        final V values = this.values = createValues.apply(getCapacity());

        copyValuesContent.accept(toCopy.values, values);
    }

    @Override
    public final K[] keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final K[] result = createKeysArray.apply(numElements);

        keys(result);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected K[] rehash(K[] hashArray, int newCapacity, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("newKeyMask", newKeyMask));
        }

        final K[] newHashArray = createKeysArray.apply(newCapacity);

        clearHashArray(newHashArray);

        final V newValuesArray = createValuesArray.apply(newCapacity);

        final int mapLength = hashArray.length;

        for (int i = 0; i < mapLength; ++ i) {

            final K key = hashArray[i];

            if (key != NO_KEY) {

                final long putResult = put(newHashArray, key);

                final int newIndex = IntPutResult.getPutIndex(putResult);

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

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValueWithKeysAndValues<K[], V, P1, P2> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final K[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final V values = getValues();

        for (int i = 0; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey != NO_KEY) {

                forEach.each(hashArray, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }
    }

    protected final <P1, P2, R, DELEGATE> R forEachKeyAndValueWithResultWithResult(R defaultResult, P1 parameter1, P2 parameter2, DELEGATE delegate,
            ForEachKeyAndValueWithKeysAndValuesWithResult<K[], V, P1, P2, DELEGATE, R> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final K[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final V values = getValues();

        for (int i = 0; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey != NO_KEY) {

                final R forEachResult = forEach.each(hashArray, i, values, i, parameter1, parameter2, delegate);

                if (forEachResult != null) {

                    result = forEachResult;
                    break;
                }
            }
        }

        if (DEBUG) {

            exit(result, b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate)
                    .add("forEach", forEach));
        }

        return result;
    }

    protected final <S, D> int keysAndValues(K[] keysDst, S src, D dst, MapIndexValueSetter<S, D> valueSetter) {

        Objects.requireNonNull(keysDst);

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final K[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        int dstIndex = 0;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey != NO_KEY) {

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

    final int getIndexScanHashArrayToMaxHashArrayIndex(K key, int hashArrayIndex, int max) {

        Objects.requireNonNull(key);
        Checks.isIndex(hashArrayIndex);
        Checks.isLengthAboveOrAtZero(max);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        final int result = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(getHashed(), key, hashArrayIndex, max);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return result;
    }

    protected final int getIndexScanEntireHashArray(K key, int keyMask) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int result = HashArray.getIndexScanEntireHashArray(getHashed(), key, keyMask);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
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

    final int removeAndReturnIndexIfExists(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, getKeyMask());

        final int result = removeAndReturnIndexIfExists(key, hashArrayIndex);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    final int removeAndReturnIndexIfExists(K key, int hashArrayIndex) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, getKeyMask());
        }

        final K[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        int removedIndex = NO_INDEX;

        @SuppressWarnings("unchecked")
        final K noKey = (K)NO_KEY;

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

                hashArray[i] = noKey;

                removedIndex = i;
                break;
            }
        }

        if (removedIndex == NO_INDEX && !done) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                final K mapKey = hashArray[i];

                if (mapKey.equals(key)) {

                    if (DEBUG) {

                        debug("remove from map foundIndex=" + i);
                    }

                    hashArray[i] = noKey;

                    removedIndex = i;
                    break;
                }
                else if (mapKey == NO_KEY) {

                    if (ASSERT) {

                        done = true;
                    }

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

    final V getValues() {
        return values;
    }

    private int keys(K[] dst) {

        Objects.requireNonNull(dst);

        return keysAndValues(dst, null, null, null);
    }

    private long put(K[] hashArray, K key) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key));
        }

        final int keyMask = getKeyMask();

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, keyMask);
        }

        final long result = put(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("key", key));
        }

        return result;
    }

    final long put(K[] hashArray, K key, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Objects.requireNonNull(key);
        Checks.isIndex(hashArrayIndex);

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final int hashArrayLength = hashArray.length;

        int found = NO_INDEX;

        boolean newAdded = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey == NO_KEY) {

                if (DEBUG) {

                    debug("add to map new foundIndex=" + i);
                }

                hashArray[i] = key;

                found = i;

                newAdded = true;
                break;
            }
            else if (mapKey == key) {

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

                if (mapKey == NO_KEY) {

                    if (DEBUG) {

                        debug("add to map foundIndex=" + i);
                    }

                    hashArray[i] = key;

                    found = i;

                    newAdded = true;
                    break;
                }
                else if (mapKey == key) {

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

            exitWithBinary(result, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    private static <T> void clearHashArray(T[] hashArray) {

        Arrays.fill(hashArray, NO_KEY);
    }

    final <P1, P2, DELEGATE> boolean equalsObjectKeyNonBucketMapWithIndex(P1 parameter1, BaseObjectKeyNonBucketMap<K, V> other, P2 parameter2, DELEGATE delegate,
            MapIndexValuesEqualityTester<V, P1, P2, DELEGATE> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("delegate", delegate).add("equalityTester", equalityTester));
        }

        final K[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final V values = getValues();
        final V otherValues = other.getValues();

        final int keyMask = getKeyMask();

        boolean equals = true;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey != NO_KEY) {

                final int otherIndex = other.getHashArrayIndex(mapKey, keyMask);

                if (otherIndex == NO_INDEX || !equalityTester.areValuesEqual(values, i, parameter1, otherValues, otherIndex, parameter2, delegate)) {

                    equals = false;
                    break;
                }
            }
        }

        if (DEBUG) {

            exit(equals);
        }

        return equals;
    }
}
