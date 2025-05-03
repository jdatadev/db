package dev.jdata.db.schema.model.objects;

import java.util.Objects;

import org.jutils.io.strings.StringResolver;

import dev.jdata.db.DBConstants;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class ColumnsObject extends SchemaObject {

    private final IIndexList<Column> columns;

    private final int numNullableColumns;

    public abstract ColumnsObject makeCopy(IIndexList<Column> columns);

    ColumnsObject(long parsedName, long hashName, int id, IIndexList<Column> columns) {
        super(parsedName, hashName, id);

        Checks.isNotEmpty(columns);
        Checks.isLessThanOrEqualTo(columns.getNumElements(), DBConstants.MAX_COLUMNS);

        this.columns = columns;

        this.numNullableColumns = countNullableColumns(columns);
    }


    public ColumnsObject(SchemaObject toCopy, IIndexList<Column> columns) {
        super(toCopy);

        this.columns = columns;

        this.numNullableColumns = countNullableColumns(columns);
    }

    private static int countNullableColumns(IIndexList<Column> columns) {

        return Integers.checkUnsignedLongToUnsignedInt(columns.countWithClosure(Column::isNullable));
    }

    public final int getNumColumns() {

        return Integers.checkUnsignedLongToUnsignedInt(columns.getNumElements());
    }

    public final Column getColumn(int index) {

        return columns.get(index);
    }

    public final int getNumNullableColumns() {
        return numNullableColumns;
    }

    public final String toString(StringResolver stringResolver) {

        Objects.requireNonNull(stringResolver);

        return getClass().getSimpleName() + " [getId()=" + getId() + ", columns=" + columns + ", numNullableColumns=" + numNullableColumns + "]";
    }
}
