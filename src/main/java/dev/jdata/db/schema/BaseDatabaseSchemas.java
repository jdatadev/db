package dev.jdata.db.schema;

abstract class BaseDatabaseSchemas extends DatabaseSchemaRootObject {

    BaseDatabaseSchemas(AllocationType allocationType, DatabaseId databaseId) {
        super(allocationType, databaseId);
    }
}
