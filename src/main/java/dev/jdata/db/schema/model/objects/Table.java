package dev.jdata.db.schema.model.objects;

import java.util.Objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.utils.adt.lists.IHeapIndexList;

public final class Table extends BaseTable {

    public static final int INITIAL_TABLE_ID = DBConstants.INITIAL_SCHEMA_OBJECT_ID;

    public Table(long parsedName, long hashName, int id, IHeapIndexList<Column> columns) {
        super(parsedName, hashName, id, columns);
    }

    private Table(Table toCopy, IHeapIndexList<Column> columns) {
        super(toCopy, columns);
    }

    private Table(Table toCopy, int newSchemaObjectId) {
        super(toCopy, newSchemaObjectId);
    }

    public boolean hasSyntheticPrimaryKey() {

        throw new UnsupportedOperationException();
    }

    @Override
    public SchemaObject recreateWithNewShemaObjectId(int newSchemaObjectId) {

        return new Table(this, newSchemaObjectId);
    }

    @Override
    public DDLObjectType getDDLObjectType() {

        return DDLObjectType.TABLE;
    }

    @Override
    public Table makeCopy(IHeapIndexList<Column> columns) {

        Objects.requireNonNull(columns);

        return new Table(this, columns);
    }

    @Override
    public <P, R> R visit(SchemaObjectVisitor<P, R> visitor, P parameter) {

        return visitor.onTable(this, parameter);
    }
}
