package dev.jdata.db.schema.model.effective;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;

public final class EffectiveDatabaseSchema extends CompleteDatabaseSchema implements IEffectiveDatabaseSchema {

    public EffectiveDatabaseSchema(DatabaseId databaseId, DatabaseSchemaVersion version, CompleteSchemaMaps schemaMaps) {
        super(databaseId, version, schemaMaps);
    }
}
