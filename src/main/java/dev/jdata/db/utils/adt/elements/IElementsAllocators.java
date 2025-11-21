package dev.jdata.db.utils.adt.elements;

import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.IAllocators;

public interface IElementsAllocators<

                IMMUTABLE extends IElements,
                MUTABLE extends Allocatable & IMutableElements,
                BUILDER extends IElementsBuilder<IMMUTABLE, ?>,
                ALLOCATE_FROM_ARRAY> extends IAllocators {

    IMMUTABLE allocateImmutableFrom(ALLOCATE_FROM_ARRAY values, long valuesLength, long startIndex, long numElements);
    void freeImmutable(IMMUTABLE immutable);
    IMMUTABLE emptyImmutable();

    MUTABLE allocateMutable(long minimumCapacity);
    void freeMutable(MUTABLE mutable);

    BUILDER allocateBuilder(long minimumCapacity);
    void freeBuilder(BUILDER builder);
}
