package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.utils.allocators.Allocatable;

public abstract class DatabaseSchemaObject extends Allocatable {

    private final DatabaseId databaseId;

    protected DatabaseSchemaObject(DatabaseId databaseId) {

        this.databaseId = Objects.requireNonNull(databaseId);
    }

    public final DatabaseId getDatabaseId() {
        return databaseId;
    }
}
