package dev.jdata.db.engine.validation.exceptions;

import dev.jdata.db.schema.DatabaseId;

abstract class SchemaEntityAlreadyExistsException extends SchemaEntityException {

    private static final long serialVersionUID = 1L;

    SchemaEntityAlreadyExistsException(DatabaseId databaseId, long entityName) {
        super(databaseId, entityName);
    }
}
