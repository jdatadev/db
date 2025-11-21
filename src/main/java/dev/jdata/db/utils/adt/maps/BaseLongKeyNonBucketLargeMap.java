package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IHeapMutableLongLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableLongLargeArray;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseLongKeyNonBucketLargeMap<VALUES, MAP extends BaseLongKeyNonBucketLargeMap<VALUES, MAP>> extends BaseLongKeyLargeMap<VALUES, MAP> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_KEY_NON_BUCKET_LARGE_MAP;

    BaseLongKeyNonBucketLargeMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<VALUES> createValues) {
        super(allocationType, initialOuterCapacityExponent, innerCapacityExponent, capacityExponentIncrease, loadFactor, (o, i) -> createHashArray(o, i),
                BaseLongKeyNonBucketLargeMap::clearHashArray, createValues);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor).add("createValues", createValues));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongKeyNonBucketLargeMap(AllocationType allocationType, BaseLongKeyNonBucketLargeMap<VALUES, ?> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyValuesContent", copyValuesContent));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    protected final void rehashKeysAndValues(IMutableLongLargeArray hashArray, IMutableLongLargeArray newHashArray, long newKeyMask, VALUES values, VALUES newValues) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("values", values).add("newValues", newValues));
        }

        final long noKey = NO_KEY;

        final long hashArrayLimit = hashArray.getLimit();

        for (long i = 0; i < hashArrayLimit; ++ i) {

            final long key = hashArray.get(i);

            if (key != noKey) {

                final long putResult = put(newHashArray, key);

                final int newIndex = IntCapacityPutResult.getPutIndex(putResult);

                newHashArray.set(newIndex, key);

                putValue(values, i, newValues, newIndex);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("values", values).add("newValues", newValues));
        }
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
        final long noKey = LongNonBucket.NO_ELEMENT;

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

    private long getIndexScanHashArrayToMaxHashArrayIndex(long key, long hashArrayIndex, int max) {

        LongNonBucket.checkIsHashArrayElement(key);
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

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacityForOneMoreElement();

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

    final <P1, P2, DELEGATE> boolean equalsLongKeyNonBucketMapWithIndex(P1 parameter1, BaseLongKeyNonBucketLargeMap<VALUES, ?> other, P2 parameter2, DELEGATE delegate,
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
