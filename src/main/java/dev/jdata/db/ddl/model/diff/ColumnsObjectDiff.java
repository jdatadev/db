package dev.jdata.db.ddl.model.diff;

import java.util.Objects;

import dev.jdata.db.schema.model.objects.Column;
import dev.jdata.db.schema.model.objects.ColumnsObject;
import dev.jdata.db.utils.adt.contains.IContainsView;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;
import dev.jdata.db.utils.adt.lists.IBaseIndexList;
import dev.jdata.db.utils.adt.lists.IBaseIndexListAllocator;
import dev.jdata.db.utils.adt.lists.IIndexListBuilder;
import dev.jdata.db.utils.adt.sets.IHeapIntSet;
import dev.jdata.db.utils.adt.sets.IBaseIntSet;
import dev.jdata.db.utils.checks.Checks;

public abstract class ColumnsObjectDiff extends SchemaObjectDiff {

    private final IHeapIndexList<Column> addedColumns;
    private final IHeapIndexList<Column> modifiedColumns;
    private final IHeapIntSet droppedColumns;

    ColumnsObjectDiff(ColumnsObject columnsObject, IHeapIndexList<Column> addedColumns, IHeapIndexList<Column> modifiedColumns, IHeapIntSet droppedColumns) {
        super(columnsObject.getParsedName(), columnsObject.getHashName(), columnsObject.getId());

        if (IContainsView.isNullOrEmpty(addedColumns) && IContainsView.isNullOrEmpty(modifiedColumns) && IContainsView.isNullOrEmpty(droppedColumns)) {

            throw new IllegalArgumentException();
        }

        this.addedColumns = addedColumns;
        this.modifiedColumns = modifiedColumns;
        this.droppedColumns = droppedColumns;
    }

    final IBaseIndexList<Column> getAddedColumns() {
        return addedColumns;
    }

    final IBaseIndexList<Column> getModifiedColumns() {
        return modifiedColumns;
    }

    final IBaseIntSet getDroppedColumns() {
        return droppedColumns;
    }

    @FunctionalInterface
    interface ColumnsObjectFactory<T extends ColumnsObject> {

        T createFrom(T from, IHeapIndexList<Column> columns);
    }

    final <T extends ColumnsObject, U extends IIndexListBuilder<Column, ?>> T applyToColumnsObject(T columnsObject, ColumnsObjectFactory<T> columnsObjectFactory,
            IIndexListAllocator<Column, ?, U> columnIndexListAllocator) {

        Objects.requireNonNull(columnsObject);
        Objects.requireNonNull(columnsObjectFactory);
        Objects.requireNonNull(columnIndexListAllocator);
        Checks.areEqual(getId(), columnsObject.getId());
        Checks.areEqual(getHashName(), columnsObject.getHashName());

        final T result;

        final int numColumns = columnsObject.getNumColumns();

        final U columnsBuilder = columnIndexListAllocator.createBuilder(numColumns);

        try {
            final IBaseIntSet dropped = droppedColumns;
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

            final IHeapIndexList<Column> columns = columnsBuilder.buildHeapAllocated();

            result = columnsObjectFactory.createFrom(columnsObject, columns);
        }
        finally {

            columnIndexListAllocator.freeBuilder(columnsBuilder);
        }

        return result;
    }
}
