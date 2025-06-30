package dev.jdata.db.engine.validation.exceptions;

import dev.jdata.db.schema.DatabaseId;

public final class ColumnDoesNotExistException extends SchemaEntityDoesNotExistException {

    private static final long serialVersionUID = 1L;

    public ColumnDoesNotExistException(DatabaseId databaseId, long entityName) {
        super(databaseId, entityName);
    }
}
