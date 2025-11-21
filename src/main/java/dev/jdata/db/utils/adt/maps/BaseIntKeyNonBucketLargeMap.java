package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.IHeapMutableIntLargeArray;
import dev.jdata.db.utils.adt.arrays.IMutableIntLargeArray;
import dev.jdata.db.utils.adt.elements.IIntAnyOrderAddable;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.LargeHashArray;
import dev.jdata.db.utils.adt.hashed.helpers.LongCapacityPutResult;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;

abstract class BaseIntKeyNonBucketLargeMap<VALUES, MAP extends BaseIntKeyNonBucketLargeMap<VALUES, MAP>>

        extends BaseNonBucketLargeMap<IMutableIntLargeArray, VALUES, MAP>
        implements IIntKeyMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_KEY_NON_BUCKET_LARGE_MAP;

    private static final int NO_KEY = IntNonBucket.NO_ELEMENT;

    protected abstract long getHashArrayIndex(int key, long keyMask);

    BaseIntKeyNonBucketLargeMap(AllocationType allocationType, int initialOuterCapacityExponent, int capacityExponentIncrease, int innerCapacityExponent, float loadFactor,
            BiIntToObjectFunction<VALUES> createValues) {
        super(allocationType, initialOuterCapacityExponent, capacityExponentIncrease, innerCapacityExponent, loadFactor, (o, i) -> createHashArray(o, i),
                BaseIntKeyNonBucketLargeMap::clearHashArray, createValues);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacityExponent", initialOuterCapacityExponent)
                    .add("capacityExponentIncrease", capacityExponentIncrease).add("innerCapacityExponent", innerCapacityExponent).add("loadFactor", loadFactor)
                    .add("createValues", createValues));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntKeyNonBucketLargeMap(AllocationType allocationType, BaseIntKeyNonBucketLargeMap<VALUES, ?> toCopy, Function<IMutableIntLargeArray, IMutableIntLargeArray> copyHashed,
            BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, copyHashed, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyHashed", copyHashed).add("copyValuesContent", copyValuesContent));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long keys(IIntAnyOrderAddable addable) {

        Objects.requireNonNull(addable);

        if (DEBUG) {

            enter(b -> b.add("addable", addable));
        }

        final long result = keysAndValues(addable, null, (srcIndex, kSrc, vSrc, dstIndex, kDst, vDst) -> kDst.addInAnyOrder(kSrc.get(srcIndex)));

        if (DEBUG) {

            exit(result, b -> b.add("addable", addable));
        }

        return result;
    }

    @Override
    protected final void rehashKeysAndValues(IMutableIntLargeArray hashArray, IMutableIntLargeArray newHashArray, long newKeyMask, VALUES values, VALUES newValues) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("values", values).add("newValues", newValues));
        }

        final int noKey = NO_KEY;

        final long hashArrayLimit = hashArray.getLimit();

        for (long i = 0; i < hashArrayLimit; ++ i) {

            final int key = hashArray.get(i);

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

    protected final <P1, P2> void forEachKeyAndValue(P1 parameter1, P2 parameter2, IForEachKeyAndValueWithKeysAndValues<IMutableIntLargeArray, VALUES, P1, P2> forEach) {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final IMutableIntLargeArray hashArray = getHashed();
        final long hashArrayLimit = hashArray.getLimit();
        final VALUES values = getValues();
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
            ForEachKeyAndValueWithKeysAndValuesWithResult<IMutableIntLargeArray, VALUES, P1, P2, DELEGATE, R> forEach) {

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final IMutableIntLargeArray hashArray = getHashed();
        final long hashArrayLimit = hashArray.getLimit();
        final VALUES values = getValues();

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
    final <KEYS_DST, VALUES_DST> long keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            ILongCapacityMapIndexKeyValueAdder<IMutableIntLargeArray, VALUES, KEYS_DST, VALUES_DST> keyValueAdder) {

        Checks.areAnyNotNull(keysDst, valuesDst);
        Objects.requireNonNull(keyValueAdder);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst).add("keyValueAdder", keyValueAdder));
        }

        Objects.requireNonNull(keysDst);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst));
        }

        final IMutableIntLargeArray hashArray = getHashed();
        final long hashArrayLimit = hashArray.getLimit();
        final VALUES values = getValues();

        final int noKey = NO_KEY;

        long numAdded = 0L;

        for (long i = 0L; i < hashArrayLimit; ++ i) {

            final int mapKey = hashArray.get(i);

            if (mapKey != noKey) {

                keyValueAdder.addValue(i, hashArray, values, numAdded, keysDst, valuesDst);

                ++ numAdded;
            }
        }

        if (DEBUG) {

            exit(numAdded, b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst));
        }

        return numAdded;
    }

    protected final long put(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacityForOneMoreElement();

        final IMutableIntLargeArray hashArray = getHashed();

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

    private long put(IMutableIntLargeArray hashArray, int key) {

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

    final long put(IMutableIntLargeArray hashArray, int key, long hashArrayIndex) {

        Objects.requireNonNull(hashArray);
        IntNonBucket.checkIsHashArrayElement(key);
        Checks.checkLongIndex(hashArrayIndex, hashArray.getLimit());

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = LargeHashArray.add(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exitWithBinary(result, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    final <P1, P2, DELEGATE> boolean equalsIntKeyNonBucketMapWithIndex(P1 parameter1, BaseIntKeyNonBucketLargeMap<VALUES, ?> other, P2 parameter2, DELEGATE delegate,
            LongMapIndexValuesEqualityTester<VALUES, P1, P2, DELEGATE> equalityTester) {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("delegate", delegate).add("equalityTester", equalityTester));
        }

        final IMutableIntLargeArray hashArray = getHashed();

        final long hashArrayLimit = hashArray.getLimit();

        final VALUES values = getValues();
        final VALUES otherValues = other.getValues();

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

    private static IMutableIntLargeArray createHashArray(int outerCapacity, int innerCapacityExponent) {

        return IHeapMutableIntLargeArray.create(outerCapacity, innerCapacityExponent, NO_KEY);
    }

    private static void clearHashArray(IMutableIntLargeArray hashArray) {

        IntNonBucket.clearHashArray(hashArray);
    }
}
