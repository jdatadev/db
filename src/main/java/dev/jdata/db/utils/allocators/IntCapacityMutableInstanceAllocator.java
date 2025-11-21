package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.ToIntFunction;

import dev.jdata.db.utils.adt.Capacity;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator.CapacityMax;

public abstract class IntCapacityMutableInstanceAllocator<T extends IMutable, U extends Allocatable, V> extends CapacityMutableInstanceAllocator<T, U, V> {

    protected abstract U allocateMutableInstance(IntFunction<V> createElements, int minimumCapacity);

    private static final CapacityMax CAPACITY_MAX = CapacityMax.INT;

    private final IntFunction<V> createElements;

    protected IntCapacityMutableInstanceAllocator(IntFunction<V> createElements, ToIntFunction<U> capacityGetter) {
        super(CAPACITY_MAX, c -> createElements.apply(Capacity.intCapacityRenamed(c)), capacityGetter::applyAsInt);

        this.createElements = Objects.requireNonNull(createElements);
    }

    @Override
    protected final U allocateMutableInstance(LongFunction<V> createElements, long minimumCapacity) {

        Objects.requireNonNull(createElements);
        CapacityMax.checkLongMinimumCapacity(CAPACITY_MAX, minimumCapacity);

        return allocateMutableInstance(this.createElements, Capacity.intCapacityRenamed(minimumCapacity));
    }
}
