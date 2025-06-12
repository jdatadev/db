package dev.jdata.db.schema.model.objects;

import java.util.Objects;

import org.jutils.io.strings.StringResolver;
import org.jutils.io.strings.StringResolver.RefStringResolver;
import org.jutils.io.strings.StringResolver.ToStringWithStringResolver;

import dev.jdata.db.DBConstants;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.checks.Checks;
import dev.jdata.db.utils.scalars.Integers;

public abstract class ColumnsObject extends SchemaObject implements ToStringWithStringResolver {

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

    public final int getMaxColumnId() {

        return columns.maxInt(DBConstants.INITIAL_COLUMN_ID - 1, Column::getId);
    }

    public final int getNumNullableColumns() {
        return numNullableColumns;
    }

    public final boolean equals(StringResolver thisStringResolver, ColumnsObject other, StringResolver otherStringResolver, boolean caseSensitive) {

        Objects.requireNonNull(thisStringResolver);
        Objects.requireNonNull(other);
        Objects.requireNonNull(otherStringResolver);

        final boolean result;

        if (this == other) {

            result = true;
        }
        else if (!super.equals(thisStringResolver, other, otherStringResolver, caseSensitive)) {

            result = false;
        }
        else if (getClass() != other.getClass()) {

            result = false;
        }
        else {
            result = equalsInstanceVariables(other);
        }

        return result;
    }

    @Override
    public boolean equals(Object object) {

        final boolean result;

        if (this == object) {

            result = true;
        }
        else if (!super.equals(object)) {

            result = false;
        }
        else if (getClass() != object.getClass()) {

            result = false;
        }
        else {
            final ColumnsObject other = (ColumnsObject)object;

            result = equalsInstanceVariables(other) && Objects.equals(columns, other.columns);
        }

        return result;
    }

    private boolean equalsInstanceVariables(ColumnsObject other) {

        return numNullableColumns == other.numNullableColumns;
    }

    @Override
    public final void toString(StringResolver stringResolver, StringBuilder sb) {

        Objects.requireNonNull(stringResolver);
        Objects.requireNonNull(sb);

        sb.append(getClass().getSimpleName()).append(" [getId()=").append(getId()).append(", columns=");

        columns.toString(sb, stringResolver, (c, b, p) -> c.toString(p, b));

        sb.append(", numNullableColumns=").append(numNullableColumns).append("]");
    }

    @Override
    public String toString() {

        return toString(RefStringResolver.INSTANCE);
    }
}
