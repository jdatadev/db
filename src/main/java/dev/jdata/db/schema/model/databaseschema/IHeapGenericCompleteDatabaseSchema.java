package dev.jdata.db.schema.model.databaseschema;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamap.IHeapCompleteSchemaMap;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapGenericCompleteDatabaseSchema extends IGenericCompleteDatabaseSchema {

    public static IHeapGenericCompleteDatabaseSchema empty(DatabaseId databaseId, DatabaseSchemaVersion version) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);

        return new HeapGenericCompleteDatabaseSchema(AllocationType.HEAP, databaseId, version, IHeapCompleteSchemaMap.empty());
    }

    public static IHeapGenericCompleteDatabaseSchema of(DatabaseId databaseId, DatabaseSchemaVersion version, IHeapCompleteSchemaMap schemaMap) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);
        Objects.requireNonNull(schemaMap);

        return new HeapGenericCompleteDatabaseSchema(AllocationType.HEAP, databaseId, version, schemaMap);
    }
}
