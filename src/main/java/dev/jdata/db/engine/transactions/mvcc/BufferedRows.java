package dev.jdata.db.engine.transactions.mvcc;

import dev.jdata.db.engine.transactions.SelectColumn;

public interface BufferedRows {

    boolean compareColumn(int tableId, long rowId, SelectColumn selectColumn, int tableColumn);
}
