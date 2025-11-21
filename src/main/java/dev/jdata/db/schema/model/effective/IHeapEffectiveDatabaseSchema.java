package dev.jdata.db.schema.model.effective;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;
import dev.jdata.db.utils.allocators.Allocatable.AllocationType;

public interface IHeapEffectiveDatabaseSchema extends IEffectiveDatabaseSchema {

    public static IHeapEffectiveDatabaseSchema empty(DatabaseId databaseId) {

        Objects.requireNonNull(databaseId);

        return of(databaseId, DatabaseSchemaVersion.INITIAL, IHeapAllCompleteSchemaMaps.empty());
    }

    public static IHeapEffectiveDatabaseSchema of(DatabaseId databaseId, DatabaseSchemaVersion version, IHeapAllCompleteSchemaMaps schemaMaps) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(version);
        Objects.requireNonNull(schemaMaps);

        return new HeapEffectiveDatabaseSchema(AllocationType.HEAP, databaseId, version, schemaMaps);
    }
}
