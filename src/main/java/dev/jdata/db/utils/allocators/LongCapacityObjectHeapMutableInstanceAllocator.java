package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.LongFunction;

import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.checks.Checks;

public abstract class LongCapacityObjectHeapMutableInstanceAllocator<

                INTERFACE_MUTABLE extends IMutable,
                CLASS_MUTABLE extends INTERFACE_MUTABLE,
                ELEMENTS,
                MUTABLE_FROM extends IMutableFrom>

        extends LongCapacityHeapMutableInstanceAllocator<INTERFACE_MUTABLE, MUTABLE_FROM> {

    protected static <T> void checkAllocateMutableParameters(long minimumCapacity, LongFunction<T> createElements) {

        checkAllocateMutableParameters(minimumCapacity);
        Objects.requireNonNull(createElements);
    }

    protected static <T> void checkAllocateMutableWithoutCreateElementsParameters(long minimumCapacity, LongFunction<T> createElements) {

        checkAllocateMutableParameters(minimumCapacity);
        Checks.isNull(createElements);
    }

    protected abstract CLASS_MUTABLE allocateMutable(long minimumCapacity, LongFunction<ELEMENTS> createElements);

    private final LongFunction<ELEMENTS> createElements;

    protected LongCapacityObjectHeapMutableInstanceAllocator() {

        this.createElements = null;
    }

    private LongCapacityObjectHeapMutableInstanceAllocator(LongFunction<ELEMENTS> createElements) {

        this.createElements = Objects.requireNonNull(createElements);
    }

    @Override
    public final INTERFACE_MUTABLE createMutable(long minimumCapacity) {

        checkIntOrLongMinimumCapacity(minimumCapacity);

        return allocateMutable(minimumCapacity, createElements);
    }
}
