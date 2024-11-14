package dev.jdata.db.schema;

import java.util.List;

import dev.jdata.db.utils.adt.lists.Lists;
import dev.jdata.db.utils.checks.Checks;

abstract class ColumnsObject extends SchemaObject {

    private final List<Column> columns;

    private final int numNullableColumns;

    ColumnsObject(String name, int id, List<Column> columns) {
        super(name, id);

        Checks.isNotEmpty(columns);

        this.columns = Lists.unmodifiableCopyOf(columns);

        this.numNullableColumns = (int)columns.stream()
                .filter(Column::isNullable)
                .count();
    }

    public final int getNumColumns() {

        return columns.size();
    }

    public final Column getColumn(int index) {

        return columns.get(index);
    }

    public final int getNumNullableColumns() {
        return numNullableColumns;
    }

    @Override
    public final String toString() {

        return getClass().getSimpleName() + " [getId()=" + getId() + ", columns=" + columns + ", numNullableColumns=" + numNullableColumns + "]";
    }
}
