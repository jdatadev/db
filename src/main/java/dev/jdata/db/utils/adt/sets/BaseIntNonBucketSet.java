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

abstract class BaseIntNonBucketSet extends BaseIntegerNonBucketSet<int[]> implements IBaseIntSetCommon, IIntUnorderedAddable {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_INT_NON_BUCKET_SET;

    static final int NO_ELEMENT = IntNonBucket.NO_ELEMENT;

    BaseIntNonBucketSet(AllocationType allocationType, int initialCapacityExponent, float loadFactor) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor);
    }

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
        this(allocationType, HashUtil.computeHashCapacityExponent(values.length, DEFAULT_LOAD_FACTOR), DEFAULT_LOAD_FACTOR);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("values", values));
        }

        for (int value : values) {

            addValue(value);
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
    public final void addUnordered(int[] values, int startIndex, int numElements) {

        Checks.isNotEmpty(values);
        Checks.checkIntAddFromArray(values, startIndex, numElements);

        final int endIndex = startIndex + numElements;

        for (int i = startIndex; i < endIndex; ++ i) {

            addValue(values[i]);
        }
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

        checkCapacity(1);

        final int[] hashArray = getHashed();

        final long putResult = add(hashArray, value);

        final boolean newAdded = IntCapacityPutResult.getPutNewAdded(putResult);

        if (newAdded) {

            incrementNumElements();
        }

        if (DEBUG) {

            exitWithHex(putResult, b -> b.add("key", value).add("newAdded", newAdded).add("getNumElements()", getNumElements()));
        }

        return putResult;
    }

    @Override
    public final IHeapIntSet toHeapAllocated() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected int[] rehash(int[] hashArray, int newCapacity, int newCapacityExponent, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        final int[] newHashArray = new int[newCapacity];

        clearHashArray(newHashArray);

        final int hashArrayLength = hashArray.length;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final int element = hashArray[i];

            if (element != NO_ELEMENT) {

                final long addResult = add(newHashArray, element);

                final int newIndex = IntCapacityPutResult.getPutIndex(addResult);

                newHashArray[newIndex] = element;
            }
        }

        if (DEBUG) {

            exit(newHashArray, b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        return newHashArray;
    }

    private long add(int[] hashArray, int value) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("value", value));
        }

        final int hashArrayIndex = HashFunctions.hashArrayIndex(value, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, value, getKeyMask());
        }

        final long result = add(hashArray, value, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("value", value));
        }

        return result;
    }

    private long add(int[] hashArray, int value, int hashArrayIndex) {

        IntNonBucket.checkIsHashArrayElement(value);

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
        }

        final long result = HashArray.add(hashArray, value, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("value", value).add("hashArrayIndex", hashArrayIndex));
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
