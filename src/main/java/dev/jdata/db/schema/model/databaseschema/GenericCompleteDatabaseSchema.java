package dev.jdata.db.schema.model.databaseschema;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;

@Deprecated // is generic schema necessary?
abstract class GenericCompleteDatabaseSchema extends CompleteDatabaseSchema implements IGenericCompleteDatabaseSchema {

    GenericCompleteDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IHeapCompleteSchemaMap schemaMap) {
        super(allocationType, databaseId, version, schemaMap);
    }
}
