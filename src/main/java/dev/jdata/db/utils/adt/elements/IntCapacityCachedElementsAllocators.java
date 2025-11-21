package dev.jdata.db.utils.adt.elements;

import java.util.function.Supplier;
import java.util.function.ToLongFunction;

import dev.jdata.db.utils.allocators.Allocatable;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.BaseCapacityInstanceAllocator.CapacityMax;
import dev.jdata.db.utils.allocators.IAllocators;
import dev.jdata.db.utils.function.ObjIntFunction;

public final class IntCapacityCachedElementsAllocators<

                INTERFACE_IMMUTABLE extends IElements,
                CLASS_IMMUTABLE extends BaseADTElements<?, ?, ALLOCATE_FROM_ARRAY> & IElements,
                CLASS_MUTABLE extends Allocatable & IMutableElements,
                INTERFACE_BUILDER extends IElementsBuilder<INTERFACE_IMMUTABLE, ?>,
                CLASS_BUILDER extends Allocatable & IElementsBuilder<INTERFACE_IMMUTABLE, ?>,
                ALLOCATE_FROM_ARRAY>

        extends BaseCachedElementsAllocators<INTERFACE_IMMUTABLE, CLASS_IMMUTABLE, CLASS_MUTABLE, INTERFACE_BUILDER, CLASS_BUILDER, ALLOCATE_FROM_ARRAY>
        implements IElementsAllocators<INTERFACE_IMMUTABLE, CLASS_MUTABLE, INTERFACE_BUILDER, ALLOCATE_FROM_ARRAY>, IAllocators {


    public IntCapacityCachedElementsAllocators(AllocationType allocationType, CapacityMax capacityMax,
            Class<INTERFACE_IMMUTABLE> immutableInterface, Class<CLASS_IMMUTABLE> immutableClass,
            ObjIntFunction<AllocationType, CLASS_IMMUTABLE> createImmutable, ToLongFunction<CLASS_IMMUTABLE> immutableCapacityGetter,
            Supplier<INTERFACE_IMMUTABLE> emptyImmutableSupplier,
            Class<CLASS_MUTABLE> mutableClass, ObjIntFunction<AllocationType, CLASS_MUTABLE> createMutable, ToLongFunction<CLASS_MUTABLE> mutableCapacityGetter,
            Class<INTERFACE_BUILDER> builderInterface, Class<CLASS_BUILDER> builderClass,
            ObjIntFunction<AllocationType, CLASS_BUILDER> createBuilder, ToLongFunction<CLASS_BUILDER> builderCapacityGetter) {
        super(allocationType, capacityMax, immutableInterface, immutableClass, (a, c) -> createImmutable.apply(a, BaseADTElements.intMinimumCapacity(c)), immutableCapacityGetter,
                emptyImmutableSupplier, mutableClass, (a, c) -> createMutable.apply(a, BaseADTElements.intMinimumCapacity(c)), mutableCapacityGetter, builderInterface,
                builderClass, (a, c) -> createBuilder.apply(a, BaseADTElements.intMinimumCapacity(c)), builderCapacityGetter);
    }
}
