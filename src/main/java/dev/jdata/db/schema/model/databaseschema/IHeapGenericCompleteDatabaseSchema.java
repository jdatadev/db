package dev.jdata.db.schema.model.databaseschema;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapGenericCompleteDatabaseSchema extends IGenericCompleteDatabaseSchema {

    public static IHeapGenericCompleteDatabaseSchema empty(DatabaseId databaseId, DatabaseSchemaVersion version) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);

        return new HeapGenericCompleteDatabaseSchema(AllocationType.HEAP, databaseId, version, IHeapAllCompleteSchemaMaps.empty());
    }

    public static IHeapGenericCompleteDatabaseSchema of(DatabaseId databaseId, DatabaseSchemaVersion version, IHeapAllCompleteSchemaMaps schemaMaps) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);
        Objects.requireNonNull(schemaMaps);

        return new HeapGenericCompleteDatabaseSchema(AllocationType.HEAP, databaseId, version, schemaMaps);
    }
}
