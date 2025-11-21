package dev.jdata.db.utils.adt.arrays;

import java.util.Objects;

import dev.jdata.db.DebugConstants;
import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

class LargeOneDimensionalArrayCapacityAlgorithm {

    private static final Boolean DEBUG = DebugConstants.DEBUG_LARGE_ONE_DIMENSIONAL_ARRAY_CAPACITY_ALGORITHM;

    private static final Class<?> debugClass = LargeOneDimensionalArrayCapacityAlgorithm.class;

    interface OneDimensionalArrayCapacityOperations<INSTANCE, INNER_ARRAY> {

        void allocateInitialOuterAndInnerArrays(INSTANCE instance, int numOuter, boolean clearInnerArrays);
        void reallocateOuterAndAllocateInnerArrays(INSTANCE instance, int newOuterCapacity, boolean clearInnerArrays);

        void increaseNumOuterAllocatedInnerArrays(INSTANCE instance, int numAdditional);
        void increaseNumOuterUtilizedEntries(INSTANCE instance, int numAdditional, int maxValue);
    }

    static <INSTANCE, INNER_ARRAY> int checkCapacityAndReturnOuterIndex(INSTANCE instance, long limit, long newLimit, int numOuterAllocatedInnerArrays,
            int numOuterUtilizedEntries, long innerElementCapacity, boolean clearInnerArrays,
            OneDimensionalArrayCapacityOperations<INSTANCE, INNER_ARRAY> arrayCapacityOperations) {

        Checks.isArrayLimit(limit);
        Checks.isArrayLimit(newLimit);
        Checks.isGreaterThan(newLimit, limit);
        Checks.isIntLengthAboveOrAtZero(numOuterAllocatedInnerArrays);
        Checks.isIntLengthAboveOrAtZero(numOuterUtilizedEntries);
        Checks.isIntOrLongInnerElementCapacity(innerElementCapacity);
        Objects.requireNonNull(arrayCapacityOperations);
        Checks.areEqual(numOuterUtilizedEntries, limit != 0L ? ((limit - 1) / innerElementCapacity) + 1 : 0);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("instance", instance).add("limit", limit).add("newLimit", newLimit)
                    .add("numOuterAllocatedInnerArrays", numOuterAllocatedInnerArrays).add("numOuterUtilizedEntries", numOuterUtilizedEntries)
                    .add("innerElementCapacity", innerElementCapacity).add("clearInnerArrays", clearInnerArrays)
                    .add("arrayCapacityOperations", arrayCapacityOperations));
        }

        final int resultIndex;

        final long numAdditionalElements = newLimit - limit;

        if (numOuterAllocatedInnerArrays == 0) {

            final int numInitialOuter = computeNumOuter(numAdditionalElements, innerElementCapacity);

            arrayCapacityOperations.allocateInitialOuterAndInnerArrays(instance, numInitialOuter, clearInnerArrays);

            arrayCapacityOperations.increaseNumOuterAllocatedInnerArrays(instance, numInitialOuter);

            arrayCapacityOperations.increaseNumOuterUtilizedEntries(instance, numInitialOuter, numInitialOuter);

            resultIndex = 0;
        }
        else if (numOuterUtilizedEntries == 0) {

            final int numOuter = computeNumOuter(numAdditionalElements, innerElementCapacity);

            final int maxOuter;

            if (numOuter > numOuterAllocatedInnerArrays) {

                arrayCapacityOperations.reallocateOuterAndAllocateInnerArrays(instance, numOuter, clearInnerArrays);

                arrayCapacityOperations.increaseNumOuterAllocatedInnerArrays(instance, numOuter - numOuterAllocatedInnerArrays);

                maxOuter = numOuter;
            }
            else {
                maxOuter = numOuterAllocatedInnerArrays;
            }

            arrayCapacityOperations.increaseNumOuterUtilizedEntries(instance, numOuter, maxOuter);

            resultIndex = 0;
        }
        else {
            final long lastInnerRemainingElements;

            final int lastOuterIndex = numOuterUtilizedEntries - 1;

            lastInnerRemainingElements = Capacity.getRemainderOfLastInnerArrayWithLimit(limit, innerElementCapacity, numOuterUtilizedEntries, lastOuterIndex);

            resultIndex = lastInnerRemainingElements != 0L ? lastOuterIndex : numOuterUtilizedEntries;

            if (DEBUG) {

                PrintDebug.debug(debugClass, "with allocated entries", b -> b.add("lastInnerRemainingElements", lastInnerRemainingElements).add("resultIndex", resultIndex));
            }

            if (numAdditionalElements > lastInnerRemainingElements) {

                final int numAdditionalOuter = computeNumAdditionalOuter(numAdditionalElements, lastInnerRemainingElements, innerElementCapacity);

                final int totalRequiredOuterAllocatedInnerArrays = numOuterUtilizedEntries + numAdditionalOuter;

                final int maxOuter;

                if (totalRequiredOuterAllocatedInnerArrays > numOuterAllocatedInnerArrays) {

                    arrayCapacityOperations.reallocateOuterAndAllocateInnerArrays(instance, totalRequiredOuterAllocatedInnerArrays, clearInnerArrays);

                    arrayCapacityOperations.increaseNumOuterAllocatedInnerArrays(instance, totalRequiredOuterAllocatedInnerArrays - numOuterAllocatedInnerArrays);

                    maxOuter = totalRequiredOuterAllocatedInnerArrays;
                }
                else {
                    maxOuter = numOuterAllocatedInnerArrays;
                }

                arrayCapacityOperations.increaseNumOuterUtilizedEntries(instance, numAdditionalOuter, maxOuter);
            }
        }

        if (DEBUG) {

            PrintDebug.exit(debugClass, resultIndex);
        }

        return resultIndex;
    }

    private static int computeNumAdditionalOuter(long numAdditional, long lastInnerRemaining, long innerElementCapacity) {

        Checks.isGreaterThan(numAdditional, lastInnerRemaining);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("numAdditional", numAdditional).add("lastInnerRemaining", lastInnerRemaining)
                    .add("innerElementCapacity", innerElementCapacity));
        }

        final int result = computeNumOuter(numAdditional - lastInnerRemaining, innerElementCapacity);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }

    private static int computeNumOuter(long numAdditional, long innerElementCapacity) {

        Checks.isIntOrLongLengthAboveZero(numAdditional);
        Checks.isIntOrLongInnerElementCapacity(innerElementCapacity);

        if (DEBUG) {

            PrintDebug.enter(debugClass, b -> b.add("numAdditional", numAdditional).add("innerElementCapacity", innerElementCapacity));
        }

        final int result = Integers.checkUnsignedLongToUnsignedInt(((numAdditional - 1) / innerElementCapacity) + 1);

        if (DEBUG) {

            PrintDebug.exit(debugClass, result);
        }

        return result;
    }
}
