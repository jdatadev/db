package dev.jdata.db.schema.model.effective;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;

abstract class EffectiveDatabaseSchema extends CompleteDatabaseSchema implements IEffectiveDatabaseSchema {

    EffectiveDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IHeapCompleteSchemaMap schemaMap) {
        super(allocationType, databaseId, version, schemaMap);
    }
}
