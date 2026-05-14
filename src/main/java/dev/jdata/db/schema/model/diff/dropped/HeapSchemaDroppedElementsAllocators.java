package dev.jdata.db.schema.model.diff.dropped;

import java.util.Objects;

import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMapAllocator;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class HeapSchemaDroppedElementsAllocators<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>>

        extends SchemaDroppedElementsAllocators<T, U> {

    public HeapSchemaDroppedElementsAllocators(IMutableIntSetAllocator<T> mutableIntSetAllocator,
            IMutableIntToObjectWithRemoveStaticMapAllocator<T, U> mutableIntToObjectMapAllocator) {
        super(mutableIntSetAllocator, mutableIntToObjectMapAllocator);
    }

    @Override
    DroppedElements<T, U> allocateDroppedElements() {

        return new DroppedElements<>(AllocationType.HEAP_ALLOCATOR);
    }

    @Override
    void freeDroppedElements(DroppedElements<T, U> droppedElements) {

        Objects.requireNonNull(droppedElements);

        droppedElements.free(getMutableIntToObjectMapAllocator(), getMutableIntSetAllocator());
    }
}
