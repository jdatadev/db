package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Supplier;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator.CapacityMax;
import dev.jdata.db.utils.allocators.CapacityInstanceAllocator;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.allocators.IAllocators.IAllocatorsStatisticsGatherer.RefType;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.function.ObjLongFunction;

abstract class BaseCachedElementsAllocators<

                INTERFACE_IMMUTABLE extends IElements,
                CLASS_IMMUTABLE extends BaseADTElements<?, ?, ALLOCATE_FROM_ARRAY> & IElements,
                CLASS_MUTABLE extends Allocatable & IMutableElements,
                INTERFACE_BUILDER extends IElementsBuilder<INTERFACE_IMMUTABLE, ?>,
                CLASS_BUILDER extends Allocatable & IElementsBuilder<INTERFACE_IMMUTABLE, ?>,
                ALLOCATE_FROM_ARRAY>

        extends ElementsAllocators<INTERFACE_IMMUTABLE, ALLOCATE_FROM_ARRAY>
        implements IElementsAllocators<INTERFACE_IMMUTABLE, CLASS_MUTABLE, INTERFACE_BUILDER, ALLOCATE_FROM_ARRAY>, IAllocators {

    private final CapacityMax capacityMax;
    private final Class<CLASS_IMMUTABLE> immutableClass;
    private final Class<CLASS_MUTABLE> mutableClass;
    private final Class<CLASS_BUILDER> builderClass;

    private final CapacityInstanceAllocator<CLASS_IMMUTABLE> immutableAllocator;
    private final CapacityInstanceAllocator<CLASS_MUTABLE> mutableAllocator;
    private final CapacityInstanceAllocator<CLASS_BUILDER> builderAllocator;

    BaseCachedElementsAllocators(AllocationType allocationType, CapacityMax capacityMax,
            Class<INTERFACE_IMMUTABLE> immutableInterface, Class<CLASS_IMMUTABLE> immutableClass,
            ObjLongFunction<AllocationType, CLASS_IMMUTABLE> createImmutable, ToLongFunction<CLASS_IMMUTABLE> immutableCapacityGetter,
            Supplier<INTERFACE_IMMUTABLE> emptyImmutableSupplier,
            Class<CLASS_MUTABLE> mutableClass, ObjLongFunction<AllocationType, CLASS_MUTABLE> createMutable, ToLongFunction<CLASS_MUTABLE> mutableCapacityGetter,
            Class<INTERFACE_BUILDER> builderInterface, Class<CLASS_BUILDER> builderClass,
            ObjLongFunction<AllocationType, CLASS_BUILDER> createBuilder, ToLongFunction<CLASS_BUILDER> builderCapacityGetter) {
        super(emptyImmutableSupplier);

        this.capacityMax = Objects.requireNonNull(capacityMax);
        this.immutableClass = Objects.requireNonNull(immutableClass);
        this.mutableClass = Objects.requireNonNull(mutableClass);
        this.builderClass = Objects.requireNonNull(builderClass);

        this.immutableAllocator = new CapacityInstanceAllocator<>(capacityMax, allocationType, createImmutable, immutableCapacityGetter::applyAsLong);
        this.mutableAllocator = new CapacityInstanceAllocator<>(capacityMax, allocationType, createMutable, mutableCapacityGetter::applyAsLong);
        this.builderAllocator = new CapacityInstanceAllocator<>(capacityMax, allocationType, createBuilder, builderCapacityGetter::applyAsLong);
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

        Objects.requireNonNull(statisticsGatherer);

        statisticsGatherer.addInstanceAllocator("immutableAllocator", RefType.INSTANTIATED, immutableClass, immutableAllocator);
        statisticsGatherer.addInstanceAllocator("mutableAllocator", RefType.INSTANTIATED, mutableClass, mutableAllocator);
        statisticsGatherer.addInstanceAllocator("builderAllocator", RefType.INSTANTIATED, builderClass, builderAllocator);
    }

    @Override
    public final INTERFACE_IMMUTABLE allocateImmutableFrom(ALLOCATE_FROM_ARRAY values, long valuesLength, long startIndex, long numElements) {

        Objects.requireNonNull(values);
        Checks.checkLongIndexAndNumElements(startIndex, numElements);

        final CLASS_IMMUTABLE classImmutable = immutableAllocator.allocateCapacityInstance(numElements);

        classImmutable.initialize(values, valuesLength, startIndex, numElements);

        @SuppressWarnings("unchecked")
        final INTERFACE_IMMUTABLE result = (INTERFACE_IMMUTABLE)classImmutable;

        return result;
    }

    @Override
    public final void freeImmutable(INTERFACE_IMMUTABLE immutable) {

        Objects.requireNonNull(immutable);

        @SuppressWarnings("unchecked")
        final CLASS_IMMUTABLE classImmutable = (CLASS_IMMUTABLE)immutable;

        immutableAllocator.freeCapacityInstance(classImmutable);
    }

    @Override
    public final CLASS_MUTABLE allocateMutable(long minimumCapacity) {

        CapacityMax.checkLongMinimumCapacity(capacityMax, minimumCapacity);

        return mutableAllocator.allocateCapacityInstance(minimumCapacity);
    }

    @Override
    public final void freeMutable(CLASS_MUTABLE mutable) {

        Objects.requireNonNull(mutable);

        mutableAllocator.freeCapacityInstance(mutable);
    }

    @SuppressWarnings("unchecked")
    @Override
    public final INTERFACE_BUILDER allocateBuilder(long minimumCapacity) {

        CapacityMax.checkLongMinimumCapacity(capacityMax, minimumCapacity);

        return (INTERFACE_BUILDER)builderAllocator.allocateCapacityInstance(minimumCapacity);
    }

    @Override
    public final void freeBuilder(INTERFACE_BUILDER builder) {

        Objects.requireNonNull(builder);

        @SuppressWarnings("unchecked")
        final CLASS_BUILDER classBuilder = (CLASS_BUILDER)builder;

        builderAllocator.freeCapacityInstance(classBuilder);
    }
}
