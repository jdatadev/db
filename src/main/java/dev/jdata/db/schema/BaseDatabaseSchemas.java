package dev.jdata.db.schema;

abstract class BaseDatabaseSchemas extends DatabaseSchemaObject {

    BaseDatabaseSchemas(AllocationType allocationType, DatabaseId databaseId) {
        super(allocationType, databaseId);
    }
}
