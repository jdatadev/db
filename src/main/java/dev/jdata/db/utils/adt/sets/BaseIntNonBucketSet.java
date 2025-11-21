package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;
import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IIntForEach;
import dev.jdata.db.utils.adt.elements.IIntForEachWithResult;
import dev.jdata.db.utils.adt.elements.IIntUnorderedAddable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashUtil;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.IntNonBucket;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseIntNonBucketSet extends BaseNonBucketSet<int[]> implements IBaseIntSetCommon, IIntUnorderedAddable {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_NON_BUCKET_SET;

    private static final Class<?> debugClass = BaseIntNonBucketSet.class;

    static final int NO_ELEMENT = IntNonBucket.NO_ELEMENT;

    BaseIntNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, int[]::new, BaseIntNonBucketSet::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntNonBucketSet(AllocationType allocationType, int[] values) {
        this(allocationType, values.length);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("values", values));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseIntNonBucketSet(AllocationType allocationType, BaseIntNonBucketSet toCopy) {
        super(allocationType, toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    private BaseIntNonBucketSet(AllocationType allocationType, int initialCapacity) {
        this(allocationType, HashUtil.computeHashCapacityExponent(initialCapacity, DEFAULT_LOAD_FACTOR), DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR);
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IIntForEach<P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final int noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int element = hashArray[i];

            if (element != noElement) {

                forEach.each(element, parameter);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IIntForEachWithResult<P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        R result = defaultResult;

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final int noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int element = hashArray[i];

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
    protected final int[] copyValues(int[] elements, long startIndex, long numElements) {

        checkIntCopyValuesParameters(elements, elements.length, startIndex, numElements);

        return Arrays.copyOfRange(elements, intIndex(startIndex), intIndex(startIndex + numElements));
    }

    @Override
    protected final void initializeWithValues(int[] values, long numElements) {

        if (DEBUG) {

            enter(b -> b.add("values", values).add("numElements", numElements));
        }

        checkIntIntitializeWithValuesParameters(values, values.length, numElements);

        clearHashed();

        addUnordered(values, 0, intNumElements(numElements));

        if (DEBUG) {

            exit();
        }
    }

    @Override
    final void rehashElements(int[] hashArray, int[] newHashArray, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hasheArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask));
        }

        final int hashArrayLength = hashArray.length;

        final int noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int element = hashArray[i];

            if (element != noElement) {

                addWithKeyMask(newHashArray, element, newKeyMask);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("hasheArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask));
        }
    }

    final int getIndexScanHashArrayToMax(int value, int hashArrayIndex, int max) {

        IntNonBucket.checkIsHashArrayElement(value);
        Checks.isIntLengthAboveOrAtZero(max);

        if (DEBUG) {

            enter(b -> b.add("value", value).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        final int result = HashArray.getIndexScanHashArrayToMaxHashArrayIndex(getHashed(), value, hashArrayIndex, max);

        if (DEBUG) {

            exit(result, b -> b.add("values", value).add("hashArrayIndex", hashArrayIndex).add("max", max));
        }

        return result;
    }

    final long addValue(int value) {

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

    private static long addWithKeyMask(int[] hashArray, int value, int keyMask) {

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

    private static long addWithHashArrayIndex(int[] hashArray, int value, int hashArrayIndex) {

        IntNonBucket.checkIsHashArrayElement(value);

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
    public final int[] toArray() {

        if (DEBUG) {

            enter();
        }

        final int numElements = IOnlyElementsView.intNumElements(this);

        final int[] result = new int[numElements];

        final int[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final int noElement = NO_ELEMENT;

        int dstIndex = 0;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int element = hashArray[i];

            if (element != noElement) {

                result[dstIndex ++] = element;
            }
        }

        if (DEBUG) {

            exit();
        }

        return result;
    }

    final void clearBaseIntNonBucketMap() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    private static void clearHashArray(int[] hashArray) {

        IntNonBucket.clearHashArray(hashArray);
    }
}
