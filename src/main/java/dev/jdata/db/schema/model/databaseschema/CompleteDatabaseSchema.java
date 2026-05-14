package dev.jdata.db.schema.model.databaseschema;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;

public abstract class CompleteDatabaseSchema extends BaseDatabaseSchema<IHeapCompleteSchemaMap> implements ICompleteDatabaseSchema {

    protected CompleteDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IHeapCompleteSchemaMap schemaMap) {
        super(allocationType, databaseId, version, schemaMap);
    }
}
