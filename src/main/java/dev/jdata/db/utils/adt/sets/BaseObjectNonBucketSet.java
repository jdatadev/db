package dev.jdata.db.utils.adt.sets;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiPredicate;
import java.util.function.IntFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.Array;
import dev.jdata.db.utils.adt.elements.ElementsExceptions;
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
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseObjectNonBucketSet<T> extends BaseNonBucketSet<T[]> implements IBaseObjectSetCommon<T>, IObjectUnorderedAddable<T> {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_OBJECT_NON_BUCKET_SET;

    private static final Class<?> debugClass = BaseObjectNonBucketSet.class;

    static final Object NO_ELEMENT = ObjectNonBucket.NO_ELEMENT;

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
        this(allocationType, values.length, createHashed);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("values", values).add("createHashed", createHashed));
        }

        addUnordered(values);

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

    private BaseObjectNonBucketSet(AllocationType allocationType, int initialCapacity, IntFunction<T[]> createHashed) {
        this(allocationType, HashUtil.computeHashCapacityExponent(initialCapacity, DEFAULT_LOAD_FACTOR), DEFAULT_CAPACITY_EXPONENT_INCREASE, DEFAULT_LOAD_FACTOR, createHashed);
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
    public final <P> long count(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        int count = 0;

        final T[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;

        final Object noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final T element = hashArray[i];

            if (element != noElement && predicate.test(element, parameter)) {

                ++ count;
            }
        }

        if (DEBUG) {

            exit(count);
        }

        return count;
    }

    @Override
    public final int maxInt(int defaultValue, ToIntFunction<? super T> mapper) {

        Objects.requireNonNull(mapper);

        if (DEBUG) {

            enter(b -> b.add("defaultValue", defaultValue).add("mapper", mapper));
        }

        int max = Integer.MIN_VALUE;
        boolean found = false;

        final T[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;

        final Object noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final T element = hashArray[i];

            if (element != noElement) {

                final int value = mapper.applyAsInt(element);

                if (value > max) {

                    max = value;
                    found = true;
                }
            }
        }

        final int result = found ? max : defaultValue;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final long maxLong(long defaultValue, ToLongFunction<? super T> mapper) {

        Objects.requireNonNull(mapper);

        if (DEBUG) {

            enter(b -> b.add("defaultValue", defaultValue).add("mapper", mapper));
        }

        long max = Long.MIN_VALUE;
        boolean found = false;

        final T[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;

        final Object noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final T element = hashArray[i];

            if (element != noElement) {

                final long value = mapper.applyAsLong(element);

                if (value > max) {

                    max = value;
                    found = true;
                }
            }
        }

        final long result = found ? max : defaultValue;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    public final <P> T findAtMostOne(P parameter, BiPredicate<T, P> predicate) {

        Objects.requireNonNull(predicate);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("predicate", predicate));
        }

        T found = null;

        final T[] hashArray = getHashed();
        final int hashArrayLength = hashArray.length;

        final Object noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final T element = hashArray[i];

            if (element != noElement && predicate.test(element, parameter)) {

                if (found != null) {

                    throw ElementsExceptions.moreThanOneFoundException();
                }

                found = element;
            }
        }

        if (DEBUG) {

            exit(found);
        }

        return found;
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

    @Override
    final void rehashElements(T[] hashArray, T[] newHashArray, int newKeyMask) {

        if (DEBUG) {

            enter(b -> b.add("hasheArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask));
        }

        final int hashArrayLength = hashArray.length;

        final Object noElement = NO_ELEMENT;

        for (int i = 0; i < hashArrayLength; ++ i) {

            final T element = hashArray[i];

            if (element != noElement) {

                addWithKeyMask(newHashArray, element, newKeyMask);
            }
        }

        if (DEBUG) {

            exit(b -> b.add("hasheArray", hashArray).add("newHashArray", newHashArray).hex("newKeyMask", newKeyMask));
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

    private static <T> long addWithKeyMask(T[] hashArray, T value, int keyMask) {

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("hashArray", hashArray).add("value", value).hex("keyMask", keyMask));
        }

        final int hashArrayIndex = HashFunctions.objectHashArrayIndex(value, keyMask);

        if (DEBUG) {

            PrintDebug.debugFormatln(debugClass, "lookup hashArrayIndex=%d key=%d keyMask=0x%08x", hashArrayIndex, value, keyMask);
        }

        final long result = addWithHashArrayIndex(hashArray, value, hashArrayIndex);

        if (DEBUG) {

            PrintDebug.exit(debugClass, b -> b.add("hashArray", hashArray).add("value", value).hex("keyMask", keyMask));
        }

        return result;
    }

    private static <T> long addWithHashArrayIndex(T[] hashArray, T value, int hashArrayIndex) {

        ObjectNonBucket.checkIsHashArrayElement(value);

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
