package dev.jdata.db.utils.adt;

import java.util.Objects;

import dev.jdata.db.utils.adt.arrays.IAnyDimensionalArrayView;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.BiIntToObjectFunction;
import dev.jdata.db.utils.function.ObjIntFunction;
import dev.jdata.db.utils.scalars.Integers;

public class Capacity {

    private static final int DEFAULT_ARRAY_INITIAL_CAPACITY = 0;

    private static final int DEFAULT_INNER_CAPACITY_EXPONENT = CapacityExponents.DEFAULT_INNER_CAPACITY_EXPONENT;

    public static int intCapacityRenamed(long capacity) {

        Checks.isIntCapacity(capacity);

        return Integers.checkUnsignedLongToUnsignedInt(capacity);
    }

    public static <T extends IAnyDimensionalArrayView> T instantiateArray(AllocationType allocationType, ObjIntFunction<AllocationType, T> instantiator) {

        Objects.requireNonNull(instantiator);

        return instantiator.apply(allocationType, DEFAULT_ARRAY_INITIAL_CAPACITY);
    }

    private static <T> T instantiateOuterCapacityInnerExponent(BiIntToObjectFunction<T> instantiator) {

        Objects.requireNonNull(instantiator);

        return instantiator.apply(1, DEFAULT_INNER_CAPACITY_EXPONENT);
    }

    private static <T> T instantiateOuterCapacityInnerExponent(int capacity, BiIntToObjectFunction<T> instantiator) {

        return instantiateOuterCapacityInnerExponent((long)capacity, instantiator);
    }

    private static <T> T instantiateOuterCapacityInnerExponent(long capacity, BiIntToObjectFunction<T> instantiator) {

        return instantiateOuterCapacityInnerExponent(capacity, DEFAULT_INNER_CAPACITY_EXPONENT, instantiator);
    }

    private static <T> T instantiateOuterCapacityInnerExponent(long capacity, int innerCapacityExponent, BiIntToObjectFunction<T> instantiator) {

        Checks.isIntInitialCapacity(capacity);
        Checks.isIntCapacityExponent(innerCapacityExponent);
        Objects.requireNonNull(instantiator);

        final int initialOuterCapacity = computeArrayOuterCapacity(capacity, CapacityExponents.computeIntCapacityFromExponent(innerCapacityExponent));

        return instantiator.apply(initialOuterCapacity, innerCapacityExponent);
    }

    @FunctionalInterface
    public interface IOuterInnerInstantiator<T> {

        T instantiate(AllocationType allocationType,int outerCapacity, int innerCapacity);
    }

    public static <T> T instantiateOuterCapacityInnerExponent(AllocationType allocationType, IOuterInnerInstantiator<T> instantiator) {

        Objects.requireNonNull(instantiator);

        return instantiator.instantiate(allocationType, 1, DEFAULT_INNER_CAPACITY_EXPONENT);
    }

    public static <T> T instantiateOuterCapacityInnerExponent(AllocationType allocationType, int capacity, IOuterInnerInstantiator<T> instantiator) {

        return instantiateOuterCapacityInnerExponent(allocationType, (long)capacity, instantiator);
    }

    public static <T> T instantiateOuterCapacityInnerExponent(AllocationType allocationType, long capacity, IOuterInnerInstantiator<T> instantiator) {

        return instantiateOuterCapacityInnerExponent(allocationType, capacity, DEFAULT_INNER_CAPACITY_EXPONENT, instantiator);
    }

    private static <T> T instantiateOuterCapacityInnerExponent(AllocationType allocationType, long capacity, int innerCapacityExponent, IOuterInnerInstantiator<T> instantiator) {

        Objects.requireNonNull(allocationType);
        Checks.isIntInitialCapacity(capacity);
        Checks.isIntCapacityExponent(innerCapacityExponent);
        Objects.requireNonNull(instantiator);

        final int initialOuterCapacity = computeArrayOuterCapacity(capacity, CapacityExponents.computeIntCapacityFromExponent(innerCapacityExponent));

        return instantiator.instantiate(allocationType, initialOuterCapacity, innerCapacityExponent);
    }

    public static long getRemainderOfLastInnerArrayWithLimit(long limit, long innerElementCapacity, int numOuterUtilizedEntries, int expectedOuterIndex) {

        Checks.areEqual(expectedOuterIndex, numOuterUtilizedEntries != 0 ? numOuterUtilizedEntries - 1 : 0);
        Checks.isArrayLimit(limit);
        Checks.isIntOrLongCapacityAboveZero(innerElementCapacity);
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

        Checks.isLongCapacity(capacity);
        Checks.isInnerCapacity(innerCapacity);

        final long outerCapacity = capacity != 0L ? ((capacity - 1L) / innerCapacity) + 1L : 0L;

        return Integers.checkUnsignedLongToUnsignedInt(outerCapacity);
    }
}
