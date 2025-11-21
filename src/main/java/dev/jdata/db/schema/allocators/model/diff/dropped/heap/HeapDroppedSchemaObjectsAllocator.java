package dev.jdata.db.schema.allocators.model.diff.dropped.heap;

import dev.jdata.db.schema.allocators.model.diff.dropped.DroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.model.diff.dropped.DroppedElements;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.adt.sets.IBaseMutableIntSetAllocator;
import dev.jdata.db.utils.allocators.IIntToObjectMapAllocator;

public final class HeapDroppedSchemaObjectsAllocator extends DroppedSchemaObjectsAllocator {

    public HeapDroppedSchemaObjectsAllocator(IBaseMutableIntSetAllocator intSetAllocator, IIntToObjectMapAllocator<IMutableIntSet> intToObjectMapAllocator) {
        super(intSetAllocator, intToObjectMapAllocator);
    }

    @Override
    public DroppedElements allocateDroppedElements() {

        return new DroppedElements();
    }

    @Override
    public void freeDroppedElements(DroppedElements droppedElements) {

    }
}
