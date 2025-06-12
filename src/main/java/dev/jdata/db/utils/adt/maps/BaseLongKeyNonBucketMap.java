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
import dev.jdata.db.utils.adt.hashed.helpers.NonBucket;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class BaseLongKeyNonBucketMap<T> extends BaseIntCapacityExponentMap<long[]> implements ILongKeyMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_NON_BUCKET_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_LONG_NON_BUCKET_MAP;

    private static final long NO_KEY = -1L;

    private final IntFunction<T> createValues;

    private T values;

    protected abstract void put(T values, int index, T newValues, int newIndex);
    protected abstract void clearValues(T values);

    protected abstract int getHashArrayIndex(long key, int keyMask);

    protected BaseLongKeyNonBucketMap(int initialCapacityExponent, IntFunction<T> createValues) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createValues);
    }

    BaseLongKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createValues) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, long[]::new, BaseLongKeyNonBucketMap::clearKeyMap);

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

    BaseLongKeyNonBucketMap(BaseLongKeyNonBucketMap<T> toCopy, BiConsumer<T, T> copyValuesContent) {
        super(toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));

        final IntFunction<T> createValues = this.createValues = toCopy.createValues;

        final T values = this.values = createValues.apply(getCapacity());

        copyValuesContent.accept(toCopy.values, values);
    }

    @Override
    public final long[] keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final long[] result = new long[numElements];

        keys(result);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected long[] rehash(long[] hashArray, int newCapacity, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("keyMask", keyMask));
        }

        final long[] newHashArray = new long[newCapacity];

        clearKeyMap(newHashArray);

        final T newValuesArray = createValues.apply(newCapacity);

        final int hashArrayLength = hashArray.length;

        final long noKey = NonBucket.NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long key = hashArray[i];

            if (key != noKey) {

                final int newIndex = IntPutResult.getPutIndex(put(newHashArray, key));

                newHashArray[newIndex] = key;

                put(values, i, newValuesArray, newIndex);
            }
        }

        this.values = newValuesArray;

        if (DEBUG) {

            exit(newHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("keyMask", keyMask));
        }

        return newHashArray;
    }

    @Deprecated // not public?
    public final IntFunction<T> getCreateValues() {
        return createValues;
    }

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValueWithKeysAndValues<long[], T, P1, P2> forEach) {

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final T values = getValues();

        final long noKey = NonBucket.NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray[i];

            if (mapKey != noKey) {

                forEach.each(hashArray, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }
    }

    protected final <P1, P2, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2, DELEGATE delegate,
            ForEachKeyAndValueWithKeysAndValuesWithResult<long[], T, P1, P2, DELEGATE, R> forEach) {

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final T values = getValues();

        final long noKey = NonBucket.NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray[i];

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

    protected final <S, D> void keysAndValues(long[] keysDst, S src, D dst, MapIndexValueSetter<S, D> valueSetter) {

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final int noKey = NonBucket.NO_ELEMENT;

        int dstIndex = 0;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray[i];

            if (mapKey != noKey) {

                keysDst[dstIndex] = mapKey;

                if (dst != null) {

                    valueSetter.setValue(src, i, dst, dstIndex);
                }

                ++ dstIndex;
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final int getIndex(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        int found = NO_INDEX;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            if (hashArray[i] == key) {

                found = i;
                break;
            }
        }

        if (found == NO_INDEX) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                if (hashArray[i] == key) {

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

    private int getIndexScanHashArrayToMaxKeyMask(long key, int keyMask, int max) {

        NonBucket.checkIsHashArrayElement(key);
        Checks.isLengthAboveZero(max);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask).add("max", max));
        }

        final int result = HashArray.getIndexScanHashArrayToMaxKeyMask(getHashed(), key, keyMask, max);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask).add("max", max));
        }

        return result;
    }

    final int getIndexScanHashArrayToMax(long key, int hashArrayIndex, int max) {

        NonBucket.checkIsHashArrayElement(key);
        Checks.isIndex(hashArrayIndex);
        Checks.isLengthAboveOrAtZero(max);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        final int result = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(getHashed(), key, hashArrayIndex, max);

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    protected final int getIndexScanEntireHashArray(long key, int keyMask) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int result = HashArray.getIndexScanEntireHashArray(getHashed(), key, keyMask);

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    protected final int put(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final long[] hashArray = getHashed();

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

    protected final int removeAndReturnIndex(long key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final int result = removeAndReturnIndex(key, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    protected final int removeAndReturnIndex(long key, int hashArrayIndex) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, getKeyMask());
        }

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final long noKey = NonBucket.NO_ELEMENT;

        int removedIndex = NO_INDEX;

        boolean done = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray[i];

            if (mapKey == key) {

                if (DEBUG) {

                    debug("remove from map foundIndex=" + i);
                }

                hashArray[i] = noKey;

                removedIndex = i;
                break;
            }
            else if (mapKey == noKey) {

                done = true;

                break;
            }
        }

        if (removedIndex == NO_INDEX && !done) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                final long mapKey = hashArray[i];

                if (mapKey == key) {

                    if (DEBUG) {

                        debug("remove from map foundIndex=" + i);
                    }

                    hashArray[i] = noKey;

                    removedIndex = i;
                    break;
                }
                else if (mapKey == noKey) {

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

    protected final T getValues() {
        return values;
    }

    private void keys(long[] dst) {

        Objects.requireNonNull(dst);

        keysAndValues(dst, null, null, null);
    }

    private long put(long[] hashArray, long key) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, getKeyMask());
        }

        final long result = put(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("key", key));
        }

        return result;
    }

    final long put(long[] hashArray, long key, int hashArrayIndex) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, getKeyMask());
        }

        final int hashArrayLength = hashArray.length;

        final long noKey = NonBucket.NO_ELEMENT;

        int found = NO_INDEX;

        boolean newAdded = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray[i];

            if (mapKey == noKey) {

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

                final long mapKey = hashArray[i];

                if (mapKey == noKey) {

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

    private static void clearKeyMap(long[] hashArray) {

        Arrays.fill(hashArray, NonBucket.NO_ELEMENT);
    }

    final <P1, P2, DELEGATE> boolean equalsLongKeyNonBucketMapWithIndex(P1 parameter1, BaseLongKeyNonBucketMap<T> other, P2 parameter2, DELEGATE delegate,
            MapIndexValuesEqualityTester<T, P1, P2, DELEGATE> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("delegate", delegate).add("equalityTester", equalityTester));
        }

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final T values = getValues();
        final T otherValues = other.getValues();

        final int otherKeyMask = other.getKeyMask();

        boolean equals = true;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray[i];

            if (mapKey != NO_KEY) {

                final int otherIndex = other.getHashArrayIndex(mapKey, otherKeyMask);

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
