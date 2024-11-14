package dev.jdata.db.schema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;

import dev.jdata.db.utils.adt.ForEachSequenceElement;
import dev.jdata.db.utils.adt.arrays.TwoDimensionalArray;
import dev.jdata.db.utils.adt.collections.Coll;

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

    public static VersionedDatabaseSchemas of(Collection<DatabaseSchema> schemas) {

        Objects.requireNonNull(schemas);

        return new VersionedDatabaseSchemas(schemas);
    }

    private final LinkedHashMap<DatabaseSchemaVersion, DatabaseSchema> schemasByVersion;
    private final TwoDimensionalArray<VersionedTable> tableSchemasOrderedByVersion;

    VersionedDatabaseSchemas(Collection<DatabaseSchema> schemas) {

        Objects.requireNonNull(schemas);

        this.schemasByVersion = new LinkedHashMap<>(schemas.size());
        this.tableSchemasOrderedByVersion = new TwoDimensionalArray<>(100, VersionedTable[][]::new, 10, VersionedTable[]::new);

        final List<DatabaseSchema> sorted = Coll.unmodifiableSorted(schemas, (s1, s2) -> s1.getVersion().compareTo(s2.getVersion()));

        for (DatabaseSchema schema : sorted) {

            final DatabaseSchemaVersion schemaVersion = schema.getVersion();

            if (schemasByVersion.containsKey(schemaVersion)) {

                throw new IllegalArgumentException();
            }

            schemasByVersion.put(schemaVersion, schema);

            for (Table table : schema.getTables().getSchemaObjects()) {

                tableSchemasOrderedByVersion.addWithOuterExpand(table.getId(), new VersionedTable(table, schemaVersion));
            }
        }
    }

    public synchronized boolean containsVersion(DatabaseSchemaVersion version) {

        Objects.requireNonNull(version);

        return schemasByVersion.containsKey(version);
    }

    public synchronized Iterable<DatabaseSchemaVersion> getVersions() {

        return new ArrayList<>(schemasByVersion.keySet());
    }

    public synchronized DatabaseSchema getSchema(DatabaseSchemaVersion version) {

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
