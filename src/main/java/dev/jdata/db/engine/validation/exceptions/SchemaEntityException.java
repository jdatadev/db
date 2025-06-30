package dev.jdata.db.engine.validation.exceptions;

import org.jutils.io.strings.StringRef;

import dev.jdata.db.schema.DatabaseId;

abstract class SchemaEntityException extends DatabaseValidationException {

    private static final long serialVersionUID = 1L;

    private final long entityName;

    SchemaEntityException(DatabaseId databaseId, long entityName) {
        super(databaseId);

        this.entityName = StringRef.checkIsString(entityName);
    }

    public final long getEntityName() {
        return entityName;
    }
}
