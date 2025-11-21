package dev.jdata.db.utils.adt.elements;

import java.util.function.Supplier;

import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class IntCapacityHeapElementsAllocators<

                INTERFACE_IMMUTABLE extends IElements,
                CLASS_MUTABLE extends Allocatable & IMutableElements,
                INTERFACE_BUILDER extends IElementsBuilder<INTERFACE_IMMUTABLE, ?>,
                ALLOCATE_FROM_ARRAY>

        extends BaseHeapElementsAllocators<INTERFACE_IMMUTABLE, CLASS_MUTABLE, INTERFACE_BUILDER, ALLOCATE_FROM_ARRAY> {

    @FunctionalInterface
    public interface IIntCapacityAllocateFunction<T> {

        T allocate(AllocationType allocationType, int capacity);
    }

    @FunctionalInterface
    public interface IIntCapacityAllocateFromArrayFunction<T, U extends IElements> {

        U allocate(AllocationType allocationType, T values, int startIndex, int numElements);
    }

    public IntCapacityHeapElementsAllocators(AllocationType allocationType, IIntCapacityAllocateFromArrayFunction<ALLOCATE_FROM_ARRAY, INTERFACE_IMMUTABLE> createImmutable,
            Supplier<INTERFACE_IMMUTABLE> emptyImmutableSupplier, IIntCapacityAllocateFunction<CLASS_MUTABLE> createMutable,
            IIntCapacityAllocateFunction<INTERFACE_BUILDER> createBuilder) {
        super(allocationType, (a, v, s, n) -> createImmutable.allocate(a, v, BaseADTElements.intIndex(s), BaseADTElements.intNumElements(n)), emptyImmutableSupplier,
                (a, c) -> createMutable.allocate(a, BaseADTElements.intMinimumCapacity(c)), (a, c) -> createBuilder.allocate(a, BaseADTElements.intMinimumCapacity(c)));
    }
}
