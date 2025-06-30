package dev.jdata.db.engine.validation.exceptions;

import dev.jdata.db.schema.DatabaseId;

abstract class SchemaEntityDoesNotExistException extends SchemaEntityException {

    private static final long serialVersionUID = 1L;

    SchemaEntityDoesNotExistException(DatabaseId databaseId, long entityName) {
        super(databaseId, entityName);
    }
}
