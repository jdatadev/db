package dev.jdata.db.schema.model.diff.databaseschema;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.databaseschema.BaseDatabaseSchema;
import dev.jdata.db.schema.model.diff.dropped.IHeapSchemaDroppedElements;
import dev.jdata.db.schema.model.diff.dropped.ISchemaDroppedElements;
import dev.jdata.db.schema.model.diff.schemamap.IDiffSchemaMap;

abstract class DiffDatabaseSchema extends BaseDatabaseSchema<IDiffSchemaMap> implements IDiffDatabaseSchema {

    private final IHeapSchemaDroppedElements schemaDroppedElements;

    DiffDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IDiffSchemaMap schemaMap,
            IHeapSchemaDroppedElements schemaDroppedElements) {
        super(allocationType, databaseId, version, schemaMap);

        this.schemaDroppedElements = schemaDroppedElements;
    }

    @Override
    public final ISchemaDroppedElements getSchemaDroppedElements() {
        return schemaDroppedElements;
    }
}
