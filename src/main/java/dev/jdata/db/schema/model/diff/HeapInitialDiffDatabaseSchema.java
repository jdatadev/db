package dev.jdata.db.schema.model.diff;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.diff.dropped.IHeapSchemaDroppedElements;
import dev.jdata.db.schema.model.diff.schemamaps.IDiffSchemaMaps;

final class HeapInitialDiffDatabaseSchema extends InitialDiffDatabaseSchema implements IHeapInitialDiffDatabaseSchema {

    HeapInitialDiffDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IDiffSchemaMaps schemaMaps,
            IHeapSchemaDroppedElements schemaDroppedElements) {
        super(allocationType, databaseId, version, schemaMaps, schemaDroppedElements);
    }
}
