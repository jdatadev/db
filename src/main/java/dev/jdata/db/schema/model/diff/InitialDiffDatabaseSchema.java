package dev.jdata.db.schema.model.diff;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.allocators.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.schema.model.diff.dropped.DroppedElementsSchemaObjects;
import dev.jdata.db.schema.model.diff.schemamaps.DiffSchemaMaps;

public final class InitialDiffDatabaseSchema extends DiffDatabaseSchema {

    private InitialDiffDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, DiffSchemaMaps<?> schemaMaps,
            DroppedElementsSchemaObjects droppedElementsSchemaObjects, IDatabaseSchemasAllocator databaseSchemasAllocator) {
        super(allocationType, databaseId, version, schemaMaps, droppedElementsSchemaObjects, databaseSchemasAllocator);
    }
}
