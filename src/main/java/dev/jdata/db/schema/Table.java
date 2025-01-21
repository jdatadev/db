package dev.jdata.db.schema;

import java.util.List;

import dev.jdata.db.utils.checks.Checks;

public final class Table extends ColumnsObject {

    public static final int INITIAL_TABLE_ID = 0;

    public Table(String name, int id, List<Column> columns) {
        super(name, id, columns);

        Checks.isTableId(id);
    }

    public boolean hasSyntheticPrimaryKey() {

        throw new UnsupportedOperationException();
    }
}
