package dev.jdata.db.schema.model.databaseschema;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.model.schemamaps.IHeapAllCompleteSchemaMaps;

public abstract class CompleteDatabaseSchema extends BaseDatabaseSchema<IHeapAllCompleteSchemaMaps> implements ICompleteDatabaseSchema {

    protected CompleteDatabaseSchema(AllocationType allocationType, DatabaseId databaseId, DatabaseSchemaVersion version, IHeapAllCompleteSchemaMaps schemaMaps) {
        super(allocationType, databaseId, version, schemaMaps);
    }
}
