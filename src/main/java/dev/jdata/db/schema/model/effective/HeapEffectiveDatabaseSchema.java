package dev.jdata.db.schema.model.effective;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;

final class HeapEffectiveDatabaseSchema extends EffectiveDatabaseSchema implements IHeapEffectiveDatabaseSchema {

    HeapEffectiveDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IHeapCompleteSchemaMap schemaMap) {
        super(allocationType, databaseId, version, schemaMap);
    }
}
