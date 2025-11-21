package dev.jdata.db.schema.allocators.databases.heap;

import dev.jdata.db.schema.allocators.databases.DatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.model.diff.dropped.HeapDroppedSchemaObjectsAllocator;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemas.IDatabaseSchemasAllocator;

public final class HeapDatabasesSchemaManagerAllocator

        extends DatabasesSchemaManagerAllocator<IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMapsBuilder> {

    public HeapDatabasesSchemaManagerAllocator(HeapDroppedSchemaObjectsAllocator droppedSchemaObjectsAllocator, IDatabaseSchemasAllocator databaseSchemasAllocator) {
        super(droppedSchemaObjectsAllocator, databaseSchemasAllocator, HeapAllCompleteSchemaMapsBuilderAllocator.INSTANCE);
    }
}
