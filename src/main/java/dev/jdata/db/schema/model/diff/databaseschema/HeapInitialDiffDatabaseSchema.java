package dev.jdata.db.schema.model.diff.databaseschema;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.diff.dropped.IHeapSchemaDroppedElements;
import dev.jdata.db.schema.model.diff.schemamap.IDiffSchemaMap;

final class HeapInitialDiffDatabaseSchema extends InitialDiffDatabaseSchema implements IHeapInitialDiffDatabaseSchema {

    HeapInitialDiffDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IDiffSchemaMap schemaMap,
            IHeapSchemaDroppedElements schemaDroppedElements) {
        super(allocationType, databaseId, version, schemaMap, schemaDroppedElements);
    }
}
