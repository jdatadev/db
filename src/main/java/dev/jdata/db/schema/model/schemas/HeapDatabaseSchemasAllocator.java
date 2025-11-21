package dev.jdata.db.schema.model.schemas;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.HeapSchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class HeapDatabaseSchemasAllocator<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>> extends DatabaseSchemasAllocator<T, U> {

    public HeapDatabaseSchemasAllocator(SchemaDroppedElementsAllocators<T, U> droppedSchemaObjectsAllocator) {
        super(droppedSchemaObjectsAllocator);
    }

    @Override
    public SchemaDroppedElements<T, U> allocateSchemaDroppedElements() {

        return new HeapSchemaDroppedElements<>(AllocationType.HEAP_ALLOCATOR);
    }

    @Override
    public void freeSchemaDroppedElements(SchemaDroppedElements<T, U> droppedElements) {

        Objects.requireNonNull(droppedElements);
    }
}
