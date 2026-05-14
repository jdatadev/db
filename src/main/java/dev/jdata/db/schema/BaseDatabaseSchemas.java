package dev.jdata.db.schema;

import dev.jdata.db.common.database.DatabaseIdentifiable;

abstract class BaseDatabaseSchemas extends DatabaseIdentifiable {

    BaseDatabaseSchemas(AllocationType allocationType, DatabaseId databaseId) {
        super(allocationType, databaseId);
    }
}
