package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.ObjectNonBucket;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseObjectKeyNonBucketMap<K, V> extends BaseIntCapacityExponentMap<K[]> implements IObjectKeyMap<K> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_NON_BUCKET_MAP;

    private static final Object NO_KEY = null;

    @SuppressWarnings("unchecked")
    static <T> T getNoKey() {

        return (T)NO_KEY;
    }

    private final IntFunction<K[]> createKeysArray;
    private final IntFunction<V> createValuesArray;

    private V values;

    protected abstract void put(V values, int index, V newValues, int newIndex);
    protected abstract void clearValues(V values);

    protected abstract int getHashArrayIndex(K key, int keyMask);

    BaseObjectKeyNonBucketMap(int initialCapacityExponent, IntFunction<K[]> createKeysArray, IntFunction<V> createValuesArray) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createKeysArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<K[]> createKeysArray, IntFunction<V> createValuesArray) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, BaseObjectKeyNonBucketMap::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease).add("loadFactor", loadFactor)
                    .add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        this.createKeysArray = Objects.requireNonNull(createKeysArray);
        this.createValuesArray = Objects.requireNonNull(createValuesArray);

        this.values = createValuesArray.apply(getCapacity());

        clearValues(values);

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectKeyNonBucketMap(BaseObjectKeyNonBucketMap<K, V> toCopy, BiConsumer<V, V> copyValuesContent) {
        super(toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyValuesContent", copyValuesContent));
        }

        this.createKeysArray = toCopy.createKeysArray;
        final IntFunction<V> createValues = this.createValuesArray = toCopy.createValuesArray;

        final V values = this.values = createValues.apply(getCapacity());

        copyValuesContent.accept(toCopy.values, values);

        if (DEBUG) {

            exit();
        }
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
    protected K[] rehash(K[] hashArray, int newCapacity, int newCapacityExponent, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        final K[] newHashArray = createKeysArray.apply(newCapacity);

        clearHashArray(newHashArray);

        final V newValuesArray = createValuesArray.apply(newCapacity);

        final int hashArrayLength = hashArray.length;

        for (int i = 0; i < hashArrayLength; ++ i) {

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

            exit(newHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
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

        final Object noKey = NO_KEY;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey != noKey) {

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

        final Object noKey = NO_KEY;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey != noKey) {

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

    protected final <S, D> int keysAndValues(K[] keysDst, S src, D dst, IIntMapIndexValueSetter<S, D> valueSetter) {

        Objects.requireNonNull(keysDst);

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final K[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final Object noKey = NO_KEY;

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

    protected final long put(K key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final K[] hashArray = getHashed();

        final long putResult = put(hashArray, key);

        final boolean newAdded = IntPutResult.getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exitWithHex(putResult, b -> b.add("key", key).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return putResult;
    }

    private long put(K[] hashArray, K key) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key));
        }

        final int keyMask = getKeyMask();

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%s keyMask=0x%08x", hashArrayIndex, key, keyMask);
        }

        final long result = put(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exitWithHex(result, b -> b.add("hashArray", hashArray).add("key", key));
        }

        return result;
    }

    final V getValues() {
        return values;
    }

    private int keys(K[] dst) {

        Objects.requireNonNull(dst);

        return keysAndValues(dst, null, null, null);
    }

    final long put(K[] hashArray, K key, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Objects.requireNonNull(key);
        Checks.isIndex(hashArrayIndex);

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = HashArray.add(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exitWithHex(result, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    final <P1, P2, DELEGATE> boolean equalsObjectKeyNonBucketMapWithIndex(P1 parameter1, BaseObjectKeyNonBucketMap<K, V> other, P2 parameter2, DELEGATE delegate,
            IntMapIndexValuesEqualityTester<V, P1, P2, DELEGATE> equalityTester) {

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

        final Object noKey = NO_KEY;
        final int noIndex = NO_INDEX;

        boolean equals = true;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final K mapKey = hashArray[i];

            if (mapKey != noKey) {

                final int otherIndex = other.getHashArrayIndex(mapKey, keyMask);

                if (otherIndex == noIndex || !equalityTester.areValuesEqual(values, i, parameter1, otherValues, otherIndex, parameter2, delegate)) {

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

    private static <T> void clearHashArray(T[] hashArray) {

        ObjectNonBucket.clearHashArray(hashArray);
    }
}
