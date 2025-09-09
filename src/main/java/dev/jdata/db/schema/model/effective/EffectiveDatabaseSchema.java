package dev.jdata.db.schema.model.effective;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.databaseschema.CompleteDatabaseSchema;
import dev.jdata.db.schema.model.schemamaps.HeapAllCompleteSchemaMaps;

public final class EffectiveDatabaseSchema extends CompleteDatabaseSchema implements IEffectiveDatabaseSchema {

    public static EffectiveDatabaseSchema empty(DatabaseId databaseId) {

        Objects.requireNonNull(databaseId);

        return new EffectiveDatabaseSchema(AllocationType.HEAP, databaseId, DatabaseSchemaVersion.of(DatabaseSchemaVersion.INITIAL_VERSION), HeapAllCompleteSchemaMaps.empty());
    }

    public static EffectiveDatabaseSchema of(DatabaseId databaseId, DatabaseSchemaVersion version, HeapAllCompleteSchemaMaps schemaMaps) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);
        Objects.requireNonNull(schemaMaps);

        return new EffectiveDatabaseSchema(AllocationType.HEAP, databaseId, version, schemaMaps);
    }

    private EffectiveDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, HeapAllCompleteSchemaMaps schemaMaps) {
        super(allocationType, databaseId, version, schemaMaps);
    }
}
