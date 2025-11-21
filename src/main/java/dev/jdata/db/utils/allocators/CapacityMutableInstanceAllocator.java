package dev.jdata.db.utils.allocators;

import java.util.Objects;
import java.util.function.LongFunction;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.adt.capacity.CapacityMax;
import dev.jdata.db.utils.adt.elements.IMutableFrom;
import dev.jdata.db.utils.adt.mutability.IMutable;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;

abstract class CapacityMutableInstanceAllocator<INTERFACE_MUTABLE extends IMutable, CLASS_MUTABLE extends Allocatable, ELEMENTS, MUTABLE_FROM extends IMutableFrom>

        extends MutableInstanceAllocator<INTERFACE_MUTABLE, CLASS_MUTABLE, MUTABLE_FROM>
        implements IAllocators {

    protected abstract CLASS_MUTABLE allocateMutableInstance(LongFunction<ELEMENTS> createElements, long minimumCapacity);

    private final LongFunction<ELEMENTS> createElements;
    private final CapacityInstanceAllocator<CLASS_MUTABLE> capacityInstanceAllocator;

    CapacityMutableInstanceAllocator(CapacityMax capacityMax, LongFunction<ELEMENTS> createElements, ToLongFunction<CLASS_MUTABLE> capacityGetter) {
        super(capacityMax);

        Objects.requireNonNull(capacityMax);
        Objects.requireNonNull(createElements);
        Objects.requireNonNull(capacityGetter);

        this.createElements = createElements;

        this.capacityInstanceAllocator = new CapacityInstanceAllocator<>(capacityMax, this, (i, c) -> i.allocateMutableFromSubClass(c), capacityGetter);
    }

    @Override
    public final CLASS_MUTABLE allocateMutableInstance(long minimumCapacity) {

        checkAllocateMutableParameters(getCapacityMax(), minimumCapacity);

        return capacityInstanceAllocator.allocateCapacityInstance(minimumCapacity);
    }

    @Override
    public final void freeMutableInstance(CLASS_MUTABLE mutableElements) {

        checkFreeCreatedMutableParameters(mutableElements);

        Objects.requireNonNull(mutableElements);

        capacityInstanceAllocator.freeCapacityInstance(mutableElements);
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("capacityInstanceAllocator", RefType.INSTANTIATED, CapacityMutableInstanceAllocator.class, capacityInstanceAllocator);
    }

    private CLASS_MUTABLE allocateMutableFromSubClass(long minimumCapacity) {

        return allocateMutableInstance(createElements, minimumCapacity);
    }
}
