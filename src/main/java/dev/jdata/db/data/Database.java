package dev.jdata.db.data;

import java.util.Objects;

import dev.jdata.db.data.cache.DataCache;
import dev.jdata.db.schema.DatabaseSchema;

public final class Database {

    private final DatabaseSchema schema;
    private final Indices indices;

    private final DataCache dataCache;

    Database(DatabaseSchema databaseSchema, DataCache dataCache) {

        this.schema = Objects.requireNonNull(databaseSchema);
        this.dataCache = Objects.requireNonNull(dataCache);

        this.indices = new Indices();
    }
}
