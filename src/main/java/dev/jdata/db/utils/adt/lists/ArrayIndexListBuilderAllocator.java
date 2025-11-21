package dev.jdata.db.utils.adt.lists;

import java.util.function.IntFunction;

import dev.jdata.db.utils.adt.elements.ICapacity;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;
import dev.jdata.db.utils.allocators.BaseAllocatableArrayAllocator;

private final class ArrayIndexListBuilderAllocator<T, U extends IndexListBuilder<T, ?, ?, ?, ?, ?>> extends BaseAllocatableArrayAllocator<U> {

    ArrayIndexListBuilderAllocator(AllocationType allocationType, IntFunction<U> createBuilder) {
        super(createBuilder, l -> ICapacity.intCapacity(l.getListCapacity()));
    }

    U allocateIndexListBuilder(int minimumCapacity) {

        return allocateAllocatableArrayInstance(minimumCapacity);
    }

    void freeIndexListBuilder(U builder) {

        freeAllocatableArrayInstance(builder);
    }
}
