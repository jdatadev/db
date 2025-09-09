package dev.jdata.db.test.unit;

import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.database.HashNameStrings;
import dev.jdata.db.engine.database.StringStorer;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListBuilder;
import dev.jdata.db.utils.checks.Checks;

public final class TableBuilder {

    public static TableBuilder create(String tableName, int tableId, StringStorer stringStorer) {

        return new TableBuilder(tableName, tableId, stringStorer);
    }

    private final String tableName;
    private final int tableId;

    private final StringStorer stringStorer;
    private final IndexListBuilder<Column, ?, ?> columnsBuilder;

    private int columnIdSequenceNo;

    private TableBuilder(String tableName, int tableId) {
        this(tableName, tableId, new StringStorer(1, 10));
    }

    private TableBuilder(String tableName, int tableId, StringStorer stringStorer) {

        this.tableName = Checks.isTableName(tableName);
        this.tableId = Checks.isTableId(tableId);
        this.stringStorer = Objects.requireNonNull(stringStorer);

        this.columnsBuilder = IndexList.createBuilder(Column[]::new);

        this.columnIdSequenceNo = DBConstants.INITIAL_COLUMN_ID;
    }

    public TableBuilder addColumn(String columnName, SchemaDataType schemaDataType) {

        Checks.isColumnName(columnName);
        Objects.requireNonNull(schemaDataType);

        final String hashColumnName = HashNameStrings.getHashNameString(columnName);

        final Column column = new Column(stringStorer.getOrAddStringRef(columnName), stringStorer.getOrAddStringRef(hashColumnName), columnIdSequenceNo ++, schemaDataType);

        columnsBuilder.addTail(column);

        return this;
    }

    public Table build() {

        final String name = tableName;

        final String hashTableName = HashNameStrings.getHashNameString(name);

        return new Table(stringStorer.getOrAddStringRef(name), stringStorer.getOrAddStringRef(hashTableName), tableId, columnsBuilder.buildHeapAllocated());
    }
}
