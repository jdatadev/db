package dev.jdata.db.utils.adt.maps;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ILongAnyOrderAddable;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseLongKeyNonBucketMap<VALUES, MAP extends BaseLongKeyNonBucketMap<VALUES, MAP>>

        extends BaseIntCapacityExponentMap<long[], VALUES, MAP>
        implements ILongKeyMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_NON_BUCKET_MAP;

    private static final long NO_KEY = -1L;

    private final IntFunction<VALUES> createValues;

    private VALUES values;

    protected abstract void put(VALUES values, int index, VALUES newValues, int newIndex);
    protected abstract void clearValues(VALUES values);

    protected abstract int scanHashArrayForIndex(long key, int keyMask);

    BaseLongKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<VALUES> createValuesArray) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValuesArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createValuesArray", createValuesArray));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<VALUES> createValuesArray) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, long[]::new, BaseLongKeyNonBucketMap::clearKeyMap);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createValuesArray", createValuesArray));
        }

        this.createValues = Objects.requireNonNull(createValuesArray);

        this.values = createValuesArray.apply(getHashedCapacity());

        clearValues(values);

        if (DEBUG) {

            exit();
        }
    }

    BaseLongKeyNonBucketMap(AllocationType allocationType, BaseLongKeyNonBucketMap<VALUES, MAP> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
        super(allocationType, toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyValuesContent", copyValuesContent));
        }

        final IntFunction<VALUES> createValues = this.createValues = toCopy.createValues;

        final VALUES values = this.values = createValues.apply(getHashedCapacity());

        copyValuesContent.accept(toCopy.values, values);

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
    protected long[] rehash(long[] hashArray, int newCapacity, int newCapacityExponent, int keyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).add("keyMask", keyMask));
        }

        final long[] newHashArray = new long[newCapacity];

        clearKeyMap(newHashArray);

        final VALUES newValuesArray = createValues.apply(newCapacity);

        final int hashArrayLength = hashArray.length;

        final long noKey = IntNonBucket.NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long key = hashArray[i];

            if (key != noKey) {

                final int newIndex = IntCapacityPutResult.getPutIndex(put(newHashArray, key));

                newHashArray[newIndex] = key;

                put(values, i, newValuesArray, newIndex);
            }
        }

        this.values = newValuesArray;

        if (DEBUG) {

            exit(newHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).add("keyMask", keyMask));
        }

        return newHashArray;
    }

    @Deprecated // not public?
    public final IntFunction<VALUES> getCreateValues() {
        return createValues;
    }

    protected final <P1, P2, E extends Exception> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValueWithKeysAndValues<long[], VALUES, P1, P2, E> forEach)
            throws E {

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final VALUES values = getValues();

        final long noKey = IntNonBucket.NO_ELEMENT;

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

        final long noKey = IntNonBucket.NO_ELEMENT;

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
    protected final <KEYS_DST, VALUES_DST> long keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            IIntCapacityMapIndexKeyValueAdder<long[], VALUES, KEYS_DST, VALUES_DST> keyValueAdder) {

        Checks.areAnyNotNull(keysDst, valuesDst);
        Objects.requireNonNull(keyValueAdder);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst).add("keyValueAdder", keyValueAdder));
        }

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final int noKey = IntNonBucket.NO_ELEMENT;

        int numAdded = 0;

        final VALUES v = values;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long mapKey = hashArray[i];

            if (mapKey != noKey) {

                keyValueAdder.addValue(i, hashArray, v, numAdded, keysDst, valuesDst);

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

        checkCapacity(1);

        final long[] hashArray = getHashed();

        final long putResult = put(hashArray, key);

        final boolean newAdded = IntCapacityPutResult.getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exitWithHex(putResult, b -> b.add("key", key).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return putResult;
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

    protected final VALUES getValues() {
        return values;
    }

    final long put(long[] hashArray, long key, int hashArrayIndex) {

        Objects.requireNonNull(hashArray);
        LongNonBucket.checkIsHashArrayElement(key);
        Checks.checkArrayIndex(hashArray, hashArrayIndex);

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = HashArray.add(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exitWithBinary(result, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    private static void clearKeyMap(long[] hashArray) {

        Arrays.fill(hashArray, IntNonBucket.NO_ELEMENT);
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
}
