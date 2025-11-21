package dev.jdata.db.utils.adt.arrays;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.checks.AssertionContants;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;

abstract class BaseAnyDimensionalLargeArray<O, I> extends BaseAnyDimensionalArray<Void, Void, Void> implements PrintDebug {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_ANY_LARGE_ARRAY;

    private static final boolean ASSERT = AssertionContants.ASSERT_BASE_ANY_LARGE_ARRAY;

    protected static int DEFAULT_INITIAL_OUTER_CAPACITY = 0;

    protected abstract int getOuterArrayCapacity();
    protected abstract long getInnerElementCapacity();

    protected abstract int getOuterArrayLength(O outerArray);
    protected abstract O copyOuterArray(O outerArray, int newCapacity);

    protected abstract I getInnerArray(O outerArray, int index);
    protected abstract void clearInnerArray(I innerArray, long startIndex, long numElements);

    private final IntFunction<O> createOuterArray;
    private final boolean isNumInnerElementsRequired;

    private O outerArray;
    private int[] innerArrayNumElements;

    private int numOuterUtilizedEntries;

    BaseAnyDimensionalLargeArray(AllocationType allocationType, int initialOuterCapacity, boolean hasClearValue, IntFunction<O> createOuterArray, boolean requiresInnerArrayNumElements) {
        super(allocationType, hasClearValue);

        Checks.isInitialOuterCapacity(initialOuterCapacity);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("createOuterArray", createOuterArray)
                    .add("requiresInnerArrayNumElements", requiresInnerArrayNumElements));
        }

        this.createOuterArray = Objects.requireNonNull(createOuterArray);
        this.isNumInnerElementsRequired = requiresInnerArrayNumElements;

        if (initialOuterCapacity != 0) {

            this.outerArray = createOuterArray.apply(initialOuterCapacity);
            this.innerArrayNumElements = requiresInnerArrayNumElements ? new int[initialOuterCapacity] : null;
        }
        else {
            this.outerArray = null;
            this.innerArrayNumElements = null;
        }

        this.numOuterUtilizedEntries = 0;

        if (DEBUG) {

            exit();
        }
    }

    @FunctionalInterface
    protected interface IOuterAndInnerArraysCopier<T> {

        void copy(T source, T dest, int numElements);
    }

    BaseAnyDimensionalLargeArray(AllocationType allocationType, BaseAnyDimensionalLargeArray<O, I> toCopy, IOuterAndInnerArraysCopier<O> copyOuterAndInnerArrays) {
        super(allocationType, toCopy);

        Objects.requireNonNull(toCopy);
        Objects.requireNonNull(copyOuterAndInnerArrays);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyOuterAndInnerArrays", copyOuterAndInnerArrays));
        }

        final IntFunction<O> createOuterArray = this.createOuterArray = toCopy.createOuterArray;

        this.isNumInnerElementsRequired = toCopy.isNumInnerElementsRequired;

        final O toCopyOuterArray = toCopy.outerArray;

        if (toCopyOuterArray != null) {

            final O thisOuterArray = this.outerArray = createOuterArray.apply(toCopy.getOuterArrayCapacity());

            copyOuterAndInnerArrays.copy(thisOuterArray, toCopyOuterArray, numOuterUtilizedEntries);

            this.innerArrayNumElements = Array.copyOf(toCopy.innerArrayNumElements);
        }
        else {
            this.outerArray = null;
            this.innerArrayNumElements = null;
        }

        this.numOuterUtilizedEntries = toCopy.numOuterUtilizedEntries;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    public final boolean isEmpty() {

        return numOuterUtilizedEntries == 0;
    }

    @Override
    protected final <P, R> R makeFromElements(AllocationType allocationType, P parameter, IMakeFromElementsFunction<Void, Void, P, R> makeFromElements) {

        Objects.requireNonNull(allocationType);
        Objects.requireNonNull(makeFromElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void recreateElements() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void resetToNull() {

        throw new UnsupportedOperationException();
    }

    @Override
    protected final Void copyValues(Void elements, long startIndex, long numElements) {

        checkLongCopyValuesParameters(elements, 0L, startIndex, numElements);

        throw new UnsupportedOperationException();
    }

    @Override
    protected final void initializeWithValues(Void values, long numElements) {

        checkIntOrLongIntitializeWithValuesParameters(values, 0L, numElements);

        throw new UnsupportedOperationException();
    }

    protected void clearArray() {

        if (DEBUG) {

            enter();
        }

        final int numOuter = getNumOuterUtilizedEntries();
        final int[] innerNum = innerArrayNumElements;

        if (innerNum != null) {

            Arrays.fill(innerNum, 0, numOuter, 0);
        }

        this.numOuterUtilizedEntries = 0;

        if (DEBUG) {

            exit();
        }

        if (shouldClear()) {

            clearInnerArrays(numOuter, 0, getInnerElementCapacity());
        }
    }

    protected long getArrayElementCapacity() {

        return getOuterArrayCapacity() * getInnerElementCapacity();
    }

    protected final O getOuterArray() {
        return outerArray;
    }

    protected final I getInnerArray(int outerIndex) {

        return getInnerArray(outerArray, outerIndex);
    }

    protected final int getNumInnerElements(int outerIndex) {

        Checks.isOuterIndex(outerIndex);

        if (DEBUG) {

            enter(b -> b.add("outerIndex", outerIndex));
        }

        if (ASSERT) {

            Assertions.isTrue(isNumInnerElementsRequired);
        }

        final int result = innerArrayNumElements[outerIndex];

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    final O reallocateOuterArrayAndInnerArrayNumElementsWithExistingLength(int outerArrayLength) {

        Checks.isOuterCapacity(outerArrayLength);

        if (DEBUG) {

            enter(b -> b.add("outerArrayLength", outerArrayLength));
        }

        final int newCapacity = outerArrayLength << 2;

        final O result = reallocateOuterArrayAndInnerArrayNumElements(newCapacity);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    final O reallocateOuterArrayAndInnerArrayNumElements(int newCapacity) {

        Checks.isIntCapacity(newCapacity);

        if (DEBUG) {

            enter(b -> b.add("newCapacity", newCapacity));
        }

        final O result = copyOuterArray(outerArray, newCapacity);

        setOuterArray(result);

        if (isNumInnerElementsRequired()) {

            this.innerArrayNumElements = innerArrayNumElements != null
                    ? Arrays.copyOf(innerArrayNumElements, newCapacity)
                    : new int[newCapacity];
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    final void setOuterArray(O outerArray) {

        Checks.areNotSame(this.outerArray, outerArray);

        if (DEBUG) {

            enter(b -> b.add("outerArray", outerArray));
        }

        this.outerArray = Objects.requireNonNull(outerArray);

        if (DEBUG) {

            exit();
        }
    }

    final <P> O allocateInitialOuterArrayAndInnerArrayElements(int outerCapacity) {

        Checks.isOuterCapacity(outerCapacity);

        if (DEBUG) {

            enter(b -> b.add("outerCapacity", outerCapacity));
        }

        final O outerArray = createOuterArray.apply(outerCapacity);

        allocateInitialNumInnerElementsIfRequired(outerCapacity);

        setOuterArray(outerArray);

        if (DEBUG) {

            exit(outerArray);
        }

        return outerArray;
    }

    final void allocateInitialNumInnerElementsIfRequired(int numToAllocate) {

        Checks.isIntLengthAboveZero(numToAllocate);

        if (DEBUG) {

            enter(b -> b.add("numToAllocate", numToAllocate));
        }

        if (isNumInnerElementsRequired()) {

            if (innerArrayNumElements != null) {

                throw new IllegalStateException();
            }

            this.innerArrayNumElements = new int[numToAllocate];
        }

        if (DEBUG) {

            exit();
        }
    }

    final void clearNumInnerElementsIfRequired(int outerIndex) {

        if (DEBUG) {

            enter(b -> b.add("outerIndex", outerIndex));
        }

        setNumInnerElementsIfRequired(outerIndex, 0);

        if (DEBUG) {

            exit();
        }
    }

    final void setNumInnerElementsIfRequired(int outerIndex, int numElements) {

        if (DEBUG) {

            enter(b -> b.add("outerIndex", outerIndex).add("numElements", numElements));
        }

        if (isNumInnerElementsRequired()) {

            setNumInnerElements(outerIndex, numElements);
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final void setNumInnerElements(int outerIndex, long numElements) {

        Checks.isOuterIndex(outerIndex);
        Checks.isIntNumElements(numElements);

        if (DEBUG) {

            enter(b -> b.add("outerIndex", outerIndex).add("numElements", numElements));
        }

        innerArrayNumElements[outerIndex] = intNumElements(numElements);

        if (DEBUG) {

            exit();
        }
    }

    final void incrementNumInnerElements(int outerIndex) {

        Checks.isOuterIndex(outerIndex);

        if (DEBUG) {

            enter(b -> b.add("outerIndex", outerIndex));
        }

        ++ innerArrayNumElements[outerIndex];

        if (DEBUG) {

            exit();
        }
    }

    private void clearInnerArrays(int numOuter, long startIndex, long innerElementsCapacity) {

        if (DEBUG) {

            enter(b -> b.add("numOuter", numOuter).add("startIndex", startIndex).add("innerElementsCapacity", innerElementsCapacity));
        }

        clearInnerArrays(this, numOuter, startIndex, innerElementsCapacity, (a, s, n, i) -> i.clearInnerArray(a, s, n));

        if (DEBUG) {

            exit();
        }
    }

    @FunctionalInterface
    private interface ArrayClearer<T, P> {

        void clear(T array, long startIndex, long numElements, P parameter);
    }

    private <P> void clearInnerArrays(P parameter, int numOuter, long startIndex, long innerElementsCapacity, ArrayClearer<I, P> arrayClearer) {

        Objects.requireNonNull(arrayClearer);

        if (DEBUG) {

            enter(b -> b.add("parameter", parameter).add("numOuter", numOuter).add("startIndex", startIndex).add("innerElementsCapacity", innerElementsCapacity)
                    .add("arrayClearer", arrayClearer));
        }

        final O outer = outerArray;

        for (int i = 0; i < numOuter; ++ i) {

            final I a = getInnerArray(outer, i);

            if (arrayClearer != null) {

                arrayClearer.clear(a, startIndex, innerElementsCapacity, parameter);
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    protected final int getNumOuterUtilizedEntries() {

        return numOuterUtilizedEntries;
    }

    final void setNumOuterUtilizedEntries(int numOuterUtilizedEntries, int maxValue) {

        Checks.isGreaterThan(numOuterUtilizedEntries, this.numOuterUtilizedEntries);
        Checks.isLessThanOrEqualTo(numOuterUtilizedEntries, maxValue);

        if (DEBUG) {

            enter(b -> b.add("numOuterUtilizedEntries", numOuterUtilizedEntries).add("maxValue", maxValue));
        }

        this.numOuterUtilizedEntries = numOuterUtilizedEntries;

        if (DEBUG) {

            exit();
        }
    }

    final void incrementNumOuterUtilizedEntries() {

        if (DEBUG) {

            enter();
        }

        ++ numOuterUtilizedEntries;

        if (DEBUG) {

            exit(numOuterUtilizedEntries);
        }
    }

    final void increaseNumOuterUtilizedEntries(int numAdditional, int maxValue) {

        Checks.isLessThanOrEqualTo(numOuterUtilizedEntries + numAdditional, maxValue);

        if (DEBUG) {

            enter(b -> b.add("numAdditional", numAdditional).add("maxValue", maxValue));
        }

        numOuterUtilizedEntries += numAdditional;

        if (DEBUG) {

            exit(numOuterUtilizedEntries);
        }
    }

    private boolean isNumInnerElementsRequired() {

        return isNumInnerElementsRequired;
    }
}
