package dev.jdata.db.schema.model;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.schema.DatabaseSchemaObject;

public abstract class DatabaseSchemaModelObject extends DatabaseSchemaObject {

    protected DatabaseSchemaModelObject(DatabaseId databaseId) {
        super(databaseId);
    }
}
