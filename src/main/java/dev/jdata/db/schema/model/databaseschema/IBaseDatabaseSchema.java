package dev.jdata.db.schema.model.databaseschema;

import dev.jdata.db.common.database.IDatabaseIdentifiable;
import dev.jdata.db.schema.DatabaseSchemaVersion;

public interface IBaseDatabaseSchema extends IDatabaseIdentifiable, IDatabaseSchemaObjects {

    DatabaseSchemaVersion getVersion();
}
