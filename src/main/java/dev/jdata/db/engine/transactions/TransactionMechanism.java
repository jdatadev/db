package dev.jdata.db.engine.transactions;

import java.util.Objects;

import dev.jdata.db.data.BaseRowMap;
import dev.jdata.db.data.locktable.LockTable;
import dev.jdata.db.dml.DMLInsertUpdateRows;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.adt.arrays.ILongArray;
import dev.jdata.db.utils.checks.Checks;

public abstract class TransactionMechanism<T> extends BaseRowMap implements TransactionOperations<T> {

    static void checkParameters(LockTable lockTable, Table table, int statementId, ILongArray rowIds, DMLInsertUpdateRows<?> rows) {

        checkParameters(lockTable, table, statementId, rows);
        Objects.requireNonNull(rowIds);
    }

    static void checkParameters(LockTable lockTable, Table table, int statementId, DMLInsertUpdateRows<?> rows) {

        Objects.requireNonNull(lockTable);
        Objects.requireNonNull(table);
        Objects.requireNonNull(rows);
        Checks.isExactlyOne(rows.getNumElements());
    }
}
