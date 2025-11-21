package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.LongFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.mutability.IMutable;

public abstract class LongCapacityMutableInstanceAllocator<

                INTERFACE_MUTABLE extends IMutable,
                CLASS_MUTABLE extends Allocatable,
                ELEMENTS,
                MUTABLE_FROM extends IMutableFrom>

        extends CapacityMutableInstanceAllocator<INTERFACE_MUTABLE, CLASS_MUTABLE, ELEMENTS, MUTABLE_FROM> {

    private static final CapacityMax CAPACITY_MAX = CapacityMax.LONG;

    protected static void checkAllocateMutableInstanceParameters(LongFunction<?> createElements, long minimumCapacity) {

        Objects.requireNonNull(createElements);
        checkMinimumCapacity(CAPACITY_MAX, minimumCapacity);
    }

    protected LongCapacityMutableInstanceAllocator(LongFunction<ELEMENTS> createElements, ToLongFunction<CLASS_MUTABLE> capacityGetter) {
        super(CAPACITY_MAX, createElements, capacityGetter);
    }

    @Override
    public final INTERFACE_MUTABLE copyToMutable(MUTABLE_FROM mutableFrom) {

        checkCopyToMutableParameters(mutableFrom);

        throw new UnsupportedOperationException();
    }
}
