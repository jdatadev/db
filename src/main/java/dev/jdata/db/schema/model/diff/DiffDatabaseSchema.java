package dev.jdata.db.schema.model.diff;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.databaseschema.BaseDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.IHeapSchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.ISchemaDroppedElements;
import dev.jdata.db.schema.model.diff.schemamaps.IDiffSchemaMaps;

class DiffDatabaseSchema extends BaseDatabaseSchema<IDiffSchemaMaps> implements IDiffDatabaseSchema {

    private final IHeapSchemaDroppedElements schemaDroppedElements;

    DiffDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IDiffSchemaMaps schemaMaps,
            IHeapSchemaDroppedElements schemaDroppedElements) {
        super(allocationType, databaseId, version, schemaMaps);

        this.schemaDroppedElements = schemaDroppedElements;
    }

    @Override
    public final ISchemaDroppedElements getDroppedSchemaObjects() {
        return schemaDroppedElements;
    }
}
