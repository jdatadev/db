package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable;

public abstract class DatabaseSchemaObject extends Allocatable {

    private final DatabaseId databaseId;

    protected DatabaseSchemaObject(AllocationType allocationType, DatabaseId databaseId) {
        super(allocationType);

        this.databaseId = Objects.requireNonNull(databaseId);
    }

    public final DatabaseId getDatabaseId() {
        return databaseId;
    }

    @Override
    public boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (object == null) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final DatabaseSchemaObject other = (DatabaseSchemaObject)object;

            result = databaseId.equals(other.databaseId);
        }

        return result;
    }
}
