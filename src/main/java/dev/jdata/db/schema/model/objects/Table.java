package dev.jdata.db.schema.model.objects;

import dev.jdata.db.DBConstants;
import dev.jdata.db.utils.adt.lists.IIndexList;
import dev.jdata.db.utils.checks.Checks;

public final class Table extends ColumnsObject {

    public static final int INITIAL_TABLE_ID = DBConstants.INITIAL_SCHEMA_OBJECT_ID;

    public Table(long parsedName, long hashName, int id, IIndexList<Column> columns) {
        super(parsedName, hashName, id, columns);

        Checks.isTableId(id);
    }

    private Table(Table toCopy, IIndexList<Column> columns) {
        super(toCopy, columns);
    }

    @Override
    public ColumnsObject makeCopy(IIndexList<Column> columns) {

        return new Table(this, columns);
    }

    public boolean hasSyntheticPrimaryKey() {

        throw new UnsupportedOperationException();
    }
}
