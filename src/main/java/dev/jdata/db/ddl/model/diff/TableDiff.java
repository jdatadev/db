package dev.jdata.db.ddl.model.diff;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IHeapIntSet;
import dev.jdata.db.utils.checks.Checks;

public final class TableDiff extends ColumnsObjectDiff {

    public static TableDiff ofAddedColumns(Table table, IHeapIndexList<Column> addedColumns) {

        Objects.requireNonNull(table);
        Checks.isNotEmpty(addedColumns);

        return new TableDiff(table, addedColumns, null, null);
    }

    public static TableDiff ofModifiedColumns(Table table, IHeapIndexList<Column> modifiedColumns) {

        Objects.requireNonNull(table);
        Checks.isNotEmpty(modifiedColumns);

        return new TableDiff(table, null, modifiedColumns, null);
    }

    public static TableDiff ofDroppedColumns(Table table, IHeapIntSet droppedColumns) {

        Objects.requireNonNull(table);
        Checks.isNotEmpty(droppedColumns);

        return new TableDiff(table, null, null, droppedColumns);
    }

    private TableDiff(Table table, IHeapIndexList<Column> addedColumns, IHeapIndexList<Column> modifiedColumns, IHeapIntSet droppedColumns) {
        super(table, addedColumns, modifiedColumns, droppedColumns);
    }

    <U extends IIndexListBuilder<Column, ?, ? extends IHeapIndexList<Column>>> Table applyToTable(Table table, IIndexListAllocator<Column, ?, ?, U> columnIndexListAllocator) {

        return applyToColumnsObject(table, Table::makeCopy, columnIndexListAllocator);
    }
}
