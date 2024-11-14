package dev.jdata.db.schema;

import java.util.Objects;

import dev.jdata.db.utils.adt.collections.Coll;
import dev.jdata.db.utils.checks.Checks;

public final class DatabaseSchema {

    private final DatabaseSchemaVersion version;

    private final SchemaMap<Table> tables;
    private final SchemaMap<View> views;

    private final int maxTableId;

    public DatabaseSchema(DatabaseSchemaVersion version, SchemaMap<Table> tables, SchemaMap<View> views) {

        Objects.requireNonNull(version);
        Objects.requireNonNull(tables);
        Objects.requireNonNull(views);

        this.version = version;
        this.tables = tables.makeCopy();
        this.views = views.makeCopy();

        this.maxTableId = Coll.max(tables.getSchemaObjects(), -1, Table::getId);
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

    @Override
    public String toString() {

        return getClass().getSimpleName() + " [version=" + version + ", tables=" + tables + ", views=" + views + ", maxTableId=" + maxTableId + "]";
    }
}
