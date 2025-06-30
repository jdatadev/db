package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.ObjLongConsumer;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseLargeOneDimensionalArray<O, I> extends BaseAnyLargeArray<O, I> implements ICapacity {

    protected abstract int getOuterIndex(long index);

    protected abstract I abstractCreateAndSetInnerArray(O outerArray, int outerIndex, int innerArrayLength);

    private final int innerCapacity;
    private final ToIntFunction<O> getOuterArrayLength;

    private final int innerNumAllocateElements;

    private int numOuterAllocatedInnerArrays;

    BaseLargeOneDimensionalArray(int initialOuterCapacity, int innerCapacity, int numInitialOuterAllocatedInnerArrays, IntFunction<O> createOuterArray,
            ToIntFunction<O> getOuterArrayLength, boolean requiresInnerArrayNumElements) {
        super(initialOuterCapacity, createOuterArray, requiresInnerArrayNumElements);

        this.innerCapacity = Checks.isInitialCapacity(innerCapacity);
        this.getOuterArrayLength = Objects.requireNonNull(getOuterArrayLength);

        this.innerNumAllocateElements = innerCapacity;

        this.numOuterAllocatedInnerArrays = Checks.isLengthAboveOrAtZero(numInitialOuterAllocatedInnerArrays);
    }

    @Override
    public final long getCapacity() {

        return numOuterAllocatedInnerArrays * (long)innerCapacity;
    }

    protected final int getInnerCapacity() {
        return innerCapacity;
    }

    @Override
    protected long getInnerElementCapacity() {

        return getInnerCapacity();
    }

    final <P> void clearArrays(P parameter, ArrayClearer<I, P> arrayClearer) {

        clearArrays(parameter, getNumOuterUtilizedEntries(), 0, innerCapacity, arrayClearer);
    }

    final long getRemainderOfLastInnerArrayWithLimit(int expectedOuterIndex, long limit) {

        return Capacity.getRemainderOfLastInnerArrayWithLimit(expectedOuterIndex, limit, getNumOuterUtilizedEntries(), getInnerElementCapacity());
    }

    final int checkCapacityForOneAppendedElementAndReturnOuterIndex() {

        final int numOuterAllocated = numOuterAllocatedInnerArrays;
        final int numOuterUtilized = getNumOuterUtilizedEntries();

        final int result;

        if (numOuterAllocated == 0) {

            result = allocateInitialOuterAndInnerArray();
        }
        else if (numOuterUtilized == 0) {

            incrementNumOuterUtilizedEntries();

            result = 0;
        }
        else {
            final int outerIndex = numOuterUtilized - 1;

            final long numFreeInnerElements = getRemainderOfLastInnerArray(outerIndex);

            if (numFreeInnerElements == 0L) {

                if (numOuterUtilized < numOuterAllocated) {

                    result = numOuterUtilized;

                    clearNumInnerElementsIfRequired(result);

                    incrementNumOuterUtilizedEntries();
                }
                else {
                    result = reallocateOuterArray(numOuterUtilized, getOuterArrayLength);
                }
            }
            else {
                result = outerIndex;
            }
        }

        return result;
    }

    private int allocateInitialOuterAndInnerArray() {

        allocateInitialOuterAndInnerArrays(1L, null, null);

        return 0;
    }

    final <P> O allocateInitialOuterAndInnerArrays(long numAdditional, P parameter, ArrayClearer<I, P> arrayClearer) {

        Checks.isLengthAboveZero(numAdditional);

        final int numOuter = computeNumOuter(numAdditional, innerCapacity);

        incrementNumOuterAllocatedInnerArrays(numOuter);

        final O outerArray = allocateInitialOuterArrayAndInnerArrayElements(numOuter);

        allocateInnerArrays(outerArray, 0, numOuter, parameter, arrayClearer);

        return outerArray;
    }

    final int reallocateOuterArray(int numOuterUtilizedEntries, ToIntFunction<O> getArrayLength) {

        Checks.isNumElements(numOuterUtilizedEntries);

        final O outerArray = getOuterArray();
        final int outerArrayLength = getArrayLength.applyAsInt(outerArray);

        final O updatedOuterArray;

        if (numOuterUtilizedEntries == outerArrayLength) {

            updatedOuterArray = reallocateOuterArrayAndInnerArrayNumElementsWithExistingLength(outerArrayLength);
        }
        else if (numOuterUtilizedEntries > outerArrayLength) {

            throw new IllegalStateException();
        }
        else {
            updatedOuterArray = outerArray;
        }

        final int resultIndex = numOuterUtilizedEntries;

        createAndSetInnerArray(updatedOuterArray, resultIndex, getInnerNumAllocateElements());

        clearNumInnerElementsIfRequired(resultIndex);

        incrementNumOuterAllocatedInnerArrays();
        incrementNumOuterUtilizedEntries();

        return resultIndex;
    }

    protected I checkCapacity() {

        return checkCapacity(null, null);
    }

    final <P> I checkCapacity(P parameter, ArrayClearer<I, P> arrayClearer) {

         return checkCapacity(1L, parameter, arrayClearer);
    }

    protected final <P> I checkCapacity(long numAdditional) {

        return checkCapacity(numAdditional, null, null);
    }

    final <P> I checkCapacity(long numAdditional, P parameter, ArrayClearer<I, P> arrayClearer) {

        final int outerIndex = checkCapacityAndReturnOuterIndex(numAdditional, parameter, arrayClearer);

        return getInnerArray(getOuterArray(), outerIndex);
    }

    protected final <P> int checkCapacityAndReturnOuterIndex(long numAdditional) {

        return checkCapacityAndReturnOuterIndex(numAdditional, null, null);
    }

    final <P> int checkCapacityAndReturnOuterIndex(long numAdditional, P parameter, ArrayClearer<I, P> arrayClearer) {

        Checks.isLengthAboveZero(numAdditional);

        final int numOuterAllocatedInnerArrays = getNumOuterAllocatedInnerArrays();
        final int numOuterUtilizedEntries = getNumOuterUtilizedEntries();

        final long innerElementCapacity = getInnerElementCapacity();

        final int resultIndex;

        if (numOuterAllocatedInnerArrays == 0) {

            allocateInitialOuterAndInnerArrays(numAdditional, parameter, arrayClearer);

            resultIndex = 0;
        }
        else {
            final long lastInnerRemaining;

            if (numOuterUtilizedEntries == 0) {

                lastInnerRemaining = innerElementCapacity;
                resultIndex = 0;
            }
            else {
                final int lastOuterIndex = numOuterUtilizedEntries - 1;

                lastInnerRemaining = getRemainderOfLastInnerArray(lastOuterIndex);
                resultIndex = lastInnerRemaining != 0L ? lastOuterIndex : numOuterUtilizedEntries;
            }

            if (numAdditional > lastInnerRemaining) {

                final int numAdditionalOuter = computeNumAdditionalOuter(numAdditional, lastInnerRemaining, innerElementCapacity);

                final int totalOuterAllocatedInnerArrays = numOuterUtilizedEntries + numAdditionalOuter;

                if (totalOuterAllocatedInnerArrays > numOuterAllocatedInnerArrays) {

                    reallocateOuterAndAllocateInnerArrays(totalOuterAllocatedInnerArrays, numOuterAllocatedInnerArrays, parameter, arrayClearer);
                }
                else {
                    incrementNumOuterUtilizedEntries(numAdditionalOuter, numOuterAllocatedInnerArrays);
                }
            }
            else {
                if (numOuterUtilizedEntries == 0) {

                    incrementNumOuterUtilizedEntries();
                }
            }
        }

        return resultIndex;
    }

    final <P> O reallocateOuterAndAllocateInnerArrays(int newOuterCapacity, int startOuterIndex, P parameter, ArrayClearer<I, P> arrayClearer) {

        final int existingNumOuterAllocatedInnerArrays = numOuterAllocatedInnerArrays;
        Checks.isGreaterThan(newOuterCapacity, existingNumOuterAllocatedInnerArrays);

        final int existingNumOuterUtilizedEntries = getNumOuterUtilizedEntries();
        Checks.areEqual(startOuterIndex, existingNumOuterAllocatedInnerArrays);

        incrementNumOuterAllocatedInnerArrays(newOuterCapacity - existingNumOuterAllocatedInnerArrays);
        incrementNumOuterUtilizedEntries(newOuterCapacity - existingNumOuterUtilizedEntries, getNumOuterAllocatedInnerArrays());

        final O outerArray = reallocateOuterArrayAndInnerArrayNumElements(newOuterCapacity);

        final int numInnerToAllocate = newOuterCapacity - existingNumOuterAllocatedInnerArrays;

        allocateInnerArrays(outerArray, existingNumOuterAllocatedInnerArrays, numInnerToAllocate, parameter, arrayClearer);

        return outerArray;
    }

    private <P> void allocateInnerArrays(O outerArray, int startIndex, int numOuter, P parameter, ArrayClearer<I, P> arrayClearer) {

        for (int i = 0; i < numOuter; ++ i) {

            final int index = startIndex + i;

            final I a = createAndSetInnerArray(outerArray, index, innerNumAllocateElements);

            if (arrayClearer != null) {

                arrayClearer.clear(a, 0, innerCapacity, parameter);
            }
        }
    }

    final <INCREASE_LIMIT_PARAMETER, CLEAR_ARRAY_PARAMETER> int ensureCapacityAndLimit(long index, long limit, INCREASE_LIMIT_PARAMETER increaseLimitParameter,
            CLEAR_ARRAY_PARAMETER clearArrayParameter, ObjLongConsumer<INCREASE_LIMIT_PARAMETER> limitIncreaser, ArrayClearer<I, CLEAR_ARRAY_PARAMETER> arrayClearer) {

        Checks.isIndex(index);
        Checks.isArrayLimit(limit);
        Objects.requireNonNull(limitIncreaser);

        final long capacity = getCapacity();

        if (index >= limit) {

            if (index >= capacity) {

                checkCapacity(index - capacity + 1, clearArrayParameter, arrayClearer);
            }

            limitIncreaser.accept(increaseLimitParameter, index - limit + 1);
        }

        final int outerIndex = getOuterIndex(index);

        final int numOuterUtilizedEntries = getNumOuterUtilizedEntries();

        if (outerIndex >= numOuterUtilizedEntries) {

            incrementNumOuterUtilizedEntries(outerIndex - numOuterUtilizedEntries + 1, getNumOuterAllocatedInnerArrays());
        }

        return outerIndex;
    }

    final I createAndSetInnerArray(O outerArray, int outerIndex, int innerArrayLength) {

        if (getInnerArray(outerArray, outerIndex) != null) {

            throw new IllegalStateException();
        }

        return abstractCreateAndSetInnerArray(outerArray, outerIndex, innerArrayLength);
    }

    static int computeNumAdditionalOuter(long numAdditional, long lastInnerRemaining, long innerElementCapacity) {

        Checks.isGreaterThan(numAdditional, lastInnerRemaining);

        return computeNumOuter(numAdditional - lastInnerRemaining, innerElementCapacity);
    }

    private static int computeNumOuter(long numAdditional, long innerElementCapacity) {

        Checks.isLengthAboveZero(numAdditional);
        Checks.isLengthAboveZero(innerElementCapacity);

        return Integers.checkUnsignedLongToUnsignedInt(((numAdditional - 1) / innerElementCapacity) + 1);
    }

    final int getNumOuterAllocatedInnerArrays() {
        return numOuterAllocatedInnerArrays;
    }

    private void incrementNumOuterAllocatedInnerArrays() {

        ++ numOuterAllocatedInnerArrays;
    }

    final void incrementNumOuterAllocatedInnerArrays(int numAdditional) {

        Checks.isLengthAboveZero(numAdditional);

        this.numOuterAllocatedInnerArrays += numAdditional;
    }

    final int getInnerNumAllocateElements() {
        return innerNumAllocateElements;
    }
}
