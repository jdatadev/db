package dev.jdata.db.utils.adt.maps;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntAnyOrderAddable;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseIntKeyNonBucketMap<VALUES, MAP extends BaseIntKeyNonBucketMap<VALUES, MAP>>

        extends BaseIntCapacityExponentMap<int[], VALUES, MAP>
        implements IIntKeyMapCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_KEY_NON_BUCKET_MAP;

    private static final int NO_KEY = -1;

    private final IntFunction<VALUES> createValues;

    private VALUES values;

    protected abstract void put(VALUES values, int index, VALUES newValues, int newIndex);
    protected abstract void clearValues(VALUES values);

    protected abstract int getHashArrayIndex(int key, int keyMask);

    BaseIntKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, IntFunction<VALUES> createValues) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createValues);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("createValues", createValues));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntKeyNonBucketMap(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<VALUES> createValues) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, int[]::new, BaseIntKeyNonBucketMap::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createValues", createValues));
        }

        this.createValues = Objects.requireNonNull(createValues);

        this.values = createValues.apply(getHashedCapacity());

        clearValues(values);

        if (DEBUG) {

            exit();
        }
    }

    BaseIntKeyNonBucketMap(AllocationType allocationType, BaseIntKeyNonBucketMap<VALUES, MAP> toCopy, BiConsumer<VALUES, VALUES> copyValuesContent) {
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
    public final long keys(IIntAnyOrderAddable addable) {

        Objects.requireNonNull(addable);

        if (DEBUG) {

            enter(b -> b.add("addable", addable));
        }

        final long result = keysAndValues(addable, null, (srcIndex, keysSrc, valuesSrc, dstIndex, keysDst, valuesDst) -> keysDst.addInAnyOrder(keysSrc[srcIndex]));

        if (DEBUG) {

            exit(result, b -> b.add("addable", addable));
        }

        return result;
    }

    @Override
    protected void recreateElements() {

        super.recreateElements();

        this.values = createValues.apply(getHashedCapacity());
    }

    @Override
    protected void resetToNull() {

        super.resetToNull();

        this.values = null;
    }

    @Override
    protected int[] rehash(int[] hashArray, int newCapacity, int newCapacityExponent, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        final int[] newHashArray = new int[newCapacity];

        clearHashArray(newHashArray);

        final VALUES newValuesArray = createValues.apply(newCapacity);

        final int hashArrayLength = hashArray.length;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int key = hashArray[i];

            if (key != NO_KEY) {

                final long putResult = put(newHashArray, key);

                final int newIndex = IntCapacityPutResult.getPutIndex(putResult);

                newHashArray[newIndex] = key;

                put(values, i, newValuesArray, newIndex);
            }
        }

        this.values = newValuesArray;

        if (DEBUG) {

            exit(newHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        return newHashArray;
    }

    protected final <P1, P2, E extends Exception> void forEachKeyAndValue(P1 parameter1, P2 parameter2, ForEachKeyAndValueWithKeysAndValues<int[], VALUES, P1, P2, E> forEach)
            throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final VALUES values = getValues();

        final int noKey = NO_KEY;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray[i];

            if (mapKey != noKey) {

                forEach.each(hashArray, i, values, i, parameter1, parameter2);
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final <P1, P2, DELEGATE, R, E extends Exception> R forEachKeyAndValueWithResult(R defaultResult, P1 parameter1, P2 parameter2, DELEGATE delegate,
            ForEachKeyAndValueWithKeysAndValuesWithResult<int[], VALUES, P1, P2, DELEGATE, R, E> forEach) throws E {

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("delegate", delegate).add("forEach", forEach));
        }

        R result = defaultResult;

        final int[] hashArray = getHashed();

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
    final <KEYS_DST, VALUES_DST> long keysAndValues(KEYS_DST keysDst, VALUES_DST valuesDst,
            IIntCapacityMapIndexKeyValueAdder<int[], VALUES, KEYS_DST, VALUES_DST> keyValueAdder) {

        Checks.areAnyNotNull(keysDst, valuesDst);
        Objects.requireNonNull(keyValueAdder);

        if (DEBUG) {

            enter(b -> b.add("keysDst", keysDst).add("valuesDst", valuesDst).add("keyValueAdder", keyValueAdder));
        }

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final int noKey = NO_KEY;

        int numAdded = 0;

        final VALUES v = values;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray[i];

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

    protected final long put(int key) {

        IntNonBucket.checkIsHashArrayElement(key);

        if (DEBUG) {

            enter(b -> b.add("key", key));
        }

        checkCapacity(1);

        final int[] hashArray = getHashed();

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

    protected final VALUES getValues() {
        return values;
    }

    final long put(int[] hashArray, int key, int hashArrayIndex) {

        Checks.isNotEmpty(hashArray);
        IntNonBucket.checkIsHashArrayElement(key);
        Checks.isIntIndex(hashArrayIndex);

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = HashArray.add(hashArray, key, hashArrayIndex);

        if (DEBUG) {

            exitWithBinary(result, b -> b.add("hashArray", hashArray).add("key", key).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    final <P1, P2, DELEGATE, E extends Exception> boolean equalsIntKeyNonBucketMapWithIndex(P1 parameter1, BaseIntKeyNonBucketMap<VALUES, ?> other, P2 parameter2,
            DELEGATE delegate, IntMapIndexValuesEqualityTester<VALUES, P1, P2, DELEGATE, E> equalityTester) throws E {

        Objects.requireNonNull(other);
        Objects.requireNonNull(equalityTester);

        if (DEBUG) {

            enter(b -> b.add("parameter1", parameter1).add("other", other).add("parameter2", parameter2).add("delegate", delegate).add("equalityTester", equalityTester));
        }

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final VALUES values = getValues();
        final VALUES otherValues = other.getValues();

        final int keyMask = getKeyMask();

        final int noKey = NO_KEY;
        final int noIndex = NO_INDEX;

        boolean equals = true;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int mapKey = hashArray[i];

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

    private static void clearHashArray(int[] hashArray) {

        IntNonBucket.clearHashArray(hashArray);
    }
}
