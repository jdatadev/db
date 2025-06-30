package dev.jdata.db.schema.model.objects;

import java.util.Objects;

import org.jutils.io.strings.StringResolver;
import org.jutils.io.strings.StringResolver.RefStringResolver;

import dev.jdata.db.utils.adt.elements.ByIndex;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.scalars.Integers;

abstract class NullableColumnsObject extends ColumnsObject {

    private final int numNullableColumns;

    NullableColumnsObject(long parsedName, long hashName, int id, IIndexList<Column> columns) {
        super(parsedName, hashName, id, columns);

        this.numNullableColumns = countNullableColumns(columns);
    }

    NullableColumnsObject(SchemaObject toCopy, IIndexList<Column> columns) {
        super(toCopy, columns);

        this.numNullableColumns = countNullableColumns(columns);
    }

    private static int countNullableColumns(IIndexList<Column> columns) {

        return Integers.checkUnsignedLongToUnsignedInt(columns.closureOrConstantCount(Column::isNullable));
    }

    public final int getNumNullableColumns() {
        return numNullableColumns;
    }

    public final boolean equals(StringResolver thisStringResolver, NullableColumnsObject other, StringResolver otherStringResolver, boolean caseSensitive) {

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
            final NullableColumnsObject other = (NullableColumnsObject)object;

            result = equalsInstanceVariables(other);
        }

        return result;
    }

    private boolean equalsInstanceVariables(NullableColumnsObject other) {

        return numNullableColumns == other.numNullableColumns;
    }

    @Override
    public final void toString(StringResolver stringResolver, StringBuilder sb) {

        Objects.requireNonNull(stringResolver);
        Objects.requireNonNull(sb);

        sb.append(getClass().getSimpleName()).append(" [getId()=").append(getId()).append(", getColumns()=");

        ByIndex.toString(this, 0L, getNumColumns(), sb, stringResolver, (o, i, b, s) -> o.getColumn((int)i).toString(s));

        sb.append(", numNullableColumns=").append(numNullableColumns).append(']');
    }

    @Override
    public String toString() {

        return toString(RefStringResolver.INSTANCE);
    }
}
