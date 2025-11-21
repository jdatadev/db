package dev.jdata.db.engine.sessions;

import java.util.Objects;

import dev.jdata.db.engine.database.SQLExpressionEvaluator;
import dev.jdata.db.engine.database.SQLExpressionEvaluatorParameter;
import dev.jdata.db.engine.transactions.Transaction;
import dev.jdata.db.utils.adt.arrays.IArrayAllocator;

abstract class BaseDMLEvaluatorParameter extends SQLExpressionEvaluatorParameter {

    private TableAndColumnNames tableAndColumnNames;
    private Transaction transaction;

    BaseDMLEvaluatorParameter(AllocationType allocationType, IArrayAllocator<SQLExpressionEvaluator> arrayAllocator) {
        super(allocationType, arrayAllocator);
    }

    final void initialize(TableAndColumnNames tableAndColumnNames) {

        this.tableAndColumnNames = Objects.requireNonNull(tableAndColumnNames);
    }

    public final void initializeForDMLOperation(Transaction transaction) {

        this.transaction = Objects.requireNonNull(transaction);
    }

    final TableAndColumnNames getTableAndColumnNames() {
        return tableAndColumnNames;
    }

    final Transaction getTransaction() {
        return transaction;
    }
}
