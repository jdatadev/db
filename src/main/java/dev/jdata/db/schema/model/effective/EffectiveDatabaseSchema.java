package dev.jdata.db.schema.model.effective;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;

abstract class EffectiveDatabaseSchema extends CompleteDatabaseSchema implements IEffectiveDatabaseSchema {

    EffectiveDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IHeapAllCompleteSchemaMaps schemaMaps) {
        super(allocationType, databaseId, version, schemaMaps);
    }
}
