package dev.jdata.db.data;

import dev.jdata.db.DBBitEncoding;
import dev.jdata.db.utils.checks.Checks;

public abstract class BaseRowMap extends DBBitEncoding {

    protected static void checkParameters(int tableId, long rowId) {

        Checks.isTableId(tableId);

        if (tableId > MAX_TABLE_ID) {

            throw new IllegalArgumentException();
        }

        Checks.isRowId(rowId);

        if (rowId > MAX_ROW_ID) {

            throw new IllegalArgumentException();
        }
    }
}
