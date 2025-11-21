package dev.jdata.db.engine.validation.exceptions;

import dev.jdata.db.schema.DatabaseId;

public abstract class SchemaObjectDoesNotExistException extends SchemaEntityDoesNotExistException {

    private static final long serialVersionUID = 1L;

    SchemaObjectDoesNotExistException(DatabaseId databaseId, long schemaObjectName) {
        super(databaseId, schemaObjectName);
    }
}
