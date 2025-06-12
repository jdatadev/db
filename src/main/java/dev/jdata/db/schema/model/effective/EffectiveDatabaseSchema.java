package dev.jdata.db.schema.model.effective;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;

public final class EffectiveDatabaseSchema extends CompleteDatabaseSchema implements IEffectiveDatabaseSchema {

    public static EffectiveDatabaseSchema of(DatabaseId databaseId, DatabaseSchemaVersion version, CompleteSchemaMaps schemaMaps) {

        return new EffectiveDatabaseSchema(AllocationType.HEAP, databaseId, version, schemaMaps);
    }

    private EffectiveDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, CompleteSchemaMaps schemaMaps) {
        super(allocationType, databaseId, version, schemaMaps);
    }
}
