package dev.jdata.db.engine.validation.exceptions;

import dev.jdata.db.schema.DatabaseId;

abstract class SchemaColumnException extends SchemaEntityException {

    private static final long serialVersionUID = 1L;

    SchemaColumnException(DatabaseId databaseId, long columnName) {
        super(databaseId, columnName);
    }
}
