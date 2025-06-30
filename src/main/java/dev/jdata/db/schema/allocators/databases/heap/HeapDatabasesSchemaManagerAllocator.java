package dev.jdata.db.schema.allocators.databases.heap;

import dev.jdata.db.schema.allocators.databases.DatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.allocators.model.diff.dropped.heap.HeapDroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.heap.HeapCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.allocators.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMaps.HeapCompleteSchemaMapsBuilder;

public final class HeapDatabasesSchemaManagerAllocator extends DatabasesSchemaManagerAllocator<HeapSchemaMap<?>, HeapCompleteSchemaMaps, HeapCompleteSchemaMapsBuilder> {

    public HeapDatabasesSchemaManagerAllocator(HeapDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator databaseSchemasAllocator) {
        super(droppedSchemaObjectsAllocator, databaseSchemasAllocator, HeapCompleteSchemaMapsBuilderAllocator.INSTANCE);
    }
}
