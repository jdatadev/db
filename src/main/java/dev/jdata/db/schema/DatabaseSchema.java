package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.utils.adt.collections.Coll;
import dev.jdata.db.utils.checks.Checks;

public final class DatabaseSchema {

    private final String databaseName;
    private final DatabaseSchemaVersion version;

    private final SchemaMap<Table> tables;
    private final SchemaMap<View> views;

    private final int maxTableId;

    public static DatabaseSchema empty(String databaseName, DatabaseSchemaVersion version) {

        Checks.isDatabaseName(databaseName);
        Objects.requireNonNull(version);

        return new DatabaseSchema(databaseName, version, SchemaMap.empty(), SchemaMap.empty());
    }

    public DatabaseSchema(String databaseName, DatabaseSchemaVersion version, SchemaMap<Table> tables, SchemaMap<View> views) {

        Checks.isDatabaseName(databaseName);
        Objects.requireNonNull(version);
        Objects.requireNonNull(tables);
        Objects.requireNonNull(views);

        this.databaseName = databaseName;
        this.version = version;
        this.tables = tables.makeCopy();
        this.views = views.makeCopy();

        this.maxTableId = Coll.max(tables.getSchemaObjects(), -1, Table::getId);
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public DatabaseSchemaVersion getVersion() {
        return version;
    }

    public Table getTable(int tableId) {

        Checks.isTableId(tableId);

        return tables.getSchemaObjectById(tableId);
    }

    public SchemaMapGetters<Table> getTables() {
        return tables;
    }

    public SchemaMapGetters<View> getViews() {
        return views;
    }

    public int getMaxTableId() {
        return maxTableId;
    }

    DatabaseSchema addTable(Table table, DatabaseSchemaVersion newSchemaVersion) {

        Objects.requireNonNull(table);
        Checks.areEqual(newSchemaVersion.getVersionNumber(), version.getVersionNumber() + 1);

        final SchemaMap<Table> tablesCopy = tables.add(table);

        return new DatabaseSchema(databaseName, version, tablesCopy, views);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [version=" + version + ", tables=" + tables + ", views=" + views + ", maxTableId=" + maxTableId + "]";
    }
}
