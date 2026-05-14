package dev.jdata.db.schema.model.databaseschema;

import dev.jdata.db.common.database.DatabaseIdentifiable;
import dev.jdata.db.schema.DatabaseId;

public abstract class DatabaseSchemaModelRootObject extends DatabaseIdentifiable {

    protected DatabaseSchemaModelRootObject(AllocationType allocationType, DatabaseId databaseId) {
        super(allocationType, databaseId);
    }
}
