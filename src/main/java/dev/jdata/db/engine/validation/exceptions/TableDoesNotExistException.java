package dev.jdata.db.engine.validation.exceptions;

import dev.jdata.db.schema.DatabaseId;

public final class TableDoesNotExistException extends SchemaObjectDoesNotExistException {

    private static final long serialVersionUID = 1L;

    public TableDoesNotExistException(DatabaseId databaseId, long tableName) {
        super(databaseId, tableName);
    }
}
