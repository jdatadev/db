package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.IObjectForEach;
import dev.jdata.db.utils.adt.elements.IObjectForEachWithResult;
import dev.jdata.db.utils.adt.elements.IObjectUnorderedAddable;
import dev.jdata.db.utils.adt.elements.IOnlyElementsView;
import dev.jdata.db.utils.adt.hashed.HashFunctions;
import dev.jdata.db.utils.adt.hashed.HashUtil;
import dev.jdata.db.utils.adt.hashed.helpers.HashArray;
import dev.jdata.db.utils.adt.hashed.helpers.IntCapacityPutResult;
import dev.jdata.db.utils.adt.hashed.helpers.ObjectNonBucket;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseObjectNonBucketSet<T> extends BaseObjectSet<T, T> implements IBaseObjectSetCommon<T>, IObjectUnorderedAddable<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_NON_BUCKET_SET;

    static final Object NO_ELEMENT = ObjectNonBucket.NO_ELEMENT;

    BaseObjectNonBucketSet(AllocationType allocationType, int initialCapacityExponent, float loadFactor, IntFunction<T[]> createHashed) {
        this(allocationType, initialCapacityExponent, DEFAULT_CAPACITY_EXPONENT_INCREASE, loadFactor, createHashed);
    }

    BaseObjectNonBucketSet(AllocationType allocationType, int initialCapacityExponent, int capacityExponentIncrease, float loadFactor, IntFunction<T[]> createHashed) {
        super(allocationType, initialCapacityExponent, capacityExponentIncrease, loadFactor, createHashed, BaseObjectNonBucketSet::clearHashArray);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialCapacityExponent", initialCapacityExponent).add("capacityExponentIncrease", capacityExponentIncrease)
                    .add("loadFactor", loadFactor).add("createHashed", createHashed));
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectNonBucketSet(AllocationType allocationType, T[] values, IntFunction<T[]> createHashed) {
        this(allocationType, HashUtil.computeHashCapacityExponent(values.length, DEFAULT_LOAD_FACTOR), DEFAULT_LOAD_FACTOR, createHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("values", values).add("createHashed", createHashed));
        }

        for (T value : values) {

            addValue(value);
        }

        if (DEBUG) {

            exit();
        }
    }

    BaseObjectNonBucketSet(AllocationType allocationType, BaseObjectNonBucketSet<T> toCopy) {
        super(allocationType, toCopy, Array::copyOf);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy));
        }

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final <P, E extends Exception> void forEach(P parameter, IObjectForEach<T, P, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("forEach", forEach));
        }

        final T[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final Object noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final T element = hashArray[i];

            if (element != noElement) {

                forEach.each(element, parameter);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("parameter", parameter).add("forEach", forEach));
        }
    }

    @Override
    public final <P1, P2, R, E extends Exception> R forEachWithResult(R defaultResult, P1 parameter1, P2 parameter2, IObjectForEachWithResult<T, P1, P2, R, E> forEach) throws E {

        Objects.requireNonNull(forEach);

        if (DEBUG) {

            enter(b -> b.add("defaultResult", defaultResult).add("parameter1", parameter1).add("parameter2", parameter2).add("forEach", forEach));
        }

        R result = defaultResult;

        final T[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final Object noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final T element = hashArray[i];

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
    public final void addUnordered(T[] instances, int startIndex, int numElements) {

        Checks.isNotEmpty(instances);
        Checks.checkIntAddFromArray(instances, startIndex, numElements);

        final int endIndex = startIndex + numElements;

        for (int i = startIndex; i < endIndex; ++ i) {

            addValue(instances[i]);
        }
    }

    @Override
    protected final T[] copyValues(T[] instances, long startIndex, long numElements) {

        checkIntCopyValuesParameters(instances, instances.length, startIndex, numElements);

        return Arrays.copyOfRange(instances, intIndex(startIndex), intIndex(startIndex + numElements));
    }

    @Override
    protected final void initializeWithValues(T[] values, long numElements) {

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

    final int getIndexScanHashArrayToMax(T value, int hashArrayIndex, int max) {

        ObjectNonBucket.checkIsHashArrayElement(value);
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

    final long addValue(T value) {

        Objects.requireNonNull(value);

        if (DEBUG) {

            enter(b -> b.add("value", value));
        }

        checkCapacity(1);

        final T[] hashArray = getHashed();

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
    protected T[] rehash(T[] hashArray, int newCapacity, int newCapacityExponent, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("newCapacity", newCapacity).add("newCapacityExponent", newCapacityExponent).hex("newKeyMask", newKeyMask));
        }

        final T[] newHashArray = createHashed(newCapacity);

        clearHashArray(newHashArray);

        final int hashArrayLength = hashArray.length;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final T element = hashArray[i];

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

    private long add(T[] hashArray, T value) {

        if (DEBUG) {

            enter(b -> b.add("hashArray", hashArray).add("value", value));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(value, getKeyMask());

        if (DEBUG) {

            debugFormatln("lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, value, getKeyMask());
        }

        final long result = add(hashArray, value, hashArrayIndex);

        if (DEBUG) {

            exit(result, b -> b.add("hashArray", hashArray).add("value", value));
        }

        return result;
    }

    private long add(T[] hashArray, T value, int hashArrayIndex) {

        ObjectNonBucket.checkIsHashArrayElement(value);

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
    public final T[] toArray(IntFunction<T[]> createArray) {

        Objects.requireNonNull(createArray);

        if (DEBUG) {

            enter(b -> b.add("createArray", createArray));
        }

        final int numElements = IOnlyElementsView.intNumElements(this);

        final T[] result = createArray.apply(numElements);

        final T[] hashArray = getHashed();

        final int hashArrayLength = hashArray.length;

        final Object noElement = NO_ELEMENT;

        int dstIndex = 0;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final T element = hashArray[i];

            if (element != noElement) {

                result[dstIndex ++] = element;
            }
        }

        if (DEBUG) {

            exit();
        }

        return result;
    }

    final void clearBaseObjectSet() {

        if (DEBUG) {

            enter();
        }

        clearHashed();

        if (DEBUG) {

            exit();
        }
    }

    private static <T> void clearHashArray(T[] hashArray) {

        ObjectNonBucket.clearHashArray(hashArray);
    }
}
