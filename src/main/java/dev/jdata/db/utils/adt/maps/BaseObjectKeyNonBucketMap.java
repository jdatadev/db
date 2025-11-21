package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IObjectAnyOrderAddable;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.ObjectNonBucket;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseObjectKeyNonBucketMap<KEY, VALUES, MAP extends BaseObjectKeyNonBucketMap<KEY, VALUES, MAP>>

        extends BaseNonBucketMap<KEY[], VALUES, MAP>
        implements IObjectKeyMapCommon<KEY> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_KEY_NON_BUCKET_MAP;

    private static final Class<?> debugClass = BaseObjectKeyNonBucketMap.class;

    private static final Object NO_KEY = ObjectNonBucket.NO_ELEMENT;

    @SuppressWarnings("unchecked")
    static <T> T getNoKey() {

        return (T)NO_KEY;
    }

    private final IntFunction<KEY[]> createKeysArray;

    protected abstract int getHashArrayIndex(KEY key, int keyMask);

    BaseObjectKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<KEY[]> createKeysArray,
            IntFunction<VALUES> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createKeysArray, BaseObjectKeyNonBucketMap::clearHashArray, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createKeysArray", createKeysArray).add("createValuesArray", createValuesArray));
        }

        this.createKeysArray = Objects.requireNonNull(createKeysArray);

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectKeyNonBucketMap(AllocationType allocationType, BaseObjectKeyNonBucketMap<KEY, VALUES, MAP> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, Array::copyOf, copyValuesContent);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyValuesContent", copyValuesContent));
        }

        this.createKeysArray = toCopy.createKeysArray;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final long keys(IObjectAnyOrderAddable<KEY> addable) {

        Objects.requireNonNull(addable);

        if (DEBUG) {

            enter(b -> b.add("addable", addable));
        }

        final long result = keysAndValues(addable, null, (srcIndex, kSrc, vSrc, dstIndex, kDst, vDst) -> kDst.addInAnyOrder(kSrc[srcIndex]));

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected final void rehashKeysAndValues(KEY[] hashArray, KEY[] newHashArray, int newKeyMask, VALUES values, VALUES newValues) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("values", values).add("newValues", newValues));
        }

        final int hashArrayLength = hashArray.length;

        final KEY noKey = getNoKey();

        for (int i = 0; i < hashArrayLength; ++ i) {

            final KEY key = hashArray[i];

            if (key != noKey) {

                final long putResult = putKeyWithKeyMask(newHashArray, key, newKeyMask);

                final int newIndex = IntCapacityPutResult.getPutIndex(putResult);

                newHashArray[newIndex] = key;

                putValue(values, i, newValues, newIndex);
            }
        }

        if (DEBUG) {

           exit(b -> b.add("hashArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask).add("values", values).add("newValues", newValues));
        }
    }

    protected final <P1, P2, E extends Exception> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValueWithKeysAndValues<KEY[], VALUES, P1, P2, E> forEach)
            throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final KEY[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;
        final VALUES values = getValues();

        final KEY noKey = getNoKey();

        for (int i = 0; i < hashArrayLength; ++ i) {

            final KEY mapKey = hashArray[i];

            if (mapKey != noKey) {

                forEach.each(hashArray, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }
    }

    protected final <P1, P2, R, DELEGATE, E extends Exception> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2, DELEGATE delegate,
            ForEachKeyAndValueWithKeysAndValuesWithResult<KEY[], VALUES, P1, P2, DELEGATE, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final KEY[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;
        final VALUES values = getValues();

        final KEY noKey = getNoKey();

        for (int i = 0; i < hashArrayLength; ++ i) {

            final KEY mapKey = hashArray[i];

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
    final <KEYS_DST, VALUES_DST> int keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            IIntCapacityMapIndexKeyValueAdder<KEY[], VALUES, KEYS_DST, VALUES_DST> keyValueAdder) {

        Checks.areAnyNotNull(keysDst, valuesDst);
        Objects.requireNonNull(keyValueAdder);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst).add("keyValueAdder", keyValueAdder));
        }

        final KEY[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;
        final VALUES values = getValues();

        final KEY noKey = getNoKey();

        int numAdded = 0;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final KEY mapKey = hashArray[i];

            if (mapKey != noKey) {

                keyValueAdder.addValue(i, hashArray, values, numAdded, keysDst, valuesDst);

                ++ numAdded;
            }
        }

        if (DEBUG) {

            exit(numAdded, b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst).add("keyValueAdder", keyValueAdder));
        }

        return numAdded;
    }

    final int getIndexScanHashArrayToMaxHashArrayIndex(KEY key, int hashArrayIndex, int max) {

        Objects.requireNonNull(key);
        Checks.isIntIndex(hashArrayIndex);
        Checks.isIntLengthAboveOrAtZero(max);

        if (DEBUG) {

            enter(b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        final int result = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(getHashed(), key, hashArrayIndex, max);

        if (DEBUG) {

            exit(result, b -> b.add("key", key).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return result;
    }

    protected final int getIndexScanEntireHashArray(KEY key, int keyMask) {

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

    protected final long put(KEY key) {

        Objects.requireNonNull(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacityForOneMoreElement();

        final KEY[] hashArray = getHashed();

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

    private long putKey(KEY[] hashArray, KEY key) {

        return putKeyWithKeyMask(hashArray, key, getKeyMask());
    }

    private static <K> long putKeyWithKeyMask(K[] hashArray, K key, int keyMask) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(key, keyMask);

        if (DEBUG) {

            PrintDebug.debugFormatln(debugClass, "lookup hashArrayIndex=%d key=%s keyMask=0x%08x", hashArrayIndex, key, keyMask);
        }

        final long result = putKeyWithHashArrayIndex(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.exitWithHex(debugClass, result, b -> b.add("hashArray", hashArray).add("key", key).hex("keyMask", keyMask));
        }

        return result;
    }

    static <K> long putKeyWithHashArrayIndex(K[] hashArray, K key, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        Objects.requireNonNull(key);
        Checks.isIntIndex(hashArrayIndex);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = HashArray.add(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.exitWithHex(debugClass, result, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    final <P1, P2, DELEGATE, E extends Exception> boolean equalsObjectKeyNonBucketMapWithIndex(P1 parameter1, BaseObjectKeyNonBucketMap<KEY, VALUES, ?> other, P2 parameter2,
            DELEGATE delegate, IntMapIndexValuesEqualityTester<VALUES, P1, P2, DELEGATE, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("delegate", delegate).add("equalityTester", equalityTester));
        }

        final KEY[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;

        final VALUES values = getValues();
        final VALUES otherValues = other.getValues();

        final int keyMask = getKeyMask();

        final KEY noKey = getNoKey();
        final int noIndex = NO_INDEX;

        boolean equals = true;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final KEY mapKey = hashArray[i];

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
