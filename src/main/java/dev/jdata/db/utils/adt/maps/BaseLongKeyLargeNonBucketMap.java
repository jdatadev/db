package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IHeapMutableLongLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongKeyLargeNonBucketMap<VALUES> extends BaseLongKeyLargeMap<VALUES> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_KEY_LARGE_NON_BUCKET_MAP;

    BaseLongKeyLargeNonBucketMap(AllocationType allocationType, int initialOuterCapacity, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<VALUES> createValues) {
        super(allocationType, initialOuterCapacity, innerCapacityExponent, capacityExponentIncrease, loadFactor, (o, i) -> createHashArray(o, i),
                BaseLongKeyLargeNonBucketMap::clearHashArray, createValues);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerCapacityExponent", innerCapacityExponent)
                    .add("loadFactor", loadFactor).add("createValues", createValues));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongKeyLargeNonBucketMap(AllocationType allocationType, BaseLongKeyLargeNonBucketMap<VALUES> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyValuesContent", copyValuesContent));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected IMutableLongLargeArray rehash(IMutableLongLargeArray hashArray, long newCapacity, int newCapacityExponent, long newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        final int innerCapacityExponent = getInnerCapacityExponent();
        final int newOuterCapacity = computeOuterCapacity(newCapacity, newCapacityExponent, innerCapacityExponent);

        final IMutableLongLargeArray newHashArray = createHashArray(newOuterCapacity, innerCapacityExponent);

        clearHashArray(newHashArray);

        final VALUES newValuesArray = createValues(newOuterCapacity, innerCapacityExponent);

        final long hashArrayLimit = hashArray.getLimit();

        for (int i = 0; i < hashArrayLimit; ++ i) {

            final long key = hashArray.get(i);

            if (key != NO_KEY) {

                final long putResult = put(newHashArray, key);

                final int newIndex = IntCapacityPutResult.getPutIndex(putResult);

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

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, IForEachKeyAndValueWithKeysAndValues<IMutableLongLargeArray, VALUES, P1, P2> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final IMutableLongLargeArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final VALUES values = getValues();

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
            ForEachKeyAndValueWithKeysAndValuesWithResult<IMutableLongLargeArray, VALUES, P1, P2, DELEGATE, R> forEach) {

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final IMutableLongLargeArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final VALUES values = getValues();

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
    final <KEYS_DST, VALUES_DST> long keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            ILongCapacityMapIndexKeyValueAdder<IMutableLongLargeArray, VALUES, KEYS_DST, VALUES_DST> keyValueAdder) {

        Objects.requireNonNull(keysDst);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst).add("keyValueAdder", keyValueAdder));
        }

        final IMutableLongLargeArray hashArray = getHashed();
        final long hashArrayLimit = hashArray.getLimit();
        final VALUES values = getValues();

        final long noKey = NO_KEY;

        long numAdded = 0L;

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final long mapKey = hashArray.get(i);

            if (mapKey != noKey) {

                keyValueAdder.addValue(i, hashArray, values, numAdded, keysDst, valuesDst);

                ++ numAdded;
            }
        }

        if (DEBUG) {

            exit(numAdded);
        }

        return numAdded;
    }

    final long getIndexScanHashArrayToMaxHashArrayIndex(int key, long hashArrayIndex, int max) {

        IntNonBucket.checkIsHashArrayElement(key);
        Checks.isLongIndex(hashArrayIndex);
        Checks.isIntLengthAboveOrAtZero(max);

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

        final IMutableLongLargeArray hashArray = getHashed();

        final long putResult = put(hashArray, key);

        final long index = LongCapacityPutResult.getPutIndex(putResult);

        final boolean newAdded = LongCapacityPutResult.getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exit(index, b -> b.add("key", key).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return index;
    }

    private long put(IMutableLongLargeArray hashArray, long key) {

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

    final long put(IMutableLongLargeArray hashArray, long key, long hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);
        Checks.isLongIndex(hashArrayIndex);

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = LargeHashArray.add(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exitWithBinary(result, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    final <P1, P2, DELEGATE> boolean equalsLongKeyNonBucketMapWithIndex(P1 parameter1, BaseLongKeyLargeNonBucketMap<VALUES> other, P2 parameter2, DELEGATE delegate,
            LongMapIndexValuesEqualityTester<VALUES, P1, P2, DELEGATE> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("delegate", delegate).add("equalityTester", equalityTester));
        }

        final IMutableLongLargeArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final VALUES values = getValues();
        final VALUES otherValues = other.getValues();

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

    private static IMutableLongLargeArray createHashArray(int outerCapacity, int innerCapacityExponent) {

        return IHeapMutableLongLargeArray.create(outerCapacity, innerCapacityExponent, NO_KEY);
    }

    private static void clearHashArray(IMutableLongLargeArray hashArray) {

        LongNonBucket.clearHashArray(hashArray);
    }
}
