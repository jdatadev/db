package dev.jdata.db.utils.adt;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.arrays.IArrayView;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;
import dev.jdata.db.utils.scalars.Integers;

public class Capacity {

    private static final int DEFAULT_ARRAY_INITIAL_CAPACITY = 0;

    private static final int DEFAULT_INNER_CAPACITY_EXPONENT = CapacityExponents.DEFAULT_INNER_CAPACITY_EXPONENT;

    public static int intCapacity(long capacity) {

        Checks.isCapacity(capacity);

        return Integers.checkUnsignedLongToUnsignedInt(capacity);
    }

    public static <T extends IArrayView> T instantiateArray(IntFunction<T> instantiator) {

        Objects.requireNonNull(instantiator);

        return instantiator.apply(DEFAULT_ARRAY_INITIAL_CAPACITY);
    }

    public static <T> T instantiateOuterCapacityInnerExponent(BiIntToObjectFunction<T> instantiator) {

        Objects.requireNonNull(instantiator);

        return instantiator.apply(1, DEFAULT_INNER_CAPACITY_EXPONENT);
    }

    public static <T> T instantiateOuterCapacityInnerExponent(int capacity, BiIntToObjectFunction<T> instantiator) {

        return instantiateOuterCapacityInnerExponent((long)capacity, instantiator);
    }

    public static <T> T instantiateOuterCapacityInnerExponent(long capacity, BiIntToObjectFunction<T> instantiator) {

        return instantiateOuterCapacityInnerExponent(capacity, DEFAULT_INNER_CAPACITY_EXPONENT, instantiator);
    }

    private static <T> T instantiateOuterCapacityInnerExponent(long capacity, int innerCapacityExponent, BiIntToObjectFunction<T> instantiator) {

        Checks.isInitialCapacity(capacity);
        Checks.isIntCapacityExponent(innerCapacityExponent);
        Objects.requireNonNull(instantiator);

        final int initialOuterCapacity = computeArrayOuterCapacity(capacity, CapacityExponents.computeIntCapacityFromExponent(innerCapacityExponent));

        return instantiator.apply(initialOuterCapacity, innerCapacityExponent);
    }

    public static long getRemainderOfLastInnerArrayWithLimit(long limit, long innerElementCapacity, int numOuterUtilizedEntries, int expectedOuterIndex) {

        Checks.areEqual(expectedOuterIndex, numOuterUtilizedEntries != 0 ? numOuterUtilizedEntries - 1 : 0);
        Checks.isArrayLimit(limit);
        Checks.isCapacityAboveZero(innerElementCapacity);
        Checks.areEqual(expectedOuterIndex, limit != 0L ? (limit - 1) / innerElementCapacity : 0);

        final long result;

        if (numOuterUtilizedEntries == 0) {

            result = 0L;
        }
        else if (limit == 0L) {

            result = innerElementCapacity;
        }
        else {
            final long numInnerUtilizedElements = limit % innerElementCapacity;

            result = numInnerUtilizedElements != 0L ? innerElementCapacity - numInnerUtilizedElements : 0L;
        }

        return result;
    }

    @Deprecated // currently in use?
    static long getRemainderOfLastInnerArray(long numInnerUtilizedElements, long innerElementCapacity, int numOuterUtilizedEntries, int expectedOuterIndex) {

        final long result;

        if (numOuterUtilizedEntries == 0L) {

            Checks.isExactlyZero(expectedOuterIndex);

            result = 0L;
        }
        else {
            Checks.areEqual(expectedOuterIndex, numOuterUtilizedEntries - 1);

            result = innerElementCapacity - numInnerUtilizedElements;
        }

        return result;
    }

    static int computeArrayOuterCapacity(int capacity, int innerCapacity) {

        return computeArrayOuterCapacity((long)capacity, innerCapacity);
    }

    static int computeArrayOuterCapacity(long capacity, int innerCapacity) {

        Checks.isCapacity(capacity);
        Checks.isCapacityAboveZero(innerCapacity);

        final long outerCapacity = capacity != 0L ? ((capacity - 1L) / innerCapacity) + 1L : 0L;

        return Integers.checkUnsignedLongToUnsignedInt(outerCapacity);
    }
}
