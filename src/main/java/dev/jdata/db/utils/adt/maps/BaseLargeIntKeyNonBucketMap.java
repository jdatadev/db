package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeIntArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongPutResult;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeIntKeyNonBucketMap<T> extends BaseLargeIntKeyMap<T> implements ILargeIntKeyMap {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_INT_KEY_NON_BUCKET_MAP;

    BaseLargeIntKeyNonBucketMap(int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<T> createValues) {
        super(initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor,
                (o, i) -> new LargeIntArray(o, i, NO_KEY), BaseLargeIntKeyNonBucketMap::clearHashArray, createValues);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacityExponent", initialOuterCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor).add("createValues", createValues));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLargeIntKeyNonBucketMap(BaseLargeIntKeyNonBucketMap<T> toCopy, BiConsumer<T, T> copyValuesContent) {
        super(toCopy, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyValuesContent", copyValuesContent));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected LargeIntArray rehash(LargeIntArray hashArray, long newCapacity, int newCapacityExponent, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        final int innerCapacityExponent = getInnerCapacityExponent();
        final int newOuterCapacity = computeOuterCapacity(newCapacity, newCapacityExponent, innerCapacityExponent);

        final LargeIntArray newHashArray = createHashArray(newOuterCapacity, innerCapacityExponent);

        clearHashArray(newHashArray);

        final T newValuesArray = createValues(newOuterCapacity, innerCapacityExponent);

        final long hashArrayLimit = hashArray.getLimit();

        for (int i = 0; i < hashArrayLimit; ++ i) {

            final int key = hashArray.get(i);

            if (key != NO_KEY) {

                final long putResult = put(newHashArray, key);

                final int newIndex = IntPutResult.getPutIndex(putResult);

                newHashArray.set(newIndex, key);

                put(getValues(), i, newValuesArray, newIndex);
            }
        }

        setValues(newValuesArray);

        if (DEBUG) {

            exit(newHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        return newHashArray;
    }

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, IForEachKeyAndValueWithKeysAndValues<LargeIntArray, T, P1, P2> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final LargeIntArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final T values = getValues();

        final int noKey = NO_KEY;

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final int mapKey = hashArray.get(i);

            if (mapKey != noKey) {

                forEach.each(hashArray, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final <P1, P2, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2, DELEGATE delegate,
            ForEachKeyAndValueWithKeysAndValuesWithResult<LargeIntArray, T, P1, P2, DELEGATE, R> forEach) {

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final LargeIntArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final T values = getValues();

        final long noKey = IntNonBucket.NO_ELEMENT;

        for (int i = 0; i < hashArrayLimit; ++ i) {

            final int mapKey = hashArray.get(i);

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

    @Override
    protected final <S, D> long keysAndValues(LargeIntArray keysDst, S src, D dst, LongMapIndexValueSetter<S, D> valueSetter) {

        Objects.requireNonNull(keysDst);

        if (DEBUG) {

            enter(b -> b.add("keysDst.getCapacity()", keysDst.getCapacity()));
        }

        final LargeIntArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final int noKey = NO_KEY;

        int dstIndex = 0;

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final int mapKey = hashArray.get(i);

            if (mapKey != noKey) {

                keysDst.add(mapKey);

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

    protected final long put(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final LargeIntArray hashArray = getHashed();

        final long putResult = put(hashArray, key);

        final long index = LongPutResult.getPutIndex(putResult);

        final boolean newAdded = LongPutResult.getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exit(index, b -> b.add("key", key).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return index;
    }

    private long put(LargeIntArray hashArray, int key) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key));
        }

        final long keyMask = getKeyMask();

        final long hashArrayIndex = HashFunctions.longHashArrayIndex(key, keyMask);

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, keyMask);
        }

        final long result = put(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("key", key));
        }

        return result;
    }

    final long put(LargeIntArray hashArray, int key, long hashArrayIndex) {

        Objects.requireNonNull(hashArray);
        IntNonBucket.checkIsHashArrayElement(key);
        Checks.checkIndex(hashArrayIndex, hashArray.getLimit());

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = LargeHashArray.add(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exitWithBinary(result, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    final <P1, P2, DELEGATE> boolean equalsIntKeyNonBucketMapWithIndex(P1 parameter1, BaseLargeIntKeyNonBucketMap<T> other, P2 parameter2, DELEGATE delegate,
            LongMapIndexValuesEqualityTester<T, P1, P2, DELEGATE> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("delegate", delegate).add("equalityTester", equalityTester));
        }

        final LargeIntArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final T values = getValues();
        final T otherValues = other.getValues();

        final long keyMask = getKeyMask();

        final int noKey = NO_KEY;
        final long noIndex = NO_INDEX;

        boolean equals = true;

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final int mapKey = hashArray.get(i);

            if (mapKey != noKey) {

                final long otherIndex = other.getHashArrayIndex(mapKey, keyMask);

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

    private static LargeIntArray createHashArray(int outerCapacity, int innerCapacityExponent) {

        return new LargeIntArray(outerCapacity, innerCapacityExponent, NO_KEY);
    }

    private static void clearHashArray(LargeIntArray hashArray) {

        IntNonBucket.clearHashArray(hashArray);
    }
}
