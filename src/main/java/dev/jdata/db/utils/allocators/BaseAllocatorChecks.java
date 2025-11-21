package dev.jdata.db.utils.allocators;

import java.util.Objects;

import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.mutability.IImmutable;
import dev.jdata.db.utils.builders.IBuilder;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseAllocatorChecks {

    protected static void checkAllocateImmutableFromParameters(Object values, CapacityMax capacityMax, long valuesLength, long startIndex, long numElements) {

        Objects.requireNonNull(values);
        Checks.isIntIndex(startIndex);
        Checks.isIntNumElements(numElements);
        Checks.isLessThan(startIndex, numElements);
    }

    protected static void checkFreeImmutableParameters(IImmutable immutable) {

        Objects.requireNonNull(immutable);
    }

    protected static void checkCreateMutableParameters(CapacityMax capacityMax, long minimumCapacity) {

        checkMinimumCapacity(capacityMax, minimumCapacity);
    }

    protected static void checkFreeCreatedMutableParameters(Object mutable) {

        Objects.requireNonNull(mutable);
    }

    protected static void checkAllocateMutableParameters(CapacityMax capacityMax, long minimumCapacity) {

        checkMinimumCapacity(capacityMax, minimumCapacity);
    }

    protected static void checkFreeAllocatedMutableParameters(Object mutable) {

        Objects.requireNonNull(mutable);
    }

    protected static void checkAllocateBuilderParameters(CapacityMax capacityMax, long minimumCapacity) {

        checkMinimumCapacity(capacityMax, minimumCapacity);
    }

    protected static void checkFreeBuilderParameters(IBuilder builder) {

        Objects.requireNonNull(builder);
    }

    static void checkIntMinimumCapacity(int minimumCapacity) {

        checkMinimumCapacity(CapacityMax.INT, minimumCapacity);
    }

    static void checkIntMinimumCapacity(long minimumCapacity) {

        checkMinimumCapacity(CapacityMax.INT, minimumCapacity);
    }

    static void checkLongMinimumCapacity(long minimumCapacity) {

        checkMinimumCapacity(CapacityMax.LONG, minimumCapacity);
    }

    static void checkIntOrLongMinimumCapacity(long minimumCapacity) {

        checkMinimumCapacity(CapacityMax.LONG, minimumCapacity);
    }

    static void checkMinimumCapacity(CapacityMax capacityMax, long minimumCapacity) {

        CapacityMax.checkMinimumCapacityAboveZero(capacityMax, minimumCapacity);
    }
}
