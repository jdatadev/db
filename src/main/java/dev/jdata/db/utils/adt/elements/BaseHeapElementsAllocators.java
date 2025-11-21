package dev.jdata.db.utils.adt.elements;

import java.util.Objects;
import java.util.function.Supplier;

import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.checks.Checks;

abstract class BaseHeapElementsAllocators<

                INTERFACE_IMMUTABLE extends IElements,
                CLASS_MUTABLE extends Allocatable & IMutableElements,
                INTERFACE_BUILDER extends IElementsBuilder<INTERFACE_IMMUTABLE, ?>,
                ALLOCATE_FROM_ARRAY>

        extends ElementsAllocators<INTERFACE_IMMUTABLE, ALLOCATE_FROM_ARRAY>
        implements IElementsAllocators<INTERFACE_IMMUTABLE, CLASS_MUTABLE, INTERFACE_BUILDER, ALLOCATE_FROM_ARRAY>, IAllocators {

    @FunctionalInterface
    public interface IAllocateFunction<T> {

        T allocate(AllocationType allocationType, long capacity);
    }

    @FunctionalInterface
    public interface IAllocateFromArrayFunction<T, U extends IElements> {

        U allocate(AllocationType allocationType, T values, long startIndex, long numElements);
    }

    private final AllocationType allocationType;
    private final IAllocateFromArrayFunction<ALLOCATE_FROM_ARRAY, INTERFACE_IMMUTABLE> createImmutable;
    private final IAllocateFunction<CLASS_MUTABLE> createMutable;
    private final IAllocateFunction<INTERFACE_BUILDER> createBuilder;

    BaseHeapElementsAllocators(AllocationType allocationType, IAllocateFromArrayFunction<ALLOCATE_FROM_ARRAY, INTERFACE_IMMUTABLE> createImmutable,
            Supplier<INTERFACE_IMMUTABLE> emptyImmutableSupplier, IAllocateFunction<CLASS_MUTABLE> createMutable, IAllocateFunction<INTERFACE_BUILDER> createBuilder) {
        super(emptyImmutableSupplier);

        this.allocationType = Objects.requireNonNull(allocationType);
        this.createImmutable = Objects.requireNonNull(createImmutable);
        this.createMutable = Objects.requireNonNull(createMutable);
        this.createBuilder = Objects.requireNonNull(createBuilder);
    }

    @Override
    public final void gatherStatistics(IAllocatorsStatisticsGatherer statisticsGatherer) {

    }

    @Override
    public final INTERFACE_IMMUTABLE allocateImmutableFrom(ALLOCATE_FROM_ARRAY values, long valuesLength, long startIndex, long numElements) {

        Checks.isIntIndex(startIndex);
        Checks.isIntNumElements(numElements);
        Checks.isLessThan(startIndex, numElements);

        return createImmutable.allocate(allocationType, values, BaseADTElements.intIndex(startIndex), BaseADTElements.intNumElements(numElements));
    }

    @Override
    public final void freeImmutable(INTERFACE_IMMUTABLE immutable) {

        Objects.requireNonNull(immutable);
    }

    @Override
    public final CLASS_MUTABLE allocateMutable(long minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return createMutable.allocate(allocationType, BaseADTElements.intMinimumCapacity(minimumCapacity));
    }

    @Override
    public final void freeMutable(CLASS_MUTABLE mutable) {

        Objects.requireNonNull(mutable);
    }

    @Override
    public final INTERFACE_BUILDER allocateBuilder(long minimumCapacity) {

        Checks.isIntMinimumCapacity(minimumCapacity);

        return createBuilder.allocate(allocationType, BaseADTElements.intMinimumCapacity(minimumCapacity));
    }

    @Override
    public final void freeBuilder(INTERFACE_BUILDER builder) {

        Objects.requireNonNull(builder);
    }
}
