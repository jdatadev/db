package dev.jdata.db.schema.model.diff;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.IDatabaseSchemasFreeable;
import dev.jdata.db.schema.allocators.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.schema.model.databaseschema.BaseDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.DroppedSchemaObjects;
import dev.jdata.db.schema.model.diff.dropped.IDroppedSchemaObjects;
import dev.jdata.db.schema.model.diff.schemamaps.DiffSchemaMaps;
import dev.jdata.db.utils.allocators.IAllocator;

public final class DiffDatabaseSchema extends BaseDatabaseSchema<DiffSchemaMaps> implements IDiffDatabaseSchema, IDatabaseSchemasFreeable {

    private final DroppedSchemaObjects droppedSchemaObjects;

    private DiffDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, DiffSchemaMaps schemaMaps,
            DroppedSchemaObjects droppedSchemaObjects, IDatabaseSchemasAllocator databaseSchemasAllocator) {
        super(allocationType, databaseId, version, schemaMaps);

        this.droppedSchemaObjects = droppedSchemaObjects != null
                ? databaseSchemasAllocator.copyDroppedSchemaObjects(droppedSchemaObjects)
                : null;
    }

    public IDroppedSchemaObjects getDroppedSchemaObjects() {
        return droppedSchemaObjects;
    }

    @Override
    public void free(IDatabaseSchemasAllocator allocator) {

        IAllocator.safeFree(droppedSchemaObjects, allocator, IDatabaseSchemasAllocator::freeDroppedSchemaObjects);
    }
}
