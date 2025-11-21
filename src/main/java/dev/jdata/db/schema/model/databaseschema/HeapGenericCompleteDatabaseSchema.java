package dev.jdata.db.schema.model.databaseschema;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;

final class HeapGenericCompleteDatabaseSchema extends GenericCompleteDatabaseSchema implements IHeapGenericCompleteDatabaseSchema {

    HeapGenericCompleteDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IHeapAllCompleteSchemaMaps schemaMaps) {
        super(allocationType, databaseId, version, schemaMaps);
    }
}
