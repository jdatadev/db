package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.capacity.Capacity;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.mutability.IMutable;

public abstract class IntCapacityObjectHeapMutableInstanceAllocator<

                INTERFACE_MUTABLE extends IMutable,
                CLASS_MUTABLE extends INTERFACE_MUTABLE,
                ELEMENTS,
                MUTABLE_FROM extends IMutableFrom>

        extends HeapMutableInstanceAllocator<INTERFACE_MUTABLE, MUTABLE_FROM> {

    protected abstract CLASS_MUTABLE allocateMutable(IntFunction<ELEMENTS> createElements, int minimumCapacity);

    private final IntFunction<ELEMENTS> createElements;

    protected IntCapacityObjectHeapMutableInstanceAllocator(IntFunction<ELEMENTS> createElements) {

        this.createElements = Objects.requireNonNull(createElements);
    }

    @Override
    public final INTERFACE_MUTABLE createMutable(long minimumCapacity) {

        checkIntMinimumCapacity(minimumCapacity);

        return allocateMutable(createElements, Capacity.intCapacityRenamed(minimumCapacity));
    }
}
