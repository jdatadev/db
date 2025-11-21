package dev.jdata.db.schema.model.databaseschema;

import dev.jdata.db.schema.DatabaseSchemaVersion;
import dev.jdata.db.schema.IDatabaseSchemaRootObject;

public interface IBaseDatabaseSchema extends IDatabaseSchemaRootObject, IDatabaseSchemaObjects {

    DatabaseSchemaVersion getVersion();
}
