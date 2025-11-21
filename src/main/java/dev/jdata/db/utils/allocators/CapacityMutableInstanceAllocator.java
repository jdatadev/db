package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.LongFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator.CapacityMax;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

abstract class CapacityMutableInstanceAllocator<T extends IMutable, U extends Allocatable, V>

        extends MutableInstanceAllocator<T, U> implements IMutableAllocator<T>, IAllocators {

    protected abstract U allocateMutableInstance(LongFunction<V> createElements, long minimumCapacity);

    private final CapacityMax capacityMax;
    private final LongFunction<V> createElements;
    private final CapacityInstanceAllocator<U> capacityInstanceAllocator;

    CapacityMutableInstanceAllocator(CapacityMax capacityMax, LongFunction<V> createElements, ToLongFunction<U> capacityGetter) {

        Objects.requireNonNull(capacityMax);
        Objects.requireNonNull(createElements);
        Objects.requireNonNull(capacityGetter);

        this.capacityMax = capacityMax;
        this.createElements = createElements;

        this.capacityInstanceAllocator = new CapacityInstanceAllocator<>(capacityMax, this, (i, c) -> i.allocateMutableFromSubClass(c), capacityGetter);
    }

    @Override
    public final U allocateMutableInstance(long minimumCapacity) {

        CapacityMax.checkLongMinimumCapacity(capacityMax, minimumCapacity);

        return capacityInstanceAllocator.allocateCapacityInstance(minimumCapacity);
    }

    @Override
    public final void freeMutableInstance(U mutableElements) {

        Objects.requireNonNull(mutableElements);

        capacityInstanceAllocator.freeCapacityInstance(mutableElements);
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("capacityInstanceAllocator", RefType.INSTANTIATED, CapacityMutableInstanceAllocator.class, capacityInstanceAllocator);
    }

    private U allocateMutableFromSubClass(long minimumCapacity) {

        CapacityMax.checkLongMinimumCapacity(capacityMax, minimumCapacity);

        return allocateMutableInstance(createElements, minimumCapacity);
    }
}
