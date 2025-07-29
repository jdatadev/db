package dev.jdata.db.utils.adt.arrays;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.mockito.Mockito;

import dev.jdata.db.test.unit.BaseTest;
import dev.jdata.db.utils.adt.arrays.LargeOneDimensionalArrayCapacityAlgorithm.OneDimensionalArrayCapacityOperations;
import dev.jdata.db.utils.checks.Assertions;
import dev.jdata.db.utils.debug.PrintDebug;
import dev.jdata.db.utils.scalars.Integers;

public final class LargeOneDimensionalArrayCapacityAlgorithmTest extends BaseTest implements PrintDebug {

    private static final boolean DEBUG = false;

    private static final class Parameters {

        private final OneDimensionalArrayCapacityOperations<Object, int[]> arrayCapacityOperations;

        Parameters() {

            @SuppressWarnings("unchecked")
            final OneDimensionalArrayCapacityOperations<Object, int[]> arrayCapacityOperations = Mockito.mock(OneDimensionalArrayCapacityOperations.class);

            this.arrayCapacityOperations = arrayCapacityOperations;
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckCapacityArguments() {

        final Parameters parameters = new Parameters();

        assertThatThrownBy(() -> checkCapacityAndReturnOuterIndex(this, 0L, 0, 0, 0, 1L, false, parameters)).isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> checkCapacityAndReturnOuterIndex(this, 0L, 1L, 0, 0, 0L, false, parameters)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckCapacityInitialOuterAndInnerArrays() {

        final int numOuterAlreadyAllocated = 0;
        final long limit = 0L;

        for (long innerElementCapacity = 1L; innerElementCapacity < 10; ++ innerElementCapacity) {

            final long numAllocatedElements = numOuterAlreadyAllocated * innerElementCapacity;

            final long limitAdditionUpToAllocated = numAllocatedElements - limit;

            final long maxAddition = limitAdditionUpToAllocated + (innerElementCapacity * 3);

            for (long limitAddition = limitAdditionUpToAllocated + 1; limitAddition <= maxAddition; ++ limitAddition) {

                final Parameters parameters = new Parameters();

                final long newLimit = limit + limitAddition;

                final int limitNumOuter = getNumOuterFromLimit(limit, innerElementCapacity);
                final int newLimitNumOuter = getNumOuterFromLimit(newLimit, innerElementCapacity);

                final int expectedAdditionalOuterUtilizedEntries = newLimitNumOuter - limitNumOuter;
                final int expectedAdditionalOuterAllocated = newLimitNumOuter - numOuterAlreadyAllocated;

                if (DEBUG) {

                    final long closureNumOuterAlreadyAllocated = numOuterAlreadyAllocated;
                    final long closureInnerElementCapacity = innerElementCapacity;
                    final long closureLimit = limit;
                    final long closureLimitAddition = limitAddition;

                    debug("check capacity", b -> b.add("limit", closureLimit).add("limitAddition", closureLimitAddition)
                            .add("numOuterAlreadyAllocated", closureNumOuterAlreadyAllocated).add("innerElementCapacity", closureInnerElementCapacity)
                            .add("newLimit", newLimit).add("limitNumOuter", limitNumOuter).add("newLimitNumOuter", newLimitNumOuter));
                }

                final boolean clearInnerArrays = false;

                assertThat(checkCapacityAndReturnOuterIndex(this, limit, newLimit, numOuterAlreadyAllocated, limitNumOuter, innerElementCapacity, clearInnerArrays, parameters))
                        .isEqualTo(getOuterIndexFromIndex(limit, innerElementCapacity));

                verifyAllocateInitialOuterAndInnerArrays(parameters, newLimitNumOuter, clearInnerArrays);
                verifyIncreaseNumAllocatedInnerArrays(parameters, expectedAdditionalOuterAllocated);
                verifyIncreaseNumOuterUtilizedEntries(parameters, expectedAdditionalOuterUtilizedEntries, newLimitNumOuter);

                verifyNoMoreInteractions(parameters);
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckCapacityIncreaseNumOuterUtilizedArrayAlreadyAllocated() {

        for (int numOuterAlreadyAllocated = 1; numOuterAlreadyAllocated < 10; ++ numOuterAlreadyAllocated) {

            for (long innerElementCapacity = 1L; innerElementCapacity < 10; ++ innerElementCapacity) {

                final long numAllocatedElements = numOuterAlreadyAllocated * innerElementCapacity;

                for (long limit = 0L; limit < numAllocatedElements; ++ limit) {

                    for (long limitAddition = numAllocatedElements - limit; limitAddition > 0L; -- limitAddition) {

                        final Parameters parameters = new Parameters();

                        final long newLimit = limit + limitAddition;

                        final int limitNumOuter = getNumOuterFromLimit(limit, innerElementCapacity);
                        final int newLimitNumOuter = getNumOuterFromLimit(newLimit, innerElementCapacity);

                        final int expectedAdditionalOuterUtilizedEntries = newLimitNumOuter - limitNumOuter;

                        if (DEBUG) {

                            final long closureNumOuterAlreadyAllocated = numOuterAlreadyAllocated;
                            final long closureInnerElementCapacity = innerElementCapacity;
                            final long closureLimit = limit;
                            final long closureLimitAddition = limitAddition;

                            debug("check capacity", b -> b.add("limit", closureLimit).add("limitAddition", closureLimitAddition)
                                    .add("numOuterAlreadyAllocated", closureNumOuterAlreadyAllocated).add("innerElementCapacity", closureInnerElementCapacity)
                                    .add("newLimit", newLimit).add("limitNumOuter", limitNumOuter).add("newLimitNumOuter", newLimitNumOuter)
                                    );
                        }

                        assertThat(checkCapacityAndReturnOuterIndex(this, limit, newLimit, numOuterAlreadyAllocated, limitNumOuter, innerElementCapacity, false, parameters))
                                .isEqualTo(getOuterIndexFromIndex(limit, innerElementCapacity));

                        if (expectedAdditionalOuterUtilizedEntries != 0) {

                            verifyIncreaseNumOuterUtilizedEntries(parameters, expectedAdditionalOuterUtilizedEntries, numOuterAlreadyAllocated);
                        }

                        verifyNoMoreInteractions(parameters);
                    }
                }
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckCapacityIncreaseNumOuterUtilizedInsufficientArrayAllocated() {

        for (int numOuterAlreadyAllocated = 1; numOuterAlreadyAllocated < 10; ++ numOuterAlreadyAllocated) {

            for (long innerElementCapacity = 1L; innerElementCapacity < 10; ++ innerElementCapacity) {

                final long numAllocatedElements = numOuterAlreadyAllocated * innerElementCapacity;

                for (long limit = 1L; limit <= numAllocatedElements; ++ limit) {

                    final long limitAdditionUpToAllocated = numAllocatedElements - limit;

                    final long maxAddition = limitAdditionUpToAllocated + (innerElementCapacity * 3);

                    for (long limitAddition = limitAdditionUpToAllocated + 1; limitAddition <= maxAddition; ++ limitAddition) {

                        final Parameters parameters = new Parameters();

                        final long newLimit = limit + limitAddition;

                        final int limitNumOuter = getNumOuterFromLimit(limit, innerElementCapacity);
                        final int newLimitNumOuter = getNumOuterFromLimit(newLimit, innerElementCapacity);

                        final int expectedAdditionalOuterUtilizedEntries = newLimitNumOuter - limitNumOuter;
                        final int expectedAdditionalOuterAllocated = newLimitNumOuter - numOuterAlreadyAllocated;

                        if (DEBUG) {

                            final long closureNumOuterAlreadyAllocated = numOuterAlreadyAllocated;
                            final long closureInnerElementCapacity = innerElementCapacity;
                            final long closureLimit = limit;
                            final long closureLimitAddition = limitAddition;

                            debug("check capacity", b -> b.add("limit", closureLimit).add("limitAddition", closureLimitAddition)
                                    .add("numOuterAlreadyAllocated", closureNumOuterAlreadyAllocated).add("innerElementCapacity", closureInnerElementCapacity)
                                    .add("newLimit", newLimit).add("limitNumOuter", limitNumOuter).add("newLimitNumOuter", newLimitNumOuter));
                        }

                        final boolean clearInnerArrays = false;

                        assertThat(checkCapacityAndReturnOuterIndex(this, limit, newLimit, numOuterAlreadyAllocated, limitNumOuter, innerElementCapacity, clearInnerArrays,
                                parameters))
                                .isEqualTo(getOuterIndexFromIndex(limit, innerElementCapacity));

                        verifyReallocateOuterAndAllocateInnerArrays(parameters, newLimitNumOuter, clearInnerArrays);
                        verifyIncreaseNumAllocatedInnerArrays(parameters, expectedAdditionalOuterAllocated);
                        verifyIncreaseNumOuterUtilizedEntries(parameters, expectedAdditionalOuterUtilizedEntries, newLimitNumOuter);

                        verifyNoMoreInteractions(parameters);
                    }
                }
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testCheckCapacityNumOuterUtilizedZeroArrayAlreadyAllocated() {

        final long limit = 0L;

        for (int numOuterAlreadyAllocated = 1; numOuterAlreadyAllocated < 10; ++ numOuterAlreadyAllocated) {

            for (long innerElementCapacity = 1L; innerElementCapacity < 10; ++ innerElementCapacity) {

                final long numAllocatedElements = numOuterAlreadyAllocated * innerElementCapacity;

                for (long limitAddition = numAllocatedElements - limit; limitAddition > 0L; -- limitAddition) {

                    final Parameters parameters = new Parameters();

                    final long newLimit = limit + limitAddition;

                    final int limitNumOuter = getNumOuterFromLimit(limit, innerElementCapacity);
                    final int newLimitNumOuter = getNumOuterFromLimit(newLimit, innerElementCapacity);

                    final int expectedAdditionalOuterUtilizedEntries = newLimitNumOuter - limitNumOuter;

                    if (DEBUG) {

                        final long closureNumOuterAlreadyAllocated = numOuterAlreadyAllocated;
                        final long closureInnerElementCapacity = innerElementCapacity;
                        final long closureLimit = limit;
                        final long closureLimitAddition = limitAddition;

                        debug("check capacity", b -> b.add("limit", closureLimit).add("limitAddition", closureLimitAddition)
                                .add("numOuterAlreadyAllocated", closureNumOuterAlreadyAllocated).add("innerElementCapacity", closureInnerElementCapacity)
                                .add("newLimit", newLimit).add("limitNumOuter", limitNumOuter).add("newLimitNumOuter", newLimitNumOuter)
                                );
                    }

                    assertThat(checkCapacityAndReturnOuterIndex(this, limit, newLimit, numOuterAlreadyAllocated, limitNumOuter, innerElementCapacity, false, parameters))
                            .isEqualTo(getOuterIndexFromIndex(limit, innerElementCapacity));

                    if (expectedAdditionalOuterUtilizedEntries != 0) {

                        verifyIncreaseNumOuterUtilizedEntries(parameters, expectedAdditionalOuterUtilizedEntries, numOuterAlreadyAllocated);
                    }

                    verifyNoMoreInteractions(parameters);
                }
            }
        }
    }

    @Test
    @Category(UnitTest.class)
    public void testNumOuterUtilizedZeroCapacityInsufficientArrayAllocated() {

        final long limit = 0L;

        for (int numOuterAlreadyAllocated = 1; numOuterAlreadyAllocated < 10; ++ numOuterAlreadyAllocated) {

            for (long innerElementCapacity = 1L; innerElementCapacity < 10; ++ innerElementCapacity) {

                final long numAllocatedElements = numOuterAlreadyAllocated * innerElementCapacity;

                final long limitAdditionUpToAllocated = numAllocatedElements - limit;

                final long maxAddition = limitAdditionUpToAllocated + (innerElementCapacity * 3);

                for (long limitAddition = numAllocatedElements + 1; limitAddition <= maxAddition; ++ limitAddition) {

                    final Parameters parameters = new Parameters();

                    final long newLimit = limit + limitAddition;

                    final int limitNumOuter = getNumOuterFromLimit(limit, innerElementCapacity);
                    final int newLimitNumOuter = getNumOuterFromLimit(newLimit, innerElementCapacity);

                    final int expectedAdditionalOuterUtilizedEntries = newLimitNumOuter - limitNumOuter;
                    final int expectedAdditionalOuterAllocated = newLimitNumOuter - numOuterAlreadyAllocated;

                    if (DEBUG) {

                        final long closureNumOuterAlreadyAllocated = numOuterAlreadyAllocated;
                        final long closureInnerElementCapacity = innerElementCapacity;
                        final long closureLimit = limit;
                        final long closureLimitAddition = limitAddition;

                        debug("check capacity", b -> b.add("limit", closureLimit).add("limitAddition", closureLimitAddition)
                                .add("numOuterAlreadyAllocated", closureNumOuterAlreadyAllocated).add("innerElementCapacity", closureInnerElementCapacity)
                                .add("newLimit", newLimit).add("limitNumOuter", limitNumOuter).add("newLimitNumOuter", newLimitNumOuter));
                    }

                    final boolean clearInnerArrays = false;

                    assertThat(checkCapacityAndReturnOuterIndex(this, limit, newLimit, numOuterAlreadyAllocated, limitNumOuter, innerElementCapacity, clearInnerArrays,
                            parameters))
                            .isEqualTo(getOuterIndexFromIndex(limit, innerElementCapacity));

                    Assertions.isAboveZero(expectedAdditionalOuterUtilizedEntries);

                    verifyReallocateOuterAndAllocateInnerArrays(parameters, newLimitNumOuter, clearInnerArrays);
                    verifyIncreaseNumAllocatedInnerArrays(parameters, expectedAdditionalOuterAllocated);
                    verifyIncreaseNumOuterUtilizedEntries(parameters, expectedAdditionalOuterUtilizedEntries, newLimitNumOuter);

                    verifyNoMoreInteractions(parameters);
                }
            }
        }
    }

    private void verifyAllocateInitialOuterAndInnerArrays(Parameters parameters, int expectedNumOuter, boolean expectedCleanInnerArrays) {

        Mockito.verify(parameters.arrayCapacityOperations).allocateInitialOuterAndInnerArrays(same(this), eq(expectedNumOuter), eq(expectedCleanInnerArrays));
    }

    private void verifyReallocateOuterAndAllocateInnerArrays(Parameters parameters, int newOuterCapacity, boolean clearInnerArrays) {

        Mockito.verify(parameters.arrayCapacityOperations).reallocateOuterAndAllocateInnerArrays(same(this),  eq(newOuterCapacity), eq(clearInnerArrays));
    }

    private void verifyIncreaseNumOuterUtilizedEntries(Parameters parameters, int expectedNumAdditionalOuter, int expectedMaxValue) {

        Mockito.verify(parameters.arrayCapacityOperations).increaseNumOuterUtilizedEntries(same(this), eq(expectedNumAdditionalOuter), eq(expectedMaxValue));
    }

    private void verifyIncreaseNumAllocatedInnerArrays(Parameters parameters, int expectedNumAdditionalOuter) {

        Mockito.verify(parameters.arrayCapacityOperations).increaseNumOuterAllocatedInnerArrays(same(this), eq(expectedNumAdditionalOuter));
    }

    private static void verifyNoMoreInteractions(Parameters parameters) {

        Mockito.verifyNoMoreInteractions(parameters.arrayCapacityOperations);
    }

    private <INSTANCE, INNER_ARRAY, P> int checkCapacityAndReturnOuterIndex(INSTANCE instance, long limit, long newLimit, int numOuterAllocatedInnerArrays,
            int numOuterUtilizedEntries, long innerElementCapacity, boolean clearInnerArrays, Parameters parameters) {

        return LargeOneDimensionalArrayCapacityAlgorithm.checkCapacityAndReturnOuterIndex(instance, limit, newLimit, numOuterAllocatedInnerArrays, numOuterUtilizedEntries,
                innerElementCapacity, clearInnerArrays, parameters.arrayCapacityOperations);
    }

    private static int getOuterIndexFromIndex(long index, long innerElementCapacity) {

        return Integers.checkUnsignedLongToUnsignedInt(index / innerElementCapacity);
    }

    private static int getNumOuterFromLimit(long limit, long innerElementCapacity) {

        return limit != 0 ? Integers.checkUnsignedLongToUnsignedInt((limit - 1) / innerElementCapacity) + 1 : 0;
    }
}
