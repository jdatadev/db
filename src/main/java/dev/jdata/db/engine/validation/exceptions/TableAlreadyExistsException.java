package dev.jdata.db.engine.validation.exceptions;

import dev.jdata.db.schema.DatabaseId;

public final class TableAlreadyExistsException extends SchemaEntityAlreadyExistsException {

    private static final long serialVersionUID = 1L;

    public TableAlreadyExistsException(DatabaseId databaseId, long entityName) {
        super(databaseId, entityName);
    }
}
