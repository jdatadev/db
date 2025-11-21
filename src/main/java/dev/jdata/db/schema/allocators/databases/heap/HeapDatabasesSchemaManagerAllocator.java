package dev.jdata.db.schema.allocators.databases.heap;

import dev.jdata.db.schema.allocators.databases.DatabasesSchemaManagerAllocator;
import dev.jdata.db.schema.model.diff.dropped.SchemaDroppedElementsAllocators;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMapsBuilderAllocator;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMapsBuilder;
import dev.jdata.db.schema.model.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.utils.adt.maps.IMutableIntToObjectWithRemoveStaticMap;
import dev.jdata.db.utils.adt.sets.IMutableIntSet;

public final class HeapDatabasesSchemaManagerAllocator<T extends IMutableIntSet, U extends IMutableIntToObjectWithRemoveStaticMap<T>>

        extends DatabasesSchemaManagerAllocator<T, U, IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMaps, IHeapAllCompleteSchemaMapsBuilder> {

    public HeapDatabasesSchemaManagerAllocator(SchemaDroppedElementsAllocators<T, U> schemaDroppedElementsAllocators, IDatabaseSchemasAllocator<T, U> databaseSchemasAllocator) {
        super(schemaDroppedElementsAllocators, databaseSchemasAllocator, HeapAllCompleteSchemaMapsBuilderAllocator.INSTANCE);
    }
}
