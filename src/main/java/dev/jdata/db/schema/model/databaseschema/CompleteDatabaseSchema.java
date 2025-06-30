package dev.jdata.db.schema.model.databaseschema;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamaps.HeapCompleteSchemaMaps;

public class CompleteDatabaseSchema extends BaseDatabaseSchema<HeapCompleteSchemaMaps> {

    public static CompleteDatabaseSchema empty(DatabaseId databaseId, DatabaseSchemaVersion version) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);

        return new CompleteDatabaseSchema(AllocationType.HEAP, databaseId, version, HeapCompleteSchemaMaps.empty());
    }

    public static CompleteDatabaseSchema of(DatabaseId databaseId, DatabaseSchemaVersion version, HeapCompleteSchemaMaps schemaMaps) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);

        return new CompleteDatabaseSchema(AllocationType.HEAP, databaseId, version, schemaMaps);
    }

    protected CompleteDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, HeapCompleteSchemaMaps schemaMaps) {
        super(allocationType, databaseId, version, schemaMaps);
    }
}
