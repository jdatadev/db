package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.adt.capacity.Capacity;
import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.mutability.IMutable;

public abstract class IntCapacityMutableInstanceAllocator<INTERFACE_MUTABLE extends IMutable, CLASS_MUTABLE extends Allocatable, ELEMENTS, MUTABLE_FROM extends IMutableFrom>

        extends CapacityMutableInstanceAllocator<INTERFACE_MUTABLE, CLASS_MUTABLE, ELEMENTS, MUTABLE_FROM> {

    private static final CapacityMax CAPACITY_MAX = CapacityMax.INT;

    protected static void checkAllocateMutableInstanceParameters(IntFunction<?> createElements, int minimumCapacity) {

        Objects.requireNonNull(createElements);
        checkAllocateMutableParameters(CAPACITY_MAX, minimumCapacity);
    }

    protected abstract CLASS_MUTABLE allocateMutableInstance(IntFunction<ELEMENTS> createElements, int minimumCapacity);

    protected IntCapacityMutableInstanceAllocator(IntFunction<ELEMENTS> createElements, ToIntFunction<CLASS_MUTABLE> capacityGetter) {
        super(CAPACITY_MAX, c -> createElements.apply(Capacity.intCapacityRenamed(c)), capacityGetter::applyAsInt);
    }

    @Override
    protected final CLASS_MUTABLE allocateMutableInstance(LongFunction<ELEMENTS> createElements, long minimumCapacity) {

        checkAllocateMutableParameters(CAPACITY_MAX, minimumCapacity);

        return allocateMutableInstance(createElements, Capacity.intCapacityRenamed(minimumCapacity));
    }
}
