package dev.jdata.db.engine.validation.exceptions;

import dev.jdata.db.schema.DatabaseId;

public abstract class SchemaObjectAlreadyExistsException extends SchemaEntityAlreadyExistsException {

    private static final long serialVersionUID = 1L;

    SchemaObjectAlreadyExistsException(DatabaseId databaseId, long schemaObjectName) {
        super(databaseId, schemaObjectName);
    }
}
