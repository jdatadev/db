package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.ObjLongConsumer;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.arrays.LargeOneDimensionalArrayCapacityAlgorithm.OneDimensionalArrayCapacityOperations;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

abstract class BaseOneDimensionalLargeArray<O, I> extends BaseAnyDimensionalLargeArray<O, I> implements IOneDimensionalArrayCommon {

    private static final boolean DEBUG = DebugConstants.DEBUG_BASE_LARGE_ONE_DIMENSIONAL_ARRAY;

    protected abstract int getOuterIndex(long index);
    protected abstract I abstractCreateAndSetInnerArray(O outerArray, int outerIndex, long innerArrayElementCapacity);

    private final long innerElementCapacity;
    private final long innerNumAllocateElements;

    private int numOuterAllocatedInnerArrays;

    BaseOneDimensionalLargeArray(AllocationType allocationType, int initialOuterCapacity, long innerElementsCapacity, int numInitialOuterAllocatedInnerArrays,
            boolean hasClearValue, IntFunction<O> createOuterArray, boolean requiresInnerArrayNumElements) {
        super(allocationType, initialOuterCapacity, hasClearValue, createOuterArray, requiresInnerArrayNumElements);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("initialOuterCapacity", initialOuterCapacity).add("innerElementsCapacity", innerElementsCapacity)
                    .add("numInitialOuterAllocatedInnerArrays", numInitialOuterAllocatedInnerArrays).add("createOuterArray", createOuterArray)
                    .add("requiresInnerArrayNumElements", requiresInnerArrayNumElements));
        }

        this.innerElementCapacity = Checks.isLongLengthAboveZero(innerElementsCapacity);

        this.innerNumAllocateElements = innerElementsCapacity;

        this.numOuterAllocatedInnerArrays = Checks.isIntLengthAboveOrAtZero(numInitialOuterAllocatedInnerArrays);

        if (DEBUG) {

            exit();
        }
    }

    BaseOneDimensionalLargeArray(AllocationType allocationType, BaseOneDimensionalLargeArray<O, I> toCopy, IOuterAndInnerArraysCopier<O> copyOuterAndInnerArrays) {
        super(allocationType, toCopy, copyOuterAndInnerArrays);

        if (DEBUG) {

            enter(b -> b.add("allocationType", allocationType).add("toCopy", toCopy).add("copyOuterAndInnerArrays", copyOuterAndInnerArrays));
        }

        this.innerElementCapacity = toCopy.innerElementCapacity;
        this.innerNumAllocateElements = toCopy.innerNumAllocateElements;

        this.numOuterAllocatedInnerArrays = toCopy.numOuterAllocatedInnerArrays;

        if (DEBUG) {

            exit();
        }
    }

    @Override
    final long getToStringLimit() {

        return getLimit();
    }

    private long getAllocatedInnerArrayElementsCapacity() {

        if (DEBUG) {

            enter();
        }

        final long result = numOuterAllocatedInnerArrays * getInnerElementCapacity();

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected final int getOuterArrayCapacity() {

        if (DEBUG) {

            enter();
        }

        final O outerArray = getOuterArray();

        final int result = outerArray != null ? getOuterArrayLength(outerArray) : 0;

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    @Override
    protected final long getInnerElementCapacity() {

        return innerElementCapacity;
    }

    private static final OneDimensionalArrayCapacityOperations<BaseOneDimensionalLargeArray<?, Object>, Object> arrayCapacityOperations
            = new OneDimensionalArrayCapacityOperations<BaseOneDimensionalLargeArray<?, Object>, Object>() {

                @Override
                public void allocateInitialOuterAndInnerArrays(BaseOneDimensionalLargeArray<?, Object> instance, int numOuter, boolean clearInnerArrays) {

                    instance.allocateInitialOuterAndInnerArrays(numOuter, clearInnerArrays);
                }

                @Override
                public void reallocateOuterAndAllocateInnerArrays(BaseOneDimensionalLargeArray<?, Object> instance, int newOuterCapacity, boolean clearInnerArrays) {

                    instance.reallocateOuterAndAllocateInnerArrays(newOuterCapacity, clearInnerArrays);
                }

                @Override
                public void increaseNumOuterAllocatedInnerArrays(BaseOneDimensionalLargeArray<?, Object> instance, int numAdditional) {

                    instance.increaseNumOuterAllocatedInnerArrays(numAdditional);
                }

                @Override
                public void increaseNumOuterUtilizedEntries(BaseOneDimensionalLargeArray<?, Object> instance, int numAdditional, int maxValue) {

                    instance.increaseNumOuterUtilizedEntries(numAdditional, maxValue);
                }
            };

    final I checkCapacityForOneAppendedElementAndReturnInnerArray(long limit) {

        Checks.isArrayLimit(limit);

        if (DEBUG) {

            enter(b -> b.add("limit", limit));
        }

        final int outerIndex = checkCapacityForOneAppendedElementAndReturnOuterIndex(limit);

        final I result = getInnerArray(outerIndex);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    protected final int checkCapacityForOneAppendedElementAndReturnOuterIndex(long limit) {

        Checks.isArrayLimit(limit);

        if (DEBUG) {

            enter(b -> b.add("limit", limit));
        }

        final int result = checkCapacityWithNewLimitAndReturnOuterIndex(limit, limit + 1);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private O allocateInitialOuterAndInnerArrays(int outerCapacity, boolean clearInnerArrays) {

        Checks.isOuterCapacity(outerCapacity);

        if (DEBUG) {

            enter(b -> b.add("outerCapacity", outerCapacity));
        }

        final O outerArray = allocateInitialOuterArrayAndInnerArrayElements(outerCapacity);

        allocateInnerArrays(outerArray, 0, outerCapacity, clearInnerArrays);

        if (DEBUG) {

            exit(outerArray);
        }

        return outerArray;
    }

    protected final I checkCapacityWithNewLimitAndReturnInnerArray(long limit, long newLimit, boolean clearInnerArrays) {

        if (DEBUG) {

            enter(b -> b.add("limit", limit).add("newLimit", newLimit).add("clearInnerArrays", clearInnerArrays));
        }

        final int outerIndex = checkCapacityWithNewLimitAndReturnOuterIndex(limit, newLimit, clearInnerArrays);

        final I result = getInnerArray(outerIndex);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    final <P> void checkCapacityWithNewLimit(long limit, long newLimit, boolean clearInnerArrays) {

        if (DEBUG) {

            enter(b -> b.add("limit", limit).add("newLimit", newLimit).add("clearInnerArrays", clearInnerArrays));
        }

        checkCapacityWithNewLimitAndReturnOuterIndex(limit, newLimit, clearInnerArrays);

        if (DEBUG) {

            exit();
        }
    }

    private <P> int checkCapacityWithNewLimitAndReturnOuterIndex(long limit, long newLimit) {

        if (DEBUG) {

            enter(b -> b.add("limit", limit).add("newLimit", newLimit));
        }

        final int result = checkCapacityWithNewLimitAndReturnOuterIndex(limit, newLimit, false);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    protected final int checkCapacityWithNewLimitAndReturnOuterIndex(long limit, long newLimit, boolean clearInnerArrays) {

        Checks.isArrayLimit(limit);
        Checks.isArrayLimit(newLimit);
        Checks.isGreaterThanOrEqualTo(newLimit, limit);

        if (DEBUG) {

            enter(b -> b.add("limit", limit).add("newLimit", newLimit));
        }

        final int result;

        final long allocatedInnerArrayElementsCapacity = getAllocatedInnerArrayElementsCapacity();

        final int numOuterUtilizedEntries = getNumOuterUtilizedEntries();

        if (newLimit < allocatedInnerArrayElementsCapacity) {

            final int outerIndex = computeOuterIndexFromLimit(limit);

            if (outerIndex >= numOuterUtilizedEntries) {

                increaseNumOuterUtilizedEntries(outerIndex - numOuterUtilizedEntries + 1, getNumOuterAllocatedInnerArrays());
            }

            result = outerIndex;
        }
        else {
            @SuppressWarnings("unchecked")
            final OneDimensionalArrayCapacityOperations<BaseOneDimensionalLargeArray<O, I>, I> capacityOperations
                    = (OneDimensionalArrayCapacityOperations<BaseOneDimensionalLargeArray<O, I>, I>)(OneDimensionalArrayCapacityOperations<?, ?>)arrayCapacityOperations;

            result = LargeOneDimensionalArrayCapacityAlgorithm.checkCapacityAndReturnOuterIndex(this, limit, newLimit, getNumOuterAllocatedInnerArrays(),
                    getNumOuterUtilizedEntries(), getInnerElementCapacity(), clearInnerArrays, capacityOperations);
        }

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    private O reallocateOuterAndAllocateInnerArrays(int newOuterCapacity, boolean clearInnerArrays) {

        final int existingNumOuterAllocatedInnerArrays = numOuterAllocatedInnerArrays;
        Checks.isGreaterThan(newOuterCapacity, existingNumOuterAllocatedInnerArrays);

        if (DEBUG) {

            enter(b -> b.add("newOuterCapacity", newOuterCapacity));
        }

        final O outerArray = reallocateOuterArrayAndInnerArrayNumElements(newOuterCapacity);

        final int numInnerToAllocate = newOuterCapacity - existingNumOuterAllocatedInnerArrays;

        allocateInnerArrays(outerArray, existingNumOuterAllocatedInnerArrays, numInnerToAllocate, clearInnerArrays);

        if (DEBUG) {

            exit(outerArray);
        }

        return outerArray;
    }

    private void allocateInnerArrays(O outerArray, int startIndex, int numOuter, boolean clear) {

        if (DEBUG) {

            enter(b -> b.add("outerArray", outerArray).add("startIndex", startIndex).add("numOuter", numOuter).add("clear", clear));
        }

        for (int i = 0; i < numOuter; ++ i) {

            final int index = startIndex + i;

            final long toAllocate = getInnerNumAllocateElements();

            final I a = createAndSetInnerArray(outerArray, index, toAllocate);

            if (clear) {

                clearInnerArray(a, 0, toAllocate);
            }
        }

        if (DEBUG) {

            exit();
        }
    }

    <INCREASE_LIMIT_PARAMETER> int ensureCapacityAndLimitAndReturnOuterIndex(long index, long limit, INCREASE_LIMIT_PARAMETER increaseLimitParameter,
            ObjLongConsumer<INCREASE_LIMIT_PARAMETER> limitIncreaser, boolean clearInnerArrays) {

        Checks.isLongIndex(index);
        Checks.isArrayLimit(limit);
        Objects.requireNonNull(limitIncreaser);

        if (DEBUG) {

            enter(b -> b.add("index", index).add("limit", limit).add("increaseLimitParameter", increaseLimitParameter).add("limitIncreaser", limitIncreaser)
                    .add("clearInnerArrays", clearInnerArrays));
        }

        final long allocatedInnerArrayElementsCapacity = getAllocatedInnerArrayElementsCapacity();

        if (index >= limit) {

            final long newLimit = index + 1;

            if (index >= allocatedInnerArrayElementsCapacity) {

                checkCapacityWithNewLimit(limit, newLimit, clearInnerArrays);
            }
            else {
                final int numOuter = getNumOuterUtilizedEntries();

                final long innerElementCapacity = getInnerElementCapacity();

                final int requiredNumOuter = Integers.checkUnsignedLongToUnsignedInt(newLimit / innerElementCapacity);

                if (requiredNumOuter > numOuter) {

                    setNumOuterUtilizedEntries(requiredNumOuter, numOuterAllocatedInnerArrays);
                }
            }

            limitIncreaser.accept(increaseLimitParameter, newLimit - limit);
        }

        final int outerIndex = getOuterIndex(index);

        if (DEBUG) {

            exit(outerIndex);
        }

        return outerIndex;
    }

    private I createAndSetInnerArray(O outerArray, int outerIndex, long innerArrayElementCapacity) {

        if (DEBUG) {

            enter(b -> b.add("outerArray", outerArray).add("outerIndex", outerIndex).add("innerArrayElementCapacity", innerArrayElementCapacity));
        }

        if (getInnerArray(outerArray, outerIndex) != null) {

            throw new IllegalStateException();
        }

        final I result = abstractCreateAndSetInnerArray(outerArray, outerIndex, innerArrayElementCapacity);

        if (DEBUG) {

            exit(result);
        }

        return result;
    }

    int getNumOuterAllocatedInnerArrays() {
        return numOuterAllocatedInnerArrays;
    }

    private void increaseNumOuterAllocatedInnerArrays(int numAdditional) {

        if (DEBUG) {

            enter(b -> b.add("numAdditional", numAdditional));
        }

        this.numOuterAllocatedInnerArrays += numAdditional;

        if (DEBUG) {

            exit(numOuterAllocatedInnerArrays);
        }
    }

    final long getInnerNumAllocateElements() {
        return innerNumAllocateElements;
    }

    private int computeOuterIndexFromLimit(long limit) {

        return getOuterIndex(limit);
    }
}
