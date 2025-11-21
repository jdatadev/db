package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.checks.Checks;

public abstract class IntCapacityObjectHeapMutableInstanceAllocator<T extends IMutable, U> extends HeapMutableInstanceAllocator<T> {

    protected abstract T allocateMutable(IntFunction<U> createElements, int minimumCapacity);

    private final IntFunction<U> createElements;

    protected IntCapacityObjectHeapMutableInstanceAllocator(IntFunction<U> createElements) {

        this.createElements = Objects.requireNonNull(createElements);
    }

    @Override
    public final T createMutable(long minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return allocateMutable(createElements, Capacity.intCapacityRenamed(minimumCapacity));
    }
}
