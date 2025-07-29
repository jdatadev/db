package dev.jdata.db.engine.transactions;

import dev.jdata.db.dml.DMLInsertRows;
import dev.jdata.db.dml.DMLUpdateRows;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.adt.arrays.ILongArrayCommon;

public interface TransactionDMLOperations<T> {

    enum OperationResult {

        SUCCESS,
        LOCK_RETRY
    }

    OperationResult insertRows(T sharedState, Table table, int statementId, ILongArrayCommon rowIds, DMLInsertRows rows);

    OperationResult updateRows(T sharedState, Table table, int statementId, ILongArrayCommon rowIds, DMLUpdateRows rows);
    OperationResult updateAllRows(T sharedState, Table table, int statementId, DMLUpdateRows row);

    OperationResult deleteRows(T sharedState, Table table, int statementId, ILongArrayCommon rowIds);
    OperationResult deleteAllRows(T sharedState, Table table, int statementId);
}
