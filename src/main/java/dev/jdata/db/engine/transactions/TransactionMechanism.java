package dev.jdata.db.engine.transactions;

import java.util.Objects;

import dev.jdata.db.data.BaseRowMap;
import dev.jdata.db.dml.DMLInsertUpdateRows;
import dev.jdata.db.schema.model.objects.Table;
import dev.jdata.db.utils.adt.elements.ILongByIndexOrderedElementsView;
import dev.jdata.db.utils.checks.Checks;

public abstract class TransactionMechanism<T> extends BaseRowMap implements TransactionOperations<T> {

    protected static void checkParameters(ITransactionSharedStateMarker sharedState, Table table, int statementId, ILongByIndexOrderedElementsView rowIds,
            DMLInsertUpdateRows<?> rows) {

        checkParameters(sharedState, table, statementId, rows);
        checkParameter(rowIds);
    }

    protected static void checkParameters(ITransactionSharedStateMarker sharedState, Table table, int statementId, ILongByIndexOrderedElementsView rowIds) {

        checkParameters(sharedState, table, statementId, rowIds);
        checkParameter(rowIds);
    }

    protected static void checkParameters(ITransactionSharedStateMarker sharedState, Table table, int statementId, DMLInsertUpdateRows<?> rows) {

        checkParameters(sharedState, table, statementId);
        Checks.isExactlyOne(rows.getNumElements());
    }

    protected static void checkParameters(ITransactionSharedStateMarker sharedState, Table table, int statementId) {

        Objects.requireNonNull(sharedState);
        Objects.requireNonNull(table);
        Checks.isStatementId(statementId);
    }

    private static void checkParameter(ILongByIndexOrderedElementsView rowIds) {

        Checks.isNotEmpty(rowIds);
    }
}
