package dev.jdata.db.schema.model.diff;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.IDatabaseSchemasFreeable;
import dev.jdata.db.schema.allocators.schemas.IDatabaseSchemasAllocator;
import dev.jdata.db.schema.model.databaseschema.BaseDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.DroppedElementsSchemaObjects;
import dev.jdata.db.schema.model.diff.dropped.IDroppedElementsSchemaObjects;
import dev.jdata.db.schema.model.diff.schemamaps.DiffSchemaMaps;
import dev.jdata.db.utils.allocators.IAllocator;

public class DiffDatabaseSchema extends BaseDatabaseSchema<DiffSchemaMaps<?>> implements IDiffDatabaseSchema, IDatabaseSchemasFreeable {

    private final DroppedElementsSchemaObjects droppedElementsSchemaObjects;

    DiffDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, DiffSchemaMaps<?> schemaMaps,
            DroppedElementsSchemaObjects droppedElementsSchemaObjects, IDatabaseSchemasAllocator databaseSchemasAllocator) {
        super(allocationType, databaseId, version, schemaMaps);

        this.droppedElementsSchemaObjects = droppedElementsSchemaObjects != null
                ? databaseSchemasAllocator.copyDroppedSchemaObjects(droppedElementsSchemaObjects)
                : null;
    }

    public final IDroppedElementsSchemaObjects getDroppedSchemaObjects() {
        return droppedElementsSchemaObjects;
    }

    @Override
    public final void free(IDatabaseSchemasAllocator allocator) {

        IAllocator.safeFree(droppedElementsSchemaObjects, allocator, IDatabaseSchemasAllocator::freeDroppedElementsSchemaObjects);
    }
}
