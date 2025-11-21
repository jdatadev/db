package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ILongForEach;
import dev.jdata.db.utils.adt.elements.ILongForEachWithResult;
import dev.jdata.db.utils.adt.elements.ILongUnorderedAddable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.LongNonBucket;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseLongNonBucketSet extends BaseNonBucketSet<long[]> implements IBaseLongSetCommon, ILongUnorderedAddable {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LONG_NON_BUCKET_SET;

    private static final Class<?> debugClass = BaseLongNonBucketSet.class;

    static final long NO_ELEMENT = LongNonBucket.NO_ELEMENT;

    BaseLongNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, long[]::new, BaseLongNonBucketSet::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseLongNonBucketSet(AllocationType allocationType, BaseLongNonBucketSet toCopy) {
        super(allocationType, toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, ILongForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final long noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long element = hashArray[i];

            if (element != noElement) {

                forEach.each(element, parameter);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, ILongForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        R result = defaultResult;

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final long noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long element = hashArray[i];

            if (element != noElement) {

                final R forEachResult = forEach.each(element, parameter1, parameter2);

                if (forEachResult != null) {

                    result = forEachResult;
                    break;
                }
            }
        }

        if (DEBUG) {

            exit(result, b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        return result;
    }

    @Override
    protected final long[] copyValues(long[] values, long startIndex, long numElements) {

        checkIntCopyValuesParameters(values, values.length, startIndex, numElements);

        return Arrays.copyOfRange(values, intIndex(startIndex), intIndex(startIndex + numElements));
    }

    @Override
    protected final void initializeWithValues(long[] values, long numElements) {

        if (DEBUG) {

            enter(b -> b.add("values", values).add("numElements", numElements));
        }

        checkIntIntitializeWithValuesParameters(values, values.length, numElements);

        clearHashed();

        addUnordered(values, intNumElements(numElements));

        if (DEBUG) {

            exit();
        }
    }

    @Override
    final void rehashElements(long[] hashArray, long[] newHashArray, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hasheArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask));
        }

        final int hashArrayLength = hashArray.length;

        final long noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long element = hashArray[i];

            if (element != noElement) {

                addWithKeyMask(newHashArray, element, newKeyMask);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("hasheArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask));
        }
    }

    final int getIndexScanHashArrayToMax(long value, int hashArrayIndex, int max) {

        LongNonBucket.checkIsHashArrayElement(value);
        Checks.isIntLengthAboveOrAtZero(max);

        if (DEBUG) {

            enter(b -> b.add("value", value).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        final int result = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(getHashed(), value, hashArrayIndex, max);

        if (DEBUG) {

            exit(result, b -> b.add("value", value).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return result;
    }

    final long addValue(long value) {

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        checkCapacityForOneMoreElement();

        final long putResult = addWithKeyMask(getHashed(), value, getKeyMask());

        final boolean newAdded = IntCapacityPutResult.getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exitWithHex(putResult, b -> b.add("key", value).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return putResult;
    }

    private static long addWithKeyMask(long[] hashArray, long value, int keyMask) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("value", value).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, keyMask);

        if (DEBUG) {

            PrintDebug.debugFormatln(debugClass, "lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, value, keyMask);
        }

        final long result = addWithHashArrayIndex(hashArray, value, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.exit(debugClass, b -> b.add("hashArray", hashArray).add("value", value).hex("keyMask", keyMask));
        }

        return result;
    }

    private static long addWithHashArrayIndex(long[] hashArray, long value, int hashArrayIndex) {

        LongNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = HashArray.add(hashArray, value, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        return result;
    }

    @Override
    public final long[] toArray() {

        if (DEBUG) {

            enter();
        }

        final int numElements = IOnlyElementsView.intNumElements(this);

        final long[] result = new long[numElements];

        final long[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final long noElement = NO_ELEMENT;

        int dstIndex = 0;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final long element = hashArray[i];

            if (element != noElement) {

                result[dstIndex ++] = element;
            }
        }

        if (DEBUG) {

            exit();
        }

        return result;
    }

    final void clearBaseLongNonBucketSet() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    private static void clearHashArray(long[] hashArray) {

        LongNonBucket.clearHashArray(hashArray);
    }
}
