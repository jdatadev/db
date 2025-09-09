package dev.jdata.db.ddl.model.diff;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.utils.adt.IContains;
import dev.jdata.db.utils.adt.lists.HeapIndexList;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.adt.lists.IndexList;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListAllocator;
import dev.jdata.db.utils.adt.lists.IndexList.IndexListBuilder;
import dev.jdata.db.utils.adt.sets.IHeapIntSet;
import dev.jdata.db.utils.adt.sets.IIntSet;
import dev.jdata.db.utils.checks.Checks;

public abstract class ColumnsObjectDiff extends SchemaObjectDiff {

    private final IHeapIndexList<Column> addedColumns;
    private final IHeapIndexList<Column> modifiedColumns;
    private final IHeapIntSet droppedColumns;

    ColumnsObjectDiff(ColumnsObject columnsObject, IHeapIndexList<Column> addedColumns, IHeapIndexList<Column> modifiedColumns, IHeapIntSet droppedColumns) {
        super(columnsObject.getParsedName(), columnsObject.getHashName(), columnsObject.getId());

        if (IContains.isNullOrEmpty(addedColumns) && IContains.isNullOrEmpty(modifiedColumns) && IContains.isNullOrEmpty(droppedColumns)) {

            throw new IllegalArgumentException();
        }

        this.addedColumns = addedColumns;
        this.modifiedColumns = modifiedColumns;
        this.droppedColumns = droppedColumns;
    }

    final IIndexList<Column> getAddedColumns() {
        return addedColumns;
    }

    final IIndexList<Column> getModifiedColumns() {
        return modifiedColumns;
    }

    final IIntSet getDroppedColumns() {
        return droppedColumns;
    }

    @FunctionalInterface
    interface ColumnsObjectFactory<T extends ColumnsObject> {

        T createFrom(T from, HeapIndexList<Column> columns);
    }

    final <T extends ColumnsObject, U extends IndexListBuilder<Column, ?, U>> T applyToColumnsObject(T columnsObject, ColumnsObjectFactory<T> columnsObjectFactory,
            IndexListAllocator<Column, ?, U, ?> columnIndexListAllocator) {

        Objects.requireNonNull(columnsObject);
        Objects.requireNonNull(columnsObjectFactory);
        Objects.requireNonNull(columnIndexListAllocator);
        Checks.areEqual(getId(), columnsObject.getId());
        Checks.areEqual(getHashName(), columnsObject.getHashName());

        final T result;

        final int numColumns = columnsObject.getNumColumns();

        final U columnsBuilder = IndexList.createBuilder(numColumns, columnIndexListAllocator);

        try {
            final IIntSet dropped = droppedColumns;
            final IIndexList<Column> modified = modifiedColumns;

            for (int i = 0; i < numColumns; ++ i) {

                final Column column = columnsObject.getColumn(i);

                if (!dropped.contains(column.getId())) {

                    final Column modifiedColumn = modified.findAtMostOne(column, (c, p) -> c.getId() == p.getId());

                    columnsBuilder.addTail(modifiedColumn != null ? modifiedColumn : column);
                }
            }

            final IIndexList<Column> added = addedColumns;
            final long numAddedColumns = added.getNumElements();

            for (int i = 0; i < numAddedColumns; ++ i) {

                columnsBuilder.addTail(added.get(i));
            }

            final HeapIndexList<Column> columns = columnsBuilder.buildHeapAllocated();

            result = columnsObjectFactory.createFrom(columnsObject, columns);
        }
        finally {

            columnIndexListAllocator.freeIndexListBuilder(columnsBuilder);
        }

        return result;
    }
}
