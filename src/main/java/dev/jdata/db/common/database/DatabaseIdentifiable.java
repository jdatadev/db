package dev.jdata.db.common.database;

import java.util.Objects;

import dev.jdata.db.schema.DatabaseId;
import dev.jdata.db.utils.allocators.Allocatable;

public abstract class DatabaseIdentifiable extends Allocatable implements IDatabaseIdentifiable {

    private final DatabaseId databaseId;

    protected DatabaseIdentifiable(AllocationType allocationType, DatabaseId databaseId) {
        super(allocationType);

        this.databaseId = Objects.requireNonNull(databaseId);
    }

    @Override
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
            final DatabaseIdentifiable other = (DatabaseIdentifiable)object;

            result = databaseId.equals(other.databaseId);
        }

        return result;
    }
}
