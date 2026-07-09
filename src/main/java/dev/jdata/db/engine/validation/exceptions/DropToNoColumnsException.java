package dev.jdata.db.engine.validation.exceptions;

import dev.jdata.db.schema.DatabaseId;

public final class DropToNoColumnsException extends SchemaColumnException {

    private static final long serialVersionUID = 1L;

    public DropToNoColumnsException(DatabaseId databaseId, long columnName) {
        super(databaseId, columnName);
    }
}
