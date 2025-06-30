package dev.jdata.db.ddl;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.DBNamedIdentifiableObject;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.adt.IContains;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.sets.IHeapIntSet;
import dev.jdata.db.utils.adt.sets.IIntSet;
import dev.jdata.db.utils.checks.Checks;

final class TableDiff extends DBNamedIdentifiableObject {

    private final IHeapIndexList<Column> addedColumns;
    private final IHeapIndexList<Column> modifiedColumns;
    private final IHeapIntSet droppedColumns;

    static TableDiff ofAddedColumns(Table table, IHeapIndexList<Column> addedColumns) {

        Objects.requireNonNull(table);
        Checks.isNotEmpty(addedColumns);

        return new TableDiff(table, addedColumns, null, null);
    }

    static TableDiff ofModifiedColumns(Table table, IHeapIndexList<Column> modifiedColumns) {

        Objects.requireNonNull(table);
        Checks.isNotEmpty(modifiedColumns);

        return new TableDiff(table, null, modifiedColumns, null);
    }

    static TableDiff ofDroppedColumns(Table table, IHeapIntSet droppedColumns) {

        Objects.requireNonNull(table);
        Checks.isNotEmpty(droppedColumns);

        return new TableDiff(table, null, null, droppedColumns);
    }

    private TableDiff(Table table, IHeapIndexList<Column> addedColumns, IHeapIndexList<Column> modifiedColumns, IHeapIntSet droppedColumns) {
        super(table.getParsedName(), table.getHashName(), table.getId());

        if (IContains.isNullOrEmpty(addedColumns) && IContains.isNullOrEmpty(modifiedColumns) && IContains.isNullOrEmpty(droppedColumns)) {

            throw new IllegalArgumentException();
        }

        this.addedColumns = addedColumns;
        this.modifiedColumns = modifiedColumns;
        this.droppedColumns = droppedColumns;
    }

    IIndexList<Column> getAddedColumns() {
        return addedColumns;
    }

    IIndexList<Column> getModifiedColumns() {
        return modifiedColumns;
    }

    IIntSet getDroppedColumns() {
        return droppedColumns;
    }
}
