package dev.jdata.db.schema.model.effective;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;

final class HeapEffectiveDatabaseSchema extends EffectiveDatabaseSchema implements IHeapEffectiveDatabaseSchema {

    HeapEffectiveDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IHeapAllCompleteSchemaMaps schemaMaps) {
        super(allocationType, databaseId, version, schemaMaps);
    }
}
