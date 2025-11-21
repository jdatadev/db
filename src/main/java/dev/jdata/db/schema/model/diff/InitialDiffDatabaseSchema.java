package dev.jdata.db.schema.model.diff;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.diff.dropped.IHeapSchemaDroppedElements;
import dev.jdata.db.schema.model.diff.schemamaps.IDiffSchemaMaps;

abstract class InitialDiffDatabaseSchema extends DiffDatabaseSchema implements IInitialDiffDatabaseSchema {

    InitialDiffDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IDiffSchemaMaps schemaMaps,
            IHeapSchemaDroppedElements schemaDroppedElements) {
        super(allocationType, databaseId, version, schemaMaps, schemaDroppedElements);
    }
}
