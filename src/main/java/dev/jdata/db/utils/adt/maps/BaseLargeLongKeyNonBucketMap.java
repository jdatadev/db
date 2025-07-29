package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeLongArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.IntPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.LongPutResult;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLargeLongKeyNonBucketMap<T> extends BaseLargeLongKeyMap<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_LONG_KEY_NON_BUCKET_MAP;

    BaseLargeLongKeyNonBucketMap(int initialOuterCapacity, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor, BiIntToObjectFunction<T> createValues) {
        super(initialOuterCapacity, innerCapacityExponent, capacityExponentIncrease, loadFactor, (o, i) -> new LargeLongArray(o, i, NO_KEY),
                BaseLargeLongKeyNonBucketMap::clearHashArray, createValues);

        if (DEBUG) {

            enter(b -> b.add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor)
                    .add("createValues", createValues));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLargeLongKeyNonBucketMap(BaseLargeLongKeyNonBucketMap<T> toCopy, BiConsumer<T, T> copyValuesContent) {
        super(toCopy, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("toCopy", toCopy).add("copyValuesContent", copyValuesContent));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected LargeLongArray rehash(LargeLongArray hashArray, long newCapacity, int newCapacityExponent, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        final int innerCapacityExponent = getInnerCapacityExponent();
        final int newOuterCapacity = computeOuterCapacity(newCapacity, newCapacityExponent, innerCapacityExponent);

        final LargeLongArray newHashArray = createHashArray(newOuterCapacity, innerCapacityExponent);

        clearHashArray(newHashArray);

        final T newValuesArray = createValues(newOuterCapacity, innerCapacityExponent);

        final long hashArrayLimit = hashArray.getLimit();

        for (int i = 0; i < hashArrayLimit; ++ i) {

            final long key = hashArray.get(i);

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

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, IForEachKeyAndValueWithKeysAndValues<LargeLongArray, T, P1, P2> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final LargeLongArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final T values = getValues();

        final long noKey = NO_KEY;

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final long mapKey = hashArray.get(i);

            if (mapKey != noKey) {

                forEach.each(hashArray, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final <P1, P2, DELEGATE, R> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2, DELEGATE delegate,
            ForEachKeyAndValueWithKeysAndValuesWithResult<LargeLongArray, T, P1, P2, DELEGATE, R> forEach) {

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final LargeLongArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final T values = getValues();

        final long noKey = IntNonBucket.NO_ELEMENT;

        for (int i = 0; i < hashArrayLimit; ++ i) {

            final long mapKey = hashArray.get(i);

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
    protected final <S, D> long keysAndValues(LargeLongArray keysDst, S src, D dst, LongMapIndexValueSetter<S, D> valueSetter) {

        Objects.requireNonNull(keysDst);

        if (DEBUG) {

            enter(b -> b.add("keysDst.getCapacity()", keysDst.getCapacity()));
        }

        final LargeLongArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final long noKey = NO_KEY;

        int dstIndex = 0;

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final long mapKey = hashArray.get(i);

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

    final long getIndexScanHashArrayToMaxHashArrayIndex(int key, long hashArrayIndex, int max) {

        IntNonBucket.checkIsHashArrayElement(key);
        Checks.isIndex(hashArrayIndex);
        Checks.isLengthAboveOrAtZero(max);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        final long result = LargeHashArray.getIndexScanHashArrayToMaxHashArrayIndex(getHashed(), key, hashArrayIndex, max);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return result;
    }

    protected final long getIndexScanEntireHashArray(long key, long keyMask) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("keyMask", keyMask));
        }

        final long result = LargeHashArray.getIndexScanEntireHashArray(getHashed(), key, keyMask);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("keyMask", keyMask));
        }

        return result;
    }

    protected final long put(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final LargeLongArray hashArray = getHashed();

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

    private long put(LargeLongArray hashArray, long key) {

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

    final long put(LargeLongArray hashArray, long key, long hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);
        Checks.isIndex(hashArrayIndex);

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = LargeHashArray.add(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exitWithBinary(result, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    final <P1, P2, DELEGATE> boolean equalsIntKeyNonBucketMapWithIndex(P1 parameter1, BaseLargeLongKeyNonBucketMap<T> other, P2 parameter2, DELEGATE delegate,
            LongMapIndexValuesEqualityTester<T, P1, P2, DELEGATE> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("delegate", delegate).add("equalityTester", equalityTester));
        }

        final LargeLongArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final T values = getValues();
        final T otherValues = other.getValues();

        final long keyMask = getKeyMask();

        final long noKey = NO_KEY;
        final long noIndex = NO_INDEX;

        boolean equals = true;

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final long mapKey = hashArray.get(i);

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

    private static LargeLongArray createHashArray(int outerCapacity, int innerCapacityExponent) {

        return new LargeLongArray(outerCapacity, innerCapacityExponent, NO_KEY);
    }

    private static void clearHashArray(LargeLongArray hashArray) {

        LongNonBucket.clearHashArray(hashArray);
    }
}
