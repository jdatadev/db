package dev.jdata.db.schema.allocators.databases.heap;

import dev.jdata.db.schema.allocators.databases.DatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.allocators.model.diff.dropped.heap.HeapDroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.allocators.model.schemamaps.heap.HeapCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.allocators.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.schema.model.HeapSchemaMap;
import dev.jdata.db.schema.model.objects.SchemaObject;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.HeapAllSimpleCompleteSchemaMapsBuilder;

public final class HeapDatabasesSchemaManagerAllocator

        extends DatabasesSchemaManagerAllocator<HeapSchemaMap<SchemaObject>, HeapAllCompleteSchemaMaps, HeapAllSimpleCompleteSchemaMapsBuilder> {

    public HeapDatabasesSchemaManagerAllocator(HeapDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator databaseSchemasAllocator) {
        super(droppedSchemaObjectsAllocator, databaseSchemasAllocator, HeapCompleteSchemaMapsBuilderAllocator.INSTANCE);
    }
}
