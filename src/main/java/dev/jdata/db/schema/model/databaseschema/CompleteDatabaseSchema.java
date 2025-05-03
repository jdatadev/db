package dev.jdata.db.schema.model.databaseschema;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamaps.CompleteSchemaMaps;

public class CompleteDatabaseSchema extends BaseDatabaseSchema<CompleteSchemaMaps> {

    public static CompleteDatabaseSchema empty(DatabaseId databaseId, DatabaseSchemaVersion version) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);

        return new CompleteDatabaseSchema(databaseId, version, CompleteSchemaMaps.EMPTY);
    }

    @Deprecated
    public static CompleteDatabaseSchema of(DatabaseId databaseId, DatabaseSchemaVersion version, CompleteSchemaMaps schemaMaps) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);

        return new CompleteDatabaseSchema(databaseId, version, schemaMaps);
    }

    protected CompleteDatabaseSchema(DatabaseId databaseId, DatabaseSchemaVersion version, CompleteSchemaMaps schemaMaps) {
        super(databaseId, version, schemaMaps);
    }
}
