package dev.jdata.db.schema.model;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaRootObject;

public abstract class DatabaseSchemaModelRootObject extends DatabaseSchemaRootObject implements IDatabaseSchemaModelRootObject {

    protected DatabaseSchemaModelRootObject(AllocationType allocationType, DatabaseId databaseId) {
        super(allocationType, databaseId);
    }
}
