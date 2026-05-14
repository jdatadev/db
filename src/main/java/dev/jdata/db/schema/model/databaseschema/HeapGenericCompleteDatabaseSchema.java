package dev.jdata.db.schema.model.databaseschema;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;

final class HeapGenericCompleteDatabaseSchema extends GenericCompleteDatabaseSchema implements IHeapGenericCompleteDatabaseSchema {

    HeapGenericCompleteDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IHeapCompleteSchemaMap schemaMap) {
        super(allocationType, databaseId, version, schemaMap);
    }
}
