package dev.jdata.db.engine.validation.exceptions;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseId;

abstract class DatabaseValidationException extends SQLValidationException {

    private static final long serialVersionUID = 1L;

    private final DatabaseId databaseId;

    DatabaseValidationException(DatabaseId databaseId) {

        this.databaseId = Objects.requireNonNull(databaseId);
    }

    public final DatabaseId getDatabaseId() {
        return databaseId;
    }
}
