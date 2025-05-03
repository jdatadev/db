package dev.jdata.db.schema;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.schema.model.IDatabaseSchema;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.adt.ForEachSequenceElement;
import dev.jdata.db.utils.adt.arrays.TwoDimensionalArray;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.scalars.Integers;

public final class VersionedDatabaseSchemas {

    public static final class VersionedTable {

        private final Table table;
        private final DatabaseSchemaVersion schemaVersion;

        private VersionedTable(Table table, DatabaseSchemaVersion schemaVersion) {

            this.table = Objects.requireNonNull(table);
            this.schemaVersion = Objects.requireNonNull(schemaVersion);
        }

        public Table getTable() {
            return table;
        }

        public DatabaseSchemaVersion getSchemaVersion() {
            return schemaVersion;
        }
    }

    public static VersionedDatabaseSchemas of(DatabaseId databaseId, IIndexList<IDatabaseSchema> schemas, IndexListAllocator<IDatabaseSchema> indexListAllocator) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(schemas);
        Objects.requireNonNull(indexListAllocator);

        return new VersionedDatabaseSchemas(databaseId, schemas, indexListAllocator);
    }

    private final DatabaseId databaseId;
    private final LinkedHashMap<DatabaseSchemaVersion, IDatabaseSchema> schemasByVersion;
    private final TwoDimensionalArray<VersionedTable> tableSchemasOrderedByVersion;

    VersionedDatabaseSchemas(DatabaseId databaseId, IIndexList<IDatabaseSchema> schemas, IndexListAllocator<IDatabaseSchema> indexListAllocator) {

        Objects.requireNonNull(databaseId);
        Objects.requireNonNull(schemas);
        Objects.requireNonNull(indexListAllocator);

        this.databaseId = databaseId;

        final int numElements = Integers.checkUnsignedLongToUnsignedInt(schemas.getNumElements());

        this.schemasByVersion = new LinkedHashMap<>(numElements);
        this.tableSchemasOrderedByVersion = new TwoDimensionalArray<>(100, VersionedTable[][]::new, 10, VersionedTable[]::new);

        final IIndexList<IDatabaseSchema> sorted = schemas.sorted((s1, s2) -> s1.getVersion().compareTo(s2.getVersion()), IDatabaseSchema[]::new, indexListAllocator);

        for (long i = 0; i < numElements; ++ i) {

            final IDatabaseSchema schema = sorted.get(i);

            final DatabaseSchemaVersion schemaVersion = schema.getVersion();

            if (schemasByVersion.containsKey(schemaVersion)) {

                throw new IllegalArgumentException();
            }

            schemasByVersion.put(schemaVersion, schema);

            schema.getTables().forEach(tableSchemasOrderedByVersion, (t, a) ->  a.addWithOuterExpand(t.getId(), new VersionedTable(t, schemaVersion)));
        }
    }

    public DatabaseId getDatabaseId() {
        return databaseId;
    }

    public synchronized boolean containsVersion(DatabaseSchemaVersion version) {

        Objects.requireNonNull(version);

        return schemasByVersion.containsKey(version);
    }

    public synchronized Iterable<DatabaseSchemaVersion> getVersions() {

        return new ArrayList<>(schemasByVersion.keySet());
    }

    public synchronized IDatabaseSchema getSchema(DatabaseSchemaVersion version) {

        Objects.requireNonNull(version);

        return schemasByVersion.get(version);
    }

    public synchronized List<VersionedTable> getTableVersions(int tableId) {

        return tableSchemasOrderedByVersion.toUnmodifiableList(tableId);
    }

    public synchronized int getNumKnownTables() {

        return tableSchemasOrderedByVersion.getNumOuterElements();
    }

    public synchronized void forEachVersionedTable(ForEachSequenceElement<VersionedTable> forEach) {

        Objects.requireNonNull(forEach);

        tableSchemasOrderedByVersion.forEachElement(forEach);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [schemasByVersion=" + schemasByVersion + ", tableSchemasOrderedByVersion=" + tableSchemasOrderedByVersion + "]";
    }
}
