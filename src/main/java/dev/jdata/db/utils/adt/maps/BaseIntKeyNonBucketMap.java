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

abstract class BaseIntKeyNonBucketMap<T> extends BaseIntCapacityExponentMap<int[]> implements IIntKeyMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_NON_BUCKET_MAP;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_INT_NON_BUCKET_MAP;

    private static final int NO_KEY = -1;

    private final IntFunction<T> createValues;

    private T values;

    protected abstract void put(T values, int index, T newValues, int newIndex);
    protected abstract void clearValues(T values);

    protected abstract int getHashArrayIndex(int key, int keyMask);

    protected BaseIntKeyNonBucketMap(int initialCapacityExponent, IntFunction<T> createValues) {
        this(initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, HashedConstants.DEFAULT_LOAD_FACTOR, createValues);
    }

    BaseIntKeyNonBucketMap(int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T> createValues) {
        super(initialCapacityExponent, capacityExponentIncrease, loadFactor, int[]::new, BaseIntKeyNonBucketMap::clearHashArray);

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

    BaseIntKeyNonBucketMap(BaseIntKeyNonBucketMap<T> toCopy, BiConsumer<T, T> copyValuesContent) {
        super(toCopy, (a1, a2) -> System.arraycopy(a1, 0, a2, 0, a1.length));

        final IntFunction<T> createValues = this.createValues = toCopy.createValues;

        final T values = this.values = createValues.apply(getCapacity());

        copyValuesContent.accept(toCopy.values, values);
    }

    @Override
    public final int[] keys() {

        if (DEBUG) {

            enter();
        }

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(getNumElements());

        final int[] result = new int[numElements];

        keys(result);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected int[] rehash(int[] hashArray, int newCapacity, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).hex("newKeyMask", newKeyMask));
        }

        final int[] newHashArray = new int[newCapacity];

        clearHashArray(newHashArray);

        final T newValuesArray = createValues.apply(newCapacity);

        final int mapLength = hashArray.length;

        for (int i = 0; i < mapLength; ++ i) {

            final int key = hashArray[i];

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

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValueWithKeysAndValues<int[], T, P1, P2> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final T values = getValues();

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray[i];

            if (mapKey != NO_KEY) {

                forEach.each(hashArray, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final <P1, P2, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2, DELEGATE delegate,
            ForEachKeyAndValueWithKeysAndValuesWithResult<int[], T, P1, P2, DELEGATE, R> forEach) {

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final int[] hashArray = getHashed();

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

    protected final <S, D> int keysAndValues(int[] keysDst, S src, D dst, MapIndexValueSetter<S, D> valueSetter) {

        Objects.requireNonNull(keysDst);

        if (DEBUG) {

            enter(b -> b.add("keysDst.length", keysDst.length));
        }

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        int dstIndex = 0;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray[i];

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

    final int getIndexScanHashArrayToMaxHashArrayIndex(int key, int hashArrayIndex, int max) {

        NonBucket.checkIsHashArrayElement(key);
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

    protected final int getIndexScanEntireHashArray(int key, int keyMask) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final int result = HashArray.getIndexScanEntireHashArray(getHashed(), key, keyMask);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
    }

    protected final int put(int key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final int[] hashArray = getHashed();

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

    final int removeAndReturnIndex(int key) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, getKeyMask());

        final int result = removeAndReturnIndex(key, hashArrayIndex);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    final int removeAndReturnIndex(int key, int hashArrayIndex) {

        NonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, getKeyMask());
        }

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        int removedIndex = NO_INDEX;

        boolean done = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray[i];

            if (mapKey == key) {

                if (DEBUG) {

                    debug("remove from map foundIndex=" + i);
                }

                hashArray[i] = NO_KEY;

                removedIndex = i;
                break;
            }
            else if (mapKey == NO_KEY) {

                done = true;

                break;
            }
        }

        if (removedIndex == NO_INDEX && !done) {

            for (int i = 0; i < hashArrayIndex; ++ i) {

                final int mapKey = hashArray[i];

                if (mapKey == key) {

                    if (DEBUG) {

                        debug("remove from map foundIndex=" + i);
                    }

                    hashArray[i] = NO_KEY;

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

    protected final T getValues() {
        return values;
    }

    private int keys(int[] dst) {

        Objects.requireNonNull(dst);

        return keysAndValues(dst, null, null, null);
    }

    private long put(int[] hashArray, int key) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key));
        }

        final int keyMask = getKeyMask();

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, keyMask);
        }

        final long result = put(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("key", key));
        }

        return result;
    }

    final long put(int[] hashArray, int key, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        NonBucket.checkIsHashArrayElement(key);
        Checks.isIndex(hashArrayIndex);

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final int hashArrayLength = hashArray.length;

        int found = NO_INDEX;

        boolean newAdded = false;

        for (int i = hashArrayIndex; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray[i];

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

                final int mapKey = hashArray[i];

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

    private static void clearHashArray(int[] hashArray) {

        Arrays.fill(hashArray, NO_KEY);
    }

    final <P1, P2, DELEGATE> boolean equalsIntKeyNonBucketMapWithIndex(P1 parameter1, BaseIntKeyNonBucketMap<T> other, P2 parameter2, DELEGATE delegate,
            MapIndexValuesEqualityTester<T, P1, P2, DELEGATE> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("delegate", delegate).add("equalityTester", equalityTester));
        }

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final T values = getValues();
        final T otherValues = other.getValues();

        final int keyMask = getKeyMask();

        boolean equals = true;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray[i];

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
