package dev.jdata.db.schema.model.objects;

import dev.jdata.db.utils.adt.lists.IIndexList;

public final class TableDiff extends BaseTable {

    public TableDiff(long parsedName, long hashName, int id, IIndexList<Column> columns) {
        super(parsedName, hashName, id, columns);
    }

    private TableDiff(TableDiff toCopy, IIndexList<Column> columns) {
        super(toCopy, columns);
    }

    @Override
    public TableDiff makeCopy(IIndexList<Column> columns) {

        return new TableDiff(this, columns);
    }
}
