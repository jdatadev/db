package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseLongKeyNonBucketMap<VALUES, MAP extends BaseLongKeyNonBucketMap<VALUES, MAP>> extends BaseNonBucketMap<long[], VALUES, MAP> implements ILongKeyMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_KEY_NON_BUCKET_MAP;

    private static final Class<?> debugClass = BaseLongKeyNonBucketMap.class;

    private static final long NO_KEY = LongNonBucket.NO_ELEMENT;

    protected abstract int scanHashArrayForIndex(long key, int keyMask);

    BaseLongKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<VALUES> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, long[]::new, BaseLongKeyNonBucketMap::clearHashArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongKeyNonBucketMap(AllocationType allocationType, BaseLongKeyNonBucketMap<VALUES, ?> toInitializeFrom) {
        super(allocationType, toInitializeFrom);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toInitializeFrom", toInitializeFrom));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongKeyNonBucketMap(AllocationType allocationType, BaseLongKeyNonBucketMap<VALUES, ?> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, Array::copyOf, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyValuesContent", copyValuesContent));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long keys(ILongAnyOrderAddable addable) {

        Objects.requireNonNull(addable);

        if (DEBUG) {

            enter(b -> b.add("addable", addable));
        }

        final long result = keysAndValues(addable, null, (srcIndex, kSrc, vSrc, dstIndex, kDst, vDst) -> kDst.addInAnyOrder(kSrc[srcIndex]));

        if (DEBUG) {

            exit(result, b -> b.add("addable", addable));
        }

        return result;
    }

    @Override
    protected final void rehashKeysAndValues(long[] hashArray, long[] newHashArray, int newKeyMask, VALUES values, VALUES newValues) {

        if (DEBUG) {

           exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("values", values).add("newValues", newValues));
        }

        final int hashArrayLength = hashArray.length;

        final long noKey = NO_KEY;

        for (int i = 0; i < hashArrayLength; ++ i) {

             final long key = hashArray[i];

            if (key != noKey) {

                final int newIndex = IntCapacityPutResult.getPutIndex(putKeyWithKeyMask(newHashArray, key, newKeyMask));

                putValue(values, i, newValues, newIndex);
            }
        }

        if (DEBUG) {

           exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("values", values).add("newValues", newValues));
        }
    }

    protected final <P1, P2, E extends Exception> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValueWithKeysAndValues<long[], VALUES, P1, P2, E> forEach)
            throws E {

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final long[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;
        final VALUES values = getValues();

        final long noKey = NO_KEY;

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

    protected final <P1, P2, DELEGATE, R, E extends Exception> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2, DELEGATE delegate,
            ForEachKeyAndValueWithKeysAndValuesWithResult<long[], VALUES, P1, P2, DELEGATE, R, E> forEach) throws E {

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final long[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;
        final VALUES values = getValues();

        final long noKey = NO_KEY;

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

    @Override
    protected final <KEYS_DST, VALUES_DST> int keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            IIntCapacityMapIndexKeyValueAdder<long[], VALUES, KEYS_DST, VALUES_DST> keyValueAdder) {

        Checks.areAnyNotNull(keysDst, valuesDst);
        Objects.requireNonNull(keyValueAdder);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst).add("keyValueAdder", keyValueAdder));
        }

        final long[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;
        final VALUES values = getValues();

        final long noKey = NO_KEY;

        int numAdded = 0;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray[i];

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

    protected final int getIndex(long key) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        final int result = HashArray.getIndexScanEntireHashArray(getHashed(), key, getKeyMask());

        if (DEBUG) {

            exit(result, b -> b.add("key", key));
        }

        return result;
    }

    protected final long put(long key) {

        LongNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacityForOneMoreElement();

        final long[] hashArray = getHashed();

        final long putResult = putKey(hashArray, key);

        final boolean newAdded = IntCapacityPutResult.getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exitWithHex(putResult, b -> b.add("key", key).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return putResult;
    }

    private long putKey(long[] hashArray, long key) {

        return putKeyWithKeyMask(hashArray, key, getKeyMask());
    }

    private static long putKeyWithKeyMask(long[] hashArray, long key, int keyMask) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(key, keyMask);

        if (DEBUG) {

            PrintDebug.debugFormatln(debugClass, "lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, key, keyMask);
        }

        final long result = putKeyWithHashArrayIndex(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.exitWithHex(debugClass, result, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        return result;
    }

    static long putKeyWithHashArrayIndex(long[] hashArray, long key, int hashArrayIndex) {

        Objects.requireNonNull(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);
        Checks.checkArrayIndex(hashArray, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = HashArray.add(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.exitWithHex(debugClass, result, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    final <P1, P2, DELEGATE, E extends Exception> boolean equalsLongKeyNonBucketMapWithIndex(P1 parameter1, BaseLongKeyNonBucketMap<VALUES, ?> other, P2 parameter2,
            DELEGATE delegate, IntMapIndexValuesEqualityTester<VALUES, P1, P2, DELEGATE, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("delegate", delegate).add("equalityTester", equalityTester));
        }

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final VALUES values = getValues();
        final VALUES otherValues = other.getValues();

        final int otherKeyMask = other.getKeyMask();

        final long noKey = NO_KEY;
        final int noIndex = NO_INDEX;

        boolean equals = true;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray[i];

            if (mapKey != noKey) {

                final int otherIndex = other.scanHashArrayForIndex(mapKey, otherKeyMask);

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

    private static void clearHashArray(long[] hashArray) {

        LongNonBucket.clearHashArray(hashArray);
    }
}
