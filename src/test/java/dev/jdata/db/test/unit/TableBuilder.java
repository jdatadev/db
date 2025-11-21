package dev.jdata.db.test.unit;

import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.engine.database.HashNameStrings;
import dev.jdata.db.engine.database.IStringStorer;
import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.schema.types.SchemaDataType;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexListBuilder;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.checks.Checks;

public final class TableBuilder {

    public static TableBuilder create(String tableName, int tableId, IStringStorer stringStorer) {

        return new TableBuilder(tableName, tableId, stringStorer);
    }

    private final String tableName;
    private final int tableId;

    private final IStringStorer stringStorer;
    private final IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>> columnsBuilder;

    private int columnIdSequenceNo;

    private TableBuilder(String tableName, int tableId) {
        this(tableName, tableId, IStringStorer.create(1, 10));
    }

    private TableBuilder(String tableName, int tableId, IStringStorer stringStorer) {

        this.tableName = Checks.isTableName(tableName);
        this.tableId = Checks.isTableId(tableId);
        this.stringStorer = Objects.requireNonNull(stringStorer);

        this.columnsBuilder = IHeapIndexListBuilder.create(Column[]::new);

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

        return new Table(stringStorer.getOrAddStringRef(name), stringStorer.getOrAddStringRef(hashTableName), tableId, columnsBuilder.buildHeapAllocatedNotEmpty());
    }
}
