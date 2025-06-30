package dev.jdata.db.engine.validation.exceptions;

import dev.jdata.db.schema.DatabaseId;

public final class ColumnAlreadyExistsException extends SchemaEntityAlreadyExistsException {

    private static final long serialVersionUID = 1L;

    public ColumnAlreadyExistsException(DatabaseId databaseId, long entityName) {
        super(databaseId, entityName);
    }
}
