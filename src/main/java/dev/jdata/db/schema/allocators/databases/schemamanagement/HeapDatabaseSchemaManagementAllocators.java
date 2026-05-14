package dev.jdata.db.schema.allocators.databases.schemamanagement;

import java.util.Objects;

import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMapBuilder;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMapBuilderAllocator;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public final class HeapDatabaseSchemaManagementAllocators<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>>

        extends DatabaseSchemaManagementAllocators<T, U, IHeapCompleteSchemaMap, IHeapCompleteSchemaMap, IHeapCompleteSchemaMapBuilder> {

    public HeapDatabaseSchemaManagementAllocators(SchemaDroppedElementsAllocators<T, U> schemaDroppedElementsAllocators) {
        super(schemaDroppedElementsAllocators, IHeapCompleteSchemaMapBuilderAllocator.create());
    }

    @Override
    public SchemaDroppedElements<T, U> allocateSchemaDroppedElements() {

        return new SchemaDroppedElements<>(AllocationType.HEAP_ALLOCATOR);
    }

    @Override
    public void freeSchemaDroppedElements(SchemaDroppedElements<T, U> droppedElements) {

        Objects.requireNonNull(droppedElements);
    }
}
